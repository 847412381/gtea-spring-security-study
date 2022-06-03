package cn.gtea.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

/**
 * @author Ayeze_Mizon
 * 2022-05-12
 */
public class GteaToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -3330109676428092468L;

    public GteaToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public GteaToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
