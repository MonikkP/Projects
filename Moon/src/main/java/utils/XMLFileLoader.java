package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class XMLFileLoader {

    private Properties properties = null;
    private String fileName = null;

    public XMLFileLoader(String fileName) throws IOException {
        this.fileName = fileName;
        properties = new Properties();
        properties.loadFromXML(getClass().getResourceAsStream(this.fileName));
    }

    public String loadProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
