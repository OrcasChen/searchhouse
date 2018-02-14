package com.orcas.service.search;

import com.orcas.service.ServiceMultiResult;
import com.orcas.web.form.RentSearch;

/**
 * 检索接口
 * Created by xcw on 2018/2/9.
 */
public interface ISearchService {

    /**
     * 索引目标房源
     * @param houseId
     */
    void index(Long houseId);

    /**
     * 移除房源索引
     * @param houseId
     */
    void remove(Long houseId);

    /**
     * 查询房源接口
     * @param rentSearch
     * @return 回传houseId集合
     */
    ServiceMultiResult<Long> query(RentSearch rentSearch);
}
