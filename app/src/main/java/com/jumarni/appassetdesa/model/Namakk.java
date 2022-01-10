package com.jumarni.appassetdesa.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Namakk {
    private int id_kk, dusun_id;
    private String nama_kk, created_at;

    public int getDusun_id() {
        return dusun_id;
    }

    public void setDusun_id(int dusun_id) {
        this.dusun_id = dusun_id;
    }

    public int getId_kk() {
        return id_kk;
    }

    public void setId_kk(int id_kk) {
        this.id_kk = id_kk;
    }

    public String getNama_kk() {
        return nama_kk;
    }

    public void setNama_kk(String nama_kk) {
        this.nama_kk = nama_kk;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
