package cn.gtea.controller;

import cn.gtea.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/login")
    public String login(@RequestBody UserDto dto, HttpSession httpSession) {
        if (dto == null || "".equals(dto.getPassword()) || "".equals(dto.getUsername())) {
            throw new RuntimeException("账号名或密码不能为空");
        }
        UserDto user = getUser(dto.getUsername());
        if (user == null) {
            throw new RuntimeException("查询不到该用户");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        httpSession.setAttribute("JSESSION", UserDto.USER_SESSION_KEY);
        return dto.toString();
    }

    private UserDto getUser(String key) {
        return userDtoMap.get(key);
    }

    @PostMapping("/r1")
    public String r1(HttpSession httpSession) {
        return httpSession.toString();
    }

    private Map<String, UserDto> userDtoMap = new HashMap<>();
    {
        System.out.println("实例化时加载");
        userDtoMap.put("111", new UserDto("111", "123456"));
        userDtoMap.put("222", new UserDto("222", "654321"));
    }
}
