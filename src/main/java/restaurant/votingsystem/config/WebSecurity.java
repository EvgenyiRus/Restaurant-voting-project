package restaurant.votingsystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.repository.UserRepository;
import restaurant.votingsystem.web.user.AuthorizedUser;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private final UserRepository userRepository;

    public WebSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating {}", email);
            Optional<User> optionalUser = userRepository.getByEmail(email);
            return new AuthorizedUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/restaurants/**/menus/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/restaurants/**/menus/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/restaurants/**/menus/**").hasAnyRole("ADMIN")
                //Доступ разрешен анонимным пользователям и всем авторизированным
                .antMatchers("/restaurants/**/menus/**", "/restaurants/**/votes", "/profile").authenticated()
                .antMatchers("/profile/register").anonymous()
                //Доступ только для пользователей с ролью Администратор
                .antMatchers("/admin/**", "/restaurants/**", "/dishes/**").hasRole("ADMIN")
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }
}
