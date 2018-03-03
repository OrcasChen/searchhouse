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

    /**
     * 根据手机号码寻找用户
     * @param telephone
     * @return
     */
    User findUserByTelephone(String telephone);

    /**
     * 通过手机号注册用户
     * @param telephone
     * @return
     */
    User addUserByPhone(String telephone);

    /**
     * 修改指定属性值
     * @param profile
     * @param value
     * @return
     */
    ServiceResult modifyUserProfile(String profile, String value);
}
