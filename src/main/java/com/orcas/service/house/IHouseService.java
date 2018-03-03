package com.orcas.service.house;

import com.orcas.base.HouseSubscribeStatus;
import com.orcas.service.ServiceMultiResult;
import com.orcas.service.ServiceResult;
import com.orcas.web.dto.HouseDTO;
import com.orcas.web.dto.HouseSubscribeDTO;
import com.orcas.web.form.DatatableSearch;
import com.orcas.web.form.HouseForm;
import com.orcas.web.form.MapSearch;
import com.orcas.web.form.RentSearch;
import org.springframework.data.util.Pair;

import java.util.Date;

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

    /**
     * 移除图片
     * @param id
     * @return
     */
    ServiceResult removePhoto(Long id);

    /**
     * 更新封面
     * @param coverId
     * @param targetId
     * @return
     */
    ServiceResult updateCover(Long coverId, Long targetId);

    /**
     * 新增标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult addTag(Long houseId, String tag);

    /**
     * 移除标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult removeTag(Long houseId, String tag);

    /**
     * 更新房源状态
     * @param id
     * @param status
     * @return
     */
    ServiceResult updateStatus(Long id, int status);

    /**
     * 查询房源信息集
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);

    /**
     * 全地图查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearch mapSearch);

    /**
     * 精确范围查询
     * @param mapSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> boundMapQuery(MapSearch mapSearch);

    /**
     * 加入预约清单
     * @param houseId
     * @return
     */
    ServiceResult addSubscribeOrder(Long houseId);

    /**
     * 获取对应状态的预约列表   (两元素元组)
     * @param status
     * @param start
     * @param size
     * @return
     */
    ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> querySubscribeList(HouseSubscribeStatus status, int start, int size);

    /**
     * 预约看房实现
     * @param houseId
     * @param orderTime
     * @param desc
     * @param telephone
     * @return
     */
    ServiceResult subscribe(Long houseId, Date orderTime, String desc, String telephone);

    /**
     * 取消预约
     * @param houseId
     * @return
     */
    ServiceResult cancelSubscribe(Long houseId);

    /**
     * 管理员查询预约信息接口
     * @param start
     * @param size
     * @return
     */
    ServiceMultiResult<Pair<HouseDTO,HouseSubscribeDTO>> findSubscribeList(int start, int size);

    /**
     * 完成预约
     * @param houseId
     * @param userId
     * @return
     */
    ServiceResult finishSubscribe(Long houseId, Long userId);
}
