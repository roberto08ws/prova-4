package com.example.myapp4;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapp4.databinding.ActivityTelaDisciplinasBinding;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaDisciplinas extends AppCompatActivity {

    private ActivityTelaDisciplinasBinding view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        view = ActivityTelaDisciplinasBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        view.recyclerView.setLayoutManager(new LinearLayoutManager(TelaDisciplinas.this));
        view.recyclerView.setHasFixedSize(true);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http:10.0.2.2:3000").addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}