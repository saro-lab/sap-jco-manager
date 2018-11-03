package me.saro.sap.jco;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoTable;

import me.saro.commons.Converter;
import me.saro.commons.Lambdas;
import me.saro.commons.function.ThrowableBiConsumer;

/**
 * Sap Util
 * @author      PARK Yong Seo
 * @since       0.1
 */
public class SapUtils {
    
    // this class have only static method
    private SapUtils() {
    }
    
    /**
     * JCoTable to List[Map[String, Object]]
     * @param table
     * jCo table
     * @return
     */
    public static List<Map<String, Object>> toMapList(JCoTable table) {
        List<Map<String, Object>> rv = new ArrayList<>();
        
        while (table.nextRow()) {
            Map<String, Object> map = new LinkedHashMap<>();
            Converter.toStream(table).forEach(field -> map.put(field.getName(), field.getValue()));
            rv.add(map);
        }
        
        return rv;
    }
    
    /**
     * JCoTable to custom class
     * @param table
     * jCo table
     * @param createRow
     * create custom class row
     * @param bindField
     * bind field in custom class row
     * @return
     */
    public static <R> List<R> toClass(JCoTable table, Supplier<R> createRow, ThrowableBiConsumer<R, JCoField> bindField) {
        List<R> rv = new ArrayList<>();
        
        while (table.nextRow()) {
            R r = createRow.get();
            Converter.toStream(table).forEach(Lambdas.<JCoField>runtime(field -> bindField.accept(r, field)));
            rv.add(r);
        }
        
        return rv;
    }
}
