package com.potenza_pvt_ltd.AAPS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Created by Kushagr_Jolly on 7/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DetailofUser {
    //name and address string
        private String emailaddress;
        private String key;
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
    @JsonProperty("emailaddress")
    public String getEmailaddress(){
        return emailaddress;
    }
    @JsonProperty("emailaddress")
    public void setEmailaddress(String key){
        this.emailaddress =key;
    }
    @JsonProperty("pass")
    public String getPass(){
        return pwd;
    }
    @JsonProperty("pass")
    public void setPass(String key){
        this.pwd=key;
    }


}