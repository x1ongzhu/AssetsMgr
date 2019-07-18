package me.zeo_x.management.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import me.zeo_x.management.R;
import me.zeo_x.management.ui.adapter.holder.EquHolder;

/**
 * Created by xiong on 2016/1/10.
 */
public class EquAdapter extends RecyclerView.Adapter<EquHolder> {
    private Context context;
    private List<Map<String, String>> data;
    private Map<String, List<String>> map;
    private OnItemClickListener onItemClickListener;

    public EquAdapter(Context context, Map<String, List<String>> map, List<Map<String, String>> data) {
        this.context = context;
        this.map = map;
        this.data = data;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public EquHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EquHolder holder = new EquHolder(LayoutInflater.from(context).inflate(R.layout.equ_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final EquHolder holder, final int position) {
        if (map.containsKey(data.get(position).get("equ_id"))) {
            holder.tv_equ_id.setTextColor(Color.RED);
            holder.tv_equ_name.setTextColor(Color.RED);
        } else {
            holder.tv_equ_id.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tv_equ_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.tv_equ_id.setText(data.get(position).get("equ_id"));
        holder.tv_equ_name.setText(data.get(position).get("equ_name"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(holder.itemView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
