/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Offer implements Serializable {

    @SerializedName("id")
    private Long id;
    @SerializedName("fromLabelName")
    private String fromLabelName;
    @SerializedName("toLabelName")
    private String toLabelName;
    @SerializedName("createTime")
    private Date createTime;
    @SerializedName("finishTime")
    private Date finishTime;
    @SerializedName("clientId")
    private String clientId;
    @SerializedName("comment")
    private String comment;
    @SerializedName("autoClosed")
    private Boolean autoClosed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToLabelName() {
        return toLabelName;
    }

    public void setToLabelName(String toLabelName) {
        this.toLabelName = toLabelName;
    }

    public String getFromLabelName() {
        return fromLabelName;
    }

    public void setFromLabelName(String fromLabelName) {
        this.fromLabelName = fromLabelName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getAutoClosed() {
        return autoClosed;
    }

    public void setAutoClosed(Boolean autoClosed) {
        this.autoClosed = autoClosed;
    }

    public Offer(OfferBuilder offerBuilder) {
        this.id = offerBuilder.id;
        this.fromLabelName = offerBuilder.fromLabelName;
        this.toLabelName = offerBuilder.toLabelName;
        this.createTime = offerBuilder.createTime;
        this.finishTime = offerBuilder.finishTime;
        this.clientId = offerBuilder.clientId;
        this.comment = offerBuilder.comment;
        this.autoClosed = offerBuilder.autoClosed;
    }

    public static class OfferBuilder {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        private Long id;
        private String fromLabelName;
        private String toLabelName;
        private Date createTime;
        private Date finishTime;
        private String clientId;
        private String comment;
        private Boolean autoClosed;

        public OfferBuilder() {
            super();
        }

        public Offer build() {
            return new Offer(this);
        }

        public OfferBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OfferBuilder fromLabelName(String fromLabelName) {
            this.fromLabelName = fromLabelName;
            return this;
        }

        public OfferBuilder toLabelName(String toLabelName) {
            this.toLabelName = toLabelName;
            return this;
        }

        public OfferBuilder createTime(String createTime) throws ParseException {
            if (createTime.equals("null")) {
                this.createTime = null;
            } else {
                this.createTime = dateFormatter.parse(createTime);
            }
            return this;
        }

        public OfferBuilder finishTime(String finishTime) throws ParseException {
            if (finishTime.equals("null")) {
                this.finishTime = null;
            } else {
                this.finishTime = dateFormatter.parse(finishTime);
            }
            return this;
        }

        public OfferBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public OfferBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public OfferBuilder autoClosed(String autoClosed) {
            if (autoClosed.equals("null")) {
                this.autoClosed = null;
            } else {
                this.autoClosed = Boolean.parseBoolean(autoClosed);
            }
            return this;
        }
    }

}
