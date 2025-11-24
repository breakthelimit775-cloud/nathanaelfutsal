package com.example.nathanaelfutsal;

public class LapanganModel {

    String namaLapangan;
    String hargaLapangan;
    String status;
    int fotoLapangan;

    public LapanganModel(String namaLapangan, String hargaLapangan, String status, int fotoLapangan) {
        this.namaLapangan = namaLapangan;
        this.hargaLapangan = hargaLapangan;
        this.status = status;
        this.fotoLapangan = fotoLapangan;
    }

    public String getNamaLapangan() {
        return namaLapangan;
    }

    public String getHargaLapangan() {
        return hargaLapangan;
    }

    public String getStatus() {
        return status;
    }

    public int getFotoLapangan() {
        return fotoLapangan;
    }
}