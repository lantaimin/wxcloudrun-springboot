package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.TWxMessageLog;

public interface TWxMessageLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TWxMessageLog record);

    int insertSelective(TWxMessageLog record);

    TWxMessageLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TWxMessageLog record);

    int updateByPrimaryKey(TWxMessageLog record);
}