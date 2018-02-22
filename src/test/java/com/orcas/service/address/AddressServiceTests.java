package com.orcas.service.address;

import com.orcas.ApplicationTests;
import com.orcas.service.ServiceResult;
import com.orcas.service.house.IAddressService;
import com.orcas.service.search.BaiduMapLocation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xcw on 2018/2/18.
 */
public class AddressServiceTests extends ApplicationTests{

    @Autowired
    private IAddressService addressService;

    @Test
    public void testGetMapLocation() {
        String city = "北京";
        String address = "北京市昌平区巩华家园1号楼2单元";
        ServiceResult<BaiduMapLocation> serviceResult = addressService.getBaiduMapLocation(city, address);

        Assert.assertTrue(serviceResult.isSuccess());
        Assert.assertTrue(serviceResult.getResult().getLongitude() > 0);
        Assert.assertTrue(serviceResult.getResult().getLatitude() > 0);
    }
}
