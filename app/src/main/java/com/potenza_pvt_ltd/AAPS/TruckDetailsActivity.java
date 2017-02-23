package com.potenza_pvt_ltd.AAPS;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr_Jolly on 6/5/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TruckDetailsActivity {
    //name and address string
    private String transporter;
    private String dno;
    private String vno;
    private String date;
    private String aps;
    private String email;
    private String key;
    private String toa;
    private String cost;
    private String tod;
    public int pap;
    private String vtype;
    private String operator;

    public String getCashhandover() {
        return cashhandover;
    }

    public void setCashhandover(String cashhandover) {
        this.cashhandover = cashhandover;
    }

    private String cashhandover;

    public TruckDetailsActivity() {
        /*Blank default constructor essential for Firebase*/
    }

    public TruckDetailsActivity(String a){
    }

    public TruckDetailsActivity(String k, String s, String index, String index1, String index2, String index3) {
        this.key=k;
        email=s;
        transporter =index;
        dno =index1;
        date =index2;
        aps =index3;
    }

    public TruckDetailsActivity(String k1, String k2, String k3, String k4, String k5, String k6, String k7, String k8, String k9, String k10, String k11) {
        this.key=k1;
        email=k2;
        transporter =k3;
        dno =k4;
        date =k5;
        aps =k6;
        cost =k7;
        vtype =k8;
        vno =k9;
        toa =k10;
        tod =k11;
    }

    public String getTransporter() {
        return transporter;
    }
    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public String getDno() {
        return dno;
    }

    public void setDno(String dno) {
        this.dno = dno;
    }
    @JsonProperty("vno")
    public String getVno() {
        return vno;
    }
    @JsonProperty("vno")
    public void setVno(String vno) {
        this.vno = vno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAps() {
        return aps;
    }

    public void setAps(String aps) {
        this.aps = aps;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToa() {
        return toa;
    }

    public void setToa(String toa) {
        this.toa = toa;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTod() {
        return tod;
    }

    public void setTod(String tod) {
        this.tod = tod;
    }

    public int getPap() {
        return pap;
    }

    public void setPap(int pap) {
        this.pap = pap;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
