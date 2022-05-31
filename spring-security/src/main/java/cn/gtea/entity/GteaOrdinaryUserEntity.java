package cn.gtea.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 登录用户bean
 * @author Ayeze_Mizon
 * 2022-06-01
 */
@Data
public class GteaOrdinaryUserEntity {

    private Long id;
    private String userName;
    private String userPass;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String creator;
    private String updater;
    private Long version;
}
