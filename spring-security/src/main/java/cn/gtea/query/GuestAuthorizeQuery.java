package cn.gtea.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Ayeze_Mizon
 * 2022-06-12
 */
@Data
public class GuestAuthorizeQuery implements Serializable {
    private static final long serialVersionUID = -8426827916757137707L;

    private String userName;
    private String userRole;
    private String type;
    private String roleName;
    private Integer roleStatus;
}
