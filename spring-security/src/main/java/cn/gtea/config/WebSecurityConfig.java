package cn.gtea.config;

import cn.gtea.filter.GteaTokenFilter;
import cn.gtea.utils.CommonResult;
import cn.gtea.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自定义认证filter
     * @return
     * @throws Exception
     */
    @Bean
    public GteaTokenFilter gteaTokenFilter() throws Exception {
        GteaTokenFilter gteaTokenFilter = new GteaTokenFilter(authenticationManager());
        gteaTokenFilter.setAuthenticationSuccessHandler(gteaAuthenticationSuccessHandler());
        gteaTokenFilter.setAuthenticationFailureHandler(gteaAuthenticationFailureHandler());
        return gteaTokenFilter;
    }

    @Bean
    public AuthenticationSuccessHandler gteaAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            //应该由前端控制跳转，返回登录成功json即可
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                try {
                    response.setContentType("application/json;charset=utf-8");
                    CommonResult<String> commonResult = new CommonResult<>("登录成功", RandomUtil.generatedReqNo());
                    PrintWriter out = response.getWriter();
                    out.write(commonResult.toString());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler gteaAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                try {
                    response.setContentType("application/json;charset=utf-8");
                    CommonResult<String> commonResult = new CommonResult<>("登录失败", RandomUtil.generatedReqNo());
                    PrintWriter out = response.getWriter();
                    out.write(commonResult.toString());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //如不依赖UsernamePasswordAuthenticationFilter，则不要用formLogin()
//                .formLogin().and()
                .authorizeRequests()
//                .antMatchers("/admin/**").hasAnyAuthority("admin")
//                .antMatchers("/guest/**").hasAnyAuthority("guest", "admin")
                .anyRequest().authenticated()
                .and().authenticationProvider(authenticationProvider)
                .addFilterBefore(gteaTokenFilter(), RequestCacheAwareFilter.class);
    }
}
