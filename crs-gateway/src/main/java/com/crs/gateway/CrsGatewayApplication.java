package com.crs.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class CrsGatewayApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrsGatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CrsGatewayApplication.class, args);
	}

	@Bean
	@Lazy(false)
	public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters, RouteDefinitionLocator locator) {
		List<GroupedOpenApi> groups = new ArrayList<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		for (RouteDefinition definition : definitions) {
			LOGGER.info("id: " + definition.getId()+ "  "+definition.getUri().toString());
		}
		definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
			String name = routeDefinition.getId().replaceAll("-service", "");
			swaggerUiConfigParameters.addGroup(name);
			GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
		});
		return groups;
	}
}
