package com.potenza_pvt_ltd.AAPS;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kushagr on 07-Aug-16.
 */
public class SlipDetail {
    String header1,header2,header3,header4,header5,footer1,footer2;

    public SlipDetail() {

            /*Blank default constructor essential for Firebase*/
    }
    public SlipDetail(String a){

    }
    @JsonProperty("footer1")
    public String getFooter1() {
        return footer1;
    }
    public void setFooter1(String footer1) {
        this.footer1 = footer1;
    }
    @JsonProperty("footer2")
    public String getFooter2() {
        return footer2;
    }

    public void setFooter2(String footer2) {
        this.footer2 = footer2;
    }
    @JsonProperty("header1")
    public String getHeader1() {
        return header1;
    }

    public void setHeader1(String header1) {
        this.header1 = header1;
    }
    @JsonProperty("header2")
    public String getHeader2() {
        return header2;
    }

    public void setHeader2(String header2) {
        this.header2 = header2;
    }
    @JsonProperty("header3")
    public String getHeader3() {
        return header3;
    }

    public void setHeader3(String header3) {
        this.header3 = header3;
    }
    @JsonProperty("header4")
    public String getHeader4() {
        return header4;
    }

    public void setHeader4(String header4) {
        this.header4 = header4;
    }
    @JsonProperty("header5")
    public String getHeader5() {
        return header5;
    }

    public void setHeader5(String header5) {
        this.header5 = header5;
    }
}
