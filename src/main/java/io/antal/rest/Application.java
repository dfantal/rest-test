package io.antal.rest;

import io.antal.rest.application.InMemoryRestDao;
import io.antal.rest.util.ExceptionHandler;
import io.antal.rest.util.RestController;
import io.antal.rest.util.WebApplicationExceptionHandler;
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
                    .addController(CountryCollectionController.class)
                    .addController(ExceptionHandler.class)
                    .addController(WebApplicationExceptionHandler.class)
                    .build();
            appConfig.start();
            appConfig.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
