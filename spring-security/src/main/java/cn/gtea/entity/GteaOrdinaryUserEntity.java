package cn.gtea.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 登录用户bean
 * @author Ayeze_Mizon
 * 2022-06-01
 */
@Data
@TableName("gtea_ordinary_user")
public class GteaOrdinaryUserEntity {

    @TableId
    private Long id;
    @TableField
    private String userName;
    @TableField
    private String userPass;
    @TableField
    private Integer status;
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
