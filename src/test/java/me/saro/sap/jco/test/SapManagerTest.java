package me.saro.sap.jco.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

import me.saro.sap.jco.SapFunction;
import me.saro.sap.jco.SapFunctionResult;
import me.saro.sap.jco.SapManager;
import me.saro.sap.jco.SapManagerBuilderOption;

/**
 * Impossible create connect test set
 * because there is no server
 */
public class SapManagerTest {

    @Test
    public void test() throws Exception {

    }

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

    // example normal
    public void normal() throws JCoException, IOException {
        // connect
        SapManager sap = getSapManager();

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
        SapManager sap = getSapManager();

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
