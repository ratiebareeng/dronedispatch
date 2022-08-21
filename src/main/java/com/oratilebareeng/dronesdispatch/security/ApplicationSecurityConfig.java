package  com.oratilebareeng.dronesdispatch.security;


import com.oratilebareeng.dronesdispatch.model.ApplicationUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/ ", "index", "/css/*", "/js/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole(ApplicationUserRole.USER.name(), ApplicationUserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/**").hasRole(ApplicationUserRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole(ApplicationUserRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/**").hasRole(ApplicationUserRole.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        http.headers().frameOptions().disable();
        return http.build();
    }


    protected UserDetailsService userDetailsService() {
      UserDetails userAdmin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles(ApplicationUserRole.ADMIN.name())
                .build();
        UserDetails userTester = User.builder()
                .username("user")
                .password(passwordEncoder.encode("userpass"))
                .roles(ApplicationUserRole.USER.name())
                .build();
        return new InMemoryUserDetailsManager(
                userAdmin,
                userTester
        );
    }
}
