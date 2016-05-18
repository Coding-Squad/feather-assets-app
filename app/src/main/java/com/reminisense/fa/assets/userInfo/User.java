package com.reminisense.fa.assets.UserInfo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charles on 5/17/2016.
 */

public class User {

    @SerializedName("company_Id")
    private Integer companyId;

    @SerializedName("owner_Id")
    private Integer ownerId;

    private String name;

    private String description;

    @SerializedName("qr_code")
    private String qrCode;

    @SerializedName("rfid_tag")
    private String rfidTag;

    @SerializedName("bar_code")
    private String barCode;

    @SerializedName("takeout_allowed")
    private Boolean takeOutAllowed;

    @SerializedName("takeout_info")
    private String takeOutInfo;

}
