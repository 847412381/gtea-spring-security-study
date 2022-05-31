package cn.gtea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    public static final String USER_SESSION_KEY = "0110_user";
    private String username;
    private String password;
}
