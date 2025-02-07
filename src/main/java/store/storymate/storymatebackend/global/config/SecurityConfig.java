package store.storymate.storymatebackend.global.config;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import store.storymate.storymatebackend.global.filter.JwtAuthenticationFilter;
import store.storymate.storymatebackend.global.jwt.JwtTokenProvider;
import store.storymate.storymatebackend.global.properties.SecurityProperties;
import store.storymate.storymatebackend.global.util.CookieUtil;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        defaultFilterChain(http);
        http.authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/**") // 일단 임시 허용
                                        .permitAll() // Actuator
                                        .requestMatchers("/auth/**")
                                        .permitAll() // Auth endpoints
                                        .anyRequest()
                                        .authenticated())
                .exceptionHandling(
                        exception ->
                                exception.authenticationEntryPoint(
                                        (request, response, authException) ->
                                                response.setStatus(401)));

        http.addFilterBefore(
                jwtAuthenticationFilter(jwtTokenProvider, cookieUtil),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void defaultFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // TODO: CORS 임시 전체 허용
        configuration.addAllowedOriginPattern("*");

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader(SET_COOKIE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider, CookieUtil cookieUtil) {
        return new JwtAuthenticationFilter(jwtTokenProvider, cookieUtil);
    }
}
