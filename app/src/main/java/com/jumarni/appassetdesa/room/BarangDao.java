package com.jumarni.appassetdesa.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jumarni.appassetdesa.model.Barang;

@Dao
public interface BarangDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE )
    long insertBarang(Barang barang);

    @Query( "SELECT * FROM tbarang2" )
    Barang getBarang();
}
