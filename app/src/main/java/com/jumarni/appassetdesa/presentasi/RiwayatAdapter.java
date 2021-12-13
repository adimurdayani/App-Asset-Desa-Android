package com.jumarni.appassetdesa.presentasi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.helper.FormatRupiah;
import com.jumarni.appassetdesa.model.DataAssetDesaModel;
import com.jumarni.appassetdesa.model.Dusun;
import com.jumarni.appassetdesa.model.Riwayat;
import com.jumarni.appassetdesa.view.activity.DaftarAsset;
import com.jumarni.appassetdesa.view.activity.SewaActivity;

import java.util.ArrayList;
import java.util.Collection;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.HolderData> {
    private Context context;
    private ArrayList<Riwayat> dataRiwayat;

    public RiwayatAdapter(Context context, ArrayList<Riwayat> dataRiwayat) {
        this.context = context;
        this.dataRiwayat = dataRiwayat;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_riwayat, parent, false);
        return new HolderData(view);
    }

    @SuppressLint( "SetTextI18n" )
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {

        Riwayat barang = dataRiwayat.get(position);

        FormatRupiah formatRupiah = new FormatRupiah();
        String harga = barang.getHarga();
        String hasil = formatRupiah.formatRupiah(Double.parseDouble(harga));

        holder.nama_barang.setText(barang.getNama_aset());
        holder.harga_aset.setText(hasil);
        holder.tgl_sewa.setText(barang.getCreated_at());
        holder.tgl_kembali.setText(barang.getTgl_kembali());

        int cekstatus = barang.getStatus();

        if (cekstatus != 0){
            holder.status.setText("TERSEDIA");
        }else{
            holder.status.setText("MENUNGGU");
        }
    }

    @Override
    public int getItemCount() {
        return dataRiwayat.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Riwayat> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(dataRiwayat);
            } else {
                for (Riwayat getdatapenduduk : dataRiwayat) {
                    if (getdatapenduduk.getNama_aset().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getdatapenduduk.getCreated_at().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getdatapenduduk.getTgl_kembali().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            dataRiwayat.clear();
            dataRiwayat.addAll((Collection<? extends Riwayat>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchData() {
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView nama_barang, harga_aset, tgl_sewa, tgl_kembali,status;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            nama_barang = itemView.findViewById(R.id.nama_barang);
            harga_aset = itemView.findViewById(R.id.harga_aset);
            tgl_sewa = itemView.findViewById(R.id.tgl_sewa);
            tgl_kembali = itemView.findViewById(R.id.tgl_kembali);
            status = itemView.findViewById(R.id.status);
        }
    }
}
