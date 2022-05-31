package cn.gtea.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * @author Ayeze_Mizon
 * 2022-05-29
 */
public class GteaGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = 16785283511377547L;



    private final String role;

    public GteaGrantedAuthority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof GteaGrantedAuthority) {
            return role.equals(((GteaGrantedAuthority) obj).role);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }
}
