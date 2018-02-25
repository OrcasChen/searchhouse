package com.orcas.security;

import com.orcas.base.LoginUserUtil;
import com.orcas.entity.User;
import com.orcas.service.user.ISmsService;
import com.orcas.service.user.IUserService;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created by xcw on 2018/2/22.
 */
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private IUserService userService;

    @Autowired
    private ISmsService smsService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String name = obtainUsername(request);
        if (!Strings.isNullOrEmpty(name)) {
            //用户名密码登陆
            request.setAttribute("username", name);
            return super.attemptAuthentication(request, response);
        }
        //手机登陆
        String telephone = request.getParameter("telephone");
        if (Strings.isNullOrEmpty(telephone) || !LoginUserUtil.checkTelephone(telephone)) {
            throw new BadCredentialsException("Wrong telephone number");
        }

        User user = userService.findUserByTelephone(telephone);

        String inputCode = request.getParameter("smsCode");
        String sessionCode = smsService.getSmsCode(telephone);

        if (Objects.equals(inputCode, sessionCode)) {
            if (user == null) {
                //手机号未注册 注册该用户
                user = userService.addUserByPhone(telephone);
            }
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else {
            throw new BadCredentialsException("smsCodeError");
        }
    }
}
