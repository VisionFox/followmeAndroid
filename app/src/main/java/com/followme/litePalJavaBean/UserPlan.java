package com.followme.litePalJavaBean;

import android.util.Log;

import com.followme.bean.Attraction;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class UserPlan extends DataSupport {
    private Integer uid;
    private Integer planNo;
    private Long attractionId;

    public UserPlan(Integer uid, Integer planNo, Long attractionId) {
        this.uid = uid;
        this.planNo = planNo;
        this.attractionId = attractionId;
    }

    public UserPlan() {
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPlanNo() {
        return planNo;
    }

    public void setPlanNo(Integer planNo) {
        this.planNo = planNo;
    }

    public Long getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Long attractionId) {
        this.attractionId = attractionId;
    }

    @Override
    public String toString() {
        return "UserPlan{" +
                "uid=" + uid +
                ", planNo=" + planNo +
                ", attractionId=" + attractionId +
                '}';
    }
}
