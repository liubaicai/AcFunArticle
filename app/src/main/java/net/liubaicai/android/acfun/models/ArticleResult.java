/**
  * Copyright 2016 aTool.org 
  */
package net.liubaicai.android.acfun.models;

import java.util.List;

/**
 * Auto-generated: 2016-04-04 11:49:53
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class ArticleResult {


    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {

        private ArticleBean article;
        private int channelId;
        private int contentId;
        private String cover;
        private String description;
        private int display;
        private int isArticle;
        private boolean isComment;
        private int isRecommend;
        /**
         * avatar : http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201607/041600109bi14k95.jpg
         * id : 178775
         * name : 狗头神教污妖王
         */

        private OwnerBean owner;
        private long releaseDate;
        private int status;
        private String title;
        private int topLevel;
        private long updatedAt;
        private int viewOnly;
        /**
         * comments : 82
         * danmakuSize : 0
         * goldBanana : 0
         * score : 0
         * stows : 46
         * ups : 0
         * views : 4417
         */

        private VisitBean visit;
        private List<String> tags;
        private String image;
        private int parentChannelId;

        public ArticleBean getArticle() {
            return article;
        }

        public void setArticle(ArticleBean article) {
            this.article = article;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public int getContentId() {
            return contentId;
        }

        public void setContentId(int contentId) {
            this.contentId = contentId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getDisplay() {
            return display;
        }

        public void setDisplay(int display) {
            this.display = display;
        }

        public int getIsArticle() {
            return isArticle;
        }

        public void setIsArticle(int isArticle) {
            this.isArticle = isArticle;
        }

        public boolean getisComment() {
            return isComment;
        }

        public void setisComment(boolean isComment) {
            this.isComment = isComment;
        }


        public int getIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(int isRecommend) {
            this.isRecommend = isRecommend;
        }

        public OwnerBean getOwner() {
            return owner;
        }

        public void setOwner(OwnerBean owner) {
            this.owner = owner;
        }

        public long getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(long releaseDate) {
            this.releaseDate = releaseDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTopLevel() {
            return topLevel;
        }

        public void setTopLevel(int topLevel) {
            this.topLevel = topLevel;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getViewOnly() {
            return viewOnly;
        }

        public void setViewOnly(int viewOnly) {
            this.viewOnly = viewOnly;
        }

        public VisitBean getVisit() {
            return visit;
        }

        public void setVisit(VisitBean visit) {
            this.visit = visit;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getParentChannelId() {
            return parentChannelId;
        }

        public void setParentChannelId(int parentChannelId) {
            this.parentChannelId = parentChannelId;
        }

        public static class ArticleBean {
            private String content;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class OwnerBean {
            private String avatar;
            private int id;
            private String name;
            private int verified;
            private String verifiedText;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getVerified() {
                return verified;
            }

            public void setVerified(int verified) {
                this.verified = verified;
            }

            public String getVerifiedText() {
                return verifiedText;
            }

            public void setVerifiedText(String verifiedText) {
                this.verifiedText = verifiedText;
            }
        }

        public static class VisitBean {
            private int comments;
            private int danmakuSize;
            private int goldBanana;
            private int score;
            private int stows;
            private int ups;
            private int views;

            public int getComments() {
                return comments;
            }

            public void setComments(int comments) {
                this.comments = comments;
            }

            public int getDanmakuSize() {
                return danmakuSize;
            }

            public void setDanmakuSize(int danmakuSize) {
                this.danmakuSize = danmakuSize;
            }

            public int getGoldBanana() {
                return goldBanana;
            }

            public void setGoldBanana(int goldBanana) {
                this.goldBanana = goldBanana;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getStows() {
                return stows;
            }

            public void setStows(int stows) {
                this.stows = stows;
            }

            public int getUps() {
                return ups;
            }

            public void setUps(int ups) {
                this.ups = ups;
            }

            public int getViews() {
                return views;
            }

            public void setViews(int views) {
                this.views = views;
            }
        }
    }
}