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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.DataPendudukModel;
import com.jumarni.appassetdesa.model.Dusun;
import com.jumarni.appassetdesa.model.Namakk;
import com.jumarni.appassetdesa.view.activity.DetailKkActivity;
import com.jumarni.appassetdesa.view.activity.DetailPendudukActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class DetailPendudukAdapter extends RecyclerView.Adapter<DetailPendudukAdapter.HolderData> {
    private Context context;
    private ArrayList<Namakk> namakks;
    private int kk_id;
    private StringRequest getPenduduk;

    public DetailPendudukAdapter(Context context, ArrayList<Namakk> namakks) {
        this.context = context;
        this.namakks = namakks;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_jml_art, parent, false);
        return new HolderData(view);
    }

    @SuppressLint( "SetTextI18n" )
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {

        Namakk datanama = namakks.get(position);
        holder.nama.setText(datanama.getNama_kk());

        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailKkActivity.class);
            intent.putExtra("kk_id", datanama.getId_kk());
            context.startActivity(intent);
            Log.d("Respon", "Data: " + datanama.getId_kk());
        });
        kk_id = datanama.getId_kk();

        if (kk_id != 0){
            getPenduduk = new StringRequest(Request.Method.GET, URLServer.GETJMLART
                    + kk_id, response -> {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getString("data").matches("0")) {
                        holder.jml_art.setText(object.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);
            RequestQueue koneksi = Volley.newRequestQueue(context);
            koneksi.add(getPenduduk);
        }

    }

    @Override
    public int getItemCount() {
        return namakks.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Namakk> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(namakks);
            } else {
                for (Namakk getnama : namakks) {
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
            namakks.clear();
            namakks.addAll((Collection<? extends Namakk>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchData() {
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView nama, jml_art;
        private RelativeLayout layout;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            jml_art = itemView.findViewById(R.id.jml_art);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
