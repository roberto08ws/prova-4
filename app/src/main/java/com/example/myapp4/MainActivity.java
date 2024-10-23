package com.example.myapp4;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapp4.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;

    private interface Login {

        @POST("/login")
        Call<Usuarios> logar(@Query("email") String email, @Query("senha") String senha);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http:10.0.2.2:3000").addConverterFactory(GsonConverterFactory.create())
                        .build();

        view.btnEntrar.setOnClickListener(e -> {

            String email = view.edtEmail.getText().toString().trim();
            String senha = view.edtSenha.getText().toString().trim();



        });

    }
}