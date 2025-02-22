package com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security;

import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.jwt.AuthEntryPoint;
import com.example.ProgettoBE_U2_W3_D5_GestioneEventi.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
public class WebSecurityConfig{
    @Autowired
    UserDetailsServiceImpl detailsImpl;

    @Autowired
    AuthEntryPoint gestoreNOAuthorization;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();

        auth.setUserDetailsService(detailsImpl);

        auth.setPasswordEncoder(passwordEncoder());

        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //nota mancano i cors, vedi dopo se serve inserirli o meno
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(gestoreNOAuthorization))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/utente/**").permitAll() // Endpoint pubblici (registrazione/login)
                        .requestMatchers("/eventi/**").hasAuthority("ROLE_ORGANIZZATORE") // Solo organizzatori possono gestire eventi
                        .requestMatchers("/prenotazioni/**").hasAuthority("ROLE_USER")// Solo utenti possono prenotare
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        return http.build();
    }
}
