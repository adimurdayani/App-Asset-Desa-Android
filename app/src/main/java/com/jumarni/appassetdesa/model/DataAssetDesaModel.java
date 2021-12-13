package com.jumarni.appassetdesa.model;

public class DataAssetDesaModel {
    private int id_aset, jml_aset;
    private String nama_aset, harga, created_at;

    public int getJml_aset() {
        return jml_aset;
    }

    public void setJml_aset(int jml_aset) {
        this.jml_aset = jml_aset;
    }

    public int getId_aset() {
        return id_aset;
    }

    public void setId_aset(int id_aset) {
        this.id_aset = id_aset;
    }

    public String getNama_aset() {
        return nama_aset;
    }

    public void setNama_aset(String nama_aset) {
        this.nama_aset = nama_aset;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
