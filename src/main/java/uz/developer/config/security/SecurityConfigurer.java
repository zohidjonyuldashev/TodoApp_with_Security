package uz.developer.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer {
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    public static final String[] WHITE_LIST = {
            "/",
            "/auth/login",
            "/auth/register"
    };

    public SecurityConfigurer(CustomUserDetailsService userDetailsService, CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.userDetailsService(userDetailsService);
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(WHITE_LIST).permitAll().anyRequest().authenticated());

        http.formLogin(form -> form.loginPage("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/todos", true)
                .failureHandler(authenticationFailureHandler));

        http.logout(filter -> filter.logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST")));

        http.rememberMe(filter -> filter.rememberMeParameter("rememberMe")
                .rememberMeCookieName("rem-me")
                .tokenValiditySeconds(24 * 60 * 60)
                .key("secret_key")
                .userDetailsService(userDetailsService));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
