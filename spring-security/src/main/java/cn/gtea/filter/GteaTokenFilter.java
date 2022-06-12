package cn.gtea.filter;

import cn.gtea.constant.RespConstant;
import cn.gtea.dto.UserLoginDTO;
import cn.gtea.token.GteaToken;
import cn.gtea.utils.CommonResult;
import cn.gtea.utils.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Ayeze_Mizon
 * 2022-05-14
 */
@Slf4j
public class GteaTokenFilter extends AbstractAuthenticationProcessingFilter {

    public GteaTokenFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/doLogin", "POST"));
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        ServletInputStream in = request.getInputStream();
        ServletOutputStream out = response.getOutputStream();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024];
        while ((in.read(buffer)) != -1) {
            sb.append(new String(buffer, StandardCharsets.UTF_8));
        }
        log.info("获取请求内容 ==> " + sb);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        UserLoginDTO userLoginDTO;
        try {
            userLoginDTO = gson.fromJson(sb.toString().trim(), UserLoginDTO.class);
        } catch (JsonSyntaxException e) {
            log.error(log.getName(), e);
            log.error("字符串转为json出错 ====>" + sb.toString());
            Gson failResult = new GsonBuilder().create();

            CommonResult<String> result = new CommonResult<>(
                    RespConstant.FORMAT_JSON_ERROR.getCode(),
                    RespConstant.FORMAT_JSON_ERROR.getMsg(),
                    RandomUtil.generatedReqNo());
            out.write(failResult.toJson(result).getBytes());
            return null;
        }

        if (userLoginDTO != null && StrUtil.isAllNotEmpty(userLoginDTO.getPrincipal(), userLoginDTO.getCredentials())) {
            GteaToken token = new GteaToken(userLoginDTO.getPrincipal(), userLoginDTO.getCredentials());
            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        }else {
            response.setContentType("application/json;charset=utf-8");
            Gson failResult = new GsonBuilder().create();
            CommonResult<String> result = new CommonResult<>(RespConstant.PARAM_EMPTY.getCode(), "用户名或密码不存在",
                    RandomUtil.generatedReqNo());
            out.write(failResult.toJson(result).getBytes());
            log.error("用户名或密码不存在 ====> " + userLoginDTO);
            return null;
        }
    }

    protected void setDetails(HttpServletRequest request, GteaToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
