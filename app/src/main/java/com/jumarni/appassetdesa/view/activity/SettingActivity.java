package com.jumarni.appassetdesa.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SettingActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private TextView txt_nama, txt_email;
    private RelativeLayout btn_ubahpassword, btn_tentang, btn_bantuan, btn_logout;
    private Button btn_edit;
    private ImageView btn_kembali;
    private CardView btn_facebook, btn_instagram, btn_whatsapp;
    private ProgressDialog dialog;
    private StringRequest prosesLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }
    public void init(){
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        txt_nama = findViewById(R.id.nama);
        txt_email = findViewById(R.id.email);
        btn_bantuan = findViewById(R.id.btn_bantuan);
        btn_edit = findViewById(R.id.btn_edit);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_tentang = findViewById(R.id.btn_tentang);
        btn_ubahpassword = findViewById(R.id.btn_ubahpass);
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_instagram = findViewById(R.id.btn_instagram);
        btn_whatsapp = findViewById(R.id.btn_whatsapp);
        btn_logout = findViewById(R.id.btn_logout);

        txt_nama.setText(preferences.getString("nama", ""));
        txt_email.setText(preferences.getString("email", ""));

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btn_logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Apakah anda yakin ingin keluar?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        });

        btn_ubahpassword.setOnClickListener(v -> {
            startActivity(new Intent(this, UbahPassword.class));
        });

        btn_tentang.setOnClickListener(v -> {
            startActivity(new Intent(this,TentangActivity.class));
        });

        btn_edit.setOnClickListener(v -> {
            startActivity(new Intent(this, EditActivity.class));
        });

        btn_bantuan.setOnClickListener(v -> {
            startActivity(new Intent(this, BantuanActivity.class));
        });

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });

        btn_facebook.setOnClickListener(v -> {
            Intent fb = new Intent();
            fb.setAction(Intent.ACTION_VIEW);
            fb.addCategory(Intent.CATEGORY_BROWSABLE);
            fb.setData(Uri.parse("https://web.facebook.com/?_rdc=1&_rdr/jhum.guadieztbandeelsangatd"));
            startActivity(fb);
        });
        btn_whatsapp.setOnClickListener(v -> {
            Intent wa = new Intent();
            wa.setAction(Intent.ACTION_VIEW);
            wa.addCategory(Intent.CATEGORY_BROWSABLE);
            wa.setData(Uri.parse("https://api.whatsapp.com/send?phone=6282338350902"));
            startActivity(wa);
        });
        btn_instagram.setOnClickListener(v -> {
            Intent ig = new Intent();
            ig.setAction(Intent.ACTION_VIEW);
            ig.addCategory(Intent.CATEGORY_BROWSABLE);
            ig.setData(Uri.parse("https://www.instagram.com/mutijp_?r=nametag"));
            startActivity(ig);
        });
    }

    private void logout() {
        dialog.setMessage("Loading...");
        dialog.show();
        prosesLogout = new StringRequest(Request.Method.GET, URLServer.LOGOUT, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(preferences.getString("token", ""));
                    editor.remove(String.valueOf(preferences.getInt("id_regis", 0)));
                    editor.remove(preferences.getString("nama", ""));
                    editor.clear();
                    editor.apply();

                    Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }, error -> {
            dialog.dismiss();
            error.printStackTrace();
            Toast.makeText(this, "Terjadi Masalah Koneksi", Toast.LENGTH_SHORT).show();
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(prosesLogout);
    }
}