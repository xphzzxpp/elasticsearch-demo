package com.example.esdemo.test.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: meng_wei[meng_wei@suixingpay.com]
 * @date: 2020/2/7 11:37
 * @version: V1.0
 * @desc
 */
public class OrderTest {

  private String id;

  private String title;

  private String desc;

  private BigDecimal amout;

  private String imgUrl;

  private LocalDateTime createTime;

  public OrderTest(String id, String title, String desc, BigDecimal amout, String imgUrl,
      LocalDateTime createTime) {
    this.id = id;
    this.title = title;
    this.desc = desc;
    this.amout = amout;
    this.imgUrl = imgUrl;
    this.createTime = createTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public BigDecimal getAmout() {
    return amout;
  }

  public void setAmout(BigDecimal amout) {
    this.amout = amout;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }
}
