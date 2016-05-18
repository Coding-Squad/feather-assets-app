package com.reminisense.fa.models;

/**
 * Created by CHARLES on 5/18/2016.
 */
public class User {

    private String companyId;
    private String ownerId;
    private String name;
    private String description;
    private String takeOutInfo;
    private String qrCode;
    private String barCode;
    private String rfidTag;
    private String imageUrls;
    private boolean takeOutAllowed;

    public User(String companyId, String ownerId, String name, String description, String takeOutInfo){
        this.companyId = companyId;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.takeOutInfo = takeOutInfo;
    }

    public String getCompanyId(){
        return companyId;
    }

    public String getOwnerId(){
        return ownerId;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
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

    public String getImageUrls(){
        return imageUrls;
    }

    public boolean getTakeOutAllowed(){
        return takeOutAllowed;
    }

}
