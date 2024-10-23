package com.example.myapp4;

public class ValidacaoLogin {

    private String email, senha;

    public ValidacaoLogin(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public ValidacaoLogin() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
