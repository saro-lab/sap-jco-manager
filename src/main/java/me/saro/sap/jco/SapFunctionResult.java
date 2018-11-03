package me.saro.sap.jco;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

import me.saro.commons.function.ThrowableBiConsumer;

/**
 * Sap Function Result
 * @author      PARK Yong Seo
 * @since       0.1
 */
public class SapFunctionResult {

    final JCoFunction function;
    JCoParameterList exports;

    /**
     * @param jCoFunction
     * @see SapFunctionFunction
     */
    SapFunctionResult(JCoFunction function) {
        this.function = function;
    }

    /**
     * get export parameters
     * @return
     */
    public JCoParameterList exports() {
        if (exports == null) {
            exports = function.getExportParameterList();
        }
        return exports;
    }
    
    /**
     * get result table count
     * @return
     */
    public int getTableCount() {
        return function.getTableParameterList().getFieldCount();
    }
    
    /**
     * get table by index
     * @param index
     * @return
     */
    public JCoTable getJCoTable(int index) {
        return function.getTableParameterList().getTable(index);
    }
    
    /**
     * get table by name
     * @param name
     * @return
     */
    public JCoTable getJCoTable(String name) {
        return function.getTableParameterList().getTable(name);
    }
    
    /**
     * 
     * @param index
     * @return
     */
    public List<Map<String, Object>> getTable(int index) {
        return SapUtils.toMapList(function.getTableParameterList().getTable(index));
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public List<Map<String, Object>> getTable(String name) {
        return SapUtils.toMapList(function.getTableParameterList().getTable(name));
    }
    
    /**
     * 
     * @param index
     * @param createRow
     * @param bindField
     * @return
     */
    public <R> List<R> getTable(int index, Supplier<R> createRow, ThrowableBiConsumer<R, JCoField> bindField) {
        return SapUtils.toClass(function.getTableParameterList().getTable(index), createRow, bindField);
    }
    
    /**
     * 
     * @param name
     * @param createRow
     * @param bindField
     * @return
     */
    public <R> List<R> getTable(String name, Supplier<R> createRow, ThrowableBiConsumer<R, JCoField> bindField) {
        return SapUtils.toClass(function.getTableParameterList().getTable(name), createRow, bindField);
    }
    
    /**
     * get all tables
     * @return
     */
    public List<List<Map<String, Object>>> getAllTables() {
        return IntStream.range(0, getTableCount()).mapToObj(e -> getTable(e)).collect(Collectors.toList());
    }
}
