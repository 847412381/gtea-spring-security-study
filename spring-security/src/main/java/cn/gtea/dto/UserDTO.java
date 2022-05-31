package cn.gtea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Ayeze_Mizon
 * 2022-05-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 5635058988509557259L;

    /**
     * 主鍵
     */
    private String id;
    /**
     * 用戶名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像地址
     */
    private String nickImageAddr;
    /**
     * 拥有权限
     */
    private String authority;
    /**
     * 角色
     */
    private String role;
    /**
     * 类型(H5、PC、APP等)
     */
    private String type;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
