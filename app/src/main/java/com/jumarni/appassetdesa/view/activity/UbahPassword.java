package com.jumarni.appassetdesa.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.DataUserRegis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UbahPassword extends AppCompatActivity {

    private ImageView btn_kembali;
    private Button btn_simpan;
    private TextInputLayout l_password, l_konfirm_password;
    private TextInputEditText e_password, e_konfirm_password;
    private String password, konfirmasi_password, id;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^" +
            "(?=.*[1-9])" + //harus menggunakan satu angka
            "(?=.*[a-z])" + //harus menggunakan abjad
            "(?=.*[A-Z])" + //harus menggunakan huruf kapital
            "(?=.*[@#$%^&+=])" + //harus menggunakan sepesial karakter
            "(?=\\S+$)" + // tidak menggunakan spasi
            ".{6,}" + //harus lebih dari 6 karakter
            "$"
    );
    private StringRequest ubahPassword;
    private TextView id_regis;
    private ProgressDialog dialog;
    private ArrayList<DataUserRegis> dataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);
        init();
    }

    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);

        btn_kembali = findViewById(R.id.btn_kembali);
        btn_simpan = findViewById(R.id.btn_simpan);
        l_password = findViewById(R.id.l_password);
        l_konfirm_password = findViewById(R.id.l_konfir_password);
        e_password = findViewById(R.id.e_password);
        e_konfirm_password = findViewById(R.id.e_konfir_password);
        id_regis = findViewById(R.id.id_regis);

        id_regis.setText("" + preferences.getInt("id_regis", 0));

        dialog= new ProgressDialog(this);
        dialog.setCancelable(false);

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
        btn_simpan.setOnClickListener(v -> {
            if (validasi()) {
                setUbahPassword();
            }
        });
        cekvalidasi();
    }

    public void setUbahPassword() {
        dataUser = new ArrayList<>();
        dialog.setMessage("Loading...");
        ubahPassword = new StringRequest(Request.Method.POST, URLServer.POSTPASSWORD, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(preferences.getString("token", ""));
                    editor.remove(String.valueOf(preferences.getInt("id_regis", 0)));
                    editor.remove(preferences.getString("nama", ""));
                    editor.clear();
                    editor.apply();

                    Toast.makeText(this, "Update password success!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }, error -> {
            dialog.dismiss();
            error.printStackTrace();
            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_regis", id);
                map.put("password", password);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(ubahPassword);
    }

    public void getinputtext() {
        id = id_regis.getText().toString().trim();
        password = e_password.getText().toString().trim();
        konfirmasi_password = e_konfirm_password.getText().toString().trim();
    }

    public void cekvalidasi() {
        getinputtext();

        e_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.isEmpty()) {
                    l_password.setErrorEnabled(false);
                } else if (password.length() > 7) {
                    l_password.setErrorEnabled(false);
                } else if (PASSWORD_FORMAT.matcher(password).matches()) {
                    l_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_konfirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (konfirmasi_password.isEmpty()) {
                    l_konfirm_password.setErrorEnabled(false);
                } else if (konfirmasi_password.length() > 7) {
                    l_konfirm_password.setErrorEnabled(false);
                } else if (PASSWORD_FORMAT.matcher(konfirmasi_password).matches()) {
                    l_konfirm_password.setErrorEnabled(false);
                } else if (konfirmasi_password.matches(password)) {
                    l_konfirm_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validasi() {
        getinputtext();
        if (password.isEmpty()) {
            l_password.setErrorEnabled(true);
            l_password.setError("Kolom password tidak boleh kosong!");
            return false;
        } else if (password.length() < 6) {
            l_password.setErrorEnabled(true);
            l_password.setError("Password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(password).matches()) {
            l_password.setErrorEnabled(true);
            l_password.setError("Password sangat lemah!. Contoh: @Jad123");
            return false;
        }
        if (konfirmasi_password.isEmpty()) {
            l_konfirm_password.setErrorEnabled(true);
            l_konfirm_password.setError("Kolom konfirmasi password tidak boleh kosong!");
            return false;
        } else if (konfirmasi_password.length() < 6) {
            l_konfirm_password.setErrorEnabled(true);
            l_konfirm_password.setError("Konfirmasi password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(konfirmasi_password).matches()) {
            l_konfirm_password.setErrorEnabled(true);
            l_konfirm_password.setError("Konfirmasi password sangat lemah!");
            return false;
        } else if (!konfirmasi_password.matches(password)) {
            l_konfirm_password.setErrorEnabled(true);
            l_konfirm_password.setError("Konfirmasi password tidak sama dengan password!");
            return false;
        }
        return true;
    }
}