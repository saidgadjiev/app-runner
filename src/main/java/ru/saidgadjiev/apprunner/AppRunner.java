package ru.saidgadjiev.apprunner;

import gnu.getopt.Getopt;

import java.io.File;

public class AppRunner {

    private static final String PARAM_PATTERN = "f:t:";

    public static void main(String[] args) {
        Getopt g = new Getopt(Runner.class.getSimpleName(), args, PARAM_PATTERN);

        Runner runner = new Runner();

        int c;
        while ((c = g.getopt()) != -1) {
            String arg = g.getOptarg();
            switch (c) {
                case 'f':
                    runner.addFile(new File(arg));
                    break;
                case 't':
                    runner.addTarget(arg);
                    break;
            }
        }

        runner.execute();
    }
}
