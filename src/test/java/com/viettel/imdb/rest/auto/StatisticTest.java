package com.viettel.imdb.rest.auto;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.Collections;

public class StatisticTest extends TestHelper {

    @Test(priority = 2)
    public void test_Get_Metric_list() {
        getMetrics().andExpect(HttpStatus.OK);
    }

    @Test(priority = 3)
    public void test_Get_Statistic_Specific_Node() {
        getStatistic(Collections.singletonList("127"), null).andExpect(HttpStatus.OK);
    }


    @Test(priority = 4)
    public void test_Get_Statistic_Specific_Metric() {
        getStatistic(null, Collections.singletonList("total_memory")).andExpect(HttpStatus.OK);
    }


    @Test(priority = 5)
    public void test_Get_Statistic_Specific_Node_And_Metric() {
        getStatistic().andExpect(HttpStatus.OK);
    }

}
