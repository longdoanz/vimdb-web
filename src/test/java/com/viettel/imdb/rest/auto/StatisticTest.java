package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.HttpResponse;
import com.viettel.imdb.rest.common.TestHelper;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticTest extends TestHelper {

    @Test(priority = 2)
    public void test_Get_Metric_list() {
        getMetrics().andExpect(HttpStatus.OK).prettyPrint();
    }

    @Test(priority = 3)
    public void test_Get_Statistic_Specific_Node() {
        getStatistic(Collections.singletonList("127.16.31.112:10000"), null).andExpect(HttpStatus.OK).prettyPrint();
    }


    @Test(priority = 4)
    public void test_Get_Statistic_Specific_Metric() {
        getStatistic(null, Collections.singletonList("_vimdb_memory_total_used_memory")).andExpect(HttpStatus.OK).prettyPrint();
    }


    @Test(priority = 5)
    public void test_Get_Statistic_Specific_Node_And_Metric() {
        List<String> hosts = new ArrayList<String>() {{
            add("172.16.31.123:12309");
            add("172.16.31.54:14080");
        }};
        List<String> metrics = new ArrayList<String>() {{
            add("_vimdb_memory_total_used_memory");
            add("_vimdb_request_key_not_exist_read");
            add("_vimdb_request_time_total_read_request_128ms");
        }};


        HttpResponse res = getStatistic(hosts, metrics)
                .prettyPrint()
                .andExpect(HttpStatus.OK)
                /*.andExpectResponse("$.[*].node", new ArrayList<String>() {{
                    add("172.16.31.123:12309");
                    add("172.16.31.54:14080");
                }})*/
        ;
        List<String> nodeList = res.read("$.[*].node");
        Assert.assertTrue(nodeList.size() > 0, "Node list must greater than 0");
    }

}
