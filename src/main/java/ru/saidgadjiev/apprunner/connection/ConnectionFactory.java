package ru.saidgadjiev.apprunner.connection;

import org.apache.commons.lang.StringUtils;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static ConnectionFactory INSTANCE;

    private ConnectionSource connectionSource;

    public ConnectionFactory(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public static ConnectionFactory getInstance() {
        if (INSTANCE == null) {
            try {
                Properties properties = loadProperties();

                INSTANCE = new ConnectionFactory(createConnectionSource(properties));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return INSTANCE;
    }

    public DatabaseConnection getConnection() throws SQLException {
        return connectionSource.getConnection();
    }

    private static ConnectionSource createConnectionSource(Properties properties) {
        String db = properties.getProperty("db");

        if ("postgresql".equals(db)) {
            return new PostgreSQLConnectionSource(properties);
        }

        throw new IllegalArgumentException("Unsupported db");
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

            return properties;
        }

        throw new IllegalArgumentException("Miss runner.properties");
    }
}
