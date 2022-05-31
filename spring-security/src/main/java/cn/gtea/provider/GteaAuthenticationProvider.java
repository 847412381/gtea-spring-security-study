package cn.gtea.provider;

import cn.gtea.token.GteaToken;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Ayeze_Mizon
 * 2022-05-12
 */
@Component
public class GteaAuthenticationProvider implements AuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 校验信息是否正确
     *
     * @param userDetails
     * @param authentication
     * @throws AuthenticationException
     */
    public void additionalAuthenticationChecks(UserDetails userDetails, GteaToken authentication)
            throws AuthenticationException {
        if (StrUtil.isEmpty((String) authentication.getPrincipal())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (authentication == null || StrUtil.isEmpty((String) authentication.getCredentials())) {
            throw new RuntimeException("密码不能为空");
        }
        if (!userDetails.getUsername().equals(authentication.getPrincipal())) {
            throw new RuntimeException("用户名不正确");
        }
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");
            throw new RuntimeException("密码不正确");
        }
    }

    protected final UserDetails retrieveUser(String username, GteaToken authentication)
            throws AuthenticationException {
        UserDetails loadedUser = this.userDetailsService.loadUserByUsername(username);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    /**
     * 未认证的认证请求
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //校验是否为UsernamePasswordAuthenticationToken及其子类
        Assert.isInstanceOf(GteaToken.class, authentication,
                () -> "Only GteaToken is supported");
        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                : authentication.getName();

        GteaToken gteaToken = new GteaToken(authentication.getPrincipal(), authentication.getCredentials());

        UserDetails userDetails;
        try {
            userDetails = retrieveUser(username, gteaToken);
        } catch (UsernameNotFoundException notFound) {
            logger.debug("User '" + username + "' not found");
            throw new BadCredentialsException("Bad credentials");
        }
        additionalAuthenticationChecks(userDetails, gteaToken);

        return createSuccessAuthentication(userDetails.getUsername(), gteaToken, userDetails);
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        GteaToken result = new GteaToken(principal, authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    /**
     * 是否被spring security支持
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
