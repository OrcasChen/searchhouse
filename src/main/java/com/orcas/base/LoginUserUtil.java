package com.orcas.base;

import com.orcas.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by xcw on 2018/1/24.
 */
public class LoginUserUtil {

    public static User load() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal != null && principal instanceof User) {
            return (User)principal;
        }
        return null;

    }

    public static Long getLoginUserId() {
        User user = load();
        if (user == null) {
            return -1L;
        }

        return user.getId();
    }

}
