package com.example.kushagr_jolly.potenza_pvt_ltd;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr_Jolly on 6/5/2016.
 */
public class TruckDetailsActivity {
        //name and address string
        private String contractorname;
        private String drivername;
        private String driverno;
        private String vehicleno;
        private String date;
        private String APS;
        private String user;
        private String email;
    private String manager;
    private String admin;

    public TruckDetailsActivity() {
            /*Blank default constructor essential for Firebase*/
        }

    public TruckDetailsActivity(String s, String index, String index1, String index2, String index3, String index4) {
        email=s;
        contractorname=index;
        drivername=index1;
        driverno=index2;
        date=index3;
        APS=index4;
    }


    //Getters and setters
    @JsonProperty("Contractor Name")
        public String getContractorname() {
            return contractorname;
        }
    @JsonProperty("Contractor Name")
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
    @JsonProperty("User")
    public String getUser() {
        return user;
    }
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("Manager")
    public String getManager() {
        return manager;
    }
    @JsonProperty("Admin")
     public String getAdmin() {
        return admin;
    }

}
