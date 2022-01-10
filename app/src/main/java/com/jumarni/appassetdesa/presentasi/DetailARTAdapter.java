package com.jumarni.appassetdesa.presentasi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.model.DataPendudukModel;
import com.jumarni.appassetdesa.model.Namakk;
import com.jumarni.appassetdesa.view.activity.DetailKkActivity;

import java.util.ArrayList;
import java.util.Collection;

public class DetailARTAdapter extends RecyclerView.Adapter<DetailARTAdapter.HolderData> {
    private Context context;
    private ArrayList<DataPendudukModel> pendudukModels;

    public DetailARTAdapter(Context context, ArrayList<DataPendudukModel> pendudukModels) {
        this.context = context;
        this.pendudukModels = pendudukModels;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_detail_penduduk, parent, false);
        return new HolderData(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataPendudukModel datanama = pendudukModels.get(position);
        holder.nik.setText(datanama.getNik());
        holder.no_kk.setText(datanama.getNo_kk());
        holder.tgl_lahir.setText(datanama.getTgl_lahir());
        holder.nama_art.setText(datanama.getNama_art());
        holder.kelamin.setText(datanama.getKelamin());
    }

    @Override
    public int getItemCount() {
        return pendudukModels.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DataPendudukModel> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(pendudukModels);
            } else {
                for (DataPendudukModel getnama : pendudukModels) {
                    if (getnama.getNama_kk().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        searchList.add(getnama);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = searchList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pendudukModels.clear();
            pendudukModels.addAll((Collection<? extends DataPendudukModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchData() {
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView nik, no_kk, nama_art, tgl_lahir, kelamin, dusun;
        private RelativeLayout layout;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            nik = itemView.findViewById(R.id.nik);
            no_kk = itemView.findViewById(R.id.no_kk);
            nama_art = itemView.findViewById(R.id.nama_art);
            tgl_lahir = itemView.findViewById(R.id.tgl_lahir);
            kelamin = itemView.findViewById(R.id.kelamin);
            dusun = itemView.findViewById(R.id.dusun);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
