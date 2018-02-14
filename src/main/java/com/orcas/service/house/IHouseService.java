package com.orcas.service.house;

import com.orcas.service.ServiceMultiResult;
import com.orcas.service.ServiceResult;
import com.orcas.web.dto.HouseDTO;
import com.orcas.web.form.DatatableSearch;
import com.orcas.web.form.HouseForm;
import com.orcas.web.form.RentSearch;

/**
 * 房屋管理服务接口
 * Created by xcw on 2018/1/24.
 */
public interface IHouseService {

    ServiceResult<HouseDTO> save(HouseForm houseForm);

    ServiceResult update(HouseForm houseForm);

    ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody);

    /**
     * 查询完整房源信息
     * @param id
     * @return
     */
    ServiceResult<HouseDTO> findCompleteOne(Long id);

    ServiceResult removePhoto(Long id);

    ServiceResult updateCover(Long coverId, Long targetId);

    ServiceResult addTag(Long houseId, String tag);

    ServiceResult removeTag(Long houseId, String tag);

    ServiceResult updateStatus(Long id, int status);

    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);
}
