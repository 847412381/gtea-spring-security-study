package cn.gtea.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *  * 登录用户角色bean
 * @author Ayeze_Mizon
 * 2022-06-07
 */
@Data
@TableName("gtea_ordinary_user_menu")
public class GteaOrdinaryUserRoleEntity {
    /**
     * 默认雪花算法
     */
    @TableId
    private Long id;
    @TableField
    private Long ordinaryUserId;
    @TableField
    private String userRole;
    @TableField
    private String type;
    @TableField
    private String roleName;
    @TableField
    private Integer roleStatus;
    @TableField
    private LocalDateTime createTime;
    @TableField
    private LocalDateTime updateTime;
    @TableField
    private String creator;
    @TableField
    private String updater;
    @TableField
    private Long version;
}
