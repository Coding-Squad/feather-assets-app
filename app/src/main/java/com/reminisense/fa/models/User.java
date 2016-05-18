package com.reminisense.fa.models;

/**
 * Created by CHARLES on 5/18/2016.
 */
public class User {

    private int companyId;
    private int ownerId;
    private String name;
    private String description;
    private String takeOutInfo;
    private String qrCode;
    private String barCode;
    private String rfidTag;
    private String imageUrls;
    private boolean takeOutAllowed;

    public User(int companyId, int ownerId, String name, String description, String takeOutInfo){
        this.companyId = companyId;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.takeOutInfo = takeOutInfo;
    }

    public int getCompanyId( ){
        return companyId;
    }

    public void setCompanyId(int companyId){
        this.companyId = companyId;
    }

    public int getOwnerId(){
        return ownerId;
    }

    public void setOwnerId(int ownerId){
        this.ownerId = ownerId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getQrCode(){
        return qrCode;
    }

    public String getRfidTag(){
        return rfidTag;
    }

    public String getBarCode(){
        return barCode;
    }

    public String getTakeOutInfo(){
        return takeOutInfo;
    }

    public void setTakeOutInfo(String takeOutInfo){
        this.takeOutInfo = takeOutInfo;
    }

    public String getImageUrls(){
        return imageUrls;
    }

    public boolean getTakeOutAllowed(){
        return takeOutAllowed;
    }

}
