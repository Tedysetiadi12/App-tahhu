package com.tahhu.id; // pastikan sesuai dengan package Anda

public class LokasiPenukaran {
    // Atribut untuk nama lokasi dan alamat
    private String namaLokasi;
    private String alamat;

    // Constructor untuk menginisialisasi objek
    public LokasiPenukaran(String namaLokasi, String alamat) {
        this.namaLokasi = namaLokasi;
        this.alamat = alamat;
    }

    // Getter untuk nama lokasi
    public String getNamaLokasi() {
        return namaLokasi;
    }

    // Getter untuk alamat lokasi
    public String getAlamat() {
        return alamat;
    }

    // Setter untuk nama lokasi (jika diperlukan)
    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    // Setter untuk alamat (jika diperlukan)
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
