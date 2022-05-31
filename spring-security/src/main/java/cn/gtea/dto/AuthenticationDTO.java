package cn.gtea.dto;

import cn.gtea.config.GteaGrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Ayeze_Mizon
 * 2022-05-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDTO {

    private String username;
    private String password;
    private Set<GteaGrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
