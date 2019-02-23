package com.example.laci.kitchenassistant.main.Account.HistoryDialog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laci.kitchenassistant.BaseClasses.WeightHistory;
import com.example.laci.kitchenassistant.R;

import java.util.ArrayList;

public class HistoryDialogAdapter extends RecyclerView.Adapter<HistoryDialogAdapter.ViewHolder> {
    private ArrayList<WeightHistory> data;

    public HistoryDialogAdapter(ArrayList<WeightHistory> data){
        this.data = data;
    }

    @NonNull
    @Override
    public HistoryDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_history_weight,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDialogAdapter.ViewHolder viewHolder, int i) {
        viewHolder.date.setText(data.get(i).getTime());
        viewHolder.weight.setText(String.valueOf(data.get(i).getWeight()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView weight, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weight = itemView.findViewById(R.id.cardview_history_weight_weight);
            date = itemView.findViewById(R.id.cardview_history_weight_date);
        }
    }
}
