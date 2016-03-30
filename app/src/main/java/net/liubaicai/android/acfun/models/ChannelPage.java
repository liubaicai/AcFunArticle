/**
  * Copyright 2016 aTool.org 
  */
package net.liubaicai.android.acfun.models;
import java.util.Collection;
import java.util.List;
/**
 * Auto-generated: 2016-03-24 0:31:22
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class ChannelPage {

    private int pageNo;
    private int pageSize;
    private int totalCount;
    private int orderBy;
    private List<ChannelList> list;
    public void setPageNo(int pageNo) {
         this.pageNo = pageNo;
     }
     public int getPageNo() {
         return pageNo;
     }

    public void setPageSize(int pageSize) {
         this.pageSize = pageSize;
     }
     public int getPageSize() {
         return pageSize;
     }

    public void setTotalCount(int totalCount) {
         this.totalCount = totalCount;
     }
     public int getTotalCount() {
         return totalCount;
     }

    public void setOrderBy(int orderBy) {
         this.orderBy = orderBy;
     }
     public int getOrderBy() {
         return orderBy;
     }

    public void setList(List<ChannelList> list) {
         this.list = list;
     }
     public List<ChannelList> getList() {
         return list;
     }

}