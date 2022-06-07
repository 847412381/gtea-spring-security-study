package cn.gtea.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  * 登录用户角色bean
 * @author Ayeze_Mizon
 * 2022-06-07
 */
@Data
@TableName("gtea_ordinary_user_role")
public class GteaOrdinaryUserRoleEntity {

    @TableId
    private Long id;
    @TableField
    private Long ordinaryUserId;
    @TableField
    private String userRole;
}
