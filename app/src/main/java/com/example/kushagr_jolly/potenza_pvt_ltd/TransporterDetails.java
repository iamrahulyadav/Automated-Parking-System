package com.example.kushagr_jolly.potenza_pvt_ltd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr on 15-Jul-16.
 */
public class TransporterDetails {

    private String name;
    private String key;
    private String address;
    private String sms_no;
    private String contact_person;
    private String mobile_no;
    private String no_of_vhcl;
    private String vehicle_no;
    private String amt;

    public TransporterDetails(){

    }

    public TransporterDetails(String k,String s, String index, String index1, String index2, String index3, String index4, String index5) {
        this.key=k;
        name=s;
        address=index;
        sms_no=index1;
        contact_person=index2;
        mobile_no=index3;
        no_of_vhcl=index4;
        vehicle_no=index5;
    }
    public TransporterDetails(String k,String s, String index, String index1, String index2, String index3, String index4, String index5,String index6) {
        this.key=k;
        name=s;
        address=index;
        sms_no=index1;
        contact_person=index2;
        mobile_no=index3;
        no_of_vhcl=index4;
        vehicle_no=index5;
        this.amt=index6;
    }

    @JsonProperty("Key")
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }
    //Getters and setters
    @JsonProperty("Name")
    public String getName() {
        return name;
    }
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("Address")
    public String getAddress() {
        return address;
    }
    @JsonProperty("Address")
    public void setAddress(String address) {
        this.address = address;
    }
    @JsonProperty("sms_no")
    public String getSms_no() {
        return sms_no;
    }
    @JsonProperty("sms_no")
    public void setSms_no(String sms_no) {
        this.sms_no = sms_no;
    }
    @JsonProperty("contact_person")
    public String getContact_person() {
        return contact_person;
    }
    @JsonProperty("contact_person")
    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }
    @JsonProperty("mobile_no")
    public String getMobile_no() {
        return mobile_no;
    }
    @JsonProperty("mobile_no")
    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
    @JsonProperty("no_of_vhcl")
    public String getNo_of_vhcl() {
        return no_of_vhcl;
    }
    @JsonProperty("no_of_vhcl")
    public void setNo_of_vhcl(String no_of_vhcl) {
        this.no_of_vhcl= no_of_vhcl;
    }
    
    @JsonProperty("vehicle_no")
    public void setVehicle_no(String T) {
        this.vehicle_no = T;
    }
    @JsonProperty("vehicle_no")
    public String getVehicle_no() {
        return vehicle_no;
    }
    @JsonProperty("Amt")
    public void setAmt(String T) {
        this.amt= T;
    }
    @JsonProperty("Amt")
    public String getAmt() {
        return amt;
    }
}
