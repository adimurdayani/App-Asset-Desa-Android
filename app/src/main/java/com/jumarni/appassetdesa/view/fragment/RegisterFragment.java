package com.jumarni.appassetdesa.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.DataUserRegis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    private View view;
    private TextView txt_token;
    private Button btn_register;
    private ImageView btn_kembali;
    private TextInputLayout l_username, l_password, l_nama, l_email, l_konfir_password;
    private TextInputEditText e_username, e_password, e_nama, e_email, e_konfir_password;
    private ProgressBar progressBar;
    private String nama, username, email, password, konfirmasi_password, getToken;
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^" +
            "(?=.*[1-9])" + //harus menggunakan satu angka
            "(?=.*[a-z])" + //harus menggunakan abjad
            "(?=.*[A-Z])" + //harus menggunakan huruf kapital
            "(?=.*[@#$%^&+=])" + //harus menggunakan sepesial karakter
            "(?=\\S+$)" + // tidak menggunakan spasi
            ".{6,}" + //harus lebih dari 6 karakter
            "$"
    );
    private StringRequest kirimDataUser;
    private ArrayList<DataUserRegis> dataUserRegis;

    public RegisterFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        init();
        return view;
    }

    private void init() {
        btn_register = view.findViewById(R.id.btn_regis);
        btn_kembali  = view.findViewById(R.id.btn_kembali);
        l_nama = view.findViewById(R.id.l_nama);
        l_email = view.findViewById(R.id.l_email);
        l_username = view.findViewById(R.id.l_username);
        l_password = view.findViewById(R.id.l_password);
        l_konfir_password = view.findViewById(R.id.l_konfir_password);
        e_nama = view.findViewById(R.id.e_nama);
        e_email = view.findViewById(R.id.e_email);
        e_username = view.findViewById(R.id.e_username);
        e_password = view.findViewById(R.id.e_password);
        e_konfir_password = view.findViewById(R.id.e_konfir_password);
        progressBar = view.findViewById(R.id.progress);
        txt_token = view.findViewById(R.id.getToken);

        txt_token.setText(FirebaseInstanceId.getInstance().getToken());

        btn_register.setOnClickListener(v -> {
            if (validasi()){
                registerUser();
            }
        });
        btn_kembali.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .replace(R.id.frm_login, new LoginFragment())
                    .commit();
        });
        cekvalidasi();
    }

    private void registerUser() {
        dataUserRegis = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        kirimDataUser = new StringRequest(Request.Method.POST, URLServer.REGISTER, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    DataUserRegis postUser = new DataUserRegis();
                    postUser.setNama(data.getString("nama"));
                    postUser.setUsername(data.getString("username"));
                    postUser.setEmail(data.getString("email"));
                    postUser.setPassword(data.getString("password"));
                    postUser.setToken_id(data.getString("token_id"));

                    FragmentManager manager = getFragmentManager();
                    assert manager != null;
                    manager.beginTransaction().replace(R.id.frm_login, new LoginFragment())
                            .commit();
                    Toast.makeText(getContext(), "Register success!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
           progressBar.setVisibility(View.GONE);
        }, error -> {
            progressBar.setVisibility(View.GONE);
            error.printStackTrace();
            Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("nama", nama);
                map.put("email", email);
                map.put("username", username);
                map.put("password", password);
                map.put("token_id", getToken);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(requireContext());
        koneksi.add(kirimDataUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        getinputText();
    }

    public void getinputText(){
        nama = e_nama.getText().toString().trim();
        username = e_username.getText().toString().trim();
        email = e_email.getText().toString().trim();
        password = e_password.getText().toString().trim();
        konfirmasi_password = e_konfir_password.getText().toString().trim();
        getToken  = txt_token.getText().toString().trim();
    }

    public void cekvalidasi() {
        getinputText();

        e_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nama.isEmpty()) {
                    l_nama.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.isEmpty()) {
                    l_email.setErrorEnabled(false);
                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    l_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (username.isEmpty()) {
                    l_username.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        e_konfir_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (konfirmasi_password.isEmpty()) {
                    l_konfir_password.setErrorEnabled(false);
                } else if (konfirmasi_password.length() > 7) {
                    l_konfir_password.setErrorEnabled(false);
                } else if (PASSWORD_FORMAT.matcher(konfirmasi_password).matches()) {
                    l_konfir_password.setErrorEnabled(false);
                } else if (konfirmasi_password.matches(password)) {
                    l_konfir_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validasi() {
        getinputText();

        if (nama.isEmpty()) {
            l_nama.setErrorEnabled(true);
            l_nama.setError("Kolom nama tidak boleh kosong!");
            return false;
        }
        if (email.isEmpty()) {
            l_email.setErrorEnabled(true);
            l_email.setError("Kolom email tidak boleh kosong!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            l_email.setErrorEnabled(true);
            l_email.setError("Format email salah!. Contoh: gunakan @example.com");
            return false;
        }
        if (username.isEmpty()) {
            l_username.setErrorEnabled(true);
            l_username.setError("Kolom username tidak boleh kosong!");
            return false;
        }
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
            l_konfir_password.setErrorEnabled(true);
            l_konfir_password.setError("Kolom konfirmasi password tidak boleh kosong!");
            return false;
        } else if (konfirmasi_password.length() < 6) {
            l_konfir_password.setErrorEnabled(true);
            l_konfir_password.setError("Konfirmasi password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(konfirmasi_password).matches()) {
            l_konfir_password.setErrorEnabled(true);
            l_konfir_password.setError("Konfirmasi password sangat lemah!");
            return false;
        } else if (!konfirmasi_password.matches(password)) {
            l_konfir_password.setErrorEnabled(true);
            l_konfir_password.setError("Konfirmasi password tidak sama dengan password!");
            return false;
        }
        return true;
    }
}
