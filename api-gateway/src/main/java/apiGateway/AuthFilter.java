package apiGateway;
import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements WebFilter {

    private final ReactiveUserDetailsService userDetailsService;

    public AuthFilter(ReactiveUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Get the Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // If there is no Authorization header, do nothing
        if (authHeader == null) {
            return chain.filter(exchange);
        }

        // Get the user email and password from the Authorization header
        String[] authHeaderArr = authHeader.split(" ");
        String email = authHeaderArr[0];

        return userDetailsService.findByUsername(email)
                .map(userDetails -> {
                    // Extract role from userDetails
                    String role = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst()
                            .orElse(null);

                    // Modify the request to include role
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Email", email)
                            .header("X-User-Role", role)
                            .build();

                    return exchange.mutate().request(request).build();
                })
                .flatMap(chain::filter);
    }
}