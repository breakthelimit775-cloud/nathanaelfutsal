package com.example.nathanaelfutsal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LapanganModel> listLapangan;

    public LapanganAdapter(Context context, ArrayList<LapanganModel> listLapangan) {
        this.context = context;
        this.listLapangan = listLapangan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lapangan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LapanganModel model = listLapangan.get(position);
        holder.tvNama.setText(model.getNamaLapangan());
        holder.tvHarga.setText(model.getHargaLapangan());
        holder.tvStatus.setText(model.getStatus());
        holder.imgFoto.setImageResource(model.getFotoLapangan());
    }

    @Override
    public int getItemCount() {
        return listLapangan.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;
        TextView tvNama, tvHarga, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgFotoLapangan);
            tvNama = itemView.findViewById(R.id.tvNamaLapangan);
            tvHarga = itemView.findViewById(R.id.tvHargaLapangan);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        LapanganModel modelTerklik = listLapangan.get(position);
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("NAMA_LAPANGAN", modelTerklik.getNamaLapangan());
                        intent.putExtra("HARGA_LAPANGAN", modelTerklik.getHargaLapangan());
                        intent.putExtra("FOTO_LAPANGAN", modelTerklik.getFotoLapangan());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}