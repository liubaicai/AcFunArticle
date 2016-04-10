package net.liubaicai.android.acfun.models;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Baicai on 2016/4/6.
 */
public class CommentData {
    private int totalPage;
    private int pageSize;
    private int page;
    private int totalCount;

    private Map<String,CommentContent> commentContentArr;
    private boolean desc;
    private List<Integer> commentList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Map<String,CommentContent> getCommentContentArr() {
        return commentContentArr;
    }

    public void setCommentContentArr(Map<String,CommentContent> commentContentArr) {
        this.commentContentArr = commentContentArr;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public List<Integer> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Integer> commentList) {
        this.commentList = commentList;
    }
}
