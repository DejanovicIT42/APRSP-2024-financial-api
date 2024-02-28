package apiGateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {


    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {

        return builder
                .routes()
                .route(p -> p.path("/currency-exchange/**").uri("lb://currency-exchange"))
                .route(p -> p.path("/currency-conversion/**").uri("lb://currency-conversion"))
                .route(p -> p.path("/users-service/**").uri("lb://users-service"))
                .route(p -> p.path("/bank-account/**").uri("lb://bank-account"))
                .route(p -> p.path("/crypto-exchange/**").uri("lb://crypto-exchange"))
                .build();
    }

    //http://localhost:8765/currency-exchange/from/USD/to/RSD
}
