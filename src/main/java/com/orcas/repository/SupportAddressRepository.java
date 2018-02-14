package com.orcas.repository;

import com.orcas.entity.SupportAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by xcw on 2018/1/23.
 */
public interface SupportAddressRepository extends CrudRepository<SupportAddress, Long> {

    /**
     * 获取所有对应行政级别的信息
     * @return
     */
    List<SupportAddress> findAllByLevel(String level);

    List<SupportAddress> findAllByLevelAndBelongTo(String level, String belongTo);

    SupportAddress findByEnNameAndLevel(String cityEnName, String value);

    SupportAddress findByEnNameAndBelongTo(String regionEnName, String enName);
}
