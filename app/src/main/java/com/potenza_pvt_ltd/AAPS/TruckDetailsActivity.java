package com.potenza_pvt_ltd.AAPS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr_Jolly on 6/5/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TruckDetailsActivity {
        //name and address string
        private String contractorname;
        private String drivername;
        private String driverno;
        private String vehicleno;
        private String date;
        private String APS;
        private String email;
    private String key;
    private String localtime;
    private String cost;

    public TruckDetailsActivity() {

            /*Blank default constructor essential for Firebase*/
        }
    public TruckDetailsActivity(String a){

    }

    public TruckDetailsActivity(String k,String s, String index, String index1, String index2, String index3, String index4) {
        this.key=k;
        email=s;
        contractorname=index;
        drivername=index1;
        driverno=index2;
        date=index3;
        APS=index4;
    }

    @JsonProperty("Key")
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }
    //Getters and setters
    @JsonProperty("Transporter")
        public String getContractorname() {
            return contractorname;
        }
    @JsonProperty("Transporter")
        public void setContractorname(String contractorname) {
            this.contractorname = contractorname;
        }
    @JsonProperty("Driver Name")
        public String getDrivername() {
            return drivername;
        }
    @JsonProperty("Driver Name")
        public void setDrivername(String drivername) {
            this.drivername = drivername;
        }
    @JsonProperty("Driver Number")
        public String getDriverno() {
            return driverno;
        }
    @JsonProperty("Driver Number")
        public void setDriverno(String driverno) {
            this.driverno = driverno;
        }
    @JsonProperty("Vehicle Number")
        public String getVehicleno() {
            return vehicleno;
        }
    @JsonProperty("Vehicle Number")
        public void setVehicleno(String vehicleno) {
            this.vehicleno = vehicleno;
        }
    @JsonProperty("Date")
        public String getDate() {
            return date;
        }
    @JsonProperty("Date")
        public void setDate(String date) {
            this.date = date;
        }
    @JsonProperty("aps")
        public String getAPS() {
        return APS;
    }
    @JsonProperty("aps")
        public void setAPS(String APS) {
        this.APS= APS;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("Time of Arrival")
    public void settime(String T) {
        this.localtime = T;
    }
    @JsonProperty("Time of Arrival")
    public String gettime() {
        return localtime;
    }
    @JsonProperty("Cost")
    public void setCost(String T) {
        this.cost = T;
    }
    @JsonProperty("Cost")
    public String getCost() {
        return cost;
    }


}
