package cn.gtea.constant;

import cn.gtea.config.GteaGrantedAuthority;
import cn.gtea.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ayeze_Mizon
 * 2022-05-12
 */
public class AuthenticateConstant {

    private static Map<String, UserDTO> map = new HashMap<>(16);

    static {
        GteaGrantedAuthority admin = new GteaGrantedAuthority("admin");
        GteaGrantedAuthority guest = new GteaGrantedAuthority("guest");
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(admin);
        authorities.add(guest);
        map.put("yzm", new UserDTO("00000001", "yzm", "123456", "yzm",
                "https://www.baidu.com", "admin", "sys", "PC", 1,
                LocalDateTime.now(), LocalDateTime.now(), authorities, true,
                true, true, true));
    }

    public static Map<String, UserDTO> getMap() {
        return map;
    }

    public static void setUser(String key, UserDTO userDTO) {
        map.put(key, userDTO);
    }
}
