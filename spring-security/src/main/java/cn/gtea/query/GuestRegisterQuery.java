package cn.gtea.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Ayeze_Mizon
 * 2022-06-12
 */
@Data
public class GuestRegisterQuery implements Serializable {

    private static final long serialVersionUID = 8478682426940349069L;

    private String principal;
    private String credentials;
    private String type;
}
