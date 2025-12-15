package com.example.nathanaelfutsal;

public class JadwalModel {
    private int id;
    private String jam;
    private String status;
    private String bukti;

    public JadwalModel(int id, String jam, String status, String bukti) {
        this.id = id;
        this.jam = jam;
        this.status = status;
        this.bukti = bukti;
    }

    public int getId() {
        return id;
    }

    public String getJam() {
        return jam;
    }

    public String getStatus() {
        return status;
    }

    public String getBukti() {
        return bukti;
    }
}