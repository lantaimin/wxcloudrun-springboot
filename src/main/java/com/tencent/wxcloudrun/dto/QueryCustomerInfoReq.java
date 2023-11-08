package com.tencent.wxcloudrun.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QueryCustomerInfoReq {
  private String customerName;
  private String customerId;
}
