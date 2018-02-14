package com.orcas.service.user;

import com.orcas.entity.User;
import com.orcas.service.ServiceResult;
import com.orcas.web.dto.UserDTO;

/**
 * 用户服务
 * Created by xcw on 2018/1/22.
 */
public interface IUserService {

    User findUserByName(String userName);

    ServiceResult<UserDTO> findById(Long userId);
}
