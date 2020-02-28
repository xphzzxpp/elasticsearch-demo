package com.example.esdemo.test.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author: meng_wei[meng_wei@suixingpay.com]
 * @date: 2020/2/10 15:31
 * @version: V1.0
 * @desc
 */
@Data
public class Person {

  private String id;

  private String name;

  private String address;

  private String desc;

  private String phoneNo;

  private LocalDateTime createTime;


}
