package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.bloodbank.Adapters.RequestAdapter;
import com.example.bloodbank.Adapters.SearchAdapter;
import com.example.bloodbank.DataModels.Donor;
import com.example.bloodbank.DataModels.RequestDataModel;
import com.example.bloodbank.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        List<Donor> donorList = new ArrayList<>();

        String json;
        String city, blood_group;
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        city = intent.getStringExtra("city");
        blood_group = intent.getStringExtra("blood_group");
        TextView heading = findViewById(R.id.heading);
        String str = "Donors in " + city + " with Blood Group " + blood_group;
        heading.setText(str);

        Gson gson = new Gson();
        Type type = new TypeToken<List<Donor>>() {}.getType();
        List<Donor> dataModels = gson.fromJson(json,type);

        if(dataModels!=null && dataModels.isEmpty()) {
            heading.setText(R.string.no_result_text);
        }
        else if(dataModels!=null) {
            donorList.addAll(dataModels);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        SearchAdapter adapter = new SearchAdapter(donorList,SearchResultsActivity.this);
        recyclerView.setAdapter(adapter);
    }
}