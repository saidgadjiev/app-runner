package ru.saidgadjiev.apprunner.connection;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by said on 11.10.2018.
 */
public interface DatabaseConnection extends AutoCloseable {

    int executeUpdate(String query) throws SQLException;

    Statement createStatement() throws SQLException;

    boolean existTable(String table) throws SQLException;

    void close() throws SQLException;

}
