package me.saro.sap.jco.test;
/**
 * Impossible create connect test set
 * because there is no server
 */

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
