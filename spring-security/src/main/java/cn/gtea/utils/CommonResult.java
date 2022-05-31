package cn.gtea.utils;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ayeze_Mizon
 * 2022-05-22
 */

@Getter
@Setter
public class CommonResult<O> {

    private String code;
    private String msg;
    private String requestNo;
    private O data;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("msg", msg)
                .add("requestNo", requestNo)
                .add("data", data)
                .toString();
    }

    public CommonResult(O data, String requestNo) {
        this.code = "C00200";
        this.msg = "ok";
        this.requestNo = requestNo;
        this.data = data;
    }

    public CommonResult(String code, String msg, String requestNo, O data) {
        this.code = code;
        this.msg = msg;
        this.requestNo = requestNo;
        this.data = data;
    }
}
