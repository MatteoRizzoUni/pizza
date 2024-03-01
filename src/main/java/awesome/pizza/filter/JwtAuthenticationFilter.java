package awesome.pizza.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import awesome.pizza.service.EmployeeDetailsService;
import awesome.pizza.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final EmployeeDetailsService employeeDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, EmployeeDetailsService employeeDetailsService) {
        this.jwtService = jwtService;
        this.employeeDetailsService = employeeDetailsService;
    }

    @Override
    protected void doFilterInternal(
                @NonNull    HttpServletRequest request, 
                @NonNull    HttpServletResponse response, 
                @NonNull    FilterChain filterChain)
                throws ServletException, IOException {

        
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            
            filterChain.doFilter(request, response);
            return;
        }
        //7: "Bearer " is 7 characters long with the space
        String token = authHeader.substring(7);
        String userName = jwtService.extractUsername(token);

        if( userName != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails emplyeeDetails = employeeDetailsService.loadUserByUsername(userName);

            if(jwtService.isValid(token, emplyeeDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    emplyeeDetails, null, emplyeeDetails.getAuthorities());

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 
        filterChain.doFilter(request, response);



    }

}
