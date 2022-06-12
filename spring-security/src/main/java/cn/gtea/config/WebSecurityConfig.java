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
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
     *
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

    /**
     * 登录成功处理
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler gteaAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            //应该由前端控制跳转，返回登录成功json即可
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                try {
                    response.reset();
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

    /**
     * 登录失败处理
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler gteaAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                try {
                    response.reset();
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

    /**
     * 登出成功处理
     *
     * @return
     */
    @Bean
    public LogoutSuccessHandler gteaLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                try {
                    response.reset();
                    response.setContentType("application/json;charset=utf-8");
                    CommonResult<String> commonResult = new CommonResult<>("登出成功", RandomUtil.generatedReqNo());
//                    ServletOutputStream out = response.getOutputStream();

                    PrintWriter out = response.getWriter();
                    out.write(commonResult.toString());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 登出处理
     *
     * @return
     */
    @Bean
    public LogoutHandler gteaLogoutHandler() {
        return new LogoutHandler() {
            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //配置登录
        http.csrf().disable();
        http.authenticationProvider(authenticationProvider).addFilterBefore(gteaTokenFilter(), RequestCacheAwareFilter.class);
        //登录的跳转页面，和登录的动作url不应该有权限认证。
        http.authorizeRequests().antMatchers("/doLogin").permitAll();
        //配置登出页面的跳转url地址也有权限访问，不去跳转到登录页面
        http.authorizeRequests().antMatchers("/doLogOut").permitAll();
        http.authorizeRequests().antMatchers("/**/*.html").permitAll();
        http.authorizeRequests().antMatchers("/**/*.css").permitAll();
        http.authorizeRequests().antMatchers("/**/*.js").permitAll();
        http.authorizeRequests().antMatchers("/**/*.png").access("permitAll");
        http.authorizeRequests().anyRequest().authenticated();

        //配置登出
        http.logout()
                //只支持定制退出url
                //.logoutUrl("/doLogout")
                //支持定制退出url以及httpmethod
                .logoutRequestMatcher(new AntPathRequestMatcher("/doLogOut", "POST"))
                .logoutSuccessHandler(gteaLogoutSuccessHandler())
                //清除认证信息
                .clearAuthentication(true)
                .invalidateHttpSession(true);

    }
}
