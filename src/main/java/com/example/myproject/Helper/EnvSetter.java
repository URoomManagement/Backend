package com.example.myproject.Helper;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvSetter {
    public EnvSetter(){}

    public void start(){
        Dotenv dotenv = Dotenv.load(); 
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
        System.setProperty("JWT_REFRESH_SECRET_KEY", dotenv.get("JWT_REFRESH_SECRET_KEY"));
        System.setProperty("SERVER_ADDRESS", dotenv.get("SERVER_ADDRESS"));
        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));   
    }
}
