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

    @SuppressLint( "SetTextI18n" )
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataPendudukModel datanama = pendudukModels.get(position);
        holder.nama.setText(datanama.getNama_kk());
        holder.nama_art.setText(datanama.getNama_kk() + " - " + datanama.getNama_art());
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
        private TextView nama, nama_art, kelamin;
        private RelativeLayout layout;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            nama_art = itemView.findViewById(R.id.nama_art);
            layout = itemView.findViewById(R.id.layout);
            kelamin = itemView.findViewById(R.id.kelamin);
        }
    }
}
