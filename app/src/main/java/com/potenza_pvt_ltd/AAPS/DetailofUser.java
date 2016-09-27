package com.potenza_pvt_ltd.AAPS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr_Jolly on 7/10/2016.
 */
public class DetailofUser {
    @JsonIgnoreProperties
        //name and address string
        private String email;
        private String key;
        private String code;
        private String pwd;
    private String time_cash_handover;
    private String date;

    public DetailofUser() {

            /*Blank default constructor essential for Firebase*/
        }
        public DetailofUser(String a){

        }

        @JsonProperty("Key")
        public String getKey(){
            return key;
        }
        public void setKey(String key){
            this.key=key;
        }
    @JsonProperty("email-address")
    public String getEmail(){
        return email;
    }
    public void setEmail(String key){
        this.email=key;
    }
    @JsonProperty("pass")
    public String getPwd(){
        return pwd;
    }
    public void setPwd(String key){
        this.pwd=key;
    }

    @JsonProperty("Date")
    public String getDate() {
        return date;
    }

}