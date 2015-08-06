package com.aspose.cloud.cells.restapi;

public class ExampleRunner {

    public static void main(String... args) {
        Runnable example = null;

        try {
            Class klass = Class.forName(System.getProperty("com.aspose.cloud.example.main.class"));
            example = (Runnable) klass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException x) {
            System.err.println(x.getMessage());
            return;
        }

        example.run();
    }
}
