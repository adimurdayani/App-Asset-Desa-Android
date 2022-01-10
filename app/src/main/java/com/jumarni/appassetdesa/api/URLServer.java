package com.jumarni.appassetdesa.api;

public class URLServer {
    public static final String BASE_URL = "http://sispasdes.my.id//api/";
    public static final String LOGIN = BASE_URL + "auth/login";
    public static final String LOGOUT = BASE_URL + "auth/logout";
    public static final String REGISTER = BASE_URL + "auth/register";
    public static final String GETKRITIKLIMIT = BASE_URL + "kritik/kritiklimit";
    public static final String GETKRITIK = BASE_URL + "kritik";
    public static final String GETKRITIKID = BASE_URL + "kritik?id_kritik=";
    public static final String POSTPROFILE = BASE_URL + "user/edit";
    public static final String POSTPASSWORD = BASE_URL + "user/password";
    public static final String GETASSET = BASE_URL + "asset/aset";
    public static final String POSTASSET = BASE_URL + "asset/update";
    public static final String POSTPENYEWA = BASE_URL + "penyewa/penyewa";
    public static final String GETPENDUDUK = BASE_URL + "penduduk?kk_id=";
    public static final String GETNAMAPENDUDUK = BASE_URL + "penduduk/namakk?dusun_id=";
    public static final String GETDUSUN = BASE_URL + "penduduk/dusun";
    public static final String POSTKRITIK = BASE_URL + "kritik/kritik";
    public static final String GETJUMLAHPENDUDUK = BASE_URL + "penduduk/jumlahpenduduk";
    public static final String GETJUMLAHRT = BASE_URL + "penduduk/jumlahrt";
    public static final String GETJMLPERRT = BASE_URL + "penduduk/getrt?dusun_id=";
    public static final String GETJMLKK = BASE_URL + "penduduk/jmlkk?dusun_id=";
    public static final String GETJMLART = BASE_URL + "penduduk/jmlart?kk_id=";
    public static final String GETRIWAYAT = BASE_URL + "penyewa?user_id=";
    public static final String GETASETTIDAKDISEWAKAN = BASE_URL + "asset/aset_t_sewa";
}
