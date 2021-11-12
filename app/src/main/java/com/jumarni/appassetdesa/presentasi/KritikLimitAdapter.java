package com.jumarni.appassetdesa.presentasi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.model.DataKritikModel;
import com.jumarni.appassetdesa.model.DataPendudukModel;
import com.jumarni.appassetdesa.view.activity.DetailKritikActivity;

import java.util.ArrayList;
import java.util.Collection;


public class KritikLimitAdapter extends RecyclerView.Adapter<KritikLimitAdapter.HolderData> {
    private Context context;
    private ArrayList<DataKritikModel> dataKritikModels;
    private SharedPreferences preferences;

    public KritikLimitAdapter(Context context, ArrayList<DataKritikModel> dataKritikModels) {
        this.context = context;
        this.dataKritikModels = dataKritikModels;
        preferences = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_id_data_kritik, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataKritikModel kritikModel = dataKritikModels.get(position);
        holder.txt_id.setText(String.valueOf(kritikModel.getId_kritik()));
        holder.txt_nama.setText(kritikModel.getNama());
        holder.txt_tgl.setText(kritikModel.getCreated_at());
        holder.txt_kritik.setText(kritikModel.getKritik());

        if (kritikModel.getUser_id() == preferences.getInt("id_regis", 0)) {
            holder.btn_option.setVisibility(View.VISIBLE);
        } else {
            holder.btn_option.setVisibility(View.VISIBLE);
        }

        holder.btn_option.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btn_option);
            popupMenu.inflate(R.menu.menu_option);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.item_detail:{
                            Intent detail = new Intent(context, DetailKritikActivity.class);
                            detail.putExtra("id_kritik", String.valueOf(kritikModel.getId_kritik()));
                            context.startActivity(detail);
                            return true;
                        }
                    }
                    return false;
                }
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return dataKritikModels.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DataKritikModel> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(dataKritikModels);
            } else {
                for (DataKritikModel getdatakritik : dataKritikModels) {
                    if (getdatakritik.getNama().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getdatakritik.getKritik().toLowerCase().contains(constraint.toString().toLowerCase())
                            || getdatakritik.getCreated_at().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        searchList.add(getdatakritik);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = searchList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataKritikModels.clear();
            dataKritikModels.addAll((Collection<? extends DataKritikModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchData() {
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView txt_nama, txt_kritik, txt_tgl, txt_id;
        private ImageButton btn_option;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            txt_nama = itemView.findViewById(R.id.nama);
            txt_kritik = itemView.findViewById(R.id.kritik);
            txt_tgl = itemView.findViewById(R.id.tanggal);
            txt_id = itemView.findViewById(R.id.id_kritik);
            btn_option = itemView.findViewById(R.id.btn_option);
        }
    }
}
