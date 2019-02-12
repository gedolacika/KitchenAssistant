package com.example.laci.kitchenassistant.main.AddFood;

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

public class AddFoodAddPictureAdapter extends RecyclerView.Adapter<AddFoodAddPictureAdapter.ViewHolder> {
    private ArrayList<String> urlPictures;
    private ArrayList<byte[]> bytePictures;
    private Context context;
    private int current_index;

    public AddFoodAddPictureAdapter(Context context, ArrayList<String> urlPictures, ArrayList<byte[]> bytePictures){
        this.context = context;
        this.urlPictures = urlPictures;
        this.bytePictures = bytePictures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_add_food_pictures,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        current_index = i;
        if( current_index < urlPictures.size() ){
            Glide.with(context).load(urlPictures.get(i)).into(viewHolder.imageView);
        }else{
            Glide.with(context).load(bytePictures.get(i-urlPictures.size())).into(viewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return (urlPictures.size() + bytePictures.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardview_add_food_pictures_imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( current_index < urlPictures.size() ){
                        urlPictures.remove(current_index);
                    }else{
                        bytePictures.remove(current_index - urlPictures.size());
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
