package cn.gtea.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/doLogin")
    public String doLogin() {
        return "doLogin";
    }

    @PostMapping("/doLoginSuccess")
    public String doLoginSuccess() {
        return "doLoginSuccess";
    }

    @GetMapping("/login/index")
    public String loginIndex() {
        return "未通过授权校验，跳转至此登录界面";
    }

    @PostMapping("/test1")
    public String test1() {
        return "test1";
    }

    @GetMapping("/admin/r1")
    @PreAuthorize("hasAuthority('admin')")
    public String r1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        System.out.println("principal = " + principal);
        return "r1";
    }

    @PostMapping("/guest/r2")
    @PreAuthorize("hasAnyAuthority('guest','admin')")
    public String r2() {
        return "r2";
    }

}
