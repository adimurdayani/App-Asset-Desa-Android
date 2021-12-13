package com.jumarni.appassetdesa.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jumarni.appassetdesa.model.Barang;

@Database( entities = {Barang.class}, version = 2 )
public abstract class AppDatabase extends RoomDatabase {
    public abstract BarangDao barangDAO();
}


