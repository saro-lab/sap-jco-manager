### sap-jco-manager
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/sap-jco-manager/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/sap-jco-manager)
[![GitHub license](https://img.shields.io/github/license/saro-lab/sap-jco-manager.svg)](https://github.com/saro-lab/sap-jco-manager/blob/master/LICENSE)

# QUICK START

## Dependency injection
### maven
``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>sap-jco-manager</artifactId>
  <version>3.0.14.2</version>
</dependency>
```
### gradle

```
compile 'me.saro:commons:3.0.14.2'
```

## Download and include sapjco3
### If you have received a sapjco3 file from a vendor, use it instead of downloading it
### [http://maven.mit.edu/nexus/content/repositories/public/com/sap/conn/jco/sapjco3/3.0.14/](http://maven.mit.edu/nexus/content/repositories/public/com/sap/conn/jco/sapjco3/3.0.14/)
- **must rename before include :**
  - sapjco3-3.0.14.jar -> sapjco3.jar (required)
  - sapjco3-3.0.14-linuxx86_64.so -> sapjco3.so (required linux)
  - sapjco3-3.0.14-darwinintel64.jnilib -> sapjco3.jnilib (required mac)
  - sapjco3-3.0.14-ntamd64.dll -> sapjco3.dll (required windows)
- if you do not change the filename, you will see the following error message:
  - if not include sapjco3.jar
    ```
    JCo initialization failed with java.lang.ExceptionInInitializerError: 
      Illegal JCo archive "sapjco3-3.0.14.jar".
      It is not allowed to rename or repackage the original archive "sapjco3.jar".
    ```
  - if not include sapjco3.so (linux), sapjco3.jnilib (mac), sapjco3.dll (windows)
    ```
      java.lang.UnsatisfiedLinkError: no sapjco3 in java.library.path:
    ```

# example
``` java
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

import me.saro.sap.jco.SapFunction;
import me.saro.sap.jco.SapFunctionResult;
import me.saro.sap.jco.SapManager;
import me.saro.sap.jco.SapManagerBuilderOption;

public class SapManagerTest {

    // example connect
    public SapManager connect() throws JCoException, IOException {
        return  SapManager
                .builder()
                .set(SapManagerBuilderOption.ASHOST, "host") // AS host
                .set(SapManagerBuilderOption.MSSERV, "9999") // MS port [AS, MS is MSSERV, GW is JCO_GWSERV]
                .set(SapManagerBuilderOption.SYSNR, "01") // system number
                .set(SapManagerBuilderOption.GROUP, "Group Name") // group
                .set(SapManagerBuilderOption.LANG, "KO") // language code
                .set(SapManagerBuilderOption.CLIENT, "100") // client number
                .set(SapManagerBuilderOption.USER, "user") // user
                .set(SapManagerBuilderOption.PASSWD, "password") // password
                .build();
    }

    // example normal
    public void normal() throws JCoException, IOException {
        // connect
        SapManager sap = connect();

        // load sap function
        SapFunction function = sap.getFunction("SAP_RFC_FUNC_NAME");

        // set parameters
        function.imports().setValue("param1", "text");
        function.imports().setValue("param2", 1);
        function.imports().setValue("param3", true);

        // set table parameters [example table parameter name is param4]
        JCoTable requestTableParameter = function.importTable("param4");
        List.of("value1", "value2", "value3").forEach(e -> {
            requestTableParameter.appendRow();
            requestTableParameter.setValue("field1", "text");
            requestTableParameter.setValue("field2", e);
            requestTableParameter.setValue("field3", false);
        });

        // execute
        SapFunctionResult result = function.execute();

        // get result parameters
        result.exports().getString("param1");
        result.exports().getInt("param2");
        result.exports().getDate("param3");

        // get result tables
        List<Map<String, Object>> resultTable = result.getTable("SAP_RESULT_TABLE_NAME");

        // print result table
        System.out.println("print SAP_RESULT_TABLE_NAME");
        resultTable.forEach(row -> {
            System.out.println("=============================================");
            row.forEach( (key, value) -> System.out.println(key + " : " + value) );
        });
    }

    // example multiple thread
    public void multipleThread() throws JCoException, IOException {
        // example user no -> name
        List<Integer> userNoList = List.of(1, 2, 3, 4);

        // connect
        SapManager sap = connect();

        // use 10 thread
        // executeAllThreads method is blocking until complete all a tasks
        List<String> userNameList = sap.getFunctionTemplate("USER_TABLE").executeAllThreads(10, userNoList, (function, userNo) -> {

            function.imports().setValue("user_no", userNo);

            SapFunctionResult result = function.execute();

            String name = result.exports().getString("USER_NAME");

            return name;
        });

        // print
        System.out.println("user names");
        System.out.println(userNameList);
    }

    // example recv table to custom class
    public void recvTableToCustomClass() throws JCoException, IOException {
        // connect
        SapManager sap = connect();
        
        SapFunction function = sap.getFunction("CALL_ALL_USER_LIST");
        SapFunctionResult result = function.execute();
        
        // table to custom class list
        List<User> userList = result.getTable("USER_TABLE", User::new, (user, field) -> {
            switch (field.getName()) {
                case "first_name" :
                    user.setFirstName(field.getString());
                break;
                case "last_name" :
                    user.setLastName(field.getString());
                break;
                case "birth" :
                    user.setBirth(new SimpleDateFormat("yyyyMMdd").format(field.getDate()));
                break;
                case "join_date" :
                    user.setJoinDate(field.getDate());
                break;
            }
        });
        
        // print
        System.out.println("print user list");
        userList.forEach(System.out::println);
    }
    
    public static class User {
        String firstName;
        String lastName;
        String birth;
        Date joinDate;
        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getBirth() {
            return birth;
        }
        public void setBirth(String birth) {
            this.birth = birth;
        }
        public Date getJoinDate() {
            return joinDate;
        }
        public void setJoinDate(Date joinDate) {
            this.joinDate = joinDate;
        }
        public String toString() {
            return firstName + " " + lastName + " " + birth + " " + joinDate;
        }
    }
}
```
