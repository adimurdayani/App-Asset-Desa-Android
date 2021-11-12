package com.jumarni.appassetdesa.presentasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.model.DataPendudukModel;

import java.util.ArrayList;
import java.util.Collection;

public class PendudukAdapter extends RecyclerView.Adapter<PendudukAdapter.HolderData> {
    private Context context;
    private ArrayList<DataPendudukModel> pendudukModels;

    public PendudukAdapter(Context context, ArrayList<DataPendudukModel> pendudukModels) {
        this.context = context;
        this.pendudukModels = pendudukModels;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_penduduk, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataPendudukModel dataPendudukModel = pendudukModels.get(position);

        holder.txt_tgllahir.setText(dataPendudukModel.getCreated_at());
        holder.txt_nama.setText(dataPendudukModel.getNama_kk());
        holder.txt_dusun.setText(dataPendudukModel.getNama_dusun());
        holder.txt_rt.setText(dataPendudukModel.getRt());
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
                for (DataPendudukModel getdatapenduduk : pendudukModels) {
                    if (getdatapenduduk.getNama_kk().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getdatapenduduk.getRt().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getdatapenduduk.getNama_dusun().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        searchList.add(getdatapenduduk);
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
        private TextView txt_nama, txt_rt, txt_dusun, txt_tgllahir;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            txt_dusun = itemView.findViewById(R.id.nama_dusun);
            txt_nama = itemView.findViewById(R.id.nama);
            txt_rt = itemView.findViewById(R.id.rt);
            txt_tgllahir = itemView.findViewById(R.id.tgl_lahir);
        }
    }
}
