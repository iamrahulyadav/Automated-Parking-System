package com.potenza_pvt_ltd.AAPS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Kushagr on 15-Jul-16.
 */
@IgnoreExtraProperties
public class TariffDetails {

    private String vehicle_type;
    private String key;
    private String total_slab_hrs;
    private String no_of_slab_hrs;
    private String inc_dur_hrs;
    private String tariff;
    private String code;
    private ArrayList<ArrayList<String>> arr;

    public TariffDetails() {

            /*Blank default constructor essential for Firebase*/
    }
    public TariffDetails(String a){

    }
    @JsonProperty("Key")
    public String getKey(){
        return key;
    }
    @JsonProperty("Key")
    public void setKey(String key){
        this.key=key;
    }
    @JsonProperty("vtype")
    public String getVehicle_type(){
        return vehicle_type;
    }
    @JsonProperty("vtype")
    public void setVehicle_type(String key){
        this.vehicle_type=key;
    }
    @JsonProperty("total_slab_hrs")
    public String getTotal_slab_hrs(){
        return total_slab_hrs;
    }
    @JsonProperty("total_slab_hrs")
    public void setTotal_slab_hrs(String key){
        this.total_slab_hrs=key;
    }
    @JsonProperty("no_of_slab_hrs")
    public String getNo_of_slab_hrs(){
        return no_of_slab_hrs;
    }
    @JsonProperty("no_of_slab_hrs")
    public void setNo_of_slab_hrs(String key){
        this.no_of_slab_hrs=key;
    }
    @JsonProperty("inc_dur_hrs")
    public String getInc_dur_hrs(){
        return inc_dur_hrs;
    }
    @JsonProperty("inc_dur_hrs")
    public void setInc_dur_hrs(String key){
        this.inc_dur_hrs=key;
    }
    @JsonProperty("inslipp_tariff")
    public String getInslip_tariff(){
        return tariff;
    }
    @JsonProperty("inslipp_tariff")
    public void setInslip_tariff(String key){
        this.tariff=key;
    }
    @JsonProperty("code")
    public String getCode(){
        return code;
    }
    @JsonProperty("code")
    public void setCode(String key){
        this.code=key;
    }
    @JsonProperty("arr")
    public ArrayList<ArrayList<String>> getArr(){
        return arr;
    }
    @JsonProperty("arr")
    public void setArr(ArrayList<ArrayList<String>> a){
        this.arr=a;
    }


}
