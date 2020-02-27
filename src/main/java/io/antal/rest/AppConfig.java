package io.antal.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class AppConfig extends ResourceConfig {

    private final Collection<Class<?>> controllers;
    private final Collection<Object> singletons;
    private boolean initialized = false;
    private long started = 0L;
    private final Server server;

    public static class Builder {
        private Collection<Class<?>> controllers = new LinkedList<>();
        private Collection<Object> singletons = new LinkedList<>();
        private int port = 8080;

        public Builder addController(Class<?> controller) {
            this.controllers.add(controller);
            return this;
        }

        public Builder addSingleton(Object singleton) {
            this.singletons.add(singleton);
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public AppConfig build() {
            AppConfig config = new AppConfig(this);
            return config;
        }
    }

    private AppConfig(Builder builder) {
        this.controllers = Collections.unmodifiableCollection(builder.controllers);
        this.singletons = Collections.unmodifiableCollection(builder.singletons);
        this.server = new Server(builder.port);
    }

    private void initialize() {
        for (Class<?> controller : controllers) {
            register(controller);
        }
        for (Object singleton : singletons) {
            register(singleton);
        }
    }

    public synchronized void start() throws Exception {
        if (started > 0L) {
            throw new IllegalStateException("Attempting to start server that has already been started!");
        }
        if (!initialized) {
            this.initialize();
        }

        ServletHolder holder = new ServletHolder(new ServletContainer(this));
        ServletContextHandler handler = new ServletContextHandler(this.server, "/");
        handler.addServlet(holder, "/*");
        this.server.start();
    }

    public void join() throws Exception {
        this.server.join();
    }
}
