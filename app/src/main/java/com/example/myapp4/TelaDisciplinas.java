package com.example.myapp4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
    private boolean very = false, very2 = false;

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

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http:10.0.2.2:3000").addConverterFactory(GsonConverterFactory.create()).build();

        Lista lista = retrofit.create(Lista.class);

        SharedPreferences cache = getSharedPreferences("login", MODE_PRIVATE);

        very = cache.getBoolean("logadoCampos", false);
        very2 = cache.getBoolean("mostrarPop", false);

        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(4000);

        view.viewHarmonico.setAnimation(animation);
        view.viewHarmonico.setVisibility(View.GONE);

        if (very && !very2) {

            AlertDialog.Builder pop = new AlertDialog.Builder(TelaDisciplinas.this);
            pop.setTitle("Permissão de biometria");
            pop.setMessage("Aceita usar a validação biométrica como forma de login?");
            pop.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putBoolean("permissao", false);
                    editor.putBoolean("mostrarPop", true);
                    editor.apply();

                }
            });
            pop.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putBoolean("permissao", true);
                    editor.putBoolean("mostrarPop", true);
                    editor.apply();

                }
            });
            pop.create().show();

        }

        id_usuario = cache.getInt("id_usuario", 0);

        lista.lista(id_usuario).enqueue(new Callback<List<Disciplinas>>() {
            @Override
            public void onResponse(Call<List<Disciplinas>> call, Response<List<Disciplinas>> response) {

                if (response.isSuccessful()) {

                    list = response.body();

                    AdapterList adapterLista = new AdapterList(list, TelaDisciplinas.this);
                    view.recyclerView.setAdapter(adapterLista);

                } else {

                    Log.e("ERROR", "ERROR: " + response.code());

                }

            }

            @Override
            public void onFailure(Call<List<Disciplinas>> call, Throwable throwable) {

                Log.e("ERROR", "Error: "  + throwable.getMessage());

            }
        });

        view.imgMenu.setOnClickListener(e -> {

            view.imgMenu.setEnabled(false);
            view.viewFundo.setEnabled(true);

            Animation animation1 = new AlphaAnimation(0, 1);
            animation1.setDuration(400);

            view.viewFundo.setAnimation(animation1);
            view.viewFundo.setVisibility(View.VISIBLE);

            Animation animation2 = AnimationUtils.loadAnimation(TelaDisciplinas.this, R.anim.anim_in);
            animation2.setDuration(400);

            view.viewMenu.setAnimation(animation2);
            view.viewMenu.setVisibility(View.VISIBLE);

        });

        view.viewFundo.setOnClickListener(e -> {

            view.imgMenu.setEnabled(true);
            view.viewFundo.setEnabled(false);

            Animation animation1 = new AlphaAnimation(1, 0);
            animation1.setDuration(400);

            view.viewFundo.setAnimation(animation1);
            view.viewFundo.setVisibility(View.GONE);

            Animation animation2 = AnimationUtils.loadAnimation(TelaDisciplinas.this, R.anim.anim_out);
            animation2.setDuration(400);

            view.viewMenu.setAnimation(animation2);
            view.viewMenu.setVisibility(View.GONE);

        });

        view.btnLogout.setOnClickListener(e -> {

            SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();

            editor.putBoolean("logado", false);
            editor.putInt("id_usuario", 0);
            editor.apply();

            startActivity(new Intent(TelaDisciplinas.this, MainActivity.class));
            finish();

        });

    }
}