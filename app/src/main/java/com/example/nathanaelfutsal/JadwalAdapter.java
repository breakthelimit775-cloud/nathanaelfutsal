package com.example.nathanaelfutsal;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<JadwalModel> jadwalList;
    private final OnJadwalClickListener listener;
    private final Set<String> jamDipilih;

    public JadwalAdapter(Context context, ArrayList<JadwalModel> jadwalList, OnJadwalClickListener listener) {
        this.context = context;
        this.jadwalList = jadwalList;
        this.listener = listener;
        this.jamDipilih = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_jadwal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalModel model = jadwalList.get(position);
        if (holder.tvJam != null) {
            holder.tvJam.setText(model.getJam());
        }
        if (holder.tvStatus != null) {
            holder.tvStatus.setText(model.getStatus().toUpperCase());
        }

        String status = model.getStatus().toLowerCase();

        boolean isSelected = jamDipilih.contains(model.getJam());

        if (status.equals("available")) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            if (isSelected) {
                holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        } else if (status.equals("dipesan") || status.equals("terkonfirmasi")) {
            holder.tvStatus.setTextColor(Color.RED);
            holder.itemView.setBackgroundColor(Color.parseColor("#FDD8D8"));
        } else {
            holder.tvStatus.setTextColor(Color.BLACK);
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (status.equals("available")) {
                boolean currentlySelected = jamDipilih.contains(model.getJam());

                if (currentlySelected) {
                    jamDipilih.remove(model.getJam());
                    listener.onJadwalClick(model.getJam(), false);
                } else {
                    jamDipilih.add(model.getJam());
                    listener.onJadwalClick(model.getJam(), true);
                }
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Jadwal ini sudah " + model.getStatus().toUpperCase(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvJam;
        final TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    public interface OnJadwalClickListener {
        void onJadwalClick(String jam, boolean isSelected);
    }
}