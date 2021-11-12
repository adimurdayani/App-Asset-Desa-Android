package com.jumarni.appassetdesa.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.jumarni.appassetdesa.view.activity.HomeActivity;
import com.jumarni.appassetdesa.view.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private View view;
    private TextView btn_register;
    private Button btn_login;
    private TextInputLayout l_username, l_password;
    private TextInputEditText e_username, e_password;
    private String username, password;
    private StringRequest loginUser;
    private ProgressBar progressBar;
    private SharedPreferences session_data;
    private SharedPreferences.Editor editor;
    private TextView txt_token;

    public LoginFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return view;
    }

    private void init() {
        btn_register = view.findViewById(R.id.btn_register);
        btn_login = view.findViewById(R.id.btn_login);
        l_username = view.findViewById(R.id.l_username);
        l_password = view.findViewById(R.id.l_password);
        e_username = view.findViewById(R.id.e_username);
        e_password = view.findViewById(R.id.e_password);
        progressBar = view.findViewById(R.id.progress);
        txt_token = view.findViewById(R.id.getToken);

        btn_login.setOnClickListener(v -> {
            if (validasi()){
                login();
            }
        });
        btn_register.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            assert manager != null;
            manager.beginTransaction()
                    .replace(R.id.frm_login, new RegisterFragment())
                    .commit();
        });
        cekvalidasi();
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);

        loginUser = new StringRequest(Request.Method.POST, URLServer.LOGIN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    session_data = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    editor = session_data.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putInt("id_regis", data.getInt("id_regis"));
                    editor.putString("nama", data.getString("nama"));
                    editor.putString("username", data.getString("username"));
                    editor.putString("email", data.getString("email"));
                    editor.putString("no_hp", data.getString("no_hp"));
                    editor.putString("user_id", data.getString("user_id"));
                    editor.putString("token_id", data.getString("token_id"));
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    txt_token.setText(session_data.getString("token_id", ""));

                    startActivity(new Intent(getContext(), HomeActivity.class));
                    ((LoginActivity) requireContext()).finish();
                    Toast.makeText(getContext(), "Login success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }, error -> {
            progressBar.setVisibility(View.GONE);
            error.printStackTrace();
            Toast.makeText(getContext(), "Data tidak di temukan", Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String token_generate = FirebaseInstanceId.getInstance().getToken();
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                map.put("token_id", token_generate);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(requireContext());
        koneksi.add(loginUser);
    }

    public void inputtext(){
        username = e_username.getText().toString().trim();
        password = e_password.getText().toString().trim();
    }

    public void cekvalidasi() {
        inputtext();

        e_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (username.isEmpty()) {
                    l_username.setErrorEnabled(false);
                } else if (username.length() < 0) {
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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validasi() {
        inputtext();

        if (username.isEmpty()) {
            l_username.setErrorEnabled(true);
            l_username.setError("Username tidak boleh kosong!");
            return false;
        }
        if (password.isEmpty()) {
            l_password.setErrorEnabled(true);
            l_password.setError("Password tidak boleh kosong!");
            return false;
        } else if (password.length() < 6) {
            l_password.setErrorEnabled(true);
            l_password.setError("Password tidak boleh kurang dari 6 karakter!");
            return false;
        }
        return true;
    }


}
