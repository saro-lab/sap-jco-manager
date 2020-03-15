package me.saro.sap.jco;

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Sap Manager Builder 
 * @author      PARK Yong Seo
 * @since       0.1
 */
public class SapManagerBuilder {

    /** properties */
    private final Properties properties = new Properties();

    /**
     * use SapManager
     */
    SapManagerBuilder() {
    }

    /**
     * build SapManager
     * @return
     * @throws JCoException
     * @throws IOException
     */
    public synchronized SapManager build() throws JCoException, IOException {
        return build("SAP");
    }

    /**
     * build
     * @param createDestinationFileName
     * ex) SAP = make file SAP.jcoDestination
     * @return
     * @throws JCoException
     * @throws IOException
     */
    public synchronized SapManager build(String createDestinationFileName) throws JCoException, IOException {

        File file = new File(createDestinationFileName+".jcoDestination");

        if (file.exists()) {
            file.delete();
        }

        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            properties.store(fos, "make connection file");
        }

        return new SapManager(JCoDestinationManager.getDestination(createDestinationFileName));
    }

    /**
     * set options
     * @param key
     * @param value
     * @return
     */
    public SapManagerBuilder set(String key, String value) {
        properties.setProperty(key, value);
        return this;
    }

    /**
     * set options
     * @param option
     * @param value
     * @return
     */
    public SapManagerBuilder set(SapManagerBuilderOption option, String value) {
        return set(option.value(), value);
    }
}
