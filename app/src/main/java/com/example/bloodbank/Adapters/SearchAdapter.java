package com.example.bloodbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.DataModels.Donor;
import com.example.bloodbank.R;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Donor> dataSet;
    private Context context;

    public SearchAdapter(List<Donor> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ViewHolder holder, final int position) {
        String str = "Name: " + dataSet.get(position).getName();
        str +=  "\nCity: " + dataSet.get(position).getCity();
        holder.message.setText(str);
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call donor
                if(PermissionChecker.checkSelfPermission(context,CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+dataSet.get(position).getNumber()));
                    context.startActivity(intent);
                }
                else {
                    ((Activity)context).requestPermissions(new String[] {CALL_PHONE}, 401);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        ImageView imageView,callButton;

        ViewHolder(final View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.donor_name_city);
            imageView = itemView.findViewById(R.id.image);
            callButton = itemView.findViewById(R.id.donor_call_button);
        }

    }
}
