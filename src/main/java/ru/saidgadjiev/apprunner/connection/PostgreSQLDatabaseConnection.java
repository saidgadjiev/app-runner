package ru.saidgadjiev.apprunner.connection;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by said on 11.10.2018.
 */
public class PostgreSQLDatabaseConnection implements DatabaseConnection {

    private static final String TABLE_EXISTS = "SELECT EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM   information_schema.tables\n" +
            "    WHERE  table_name = '%s'\n" +
            ")";

    private final Connection connection;

    public PostgreSQLDatabaseConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int executeUpdate(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    @Override
    public boolean existTable(String table) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(String.format(TABLE_EXISTS, table))) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("exists");
                }

                return false;
            }
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
