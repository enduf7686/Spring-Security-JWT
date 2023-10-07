package springsecurity.jwtserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //내 서버가 json으로 응답할 때 자바스크립트에서 처리할 수 있게 할지 결정
        config.addAllowedOrigin("*"); //모든 IP의 응답 허용
        config.addAllowedHeader("*"); //모든 header의 응답 허용
        config.addAllowedMethod("*"); //모든 HTTP 메서도 허용(GET, POST, PUT, FETCH, ...)
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter())
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").access("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
                .antMatchers("/api/v1/manager/**").access("hasRole('MANAGER') or hasRole('ADMIN')")
                .antMatchers("/api/v1/admin/**").access("hasRole('ADMIN')")
                .anyRequest().permitAll()
                .and()
                .build();
    }
}
