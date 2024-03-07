package apiGateway.authentication;

import authentication.dtos.CustomUserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class ApiGatewayAuthentication {

    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
        List<UserDetails> users = new ArrayList<>();

        try {
            WebClient webClient = WebClient.create("http://localhost:8770/users-service");

            CustomUserDto[] usersFromDatabase = webClient.get()
                    .uri("/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(CustomUserDto[].class)
                    .block();

            for (CustomUserDto cud : usersFromDatabase) {
                users.add(User.withUsername(cud.getEmail())
                        .password(encoder.encode(cud.getPassword()))
                        .roles(cud.getRole().name())
                        .build());
            }

        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return new MapReactiveUserDetailsService(users);
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/currency-exchange/from/{from}/to/{to}").permitAll()
                .pathMatchers(HttpMethod.GET, "/users-service/**").hasAnyRole("ADMIN", "OWNER")
                .pathMatchers(HttpMethod.POST, "/users-service/**").hasAnyRole("ADMIN", "OWNER")
                .pathMatchers(HttpMethod.PUT, "/users-service/**").hasAnyRole("ADMIN", "OWNER")
                .pathMatchers(HttpMethod.DELETE, "/users-service/**").hasRole("OWNER")
                .pathMatchers("/currency-conversion/**").hasRole("USER")
                .pathMatchers(HttpMethod.GET, "/bank-account/get/{email}").hasAnyRole("ADMIN", "OWNER")
                .pathMatchers(HttpMethod.POST, "/bank-account").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/bank-account/{email}").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/bank-account/{email}/decrease/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/bank-account/{email}").permitAll()
                .pathMatchers("/crypto-exchange/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/crypto-wallet/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/crypto-wallet/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/crypto-wallet/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/crypto-wallet/{email}").permitAll()
                .pathMatchers("/crypto-conversion/**").hasRole("USER")
                .pathMatchers("/transfer-service/**").hasRole("USER")
                .pathMatchers("/trade-service/**").hasRole("USER")
                .and()
                .httpBasic().and()
                //This adds a filter to the filter chain. The filter is defined as a lambda function that takes two parameters: exchange and chain.
                // The exchange represents the current server exchange, and chain represents the remaining filter chain.
                .addFilterAfter((exchange, chain) -> {
                    //This retrieves the security context from ReactiveSecurityContextHolder. The ReactiveSecurityContextHolder provides access to
                    //the current security context in a reactive environment.
                    //The getContext() method returns a Mono that emits the security context. The following map operation extracts the authentication object from the security context.
                    return ReactiveSecurityContextHolder.getContext()
                            .map(SecurityContext::getAuthentication)
                            .flatMap(authentication -> {
                                // This retrieves the user's role from the authentication object.
                                // The authentication object represents the currently authenticated user and contains information such as the user's authorities,
                                // credentials, etc. In this case, it assumes that the user has only one authority and retrieves its value using getAuthority().
                                String role = authentication.getAuthorities().iterator().next().getAuthority();
                                String email = authentication.getName();

                                // This creates a new ServerWebExchange instance with an added header.
                                // It uses the mutate() method of the original exchange to create a builder for modifying the request.
                                // The header() method is used to add a new header named "X-User-Role" with the value of the role variable.
                                // Finally, the build() method creates the modified exchange.
                                ServerWebExchange modifiedExchange = exchange.mutate()
                                        .request(builder -> builder.header("X-User-Role", role))
                                        .request(builder -> builder.header("X-User-Email", email))
                                        .build();
                                //This applies the modified exchange to the remaining filter chain by calling the filter() method on the chain.
                                // It passes the modified exchange to the next filter in the chain.
                                return chain.filter(modifiedExchange);
                            });
                    //This specifies the position of the filter in the filter chain.
                    // The SecurityWebFiltersOrder.AUTHORIZATION argument indicates that the filter should be added after the authorization filter.
                    // The SecurityWebFiltersOrder class provides constants representing the order of various security filters.
                }, SecurityWebFiltersOrder.AUTHORIZATION)
                //This continues the configuration by invoking the authorizeExchange() method.
                // This method returns an ServerHttpSecurity.AuthorizeExchangeSpec object, allowing further configuration of authorization rules for different exchanges.
                .authorizeExchange();

        return http.build();
    }
}
