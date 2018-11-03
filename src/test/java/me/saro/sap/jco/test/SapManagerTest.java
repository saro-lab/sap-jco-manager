package me.saro.commons;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sap.conn.jco.JCoException;

import me.saro.sap.jco.SapFunction;
import me.saro.sap.jco.SapFunctionResult;
import me.saro.sap.jco.SapFunctionTemplate;
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


    public SapManager connect() throws JCoException, IOException {
        return  SapManager
                .builder()
                .set(SapManagerBuilderOption.ASHOST, "호스트") // AS host
                .set(SapManagerBuilderOption.MSSERV, "9999") // MS port [AS, MS is MSSERV, GW is JCO_GWSERV]
                .set(SapManagerBuilderOption.SYSNR, "01") // system number
                .set(SapManagerBuilderOption.GROUP, "Group Name") // group
                .set(SapManagerBuilderOption.LANG, "KO") // language code
                .set(SapManagerBuilderOption.CLIENT, "100") // client number
                .set(SapManagerBuilderOption.USER, "user") // user
                .set(SapManagerBuilderOption.PASSWD, "password") // password
                .build();
    }

    public void functionTemplate() throws JCoException, IOException {
        SapManager sap = connect();

        SapFunctionTemplate sft = sap.getFunctionTemplate("THE_TEST_FUNC");

        int nThreads = 10;
        List<String> dataList = Arrays.asList("1", "2", "3");

        List<String> result = sft.executeAllThreads(nThreads, dataList, (function, data) -> {

            function.imports().setValue("name1", "val1");

            SapFunctionResult res = function.execute();

            return (String)res.exports().getValue("result");
        });

        System.out.println(result);
    }

    public void function() throws JCoException, IOException {
        SapManager sap = connect();

        SapFunction sf = sap.getFunction("THE_TEST_FUNC");

        sf.imports().setValue("name1", "val1");

        SapFunctionResult res = sf.execute();

        String result = (String)res.exports().getValue("result");

        System.out.println(result);
    }


}
