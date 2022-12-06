/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.codejava.spring.config.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 *
 * @author Dimitar
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
         /*

        auth
            .inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles("ADMINISTRATOR");

*/


        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT User_Name, Password, true FROM user WHERE User_Name=?")
                .authoritiesByUsernameQuery("SELECT UserName, GroupName FROM user_rights_view WHERE UserName=?");


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/").hasRole("ADMINISTRATOR")
                .antMatchers("/").access("hasAuthority('ADMINISTRATOR') or hasAuthority('EMPLOYEE')")
                //.antMatchers("/newContact").hasRole("ADMINISTRATOR")
                .antMatchers("/newContact").access("hasAuthority('ADMINISTRATOR') or hasAuthority('EMPLOYEE')")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll(true)
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .rememberMe()
                .tokenValiditySeconds(2419200)
                .key("privatekey")
                .and().exceptionHandling().accessDeniedPage("/unauthorized");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256");
    }
}
