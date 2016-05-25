package com.reminisense.fa.models;

/**
 * Created by Nigs on 2016-05-18.
 */
public class VerifyRequest {
    private int companyId;
    private String tag;
    private int tagType;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }
}
