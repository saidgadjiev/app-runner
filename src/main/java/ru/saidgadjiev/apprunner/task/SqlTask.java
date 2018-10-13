package ru.saidgadjiev.apprunner.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import ru.saidgadjiev.apprunner.connection.ConnectionFactory;
import ru.saidgadjiev.apprunner.connection.DatabaseConnection;

import java.util.Objects;

public class SqlTask extends Task {

    private String sql;

    @Override
    public void execute() throws BuildException {
        Objects.requireNonNull(sql);

        try {
            try (DatabaseConnection connection = ConnectionFactory.getInstance().getConnection()) {
                log("[SQL]: " + sql + " [result]: " + connection.executeUpdate(sql));
            }
        } catch (Exception ex) {
            throw new BuildException(ex);
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
