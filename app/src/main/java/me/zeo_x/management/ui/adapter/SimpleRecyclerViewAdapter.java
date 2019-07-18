package me.zeo_x.management.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import me.zeo_x.management.R;
import me.zeo_x.management.ui.adapter.holder.SimpleAdapterHolder;

/**
 * Created by xiong on 2016/1/10.
 */
public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleAdapterHolder> {
    private Context context;
    private List<String> data;

    public SimpleRecyclerViewAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SimpleAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SimpleAdapterHolder holder = new SimpleAdapterHolder(LayoutInflater.from(context).inflate(R.layout.simple_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleAdapterHolder holder, int position) {
        holder.tv.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
