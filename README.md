### SAP JCo (Java Connector) Manager
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/sap-jco-manager/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/sap-jco-manager)
[![GitHub license](https://img.shields.io/github/license/saro-lab/sap-jco-manager.svg)](https://github.com/saro-lab/sap-jco-manager/blob/master/LICENSE)

# QUICK START

## Dependency injection
### maven
``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>sap-jco-manager</artifactId>
  <version>3.0.14.6</version>
</dependency>
```
### gradle

```
compile 'me.saro:sap-jco-manager:3.0.14.6'
```

## Download and include sapjco3
### Official download link (required SAP account)
- download 3.0.x version
- [https://support.sap.com/en/product/connectors/jco.html](https://support.sap.com/en/product/connectors/jco.html)
### If you have received a sapjco3 file from a vendor or official account, use it instead of downloading it
### [http://maven.mit.edu/nexus/content/repositories/public/com/sap/conn/jco/sapjco3/3.0.14/](http://maven.mit.edu/nexus/content/repositories/public/com/sap/conn/jco/sapjco3/3.0.14/)
- **must rename before include:**
  - sapjco3-3.0.14.jar -> **sapjco3.jar** (required)
  - sapjco3-3.0.14-linuxx86_64.so -> **libsapjco3.so** (required linux)
  - sapjco3-3.0.14-darwinintel64.jnilib -> **libsapjco3.jnilib** (required mac)
    - lib path: ~/Library/Java/Extensions/
  - sapjco3-3.0.14-ntamd64.dll -> **sapjco3.dll** (required windows)
- if you do not change the filename, you will see the following error message:
  - if not include sapjco3.jar
    ```
    JCo initialization failed with java.lang.ExceptionInInitializerError: 
      Illegal JCo archive "sapjco3-3.0.14.jar".
      It is not allowed to rename or repackage the original archive "sapjco3.jar".
    ```
  - if not include libsapjco3.so (linux), libsapjco3.jnilib (mac), sapjco3.dll (windows)
    ```
      java.lang.UnsatisfiedLinkError: no sapjco3 in java.library.path:
    ```

# example
## basic
``` java
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

import me.saro.sap.jco.SapFunction;
import me.saro.sap.jco.SapFunctionResult;
import me.saro.sap.jco.SapManager;
import me.saro.sap.jco.SapManagerBuilderOption;

public class SapManagerNormalTest {

    // example connect
    public SapManager getSapManager() throws JCoException, IOException {
        return SapManager
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

    // example basic
    public void basic() throws JCoException, IOException {
        // connect
        SapManager sap = getSapManager();

        // load sap function
        SapFunction function = sap.getFunction("SAP_RFC_FUNC_NAME");

        // set parameters
        function.getImportParameterList().setValue("param1", "text");
        function.getImportParameterList().setValue("param2", 1);
        function.getImportParameterList().setValue("param3", true);

        // set table parameters [example table parameter name is param4]
        JCoTable requestTableParameter = function.getImportTableParameter("param4");
        List.of("value1", "value2", "value3").forEach(e -> {
            requestTableParameter.appendRow();
            requestTableParameter.setValue("field1", "text");
            requestTableParameter.setValue("field2", e);
            requestTableParameter.setValue("field3", false);
        });

        // execute
        SapFunctionResult result = function.execute();

        // get result parameters
        result.getExportParameterList().getString("param1");
        result.getExportParameterList().getInt("param2");
        result.getExportParameterList().getDate("param3");

        // get result tables
        List<Map<String, Object>> resultTable = result.getTable("SAP_RESULT_TABLE_NAME");

        // print result table
        System.out.println("print SAP_RESULT_TABLE_NAME");
        resultTable.forEach(row -> {
            System.out.println("=============================================");
            row.forEach( (key, value) -> System.out.println(key + " : " + value) );
        });
    }
}

```

## multiple thread
``` java
import java.io.IOException;
import java.util.List;

import com.sap.conn.jco.JCoException;

import me.saro.sap.jco.SapFunctionResult;
import me.saro.sap.jco.SapManager;
import me.saro.sap.jco.SapManagerBuilderOption;

public class SapManagerMultipleThreadTest {

    // example connect
    public SapManager getSapManager() throws JCoException, IOException {
        return SapManager
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

    // example multiple thread
    public void multipleThread() throws JCoException, IOException {
        // example user no -> name
        List<Integer> userNoList = List.of(1, 2, 3, 4);

        // connect
        SapManager sap = getSapManager();

        // use 10 thread
        // executeAllThreads method is blocking until complete all a tasks
        List<String> userNameList = sap.getFunctionTemplate("USER_TABLE").executeAllThreads(10, userNoList, (function, userNo) -> {

            function.getImportParameterList().setValue("user_no", userNo);

            SapFunctionResult result = function.execute();

            String name = result.getExportParameterList().getString("USER_NAME");

            return name;
        });

        // print
        System.out.println("user names");
        System.out.println(userNameList);
    }
}

```

## custom class
``` java
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sap.conn.jco.JCoException;

import me.saro.sap.jco.SapFunction;
import me.saro.sap.jco.SapFunctionResult;
import me.saro.sap.jco.SapManager;
import me.saro.sap.jco.SapManagerBuilderOption;

public class SapManagerCustomClassTest {

    // example connect
    public SapManager getSapManager() throws JCoException, IOException {
        return SapManager
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

    // example recv table to custom class
    public void recvTableToCustomClass() throws JCoException, IOException {
        // connect
        SapManager sap = getSapManager();
        
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
