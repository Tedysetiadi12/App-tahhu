package com.tahhu.id;
import java.io.Serializable;


public class Piutang {
    private static final long serialVersionUID = 1L;

    private String id;           // Tambahkan atribut id untuk identifikasi unik
    private String type;
    private String jumlah;
    private String tanggal;
    private String jatuhTempo;
    private String nama;
    private String deskripsi;
    private String catatan;
    private String status;

    // Default constructor untuk Firebase
    public Piutang() {}

    // Constructor untuk menginisialisasi semua nilai
    public Piutang(String id, String type, String jumlah, String tanggal, String jatuhTempo, String nama, String deskripsi, String catatan, String status) {
        this.id = id;
        this.type = type;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.jatuhTempo = jatuhTempo;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.catatan = catatan;
        this.status = status;
    }

    // Getter dan Setter untuk semua atribut
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJatuhTempo() {
        return jatuhTempo;
    }

    public void setJatuhTempo(String jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
