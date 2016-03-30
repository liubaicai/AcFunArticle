/**
  * Copyright 2016 aTool.org 
  */
package net.liubaicai.android.acfun.models;

/**
 * Auto-generated: 2016-03-24 0:31:22
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class ChannelResult {

    private boolean success;
    private String msg;
    private int status;
    private ChannelData data;
    public void setSuccess(boolean success) {
         this.success = success;
     }
     public boolean getSuccess() {
         return success;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setData(ChannelData data) {
         this.data = data;
     }
     public ChannelData getData() {
         return data;
     }

}