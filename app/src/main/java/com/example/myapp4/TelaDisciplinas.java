package com.example.myapp4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapp4.databinding.ActivityTelaDisciplinasBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class TelaDisciplinas extends AppCompatActivity {

    private ActivityTelaDisciplinasBinding view;
    private int id_usuario = 0;
    private List<Disciplinas> list = new ArrayList<>();

    private interface Lista {

        @GET("/disciplinas-usuario/{id}}")
        Call<List<Disciplinas>> lista(@Path("id") int id);

    }

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

        Lista lista = retrofit.create(Lista.class);

        SharedPreferences cache = getSharedPreferences("login", MODE_PRIVATE);

        id_usuario = cache.getInt("id_usuario", 0);

        lista.lista(id_usuario).enqueue(new Callback<List<Disciplinas>>() {
            @Override
            public void onResponse(Call<List<Disciplinas>> call, Response<List<Disciplinas>> response) {

                if (response.isSuccessful()) {

                    list = response.body();

                    AdapterList adapterLista = new AdapterList(list, TelaDisciplinas.this);


                } else {

                    Log.e("ERROR", "ERROR: " + response.code());

                }

            }

            @Override
            public void onFailure(Call<List<Disciplinas>> call, Throwable throwable) {

                Log.e("ERROR", "Error: "  + throwable.getMessage());

            }
        });

    }
}