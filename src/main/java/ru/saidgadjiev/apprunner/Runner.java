package ru.saidgadjiev.apprunner;

import org.apache.tools.ant.*;
import ru.saidgadjiev.apprunner.task.SqlTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class Runner {

    private Collection<File> files = new ArrayList<>();

    public void execute() {
        if (files.isEmpty()) {
            System.out.println("no scripts to run");
        }
        ClassLoader loader = Runner.class.getClassLoader();

        for (File file: files) {
            Project project = new Project();

            ComponentHelper.getComponentHelper(project).initDefaultDefinitions();
            ProjectHelper.configureProject(project, file);
            project.setCoreLoader(loader);

            addTaskDefinitions(project);

            project.init();

            DefaultLogger logger = new DefaultLogger();
            logger.setOutputPrintStream(System.out);
            logger.setErrorPrintStream(System.err);

            logger.setMessageOutputLevel(Project.MSG_INFO);

            project.addBuildListener(logger);
            project.executeTargets(new Vector<>());
        }
    }

    public void addFile(File file) {
        files.add(file);
    }

    private static void addTaskDefinitions(Project project) {
        project.addTaskDefinition("Sql", SqlTask.class);
    }
}
