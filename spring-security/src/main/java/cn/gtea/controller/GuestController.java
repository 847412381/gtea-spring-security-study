package cn.gtea.controller;

import cn.gtea.constant.RespConstant;
import cn.gtea.dao.GteaOrdinaryUserDao;
import cn.gtea.dao.GteaOrdinaryUserRoleDao;
import cn.gtea.entity.GteaOrdinaryUserEntity;
import cn.gtea.entity.GteaOrdinaryUserRoleEntity;
import cn.gtea.query.GuestAuthorizeQuery;
import cn.gtea.query.GuestRegisterQuery;
import cn.gtea.utils.CommonResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * 用户注册
     * @param query
     * @return
     */
    @PostMapping("/sys/guest/register")
    public CommonResult<String> register(GuestRegisterQuery query) {
        try {
            GteaOrdinaryUserEntity entity = new GteaOrdinaryUserEntity();
            entity.setUserName(query.getPrincipal());
            entity.setUserPass(passwordEncoder.encode(query.getCredentials()));
            entity.setStatus(1);
            entity.setCreator("admin");
            entity.setUpdater("admin");
            int insert = gteaOrdinaryUserDao.insert(entity);
            return new CommonResult<>(String.valueOf(insert));
        } catch (Exception e) {
            log.error(FLAG + ".register", e);
            return new CommonResult<>(RespConstant.INTERNAL_ERROR.getCode(), RespConstant.INTERNAL_ERROR.getMsg(), e.getMessage());
        }
    }

    /**
     * 用户授权
     * @param query
     * @return
     */
    @PostMapping("/sys/guest/authorize")
    @PreAuthorize("hasAuthority('admin')")
    public CommonResult<String> authorize(GuestAuthorizeQuery query) {
        try {
            GteaOrdinaryUserEntity entity = gteaOrdinaryUserDao.selectOne(new LambdaQueryWrapper<GteaOrdinaryUserEntity>().eq(GteaOrdinaryUserEntity::getUserName, query.getUserName()));
            if (ObjectUtil.isNull(entity)) {
                return new CommonResult<>(RespConstant.EMPTY_RESULT.getCode(), RespConstant.EMPTY_RESULT.getMsg(), null);
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            GteaOrdinaryUserRoleEntity roleEntity = new GteaOrdinaryUserRoleEntity();
            roleEntity.setOrdinaryUserId(entity.getId());
            roleEntity.setRoleName(query.getRoleName());
            roleEntity.setUserRole(query.getUserRole());
            roleEntity.setRoleStatus(1);
            roleEntity.setType(query.getType());
            roleEntity.setCreator((String) authentication.getPrincipal());
            roleEntity.setUpdater((String) authentication.getPrincipal());
            int insert = gteaOrdinaryUserRoleDao.insert(roleEntity);
            return new CommonResult<>(String.valueOf(insert));
        } catch (Exception e) {
            log.error(FLAG + ".authorize", e);
            return new CommonResult<>(RespConstant.INTERNAL_ERROR.getCode(), RespConstant.INTERNAL_ERROR.getMsg(), e.getMessage());
        }
    }
}
