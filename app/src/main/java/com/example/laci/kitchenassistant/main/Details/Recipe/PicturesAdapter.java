package com.example.laci.kitchenassistant.main.Details.Recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.R;

import java.util.ArrayList;

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {
    private ArrayList<String> pictures;
    private Context context;

    public PicturesAdapter(ArrayList<String> pictures,Context context){
        this.context = context;
        this.pictures = pictures;
    }
    @NonNull
    @Override
    public PicturesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_details_pictures,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicturesAdapter.ViewHolder viewHolder, int i) {
        Glide.with(context).load(pictures.get(i)).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cardview_details_pictures_image);
        }
    }
}
