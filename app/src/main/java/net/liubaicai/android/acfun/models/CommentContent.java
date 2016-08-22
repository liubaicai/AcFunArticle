package net.liubaicai.android.acfun.models;

import java.util.List;

/**
 * Created by Baicai on 2016/4/6.
 */
public class CommentContent {
    private int cid;
    private int quoteId;
    private String content;
    private String postDate;
    private int userID;
    private String userName;
    private String userImg;
    private int count;
    private int deep;
    private int refCount;
    private int ups;
    private int downs;
    private int nameRed;
    private int avatarFrame;

    private String isDelete;
    private String isUpDelete;

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getisUpDelete() {
        return isUpDelete;
    }

    public void setisUpDelete(String isUpDelete) {
        this.isUpDelete = isUpDelete;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public int getRefCount() {
        return refCount;
    }

    public void setRefCount(int refCount) {
        this.refCount = refCount;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public int getNameRed() {
        return nameRed;
    }

    public void setNameRed(int nameRed) {
        this.nameRed = nameRed;
    }

    public int getAvatarFrame() {
        return avatarFrame;
    }

    public void setAvatarFrame(int avatarFrame) {
        this.avatarFrame = avatarFrame;
    }
}
