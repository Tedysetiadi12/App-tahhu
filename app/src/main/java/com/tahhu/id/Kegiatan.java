package com.tahhu.id;

public class Kegiatan {
    private String id;
    private String nama;
    private String tanggal;
    private String lokasi;

    public Kegiatan() {
        // Required for Firebase
    }

    public Kegiatan(String id, String nama, String tanggal, String lokasi) {
        this.id = id;
        this.nama = nama;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
}