package com.main.web.siwa.config;

import com.main.web.siwa.service.auth.SiwaUserDetailsService;
import com.main.web.siwa.filter.JwtAuthenticationFilter;
import com.main.web.siwa.utility.JwtUtil;
import com.main.web.siwa.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private JwtUtil jwtUtil;
    private MemberRepository memberRepository;
    private SiwaUserDetailsService siwaUserDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    public SecurityConfig(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }

    public SecurityConfig(JwtUtil jwtUtil,
                          SiwaUserDetailsService siwaUserDetailsService,
                          MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.siwaUserDetailsService = siwaUserDetailsService;
        this.memberRepository = memberRepository;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(memberRepository, siwaUserDetailsService, jwtUtil);
    }
    @Bean // 비밀번호 인코더(필수)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 따로 IoC에 넣기
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean // (인증 후)권한 필터
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/admin/**").hasRole("ADMIN") // hasAuthority("ADMIN")
                                // /member 이후는 어떻게 권한을 줄지 고민
                                .requestMatchers("/member/websites/**").hasAnyRole("MEMBER", "ADMIN")
                                .requestMatchers("/member/new/**").hasAnyRole("MEMBER", "ADMIN")
                                .requestMatchers("/member/mypage/**").hasAnyRole("MEMBER", "ADMIN")
//                                .requestMatchers("/auth/**").permitAll()
                                .anyRequest().permitAll()
                ).sessionManagement(
                        session
                                -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        // 아래 코드에서 @Bean으로 등록
//        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
//                UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {

        // CORS 설정을 source에 담아서 스프링 Security에게 전달
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost", "http://localhost:3007","http://192.168.0.60", "http://192.168.0.60:3007"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedHeader("*"); // 헤더
        config.addExposedHeader("Authorization");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
