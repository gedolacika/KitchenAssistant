package com.example.laci.kitchenassistant.main.Account.HistoryDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.main.MainActivity;

public class HistoryDialog extends DialogFragment {
    private Button close;
    private RecyclerView history_recyclerView;
    private HistoryDialogAdapter adapter;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_history_weight,container, false);
        close = view.findViewById(R.id.dialog_history_close);
        history_recyclerView = view.findViewById(R.id.dialog_history_recycler_view);
        adapter = new HistoryDialogAdapter(((MainActivity)getActivity()).user.getWeightHistories());
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        history_recyclerView.setLayoutManager(manager);
        history_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        return view;
    }
}
