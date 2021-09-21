package org.muntasir.lab;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskManagerApp extends Application<DefaultConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManagerApp.class);

    /**
     * Application starting point
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new TaskManagerApp().run("server", "application.yaml");
    }


    @Override
    public void initialize(Bootstrap<DefaultConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.addBundle(new AssetsBundle("/assets/apidocs", "/docs", "index.html"));
        super.initialize(bootstrap);
    }

    @Override
    public void run(DefaultConfiguration defaultConfiguration, Environment environment) throws Exception {
        LOGGER.info("Register all REST resources");
        JerseyEnvironment jerseyEnvironment = environment.jersey();
        jerseyEnvironment.setUrlPattern("/*");
        jerseyEnvironment.register(createOpenApiConfiguration());
        TaskResource taskResource = new TaskResource();
        jerseyEnvironment.register(taskResource);
        DatabaseHealthCheck healthCheck = new DatabaseHealthCheck(taskResource.getTaskResource());
        environment.healthChecks().register("database", healthCheck);
    }

    private OpenApiResource createOpenApiConfiguration() {

        final Info info = new Info();
        info.title("Task Manager")
                .description("Task Manager RESTful API")
                .termsOfService("http://localhost:8080/terms")
                .contact(new Contact().name("Muntasir Rahman").email("me@muntasir.org"));
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(info);

        SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration()
                .openAPI(openAPI)
                .prettyPrint(true)
                .alwaysResolveAppPath(false)
                .resourcePackages(Stream.of("org.muntasir.lab").collect(Collectors.toSet()));

        OpenApiResource openApiResource = new OpenApiResource();
        openApiResource.openApiConfiguration(swaggerConfiguration);
        return openApiResource;
    }
}
