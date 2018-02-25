package com.orcas.service.house;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orcas.entity.Subway;
import com.orcas.entity.SubwayStation;
import com.orcas.entity.SupportAddress;
import com.orcas.repository.HouseRepository;
import com.orcas.repository.SubwayRepository;
import com.orcas.repository.SubwayStationRepository;
import com.orcas.repository.SupportAddressRepository;
import com.orcas.service.ServiceMultiResult;
import com.orcas.service.ServiceResult;
import com.orcas.service.search.BaiduMapLocation;
import com.orcas.web.dto.SubwayDTO;
import com.orcas.web.dto.SubwayStationDTO;
import com.orcas.web.dto.SupportAddressDTO;
import javafx.print.PaperSource;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.util.RadixSelector;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xcw on 2018/1/23.
 */
@Service
public class AddressServiceImpl implements IAddressService {

    private static final Logger logger = LoggerFactory.getLogger(IAddressService.class);

    private static final String BAIDU_MAP_KEY = "c1EU9MERGYuGGEdZt8IBbca9qGP4Vhud";

    //地理编码
    private static final String BAIDU_MAP_GEOCONV_API = "http://api.map.baidu.com/geocoder/v2/?";

    /**
     * POI数据管理接口
     */
    // http://api.map.baidu.com/geodata/v3/poi/create  //POST请求
    private static final String LBS_CREATE_API = "http://api.map.baidu.com/geodata/v3/poi/create";

    // http://api.map.baidu.com/geodata/v3/poi/list // GET请求
    private static final String LBS_QUERY_API = "http://api.map.baidu.com/geodata/v3/poi/list?";

    private static final String LBS_UPDATE_API = "http://api.map.baidu.com/geodata/v3/poi/update";

    private static final String LBS_DELETE_API = "http://api.map.baidu.com/geodata/v3/poi/delete";

    private static final String GEOTABLE_ID = "184677";

    @Autowired
    private SupportAddressRepository supportAddressRepository;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private SubwayStationRepository subwayStationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        List<SupportAddress> addresses = supportAddressRepository.findAllByLevel(SupportAddress.Level.CITY.getValue());

        List<SupportAddressDTO> addressDTOS = new ArrayList<>();
        for (SupportAddress supportAddress : addresses) {
            SupportAddressDTO target = modelMapper.map(supportAddress, SupportAddressDTO.class);
            addressDTOS.add(target);
        }

        return new ServiceMultiResult<>(addressDTOS.size(), addressDTOS);
    }

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityName) {
        if (cityName == null) {
            return new ServiceMultiResult<>(0, null);
        }

        List<SupportAddressDTO> result = new ArrayList<>();
        List<SupportAddress> regions = supportAddressRepository.findAllByLevelAndBelongTo(SupportAddress.Level.REGION.getValue(), cityName);
        for (SupportAddress region : regions) {
            SupportAddressDTO target = modelMapper.map(region, SupportAddressDTO.class);
            result.add(target);
        }
        return new ServiceMultiResult<>(regions.size(), result);
    }

    @Override
    public List<SubwayDTO> findAllSubwayByCity(String cityEnName) {
        List<SubwayDTO> result = new ArrayList<>();
        List<Subway> subways = subwayRepository.findAllByCityEnName(cityEnName);
        if (subways.isEmpty()) {
            return result;
        }
        subways.forEach(subway -> result.add(modelMapper.map(subway, SubwayDTO.class)));
        return result;
    }

    @Override
    public List<SubwayStationDTO> findAllStationBySubway(Long subwaId) {
        List<SubwayStationDTO> result = new ArrayList<>();
        List<SubwayStation> stations = subwayStationRepository.findAllBySubwayId(subwaId);
        if (stations.isEmpty()) {
            return result;
        }
        stations.forEach(station -> result.add(modelMapper.map(station, SubwayStationDTO.class)));
        return result;
    }


    @Override
    public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        Map<SupportAddress.Level, SupportAddressDTO> result = new HashMap<>();

        SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY
                .getValue());
        SupportAddress region = supportAddressRepository.findByEnNameAndBelongTo(regionEnName, city.getEnName());

        result.put(SupportAddress.Level.CITY, modelMapper.map(city, SupportAddressDTO.class));
        result.put(SupportAddress.Level.REGION, modelMapper.map(region, SupportAddressDTO.class));
        return result;
    }

    @Override
    public ServiceResult<SubwayDTO> findSubway(Long subwayId) {
        if (subwayId == null) {
            return ServiceResult.notFound();
        }
        Subway subway = subwayRepository.findOne(subwayId);
        if (subway == null) {
            return ServiceResult.notFound();
        }
        return ServiceResult.of(modelMapper.map(subway, SubwayDTO.class));
    }

    @Override
    public ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId) {
        if (stationId == null) {
            return ServiceResult.notFound();
        }
        SubwayStation station = subwayStationRepository.findOne(stationId);
        if (station == null) {
            return ServiceResult.notFound();
        }
        return ServiceResult.of(modelMapper.map(station, SubwayStationDTO.class));
    }

    @Override
    public ServiceResult<SupportAddressDTO> findCity(String cityEnName) {
        if (cityEnName == null) {
            return ServiceResult.notFound();
        }
        SupportAddress supportAddress = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
        if (supportAddress == null) {
            return ServiceResult.notFound();
        }

        SupportAddressDTO addressDTO = modelMapper.map(supportAddress, SupportAddressDTO.class);

        return ServiceResult.of(addressDTO);
    }

    @Override
    public ServiceResult<BaiduMapLocation> getBaiduMapLocation(String city, String address) {
        String encodeAddress;
        String encodeCity;

        try {
            encodeAddress = URLEncoder.encode(address, "UTF-8");
            encodeCity = URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error to encode house address ", e);
            return new ServiceResult<BaiduMapLocation>(false, "Error to encode house address");
        }

        //传输到http
        HttpClient httpClient = HttpClients.createDefault();
        //URL格式：
        // http://api.map.baidu.com/geocoder/v2/?
        // address=北京市海淀区上地十街10号&output=json
        // &ak=您的ak&callback=sho
        StringBuilder sb = new StringBuilder(BAIDU_MAP_GEOCONV_API);
        sb.append("address=").append(encodeAddress).append("&").append("city=").append(encodeCity).append("&")
                .append("output=json&")
                .append("ak=").append(BAIDU_MAP_KEY);

        //执行
        HttpGet get = new HttpGet(sb.toString());

        try {
            //获取执行结果
            HttpResponse response = httpClient.execute(get);
            //判断响应是否成功200
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return new ServiceResult<BaiduMapLocation>(false, "Cannot get baidu map location");
            }

            //json结构体
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            JsonNode jsonNode = objectMapper.readTree(result);
            //获得api结果的响应码
            int status = jsonNode.get("status").asInt();
            if (status != 0) {
                return new ServiceResult<BaiduMapLocation>(false, "Error to get map location for status: " + status);
            } else {
                BaiduMapLocation location = new BaiduMapLocation();
                JsonNode jsonLocation = jsonNode.get("result").get("location");
                location.setLongitude(jsonLocation.get("lng").asDouble());
                location.setLatitude(jsonLocation.get("lat").asDouble());

                return ServiceResult.of(location);
            }
        } catch (IOException e) {
            logger.error("Error to fetch baidumap api ", e);
            return new ServiceResult<BaiduMapLocation>(false, "Error to fetch baidumap api.");
        }
    }


    @Override
    public ServiceResult lbsUpload(BaiduMapLocation location, String title, String address, long houseId, int price, int area) {

        HttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
        nvps.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));
        nvps.add(new BasicNameValuePair("coord_type", "3"));  //指定数据表格坐标系类型  3: 百度加密经纬度坐标
        nvps.add(new BasicNameValuePair("geotable_id", GEOTABLE_ID));
        nvps.add(new BasicNameValuePair("ak", BAIDU_MAP_KEY));
        nvps.add(new BasicNameValuePair("houseId", String.valueOf(houseId)));
        nvps.add(new BasicNameValuePair("price", String.valueOf(price)));
        nvps.add(new BasicNameValuePair("area", String.valueOf(area)));
        nvps.add(new BasicNameValuePair("title", title));
        nvps.add(new BasicNameValuePair("address", address));

        HttpPost post;
        //判断lbs是否已经存在
        if (lbsDataExists(houseId)) {
            post = new HttpPost(LBS_UPDATE_API);
        } else {
            post = new HttpPost(LBS_CREATE_API);
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = httpClient.execute(post);

            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("Cannot upload lbs data for response: " + result);
                return new ServiceResult(false, "Cannot upload baidu lbs data.");
            } else {
                JsonNode jsonNode = objectMapper.readTree(result);
                int status = jsonNode.get("status").asInt();
                if (status != 0) {
                    String message = jsonNode.get("message").asText();
                    logger.error("Error to upload lbs data for status: {}, and message: {} ", status, message);
                    return new ServiceResult(false, "Error to upload lbs data.");
                } else {
                    return ServiceResult.success();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ServiceResult(false);
    }

    private boolean lbsDataExists(Long houseId) {
        HttpClient httpClient = HttpClients.createDefault();
        StringBuilder sb = new StringBuilder(LBS_QUERY_API);
        sb.append("geotable_id=").append("184677").append("&")
                .append("ak=").append(BAIDU_MAP_KEY).append("&")
                .append("houseId=").append(houseId).append(",").append(houseId);
        HttpGet get = new HttpGet(sb.toString());
        try {
            HttpResponse response = httpClient.execute(get);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("Cannot get lbs data for response: " + result);
                return false;
            }

            JsonNode jsonNode = objectMapper.readTree(result);
            int status = jsonNode.get("status").asInt();
            if (status != 0) {
                logger.error("Error to get lbs data for status: " + status);
                return false;
            } else {
                long size = jsonNode.get("size").asLong();
                if (size > 0) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (IOException e) {
            logger.error("Error to get lbs data.", e);
            return false;
        }
    }

    @Override
    public ServiceResult removeLbs(Long houseId) {
        HttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("geotable_id", GEOTABLE_ID));
        nvps.add(new BasicNameValuePair("ak", BAIDU_MAP_KEY));
        nvps.add(new BasicNameValuePair("houseId", String.valueOf(houseId)));

        HttpPost delete = new HttpPost(LBS_DELETE_API);
        try {
            delete.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = httpClient.execute(delete);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("Error to delete lbs data for response: " + result);
                return new ServiceResult(false, "Error to delete lbs data.");
            }

            JsonNode jsonNode = objectMapper.readTree(result);
            int status = jsonNode.get("status").asInt();
            if (status != 0) {
                String message = jsonNode.get("message").asText();
                logger.error("Error to delete lbs data for message: " + message);
                return new ServiceResult(false, "Error to delete lbs data for message: " + message);
            }

            return ServiceResult.success();
        } catch (IOException e) {
            logger.error("Error to delete lbs data. ", e);
            return new ServiceResult(false);
        }
    }
}
