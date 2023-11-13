package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dao.TCustomerBaseMapper;
import com.tencent.wxcloudrun.dto.QueryCustomerInfoReq;
import com.tencent.wxcloudrun.model.TCustomerBase;
import com.tencent.wxcloudrun.service.WxMessageService;
import com.tencent.wxcloudrun.xfxh.component.XfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

/**
 * counter控制器
 */
@RestController

public class CounterController {

    final CounterService counterService;
    final Logger logger;

    @Autowired
    private TCustomerBaseMapper customerBaseMapper;

    public CounterController(@Autowired CounterService counterService) {
        this.counterService = counterService;
        this.logger = LoggerFactory.getLogger(CounterController.class);
    }

    @Autowired
    private WxMessageService wxMessageService;

    @PostMapping(value = "/api/message")
    String message(@RequestBody String reqData) {
        return wxMessageService.receiveMsg(reqData);
    }

    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/api/count")
    ApiResponse get() {
        logger.info("/api/count get request");
        Optional<Counter> counter = counterService.getCounter(1);
        Integer count = 0;
        if (counter.isPresent()) {
            count = counter.get().getCount();
        }

        return ApiResponse.ok(count);
    }


    /**
     * 更新计数，自增或者清零
     *
     * @param request {@link CounterRequest}
     * @return API response json
     */
    @PostMapping(value = "/api/count")
    ApiResponse create(@RequestBody CounterRequest request) {
        logger.info("/api/count post request, action: {}", request.getAction());

        Optional<Counter> curCounter = counterService.getCounter(1);
        if (request.getAction().equals("inc")) {
            Integer count = 1;
            if (curCounter.isPresent()) {
                count += curCounter.get().getCount();
            }
            Counter counter = new Counter();
            counter.setId(1);
            counter.setCount(count);
            counterService.upsertCount(counter);
            return ApiResponse.ok(count);
        } else if (request.getAction().equals("clear")) {
            if (!curCounter.isPresent()) {
                return ApiResponse.ok(0);
            }
            counterService.clearCount(1);
            return ApiResponse.ok(0);
        } else {
            return ApiResponse.error("参数action错误");
        }
    }

    @PostMapping(value = "/api/queryCustomerInfo")
    ApiResponse queryCustomerInfo(@RequestBody QueryCustomerInfoReq req) {
        logger.info("查询客户信息/api/queryCustomerInfo post request: {}", req.toString());
        List<TCustomerBase> customerList = customerBaseMapper.queryByCustomerName(req.getCustomerName());
        return ApiResponse.ok(customerList);
    }
}