package com.reminisense.fa.models;

/**
 * Created by Nigs on 2016-05-18.
 */
public class VerifyResult {
    private String name;
    private String description;
    private String imageUrls;
    private boolean takeOutAllowed;
    private String takeOutInfo;

    private String result;
    private String message;

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

    public String getTakeOutInfo() {
        return takeOutInfo;
    }

    public void setTakeOutInfo(String takeOutInfo) {
        this.takeOutInfo = takeOutInfo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}