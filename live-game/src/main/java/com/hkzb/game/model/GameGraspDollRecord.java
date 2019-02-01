package com.hkzb.game.model;

public class GameGraspDollRecord {
    private Long id;

    private Long uid;

    private Long pawsPrice;

    private Long roomId;

    private Long anchorId;

    private Long graspdollId;

    private Double multiple;

    private Double totalPrice;

    private Long createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPawsPrice() {
        return pawsPrice;
    }

    public void setPawsPrice(Long pawsPrice) {
        this.pawsPrice = pawsPrice;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public Long getGraspdollId() {
        return graspdollId;
    }

    public void setGraspdollId(Long graspdollId) {
        this.graspdollId = graspdollId;
    }

    public Double getMultiple() {
        return multiple;
    }

    public void setMultiple(Double multiple) {
        this.multiple = multiple;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}