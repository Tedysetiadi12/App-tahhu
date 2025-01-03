package com.tahhu.id;

class PeminjamanFasilitasModel {
    private String nama;
    private String noHp;
    private String alamat;
    private String keperluan;
    private String fasilitas;

    public PeminjamanFasilitasModel(String nama, String noHp, String alamat, String keperluan, String fasilitas) {
        this.nama = nama;
        this.noHp = noHp;
        this.alamat = alamat;
        this.keperluan = keperluan;
        this.fasilitas = fasilitas;
    }

    public String getNama() {
        return nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public String getFasilitas() {
        return fasilitas;
    }
}
