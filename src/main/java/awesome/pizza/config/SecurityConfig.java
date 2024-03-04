package awesome.pizza.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import awesome.pizza.exception.CustomAccessDeniedHandler;
import awesome.pizza.filter.JwtAuthenticationFilter;
import awesome.pizza.model.Role;
import awesome.pizza.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomLogoutHandler logoutHandler;
  

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                        JwtAuthenticationFilter jwtAuthenticationFilter, 
                        CustomAccessDeniedHandler accessDeniedHandler,
                        CustomLogoutHandler logoutHandler) {
                            
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //TODO: REMOVE "/all-orders", "/status-order/** "/demoAll" "/register/" FROM permitAll() 
        //     AND "/demoAdmin" FROM hasAuthority(Role.ADMIN.toString())
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                req -> req
                    .requestMatchers("/login/**", "/register/**", 
                                                "/demoAll", 
                                                "/add-order/**", "/order/**", "/all-orders", "/status-order/**",
                                                "/register-employee",
                                                "/pizzas", "/pizza/**", "/add-pizza").permitAll()

                    .requestMatchers("/status-order/**", "/all-orders", "/demoAdmin").hasAuthority(Role.EMPLOYEE.toString())

                    .requestMatchers("/register/**",
                                                "/demoAdmin", 
                                                "/status-order/**", "/all-orders", 
                                                "/add-pizza").hasAuthority(Role.ADMIN.toString())
                    .anyRequest().authenticated()
            ).userDetailsService(customUserDetailsService)
            .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(l->l.logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    

    
}
