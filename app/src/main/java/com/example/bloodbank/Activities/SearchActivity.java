package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbank.DataModels.RequestDataModel;
import com.example.bloodbank.R;
import com.example.bloodbank.Utils.UrlClass;
import com.example.bloodbank.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private EditText bloodGroupEt, cityEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cityEt = findViewById(R.id.et_city);
        bloodGroupEt = findViewById(R.id.et_bloodGroup);
        TextView findDonorButton = findViewById(R.id.find_donor_button);

        findDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEt.getText().toString();
                String blood_group = bloodGroupEt.getText().toString();
                if(isValid(city,blood_group)) {
                    get_search_result(city,blood_group);
                }
            }
        });
    }

    private void get_search_result(String city, String blood_group) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlClass.search_donors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(SearchActivity.this,SearchResultsActivity.class);
                intent.putExtra("city",city);
                intent.putExtra("blood_group",blood_group);
                intent.putExtra("json",response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("city",city);
                params.put("blood_group",blood_group);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String city, String blood_group) {
        List<String> valid_blood_groups = new ArrayList<>();
        valid_blood_groups.add("A+");
        valid_blood_groups.add("A-");
        valid_blood_groups.add("AB+");
        valid_blood_groups.add("AB-");
        valid_blood_groups.add("B+");
        valid_blood_groups.add("B-");
        valid_blood_groups.add("O+");
        valid_blood_groups.add("O-");

        if(city.isEmpty() || blood_group.isEmpty()) {
            showMessage("Empty fields are not allowed");
            return false;
        }
        if(!valid_blood_groups.contains(blood_group)) {
            showMessage("Invalid Blood Group, Choose From " + valid_blood_groups);
            return false;
        }
        return true;
    }

    private void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}