package com.market.sadang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Spring Security 를 사용하기위해 Spring Security Filter Chain을 사용한다는 것을 명시하기 위해
//WebSecurityConfigurAdapter 를 상속받을 클래스에 @EnableWebSecurity 어노테이션 달아줌
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/signup").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/verify").permitAll()
                .anyRequest().permitAll();

        http.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
    }

    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*

    //암호화에 필요한 PasswordEncoder를 Bean 등록함
    @Bean
    public PasswordEncoder passwordEncoder(){
        //PasswordEncoder는 비밀번호를 안전하게 저장할 수 있도록 비밀번호의 단방향 암호화를 지원하는 인터페이스
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //authenticationManager를 Bean 등록함
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()// rest api를 고려하여 기본 설정 해제
                .csrf().disable() // csrf 보안 토큰 disable 처리
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용 안함
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                // 아래 URL로 들어오는 요청에 대해 인증 요구
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/verify/**").permitAll()
                .anyRequest().permitAll(); //그 외 나머지 요청은 누구나 접근 가능
//                .and()
                //JwtAuthenticationFilter 를 UsernamePasswordAuthenticationFilter 전에 넣는다.
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
//                        UsernamePasswordAuthenticationFilter.class);
    }*/
}
