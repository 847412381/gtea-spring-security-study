package cn.gtea.config;

import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * @author Ayeze_Mizon
 * 2022-05-29
 */
public class GteaUser implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = -6234226203582197141L;

    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private Set<GrantedAuthority> authorities;
    @Setter
    private boolean accountNonExpired = true;
    @Setter
    private boolean accountNonLocked = true;
    @Setter
    private boolean credentialsNonExpired = true;
    @Setter
    private boolean enabled = true;

    public GteaUser() {
    }

    public GteaUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.password = password;
        this.username = username;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    public GteaUser(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean accountNonExpired,
                    boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static GteaUserBuilder builder() {
        return new GteaUserBuilder();
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority,
                    "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    public Set<GrantedAuthority> getAuthSet() {
        return this.authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public static class GteaUserBuilder{

        @Setter
        private String password;
        @Setter
        private String username;
        @Setter
        private List<GrantedAuthority> authorities;
        @Setter
        private boolean accountNonExpired = true;
        @Setter
        private boolean accountNonLocked = true;
        @Setter
        private boolean credentialsNonExpired = true;
        @Setter
        private boolean enabled = true;

        private Function<String, String> passwordEncoder = password -> password;

        private GteaUserBuilder() { }

        /**
         * 角色在接口前加注解认证是否有权限要加前缀ROLE_，而authorities不需要
         * @param roles
         * @return
         */
        public GteaUserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(
                    roles.length);
            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> role
                        + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return authorities(authorities);
        }

        /**
         * 角色在接口前加注解认证是否有权限要加前缀ROLE_，而authorities不需要
         * @param authorities
         * @return
         */
        public GteaUserBuilder authorities(GrantedAuthority... authorities) {
            return authorities(Arrays.asList(authorities));
        }

        /**
         * 角色在接口前加注解认证是否有权限要加前缀ROLE_，而authorities不需要
         * @param authorities
         * @return
         */
        public GteaUserBuilder authorities(String... authorities) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities.length);
            for (String authority : authorities) {
                grantedAuthorities.add(new GteaGrantedAuthority(authority));
            }
            return authorities(grantedAuthorities);
        }

        public GteaUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public GteaUserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public GteaUserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public GteaUserBuilder passwordEncoder(Function<String, String> passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        public GteaUserBuilder isAccountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public GteaUserBuilder isAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public GteaUserBuilder isCredentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public GteaUserBuilder isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public void eraseCredentials() {
            this.password = null;
        }

        public UserDetails build() {
            String encodingPass = this.passwordEncoder.apply(password);
            return new GteaUser(username, encodingPass, authorities, accountNonExpired, accountNonLocked,
                    credentialsNonExpired, enabled);
        }
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>,
            Serializable {

        private static final long serialVersionUID = 563740862066630977L;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set.
            // If the authority is null, it is a custom authority and should precede
            // others.
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }
}
