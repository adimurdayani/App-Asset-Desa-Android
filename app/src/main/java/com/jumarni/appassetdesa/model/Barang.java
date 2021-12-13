package com.jumarni.appassetdesa.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbarang2")
public class Barang implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int barangId;

    @ColumnInfo(name = "nama_barang")
    public String namaBarang;

    @ColumnInfo(name = "jumlah_barang")
    public String jmlBarang;

    @ColumnInfo(name = "harga_barang")
    public String hargaBarang;

    public int getBarangId() {
        return barangId;
    }

    public void setBarangId(int barangId) {
        this.barangId = barangId;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getJmlBarang() {
        return jmlBarang;
    }

    public void setJmlBarang(String jmlBarang) {
        this.jmlBarang = jmlBarang;
    }

    public String getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(String hargaBarang) {
        this.hargaBarang = hargaBarang;
    }
}
