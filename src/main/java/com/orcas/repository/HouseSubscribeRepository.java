package com.orcas.repository;

import com.orcas.entity.HouseSubscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


/**
 * Created by xcw on 2018/2/26.
 */
public interface HouseSubscribeRepository extends PagingAndSortingRepository<HouseSubscribe, Long> {

    /**
     * 根据房屋id和用户id查询所有房屋预约
     * @param houseId
     * @param loginUserId
     * @return
     */
    HouseSubscribe findByHouseIdAndUserId(Long houseId, Long loginUserId);

    /**
     * 根据用户id和预约状态码查询所有预约信息列表
     * @param userId
     * @param status
     * @param pageable
     * @return
     */
    Page<HouseSubscribe> findAllByUserIdAndStatus(Long userId, int status, Pageable pageable);

    /**
     * 管理员查询预约列表
     * @param adminId
     * @param status
     * @param pageable
     * @return
     */
    Page<HouseSubscribe> findAllByAdminIdAndStatus(Long adminId, int status, Pageable pageable);

    /**
     * 根据管理员id和房屋id查询预约
     * @param houseId
     * @param adminId
     * @return
     */
    HouseSubscribe findByHouseIdAndAdminIdAndUserId(Long houseId, Long adminId, Long useId);


    /**
     * 修改预约状态
     * @param id
     * @param status
     */
    @Modifying
    @Query("update HouseSubscribe as subscribe set subscribe.status = :status where subscribe.id = :id")
    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") int status);
}
