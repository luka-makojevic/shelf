package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseHelperPropertieManager
{
    private static BaseHelperPropertieManager instance;
    private static final Object lock = new Object();

    private static String baseuri;
    private static String dburl;
    private static String dbtable;
    private static String dbuser;
    private static String dbpass;

    //Create a Singleton instance. We need only one instance of Property Manager.
    public static BaseHelperPropertieManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new BaseHelperPropertieManager();
                instance.loadData();
            }
        }
        return instance;
    }

    //Get all configuration data and assign to related fields.
    private void loadData() {
        //Declare a properties object
        Properties prop = new Properties();
        //Read configuration.properties file
        try {
            prop.load(new FileInputStream(".\\datafiles\\data.properties"));
            //prop.load(this.getClass().getClassLoader().getResourceAsStream("configuration.properties"));
        } catch (IOException e) {
            System.out.println("Configuration properties file cannot be found");
        }
        //Get properties from configuration.properties
        baseuri = prop.getProperty("base-uri");
        dburl = prop.getProperty("db-url");
        dbtable = prop.getProperty("shelf-table");
        dbuser = prop.getProperty("db-user");
        dbpass = prop.getProperty("db-pass");
    }

    public String getURI(String s) {
        return baseuri;
    }
    public String getdbURI(String s) {
        return dburl;
    }
    public String getdbNameTable(String s) {
        return dbtable;
    }
    public String getdbUser(String s) {
        return dbuser;
    }
    public String getdbPass(String s) {
        return dbpass;
    }
}
