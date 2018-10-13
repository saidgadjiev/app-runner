package ru.saidgadjiev.apprunner.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by said on 11.10.2018.
 */
public interface ConnectionSource {

    DatabaseConnection getConnection() throws SQLException;
}
