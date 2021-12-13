package com.jumarni.appassetdesa.presentasi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.helper.FormatRupiah;
import com.jumarni.appassetdesa.model.Barang;
import com.jumarni.appassetdesa.model.DataAssetDesaModel;
import com.jumarni.appassetdesa.room.AppDatabase;
import com.jumarni.appassetdesa.view.activity.DaftarAsset;
import com.jumarni.appassetdesa.view.activity.SewaActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaftarAssetAdapter extends RecyclerView.Adapter<DaftarAssetAdapter.HolderData> {
    private Context context;
    private ArrayList<DataAssetDesaModel> databarang;
    private StringRequest updateAset;

    public DaftarAssetAdapter(Context context, ArrayList<DataAssetDesaModel> databarang) {
        this.context = context;
        this.databarang = databarang;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_aset, parent, false);
        return new HolderData(view);
    }

    @SuppressLint( "SetTextI18n" )
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {

        DataAssetDesaModel barang = databarang.get(position);

        FormatRupiah formatRupiah = new FormatRupiah();
        String harga = barang.getHarga();
        String hasil = formatRupiah.formatRupiah(Double.parseDouble(harga));

        holder.txt_hargaaset.setText(hasil);
        holder.txt_namaaset.setText(barang.getNama_aset());
        holder.jumlah_asset.setText(barang.getJml_aset() + " Barang tersedia");
        holder.txt_asetid.setText(String.valueOf(barang.getId_aset()));

        holder.relativeLayout.setOnClickListener(v -> {
            Intent i = new Intent(context, SewaActivity.class);
            i.putExtra("id_aset", barang.getId_aset());
            i.putExtra("nama_aset", barang.getNama_aset());
            i.putExtra("harga_aset", barang.getHarga());
            i.putExtra("jml_aset", barang.getJml_aset());
            context.startActivity(i);
            ((DaftarAsset) context).finish();
        });

    }

    @Override
    public int getItemCount() {
        return databarang.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView txt_namaaset, txt_hargaaset, txt_asetid, jumlah_asset;
        private RelativeLayout relativeLayout;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.id_aset);
            txt_namaaset = itemView.findViewById(R.id.nama_aset);
            txt_hargaaset = itemView.findViewById(R.id.harga_aset);
            txt_asetid = itemView.findViewById(R.id.aset_id);
            jumlah_asset = itemView.findViewById(R.id.jumlah_asset);
        }
    }
}
