package sistema.adocao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Controle de acesso por perfil:
 *  - sem login: navega e filtra animais, cria conta e entra;
 *  - ADOTANTE: preenche a triagem e acompanha as solicitações;
 *  - ADMINISTRADOR (ONG/gestor): gerencia animais e decide as solicitações.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(a -> a
                .requestMatchers("/css/**", "/js/**", "/uploads/**", "/error").permitAll()
                .requestMatchers("/", "/login", "/cadastro", "/animais", "/animais/{id}").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/animais/{id}/adotar", "/minhas-solicitacoes").hasRole("ADOTANTE")
                .anyRequest().authenticated())
            .formLogin(f -> f
                .loginPage("/login")
                .defaultSuccessUrl("/entrar-redirect")
                .permitAll())
            .logout(l -> l
                .logoutSuccessUrl("/")
                .permitAll());
        return http.build();
    }
}
