package ru.saidgadjiev.apprunner.task;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import ru.saidgadjiev.apprunner.connection.ConnectionFactory;
import ru.saidgadjiev.apprunner.connection.DatabaseConnection;
import ru.saidgadjiev.apprunner.util.QueryUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 11.10.2018.
 */
public class DcTask extends Task implements TaskContainer {

    private final List<Task> tasks = new ArrayList<>();

    private String name;

    private String description;

    @Override
    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public void execute() throws BuildException {
        Validate.isTrue(StringUtils.isNotBlank(name), "name can't be blank");
        Validate.isTrue(StringUtils.isNotBlank(description), "description can't be blank");

        try (DatabaseConnection connection = ConnectionFactory.getInstance().getConnection()) {
            installDcIfNecessary(connection);

            long result = (long) QueryUtils.get(connection, "SELECT COUNT(id) as c FROM dc WHERE name='" + name + "'", "c");

            if (result == 0) {
                log("run dc[" + name + "]", Project.MSG_INFO);

                for (Task task : tasks) {
                    task.perform();
                }

                connection.executeUpdate("INSERT INTO dc(\"name\", \"description\") VALUES('" + name + "', '" + description + "')");
            }
        } catch (SQLException ex) {
            throw new BuildException(ex);
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    private void installDcIfNecessary(DatabaseConnection connection) throws SQLException {
        if (!connection.existTable("dc")) {
            connection.executeUpdate("CREATE TABLE dc(id SERIAL PRIMARY KEY, name VARCHAR(128), description VARCHAR(512) NOT NULL)");
        }
    }
}
