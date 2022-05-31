package cn.gtea.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Ayeze_Mizon
 * 2022-05-12
 */
@Data
public class UserQuery implements Serializable {

    private static final long serialVersionUID = 7068046130977579424L;

    private String principal;
    private String credentials;
    private String type;

}
