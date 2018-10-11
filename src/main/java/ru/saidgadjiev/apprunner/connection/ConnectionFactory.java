package ru.saidgadjiev.apprunner.connection;

import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static ConnectionFactory INSTANCE;

    private String url;

    public ConnectionFactory(String url) {
        this.url = url;
    }

    public static ConnectionFactory getInstance() {
        if (INSTANCE == null) {
            try {
                Properties properties = loadProperties();

                loadDriver(properties.getProperty("driver"));
                INSTANCE = new ConnectionFactory(properties.getProperty("url"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private static void loadDriver(String driverName) throws ClassNotFoundException {
        Class.forName(driverName);
    }

    private static Properties loadProperties() throws IOException {
        String val = System.getProperty("runner.properties");

        Properties properties = new Properties();

        if (StringUtils.isNotBlank(val)) {
            File file = new File(val);
            if (!file.exists()) {
                throw new IllegalArgumentException("File[" + file + "] is missing");
            }

            try (InputStream input = new FileInputStream(file)) {
                properties.load(input);
            }
        }

        return properties;
    }
}
