package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.TCustomerBase;

import java.util.List;

public interface TCustomerBaseMapper {
    int deleteByPrimaryKey(String customerId);

    int insert(TCustomerBase record);

    int insertSelective(TCustomerBase record);

    TCustomerBase selectByPrimaryKey(String customerId);

    List<TCustomerBase> queryByCustomerName(String customerName);

    int updateByPrimaryKeySelective(TCustomerBase record);

    int updateByPrimaryKey(TCustomerBase record);
}