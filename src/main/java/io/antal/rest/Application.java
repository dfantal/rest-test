package io.antal.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.Locale;

public class Application {

    public static void main(String[] args) {

        System.out.println(String.join(", ", Locale.getISOCountries()));

        try {
            AppConfig appConfig = new AppConfig.Builder()
                    .withPort(8080)
                    .addController(StatusController.class)
                    .build();
            appConfig.start();
            appConfig.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
