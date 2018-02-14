package com.orcas.service.house;

import com.orcas.entity.SupportAddress;
import com.orcas.service.ServiceMultiResult;
import com.orcas.service.ServiceResult;
import com.orcas.web.dto.SubwayDTO;
import com.orcas.web.dto.SubwayStationDTO;
import com.orcas.web.dto.SupportAddressDTO;

import java.util.List;
import java.util.Map;


/**
 * 地址服务接口
 * Created by xcw on 2018/1/23.
 */
public interface IAddressService {

    /**
     * 获取所有支持的城市列表
     * @return
     */
    ServiceMultiResult<SupportAddressDTO> findAllCities();

    /**
     * 根据城市英文简写获取该城市所有支持的区域信息
     * @param cityName
     * @return
     */
    ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityName);

    /**
     * 获取该城市所有的地铁线路
     * @param cityEnName
     * @return
     */
    List<SubwayDTO> findAllSubwayByCity(String cityEnName);

    List<SubwayStationDTO> findAllStationBySubway(Long subwaId);

    /**
     * 根据英文简写获取具体区域的信息
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);

    /**
     * 获取地铁线信息
     * @param subwayId
     * @return
     */
    ServiceResult<SubwayDTO> findSubway(Long subwayId);

    /**
     * 获取地铁站点信息
     * @param stationId
     * @return
     */
    ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId);


    ServiceResult<SupportAddressDTO> findCity(String cityEnName);
}
