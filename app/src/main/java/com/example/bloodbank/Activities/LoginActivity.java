package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbank.R;
import com.example.bloodbank.Utils.UrlClass;
import com.example.bloodbank.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText numberEt,passwordEt;
    private TextView signUpButton,loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        numberEt = findViewById(R.id.usernameEditText);
        passwordEt = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEt.setError(null);
                passwordEt.setError(null);
                String number,password;
                number = numberEt.getText().toString();
                password = passwordEt.getText().toString();

                if(isValid(number,password)) {
                    login(number,password);
                }
            }
        });
    }

    private void login(String number,String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlClass.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("Invalid Credentials")) {
                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number",number).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",response).apply();
                    LoginActivity.this.finish();
                }
                else {
                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("password",password);
                params.put("number",number);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String number,String password) {
        if(number.isEmpty()) {
            showMessage("Number is Empty");
            numberEt.setError("Number is Empty");
            return false;
        }
        else if(password.isEmpty()) {
            showMessage("Password is Empty");
            passwordEt.setError("Password is Empty");
            return false;
        }
        return true;
    }

    private void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}