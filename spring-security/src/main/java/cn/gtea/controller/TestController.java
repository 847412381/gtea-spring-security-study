package cn.gtea.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

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




    @PostMapping(value = "/query/bbs/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public String queryBbsPostAll(@RequestBody String name) {
        return name;
    }

}
