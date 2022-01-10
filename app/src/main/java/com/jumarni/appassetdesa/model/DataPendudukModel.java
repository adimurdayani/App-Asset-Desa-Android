package com.jumarni.appassetdesa.model;

public class DataPendudukModel {
    private int id_pend, dusun_id, rt_id, kk_id, ket;
    private String nama_kk, nama_art, tgl_lahir, kelamin, created_at, nama_dusun, rt, nik, no_kk;

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNo_kk() {
        return no_kk;
    }

    public void setNo_kk(String no_kk) {
        this.no_kk = no_kk;
    }

    public int getKk_id() {
        return kk_id;
    }

    public void setKk_id(int kk_id) {
        this.kk_id = kk_id;
    }

    public int getId_pend() {
        return id_pend;
    }

    public void setId_pend(int id_pend) {
        this.id_pend = id_pend;
    }

    public int getDusun_id() {
        return dusun_id;
    }

    public void setDusun_id(int dusun_id) {
        this.dusun_id = dusun_id;
    }

    public int getRt_id() {
        return rt_id;
    }

    public void setRt_id(int rt_id) {
        this.rt_id = rt_id;
    }

    public int getKet() {
        return ket;
    }

    public void setKet(int ket) {
        this.ket = ket;
    }

    public String getNama_kk() {
        return nama_kk;
    }

    public void setNama_kk(String nama_kk) {
        this.nama_kk = nama_kk;
    }

    public String getNama_art() {
        return nama_art;
    }

    public void setNama_art(String nama_art) {
        this.nama_art = nama_art;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNama_dusun() {
        return nama_dusun;
    }

    public void setNama_dusun(String nama_dusun) {
        this.nama_dusun = nama_dusun;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }
}
