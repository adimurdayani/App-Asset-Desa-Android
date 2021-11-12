package com.jumarni.appassetdesa.presentasi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.model.DataAssetDesaModel;
import com.jumarni.appassetdesa.view.activity.DaftarAsset;
import com.jumarni.appassetdesa.view.activity.SewaActivity;

import java.util.ArrayList;

public class DaftarAssetAdapter extends RecyclerView.Adapter<DaftarAssetAdapter.HolderData> {
    private Context context;
    private ArrayList<DataAssetDesaModel> dataAssetDesaModels;

    public DaftarAssetAdapter(Context context, ArrayList<DataAssetDesaModel> dataAssetDesaModels) {
        this.context = context;
        this.dataAssetDesaModels = dataAssetDesaModels;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_aset, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataAssetDesaModel assetDesaModel = dataAssetDesaModels.get(position);
        holder.txt_hargaaset.setText(assetDesaModel.getHarga());
        holder.txt_namaaset.setText(assetDesaModel.getNama_aset());
        holder.txt_asetid.setText(String.valueOf(assetDesaModel.getId_aset()));

        holder.relativeLayout.setOnClickListener(v -> {
            Intent i = new Intent(context, SewaActivity.class);
            i.putExtra("id_aset", String.valueOf(assetDesaModel.getId_aset()));
            i.putExtra("nama_aset", assetDesaModel.getNama_aset());
            i.putExtra("harga_aset", assetDesaModel.getHarga());
            context.startActivity(i);
            ((DaftarAsset)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return dataAssetDesaModels.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView txt_namaaset, txt_hargaaset, txt_asetid;
        private RelativeLayout relativeLayout;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.id_aset);
            txt_namaaset = itemView.findViewById(R.id.nama_aset);
            txt_hargaaset = itemView.findViewById(R.id.harga_aset);
            txt_asetid = itemView.findViewById(R.id.aset_id);
        }
    }
}
