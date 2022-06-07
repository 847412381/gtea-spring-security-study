package cn.gtea.constant;

/**
 * @author Ayeze_Mizon
 * 2022-06-08
 */
public enum RespConstant {

    /*-------------------------系统返回--------------------------*/
    /**
     * 成功
     */
    SUCCESS("C00200", "成功"),
    /**
     * 服务器内部错误
     */
    INTERNAL_ERROR("C00500", "服务器内部错误"),
    /**
     * 没有权限
     */
    FORBIDDEN("C00403", "没有权限"),
    /**
     * 未授权
     */
    UNAUTHORIZED("C00401", "未授权"),
    /*-------------------------系统返回--------------------------*/
    /*-------------------------业务返回--------------------------*/
    /**
     * 参数为空
     */
    PARAM_EMPTY("C01001", "参数为空"),
    /**
     * 参数对比不同
     */
    PARAM_COMPARE_DIFF("C01002", "参数对比不同"),
    /**
     * 字符串转JSON出错
     */
    FORMAT_JSON_ERROR("C01003", "字符串转JSON出错")
    /*-------------------------业务返回--------------------------*/
    ;

    private final String code;
    private final String msg;

    RespConstant(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
