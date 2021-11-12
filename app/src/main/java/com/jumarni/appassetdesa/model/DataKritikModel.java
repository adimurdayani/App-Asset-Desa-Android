package com.jumarni.appassetdesa.model;

public class DataKritikModel {
    private int id_kritik, user_id,  admin_id;
    private String kritik, jawaban, created_at, tgl_kirim, nama, nama_grup;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama_grup() {
        return nama_grup;
    }

    public void setNama_grup(String nama_grup) {
        this.nama_grup = nama_grup;
    }

    public int getId_kritik() {
        return id_kritik;
    }

    public void setId_kritik(int id_kritik) {
        this.id_kritik = id_kritik;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getKritik() {
        return kritik;
    }

    public void setKritik(String kritik) {
        this.kritik = kritik;
    }

    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTgl_kirim() {
        return tgl_kirim;
    }

    public void setTgl_kirim(String tgl_kirim) {
        this.tgl_kirim = tgl_kirim;
    }
}
