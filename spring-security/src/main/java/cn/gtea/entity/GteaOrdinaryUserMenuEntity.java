package cn.gtea.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Ayeze_Mizon
 * 2022-06-12
 */
@Data
public class GteaOrdinaryUserMenuEntity {
    /**
     * 默认雪花算法
     */
    @TableId
    private Long id;
    @TableField
    private Long ordinaryUserId;
    @TableField
    private String menuName;
    @TableField
    private String menuStatus;
    @TableField
    private String type;
    @TableField
    private String userMenu;
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
