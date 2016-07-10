package com.example.kushagr_jolly.potenza_pvt_ltd;

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

    @JsonProperty("code")
    public String getCode(){
        return code;
    }
    public void setCode(String key){
        this.code=key;
    }

    @JsonProperty("pass")
    public String getPwd(){
        return pwd;
    }
    public void setPwd(String key){
        this.pwd=key;
    }
}