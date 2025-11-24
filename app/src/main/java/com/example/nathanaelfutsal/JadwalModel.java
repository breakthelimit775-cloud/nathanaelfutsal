package com.example.nathanaelfutsal;

public class JadwalModel {

    String jam;
    String status;
    public JadwalModel() {
    }
    public JadwalModel(String jam, String status) {
        this.jam = jam;
        this.status = status;
    }
    public String getJam() {
        return jam;
    }
    public String getStatus() {
        return status;
    }
    public void setJam(String jam) {
        this.jam = jam;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}