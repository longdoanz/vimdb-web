package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.common.TestUtil;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * @author quannh22
 * @since 16/10/2019
 */
public class StatisticServiceImplTest extends TestUtil {

    @Test
    public void testGetMetrics() {
        testGetMetrics(new ArrayList<>(), HttpStatus.OK, null);
    }

    @Test
    public void testGetStatistics() {
        testGetStatistic(new ArrayList<>(), new ArrayList<>(), HttpStatus.OK, null);
    }
}