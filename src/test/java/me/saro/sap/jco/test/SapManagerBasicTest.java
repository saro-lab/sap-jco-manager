package me.saro.sap.jco.test;
/**
 * Impossible create connect test set
 * because there is no server
 */

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;
import me.saro.sap.jco.SapFunction;
import me.saro.sap.jco.SapFunctionResult;
import me.saro.sap.jco.SapManager;
import me.saro.sap.jco.SapManagerBuilderOption;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SapManagerBasicTest {

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
    public void normal() throws JCoException, IOException {
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
        Arrays.asList("value1", "value2", "value3").forEach(e -> {
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
