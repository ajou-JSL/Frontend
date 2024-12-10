package com.example.moum.view.moum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Settlement;
import com.example.moum.view.moum.MoumPaymentActivity;

import java.util.ArrayList;

public class MoumSettlementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Settlement> settlements;
    private Context context;

    public void setSettlements(ArrayList<Settlement> settlements, @NonNull Context context) {
        this.settlements = settlements;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new MoumSettlementAdapter.MoumSettlementViewHolder(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Settlement settlement = settlements.get(position);
        ((MoumSettlementAdapter.MoumSettlementViewHolder) holder).bind(settlement);
    }

    @Override
    public int getItemCount() {
        return settlements.size();
    }

    static class MoumSettlementViewHolder extends RecyclerView.ViewHolder {
        private Settlement settlement;
        private TextView settlementName;
        private TextView settlementFee;
        private ImageView deleteButton;
        private MoumPaymentActivity activity;
        private Context context;

        public MoumSettlementViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            settlementName = itemView.findViewById(R.id.payment_name);
            settlementFee = itemView.findViewById(R.id.payment_fee);
            deleteButton = itemView.findViewById(R.id.imageview_delete);
            this.activity = (MoumPaymentActivity) context;
            this.context = context;
        }

        @SuppressLint("DefaultLocale")
        public void bind(Settlement settlement) {
            this.settlement = settlement;
            if (settlement.getSettlementName() != null) settlementName.setText(settlement.getSettlementName());
            if (settlement.getFee() != null) settlementFee.setText(String.format("%,d", settlement.getFee()));
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onSettlementDeleteClicked(settlement);
                }
            });
        }

    }
}
