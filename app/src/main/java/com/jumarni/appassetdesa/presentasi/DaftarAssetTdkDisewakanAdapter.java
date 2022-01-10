package com.jumarni.appassetdesa.presentasi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.helper.FormatRupiah;
import com.jumarni.appassetdesa.model.AsetTidakDisewakan;
import com.jumarni.appassetdesa.model.DataAssetDesaModel;
import com.jumarni.appassetdesa.view.activity.DaftarAsset;
import com.jumarni.appassetdesa.view.activity.SewaActivity;

import java.util.ArrayList;

public class DaftarAssetTdkDisewakanAdapter extends RecyclerView.Adapter<DaftarAssetTdkDisewakanAdapter.HolderData> {
    private Context context;
    private ArrayList<AsetTidakDisewakan> databarang;
    private StringRequest updateAset;

    public DaftarAssetTdkDisewakanAdapter(Context context, ArrayList<AsetTidakDisewakan> databarang) {
        this.context = context;
        this.databarang = databarang;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_aset2, parent, false);
        return new HolderData(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {

        AsetTidakDisewakan barang = databarang.get(position);
        holder.txt_namaaset.setText(barang.getNama_barang());
        holder.jumlah_asset.setText("Jumlah: "+barang.getJml_barang());

    }

    @Override
    public int getItemCount() {
        return databarang.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView txt_namaaset, jumlah_asset;
        private RelativeLayout relativeLayout;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.id_aset);
            txt_namaaset = itemView.findViewById(R.id.nama_aset);
            jumlah_asset = itemView.findViewById(R.id.jumlah_asset);
        }
    }
}
