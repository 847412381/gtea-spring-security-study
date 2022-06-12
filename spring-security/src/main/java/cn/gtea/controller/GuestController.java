package cn.gtea.controller;

import cn.gtea.constant.RespConstant;
import cn.gtea.dao.GteaOrdinaryUserDao;
import cn.gtea.dao.GteaOrdinaryUserRoleDao;
import cn.gtea.entity.GteaOrdinaryUserEntity;
import cn.gtea.query.GuestRegisterQuery;
import cn.gtea.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ayeze_Mizon
 * 2022-06-12
 */
@RestController
@Slf4j
public class GuestController {

    @Autowired
    private GteaOrdinaryUserDao gteaOrdinaryUserDao;
    @Autowired
    private GteaOrdinaryUserRoleDao gteaOrdinaryUserRoleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String FLAG = "Guest";

    @PostMapping("/sys/guest/register")
    public CommonResult<Integer> register(GuestRegisterQuery query) {
        try {
            GteaOrdinaryUserEntity entity = new GteaOrdinaryUserEntity();
            entity.setUserName(query.getPrincipal());
            entity.setUserPass(passwordEncoder.encode(query.getCredentials()));
            entity.setStatus(1);
            entity.setCreator("admin");
            entity.setUpdater("admin");
            int insert = gteaOrdinaryUserDao.insert(entity);
            return new CommonResult<>(insert);
        } catch (Exception e) {
            log.error(FLAG + ".register", e);
            return new CommonResult<>(RespConstant.INTERNAL_ERROR.getCode(), RespConstant.INTERNAL_ERROR.getMsg(), 0);
        }
    }

    @PostMapping("/sys/guest/authorization")
    public CommonResult authorization() {
        return null;
    }
}
