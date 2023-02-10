package com.dam2023.snippets.activity05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam2023.snippets.R;

//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
// RECYCLERVIEW
//=============================================================================
// ADAPTER : adapte les DATA aux template design
//-----------------------------------------------------------------------------
// VIEWHOLDER : fait le lien entre JAVA et le design
//=============================================================================
public class A54_AdapterRecyclerView extends RecyclerView.Adapter<A54_AdapterRecyclerView.MyViewHolder> {

    private Context context;
    private String[] stagiaires, descs;
    private int[] avatars;

    public A54_AdapterRecyclerView() {
    }

    public A54_AdapterRecyclerView(Context context, String[] stagiaires, String[] descs, int[] avatars) {
        this.context = context;
        this.stagiaires = stagiaires;
        this.descs = descs;
        this.avatars = avatars;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.a54_item_recyclerview, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ivAvatar.setImageResource(avatars[position]);
        holder.tvTitre.setText(stagiaires[position]);
        holder.tvDesc.setText(descs[position]);
    }

    @Override
    public int getItemCount() {
        return avatars.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatar;
        private TextView tvTitre, tvDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }

}
