package com.example.kushagr_jolly.potenza_pvt_ltd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr on 15-Jul-16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TariffDetails {

    private String vehicle_type;
    private String key;
    private String total_slab_hrs;
    private String no_of_slab_hrs;
    private String inc_dur_hrs;
    private String tariff;
    private String code;

    public TariffDetails() {

            /*Blank default constructor essential for Firebase*/
    }
    public TariffDetails(String a){

    }

    @JsonProperty("Key")
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }
    @JsonProperty("vehicle_type")
    public String getVehicle_type(){
        return vehicle_type;
    }
    public void setVehicle_type(String key){
        this.vehicle_type=key;
    }

    @JsonProperty("total_slab_hrs")
    public String getTotal_slab_hrs(){
        return total_slab_hrs;
    }
    public void setTotal_slab_hrs(String key){
        this.total_slab_hrs=key;
    }

    @JsonProperty("no_of_slab_hrs")
    public String getNo_of_slab_hrs(){
        return no_of_slab_hrs;
    }
    public void setNo_of_slab_hrs(String key){
        this.no_of_slab_hrs=key;
    }
    @JsonProperty("inc_dur_hrs")
    public String getInc_dur_hrs(){
        return inc_dur_hrs;
    }
    public void setInc_dur_hrs(String key){
        this.inc_dur_hrs=key;
    }
    @JsonProperty("inslip_tariff")
    public String getTariff(){
        return tariff;
    }
    public void setTariff(String key){
        this.tariff=key;
    }
    @JsonProperty("code")
    public String getCode(){
        return code;
    }
    public void setCode(String key){
        this.code=key;
    }

}
