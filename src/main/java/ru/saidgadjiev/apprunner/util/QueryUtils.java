package ru.saidgadjiev.apprunner.util;

import ru.saidgadjiev.apprunner.connection.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by said on 11.10.2018.
 */
public class QueryUtils {

    private QueryUtils() {}

    public static Object get(DatabaseConnection connection, String query, String column) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                if (resultSet.next()) {
                    return resultSet.getObject(column);
                }
            }
        }

        return null;
    }
}
