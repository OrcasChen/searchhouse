package com.orcas.service.user;

import com.orcas.service.ServiceResult;

/**
 * 验证码服务
 * Created by xcw on 2018/2/22.
 */
public interface ISmsService {

    /**
     * 发送验证码到指定手机 并 缓存验证码 10分钟 及 请求间隔时间 1分钟
     * @param telephone
     * @return
     */
    ServiceResult<String> sendSms(String telephone);

    /**
     * 获取缓存中的验证码
     * @param telephone
     * @return
     */
    String getSmsCode(String telephone);

    /**
     * 移除指定手机的验证码缓存
     * @param telephone
     */
    void remove(String telephone);
}
