package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbank.Adapters.RequestAdapter;
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

public class MainActivity extends AppCompatActivity {

    private TextView makeRequestButton, pick_location;

    private RecyclerView recyclerView;
    private List<RequestDataModel> requestDataModelList;
    private RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeRequestButton = findViewById(R.id.make_request_button);

        makeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MakeRequestActivity.class));
            }
        });

        requestDataModelList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.search_button) {
                    //open search
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RequestAdapter(requestDataModelList,this);
        recyclerView.setAdapter(adapter);

        homePage();

        pick_location = findViewById(R.id.pick_location);
        String location = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this).getString("city","no_city_found");
        if(!location.equals("no_city_found")) {
            pick_location.setText(location);
        }
    }

    private void homePage() {
        String city = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("city","No city");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlClass.get_requests, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<RequestDataModel>>() {}.getType();
                List<RequestDataModel> dataModels = gson.fromJson(response,type);
                requestDataModelList.addAll(dataModels);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("city",city);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}