package ru.saidgadjiev.apprunner.connection;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by said on 11.10.2018.
 */
public class PostgreSQLConnectionSource implements ConnectionSource {

    private final DataSource dataSource;

    public PostgreSQLConnectionSource(Properties properties) {
        dataSource = loadDataSource(properties);
    }

    @Override
    public DatabaseConnection getConnection() throws SQLException {
        return new PostgreSQLDatabaseConnection(dataSource.getConnection());
    }

    private static DataSource loadDataSource(Properties properties) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerName(properties.getProperty("server"));
        dataSource.setPortNumber(Integer.parseInt(properties.getProperty("port")));
        dataSource.setDatabaseName(properties.getProperty("database"));
        dataSource.setUser(properties.getProperty("user"));
        dataSource.setPassword(properties.getProperty("password"));

        return dataSource;

    }
}
