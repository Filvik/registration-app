package com.test_example.registration_app.config;

import com.test_example.registration_app.filters.JwtRequestFilter;
import com.test_example.registration_app.handler.CustomAccessDeniedHandler;
import com.test_example.registration_app.handler.CustomAuthenticationEntryPoint;
import com.test_example.registration_app.handler.CustomAuthenticationSuccessHandler;
import com.test_example.registration_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;
    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
    private static final String[] ERROR_WHITELIST = {
            "/error",
            "/noAuthorizationError"
    };
    private static final String[] AUTH_WHITELIST = {
            "/auth"
    };

    /**
     * Настраивает цепочку фильтров безопасности для HTTP-запросов.
     * Определяет правила доступа к различным URL, настройки формы входа, выхода из системы и обработку исключений.
     *
     * @param http объект HttpSecurity для настройки параметров безопасности.
     * @return сконфигурированный объект SecurityFilterChain.
     * @throws Exception если произошла ошибка при конфигурации.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(ERROR_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, "/logout").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login-error")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .permitAll())
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(x -> x.accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll());
//        return http.build();
//    }

    /**
     * Создает провайдер аутентификации для интеграции с пользовательским сервисом и шифрованием паролей.
     *
     * @return объект DaoAuthenticationProvider для аутентификации.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    /**
     * Создает и возвращает кодировщик паролей.
     *
     * @return объект BCryptPasswordEncoder для шифрования паролей.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает и возвращает менеджер аутентификации.
     *
     * @param authenticationConfiguration конфигурация аутентификации.
     * @return объект AuthenticationManager для управления аутентификацией.
     * @throws Exception если произошла ошибка при создании менеджера.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Создает и возвращает обработчик отказа в доступе.
     *
     * @return объект AccessDeniedHandler для обработки событий доступа.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /**
     * Создает и возвращает точку входа для аутентификации.
     *
     * @return объект AuthenticationEntryPoint для обработки ошибок аутентификации.
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}
