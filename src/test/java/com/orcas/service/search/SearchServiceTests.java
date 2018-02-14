package com.orcas.service.search;

import com.orcas.ApplicationTests;
import com.orcas.service.ServiceMultiResult;
import com.orcas.web.form.RentSearch;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xcw on 2018/2/10.
 */
public class SearchServiceTests extends ApplicationTests{

    @Autowired
    private ISearchService searchService;

    @Test
    public void testIndex() {
        Long targetHouseId = 31L;
        searchService.index(targetHouseId);
    }

    @Test
    public void testRemove() {
        long targetHouseId = 15L;
        searchService.remove(targetHouseId);
    }

    @Test
    public void testQuery() {
        RentSearch rentSearch = new RentSearch();
        rentSearch.setCityEnName("bj");
        rentSearch.setStart(0);
        rentSearch.setSize(10);
        ServiceMultiResult<Long> serviceResult = searchService.query(rentSearch);
        Assert.assertEquals(8, serviceResult.getTotal());
    }
}
