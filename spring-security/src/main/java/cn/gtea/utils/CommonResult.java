package cn.gtea.utils;

import cn.gtea.constant.RespConstant;
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

    public CommonResult(O data) {
        this.code = RespConstant.SUCCESS.getCode();
        this.msg = RespConstant.SUCCESS.getMsg();
        this.requestNo = RandomUtil.generatedReqNo();
        this.data = data;
    }

    public CommonResult(O data, String requestNo) {
        this.code = RespConstant.SUCCESS.getCode();
        this.msg = RespConstant.SUCCESS.getMsg();
        this.requestNo = requestNo;
        this.data = data;
    }

    public CommonResult(String code, String msg, O data) {
        this.code = code;
        this.msg = msg;
        this.requestNo = RandomUtil.generatedReqNo();
        this.data = data;
    }
}
