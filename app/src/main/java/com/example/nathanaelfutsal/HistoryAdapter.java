package com.example.nathanaelfutsal;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<JadwalModel> {

    private Context context;

    public HistoryAdapter(@NonNull Context context, ArrayList<JadwalModel> historyList) {
        super(context, 0, historyList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        JadwalModel model = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        }

        TextView tvJam = convertView.findViewById(R.id.tvHistoryJam);
        TextView tvStatus = convertView.findViewById(R.id.tvHistoryStatus);
        ImageView ivBukti = convertView.findViewById(R.id.ivHistoryBukti);

        if (model != null) {
            tvJam.setText("Jam Booking: " + model.getJam());

            String status = model.getStatus().toUpperCase();
            tvStatus.setText("Status: " + status);

            if (status.equals("DIPESAN")) {
                tvStatus.setTextColor(Color.parseColor("#FFC107"));
            } else if (status.equals("AVAILABLE")) {
                tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            } else if (status.equals("TERKONFIRMASI")) {
                tvStatus.setTextColor(Color.parseColor("#2196F3"));
            } else {
                tvStatus.setTextColor(Color.BLACK);
            }

            if (model.getBukti() != null && !model.getBukti().isEmpty()) {
                ivBukti.setVisibility(View.VISIBLE);
                try {
                    ivBukti.setImageURI(Uri.parse(model.getBukti()));
                } catch (Exception e) {
                    ivBukti.setImageResource(R.drawable.ic_image_placeholder);
                }
            } else {
                ivBukti.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}