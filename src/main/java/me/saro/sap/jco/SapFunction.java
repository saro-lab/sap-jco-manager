package me.saro.sap.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

/**
 * Sap Function
 * @author      PARK Yong Seo
 * @since       0.1
 */
public class SapFunction {

    final JCoDestination destination;
    final JCoFunction function;
    JCoParameterList imports;

    /**
     * @param jCoFunction
     * @see SapFunctionTemplate
     */
    SapFunction(JCoDestination destination, JCoFunction jCoFunction) {
        this.destination = destination;
        this.function = jCoFunction;
    }

    /**
     * insert import parameters
     * @return
     */
    public JCoParameterList imports() {
        if (imports == null) {
            imports = function.getImportParameterList();
        }
        return imports;
    }

    /**
     * get table that will imported
     * @param tableName
     * @return
     */
    public JCoTable importTable(String tableName) {
        return function.getTableParameterList().getTable(tableName);
    }

    /**
     * execute and return SapFunctionResult
     * @return
     * @throws JCoException
     */
    public SapFunctionResult execute() throws JCoException {
        function.execute(destination);
        return new SapFunctionResult(function);
    }
}