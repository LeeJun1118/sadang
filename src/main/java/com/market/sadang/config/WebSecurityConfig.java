package com.market.sadang.config;

import com.market.sadang.service.authUtil.MyUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private MyUserDetailService myUserDetailService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()// rest api를 고려하여 기본 설정 해제
                // 토큰 기반 인증이므로 세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                //로그인 안한 사람이 접근시 설정
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                //메일 인증을 안한 사람이 접근시 설정
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .antMatchers("/signup").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/verify").permitAll()
                .antMatchers("/board/new").hasRole("USER")
                .antMatchers("/myPage").hasRole("USER")
                .antMatchers("/test/**").hasRole("USER")
                .anyRequest().permitAll();


        // 모든 요청에 토큰을 검증하는 필터를 추가한다.
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
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
