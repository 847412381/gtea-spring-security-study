package cn.gtea.service;

import cn.gtea.config.GteaUser;
import cn.gtea.dao.GteaOrdinaryUserDao;
import cn.gtea.dao.GteaOrdinaryUserRoleDao;
import cn.gtea.dto.AuthenticationDTO;
import cn.gtea.entity.GteaOrdinaryUserEntity;
import cn.gtea.entity.GteaOrdinaryUserRoleEntity;
import cn.gtea.token.GteaToken;
import cn.gtea.utils.GsonUtil;
import cn.gtea.utils.JedisUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author Ayeze_Mizon
 * 2022-05-29
 */
@Slf4j
@Component
public class GteaUserDetailServiceManager implements UserDetailsManager {

    @Autowired(required = false)
    private GteaOrdinaryUserDao gteaOrdinaryUserDao;
    @Autowired(required = false)
    private GteaOrdinaryUserRoleDao gteaOrdinaryUserRoleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void createUser(UserDetails user) {
        Jedis jedis = JedisUtil.create();
        Gson gson = GsonUtil.includeNullCreate();
        try {
            GteaUser gteaUser = new GteaUser(user.getUsername(), user.getPassword(), user.getAuthorities());
            BeanUtils.copyProperties(user, gteaUser);
            //set集合拷贝后会为null
            String gteaUserStr = gson.toJson(gteaUser);
            Long redisRecord = jedis.hset(gteaUser.getUsername(), gteaUser.getUsername(), gteaUserStr);
            log.info("[ redis创建用户 ] ==> " + redisRecord);
            log.info("[ redis创建用户 ] ==> " + gteaUserStr);
        } finally {
            jedis.close();
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        Jedis jedis = JedisUtil.create();
        Gson gson = GsonUtil.includeNullCreate();
        try {
            AuthenticationDTO authDTO = new AuthenticationDTO();
            BeanUtils.copyProperties(user, authDTO);
            String userStr = gson.toJson(authDTO);
            Long redisRecord = jedis.hset(authDTO.getUsername(), authDTO.getUsername(), userStr);
            log.info("[ redis更新用户 ] ==> " + redisRecord);
        } finally {
            jedis.close();
        }
    }

    @Override
    public void deleteUser(String username) {
        Jedis jedis = JedisUtil.create();
        try {
            Long redisRecord = jedis.hdel(username);
            log.info("[ redis删除用户 ] ==> " + redisRecord);
        } finally {
            jedis.close();
        }
    }

    /**
     * 先获取当前用户，查看旧密码是否与当前用户密码相等，再改密码
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Jedis jedis = JedisUtil.create();
        Gson gson = GsonUtil.includeNullCreate();
        try {
            Authentication currUser = SecurityContextHolder.getContext().getAuthentication();
            if (currUser == null) {
                throw new AccessDeniedException(
                        "Can't change password as no Authentication object found in context "
                                + "for current authenticationDTO.");
            }
            String name = currUser.getName();
            // If an authentication manager has been set, re-authenticate the authenticationDTO with the
            // supplied password.
            if (authenticationManager != null) {
                authenticationManager.authenticate(new GteaToken(name, oldPassword));
            }else {
                log.debug("No authentication manager set. Password won't be re-checked.");
            }
            String authDtoStr = jedis.hget(name, name);
            AuthenticationDTO authDTO = gson.fromJson(authDtoStr, AuthenticationDTO.class);
            authDTO.setPassword(newPassword);
            String newUserStr = gson.toJson(authDTO);
            jedis.hset(name, name, newUserStr);
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean userExists(String username) {
        Jedis jedis = JedisUtil.create();
        String user;
        try {
            user = jedis.get(username);
        } finally {
            jedis.close();
        }
        return StrUtil.isNotEmpty(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GteaOrdinaryUserEntity entity = gteaOrdinaryUserDao.selectOne(
                new LambdaQueryWrapper<GteaOrdinaryUserEntity>()
                        .eq(GteaOrdinaryUserEntity::getUserName, username));
        if (ObjectUtil.isNull(entity)) {
            throw new RuntimeException("用户不存在");
        }
        List<GteaOrdinaryUserRoleEntity> rolesEntity = gteaOrdinaryUserRoleDao.selectList(
                new LambdaQueryWrapper<GteaOrdinaryUserRoleEntity>()
                        .eq(GteaOrdinaryUserRoleEntity::getOrdinaryUserId, entity.getId()));
        if (CollectionUtil.isEmpty(rolesEntity)) {
            throw new RuntimeException("用户无角色权限");
        }
        String[] roles = new String[rolesEntity.size()];
        for (int i = 0; i < rolesEntity.size(); i++) {
            roles[i] = rolesEntity.get(i).getUserRole();
        }

        return GteaUser.builder()
                .username(entity.getUserName())
                .password(entity.getUserPass())
                .authorities(roles)
                .passwordEncoder(t -> passwordEncoder.encode(t)).build();

//        AuthenticationDTO authenticationDTO;
//        Jedis jedis = JedisUtil.create();
//        GteaUser gteaUser = new GteaUser();
//        try {
//            Gson gson = GsonUtil.includeNullCreate();
//            String gtea = jedis.hget(username, username);
//            if (StrUtil.isEmpty(gtea)) {
//                throw new RuntimeException("redis查询用户不存在");
//            }
//            log.info("redis查出结果 ===> " + gtea);
//            authenticationDTO = gson.fromJson(gtea, AuthenticationDTO.class);
//            BeanUtils.copyProperties(authenticationDTO, gteaUser);
//        } finally {
//            jedis.close();
//        }
//        return gteaUser;
    }
}
