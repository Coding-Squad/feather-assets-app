package com.reminisense.fa.models;

/**
 * Created by CHARLES on 5/18/2016.
 */
public class Asset {

    private int companyId;
    private int ownerId;
    private String name;
    private String description;
    private String takeOutInfo;;
    private String imageUrls;
    private boolean takeOutAllowed;
    private String tag;
    private int tagType;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTakeOutInfo() {
        return takeOutInfo;
    }

    public void setTakeOutInfo(String takeOutInfo) {
        this.takeOutInfo = takeOutInfo;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public boolean isTakeOutAllowed() {
        return takeOutAllowed;
    }

    public void setTakeOutAllowed(boolean takeOutAllowed) {
        this.takeOutAllowed = takeOutAllowed;
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

    @Override
    public String toString() {
        return "Asset{" +
                "companyId=" + companyId +
                ", ownerId=" + ownerId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", takeOutInfo='" + takeOutInfo + '\'' +
                ", imageUrls='" + imageUrls + '\'' +
                ", takeOutAllowed=" + takeOutAllowed +
                ", tag='" + tag + '\'' +
                ", tagType=" + tagType +
                '}';
    }
}
