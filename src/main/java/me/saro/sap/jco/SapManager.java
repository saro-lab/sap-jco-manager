package me.saro.sap.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;

/**
 * Sap Manager 
 * @author      PARK Yong Seo
 * @since       0.1
 */
public class SapManager {

    final JCoDestination destination;

    /**
     * use SapManagerBuilder
     */
    SapManager(JCoDestination destination) {
        this.destination = destination;
    }

    /**
     * SapManager builder
     * @return
     */
    public static SapManagerBuilder builder() {
        return new SapManagerBuilder();
    }

    /**
     * get function template
     * @param rfcFunctionName
     * @return
     * @throws JCoException
     */
    public SapFunctionTemplate getFunctionTemplate(String rfcFunctionName) throws JCoException {
        JCoFunctionTemplate jCoFunctionTemplate = destination.getRepository().getFunctionTemplate(rfcFunctionName);
        if (jCoFunctionTemplate == null) {
            throw new RuntimeException("function `"+rfcFunctionName+"` not found");
        }
        return new SapFunctionTemplate(destination, jCoFunctionTemplate);
    }

    /**
     * get function
     * @param rfcFunctionName
     * @return
     * @throws JCoException
     */
    public SapFunction getFunction(String rfcFunctionName) throws JCoException {
        JCoFunction jCoFunctionTemplate = destination.getRepository().getFunction(rfcFunctionName);
        if (jCoFunctionTemplate == null) {
            throw new RuntimeException("function `"+rfcFunctionName+"` not found");
        }
        return new SapFunction(destination, jCoFunctionTemplate);
    }
}
