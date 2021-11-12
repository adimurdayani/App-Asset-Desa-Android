package com.jumarni.appassetdesa.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.view.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frm_login, new LoginFragment()).commit();
    }
}