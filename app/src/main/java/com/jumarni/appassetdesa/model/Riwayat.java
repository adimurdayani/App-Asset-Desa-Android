package com.jumarni.appassetdesa.model;

public class Riwayat {
    private int id_penyewa, user_id, aset_id, harga_id, status;
    private String created_at, tgl_kembali, nama_aset, harga, nama;

    public int getId_penyewa() {
        return id_penyewa;
    }

    public void setId_penyewa(int id_penyewa) {
        this.id_penyewa = id_penyewa;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAset_id() {
        return aset_id;
    }

    public void setAset_id(int aset_id) {
        this.aset_id = aset_id;
    }

    public int getHarga_id() {
        return harga_id;
    }

    public void setHarga_id(int harga_id) {
        this.harga_id = harga_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTgl_kembali() {
        return tgl_kembali;
    }

    public void setTgl_kembali(String tgl_kembali) {
        this.tgl_kembali = tgl_kembali;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
