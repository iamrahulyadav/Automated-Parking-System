package com.potenza_pvt_ltd.AAPS;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr on 15-Jul-16.
 */
public class ParkingSlotDetails {

    private String key;
    private String slot_name;
    private String code;
    private String slot_number;

    public void ParkingSlotDetails(){

    }

    @JsonProperty("Key")
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }
    @JsonProperty("slot_name")
    public String getSlot_name(){
        return slot_name;
    }
    public void setSlot_name(String key){
        this.slot_name=key;
    }

    @JsonProperty("code")
    public String getCode(){
        return code;
    }
    public void setCode(String key){
        this.code=key;
    }

    @JsonProperty("slot_number")
    public String getSlot_number(){
        return slot_number;
    }
    public void setSlot_number(String key){
        this.slot_number=key;
    }

}
