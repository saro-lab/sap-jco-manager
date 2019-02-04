package me.saro.sap.jco.test;
/**
 * Impossible create connect test set
 * because there is no server
 */

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
