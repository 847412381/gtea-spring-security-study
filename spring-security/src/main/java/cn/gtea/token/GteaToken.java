package cn.gtea.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

/**
 * @author Ayeze_Mizon
 * 2022-05-12
 */
public class GteaToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -3330109676428092468L;

    private final Object principal;
    private Object credentials;

    public GteaToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public GteaToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

}
