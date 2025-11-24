package com.example.nathanaelfutsal;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder> {

    private Context context;
    private ArrayList<JadwalModel> listJadwal;
    private ArrayList<JadwalModel> selectedSlots = new ArrayList<>();
    private OnSelectionChangedListener listener;
    public JadwalAdapter(Context context, ArrayList<JadwalModel> listJadwal, OnSelectionChangedListener listener) {
        this.context = context;
        this.listJadwal = listJadwal;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_jadwal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalModel model = listJadwal.get(position);
        if (model.getJam() != null) {
            holder.tvJam.setText(model.getJam());
        } else {
            holder.tvJam.setText("Jam Error");
        }
        String status = model.getStatus();
        if (status == null) {
            status = "dipesan";
        }
        holder.tvStatus.setText(status);
        if (status.equalsIgnoreCase("tersedia")) {
            holder.tvStatus.setTextColor(Color.parseColor("#008000"));
            holder.itemView.setAlpha(1.0f);

            if (selectedSlots.contains(model)) {
                holder.cardJadwal.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple_200));
            } else {
                holder.cardJadwal.setCardBackgroundColor(Color.WHITE);
            }

        } else {
            holder.tvStatus.setTextColor(Color.RED);
            holder.itemView.setAlpha(0.5f);
            holder.cardJadwal.setCardBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return listJadwal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJam, tvStatus;
        CardView cardJadwal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvStatus = itemView.findViewById(R.id.tvStatusJadwal);
            cardJadwal = itemView.findViewById(R.id.cardJadwal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        JadwalModel model = listJadwal.get(position);

                        if (model.getStatus() == null) {
                            Toast.makeText(context, "Jadwal ini error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (model.getStatus().equalsIgnoreCase("tersedia")) {

                            if (selectedSlots.contains(model)) {
                                selectedSlots.remove(model);
                            } else {
                                selectedSlots.add(model);
                            }
                            notifyItemChanged(position);
                            listener.onSelectionChanged(selectedSlots);

                        } else {
                            Toast.makeText(context, "Jadwal ini sudah dipesan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    public interface OnSelectionChangedListener {
        void onSelectionChanged(ArrayList<JadwalModel> selectedSlots);
    }
    public void clear() {
        listJadwal.clear();
        selectedSlots.clear();
        if (listener != null) {
            listener.onSelectionChanged(selectedSlots);
        }
        notifyDataSetChanged();
    }
    public void add(JadwalModel model) {
        listJadwal.add(model);
        notifyItemInserted(listJadwal.size() - 1);
    }

}