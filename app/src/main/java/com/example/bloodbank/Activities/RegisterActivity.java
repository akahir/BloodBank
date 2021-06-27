package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEt,cityEt,mobileNoEt,bloodGroupEt,passwordEt;
    private TextView DonorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEt = findViewById(R.id.NameEditText);
        cityEt = findViewById(R.id.CityEditText);
        mobileNoEt = findViewById(R.id.MobileEditText);
        bloodGroupEt = findViewById(R.id.BloodGroupEditText);
        passwordEt = findViewById(R.id.PasswordEditText);

        DonorButton = findViewById(R.id.DonorButton);

        DonorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,city,blood_group,password,mobile;
                name = nameEt.getText().toString();
                city = cityEt.getText().toString();
                blood_group = bloodGroupEt.getText().toString();
                password = passwordEt.getText().toString();
                mobile = mobileNoEt.getText().toString();

                if(isValid(name,city,blood_group,password,mobile)) {
                    register(name,city,blood_group,password,mobile);
                }
            }
        });
    }

    private void register(String name,String city,String blood_group,String password,String mobile) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlClass.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",city).apply();
                    Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    RegisterActivity.this.finish();
                }
                else {
                    Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("city",city);
                params.put("blood_group",blood_group);
                params.put("password",password);
                params.put("number",mobile);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String name,String city,String blood_group,String password,String mobile) {
        List<String> valid_blood_groups = new ArrayList<>();
        valid_blood_groups.add("A+");
        valid_blood_groups.add("A-");
        valid_blood_groups.add("AB+");
        valid_blood_groups.add("AB-");
        valid_blood_groups.add("B+");
        valid_blood_groups.add("B-");
        valid_blood_groups.add("O+");
        valid_blood_groups.add("O-");

        if(name.isEmpty() || city.isEmpty() || blood_group.isEmpty() || password.isEmpty() || mobile.isEmpty()) {
            showMessage("Empty fields are not allowed");
            return false;
        }
        if(!valid_blood_groups.contains(blood_group)) {
            showMessage("Invalid Blood Group, Choose From " + valid_blood_groups);
            return false;
        }
        if(mobile.length()!=10) {
            showMessage("Invalid Mobile Number, number should be of 10 digits");
            return false;
        }
        return true;
    }

    private void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}