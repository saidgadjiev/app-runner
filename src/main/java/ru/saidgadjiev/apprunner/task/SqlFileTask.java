package ru.saidgadjiev.apprunner.task;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import ru.saidgadjiev.apprunner.connection.ConnectionFactory;
import ru.saidgadjiev.apprunner.connection.DatabaseConnection;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * Created by said on 11.10.2018.
 */
public class SqlFileTask extends Task {

    private File file;

    @Override
    public void execute() throws BuildException {
        if (file == null) {
            throw new NullPointerException("script is not defined");
        }

        if (!file.exists()) {
            throw new BuildException("File [" + file + "] is missing");
        }

        if (file.isDirectory()) {
            throw new BuildException("File [" + file + "] is directory");
        }

        log("start execute sql script[" + file + "]");

        try {
            try (Scanner scanner = new Scanner(new FileInputStream(file), "UTF-8").useDelimiter(";")) {
                while (scanner.hasNext()) {
                    String sql = scanner.next().trim();

                    if (StringUtils.isNotBlank(sql)) {
                        try (DatabaseConnection connection = ConnectionFactory.getInstance().getConnection()) {
                            log("[SQL]: " + sql + " [result]: " + connection.executeUpdate(sql));
                        }
                    }
                }

            }
        } catch (Exception ex) {
            throw new BuildException(ex);
        }

        log("finish execute sql script[" + file + "]");
    }

    public void setFile(File file) {
        this.file = file;
    }
}
