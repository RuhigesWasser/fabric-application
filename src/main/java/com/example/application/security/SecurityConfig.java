package com.example.application.security;

import com.example.application.entity.Member;
import com.example.application.entity.Org;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    Contract contract;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    用户信息
    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserDetails userDetailsAdmin = User.withUsername("admin")
                                .password(encoder.encode("admin"))
                                .roles("ADMIN")
                                .build();
        userDetailsManager.createUser(userDetailsAdmin);


        try {
            String s = new String(contract.evaluateTransaction("queryAllMember"));
            Gson gson = new Gson();
            Pattern pattern = Pattern.compile("member.*?Z");
            Pattern patternAdmin = Pattern.compile("admin");
            String adminExist = "";
            Type type = new TypeToken<List<Org>>(){}.getType();
            List<Org> list = gson.fromJson(s, type);
            for (Org org : list) {
                Matcher matcher = pattern.matcher(org.getTraceability());
                Matcher matcherAdmin = patternAdmin.matcher(org.getTraceability());
                if (matcher.find() && org.getAuthentication().equals("true")) {
                    UserDetails userDetailsMember = User.withUsername(org.getName())
                            .password(org.getPassword())
                            .roles(org.getType())
                            .build();
                    userDetailsManager.createUser(userDetailsMember);
                }
                if (matcherAdmin.find()) {
                    adminExist = "true";
                }
            }
            if (!adminExist.equals("true")) {
                contract.submitTransaction("updateMember");
            }
        } catch (GatewayException | CommitException e) {
            System.out.println(e.getMessage());
        }

        return userDetailsManager;
    }

    @Bean(name = "filterChain")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/queryComById", "/regis").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(formLogin -> formLogin
                        .successHandler((req, resp, authentication) -> {
                            Object principal = authentication.getPrincipal();
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new ObjectMapper().writeValueAsString(principal));
                            out.flush();
                            out.close();
                        })
                        .failureHandler((req, resp, e) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(e.getMessage());
                            out.flush();
                            out.close();
                        })
                        );
        return http.build();
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
