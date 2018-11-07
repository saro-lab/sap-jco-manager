package me.saro.sap.jco;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;

import me.saro.commons.Utils;
import me.saro.commons.function.ThrowableBiFunction;

/**
 * Sap Function Template
 * @author      PARK Yong Seo
 * @since       0.1
 */
public class SapFunctionTemplate {

    final JCoDestination destination;
    final JCoFunctionTemplate jCoFunctionTemplate;

    /**
     * use SapManagerBuilder
     * @param jCoFunctionTemplate
     */
    SapFunctionTemplate(JCoDestination destination, JCoFunctionTemplate jCoFunctionTemplate) {
        this.destination = destination;
        this.jCoFunctionTemplate = jCoFunctionTemplate;
    }

    /**
     * get function
     * @return
     */
    public SapFunction getFunction() {
        JCoFunction jCoFunction = jCoFunctionTemplate.getFunction();
        if (jCoFunction == null) {
            throw new RuntimeException("function `"+jCoFunctionTemplate.getName()+"` not found");
        }
        return new SapFunction(destination, jCoFunction);
    }

    /**
     * execute all functions with threads
     * @param nThreads
     * @param list
     * @param forEach
     * @return
     */
    public <T, R> List<R> executeAllThreads(int nThreads, List<T> list, ThrowableBiFunction<SapFunction, T, R> forEach) {
        return Utils.executeAllThreads(nThreads, list, e -> forEach.apply(getFunction(), e));
    }

    /**
     * execute all functions with threads
     * <b>WARNING : </b>this method does not shutdown to ExecutorService instance
     * @param executorService
     * @param list
     * @param forEach
     * @return
     */
    public <T, R> List<R> executeAllThreads(ExecutorService executorService, List<T> list, ThrowableBiFunction<SapFunction, T, R> forEach) {
        return Utils.executeAllThreads(executorService, list, e -> forEach.apply(getFunction(), e));
    }
}
