package me.zeo_x.management.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import me.zeo_x.management.R;
import me.zeo_x.management.ui.activity.ContractDetailActivity;
import me.zeo_x.management.ui.adapter.holder.ContractHolder;

/**
 * Created by xiong on 2016/1/21.
 */
public class ContractAdapter extends RecyclerView.Adapter<ContractHolder> {
    private Context context;
    private List<Map<String, String>> data;

    public ContractAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ContractHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContractHolder(LayoutInflater.from(context).inflate(R.layout.contract_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContractHolder holder, int position) {
        final Map<String, String> m = data.get(position);
        holder.tv_contract_id.setText(m.get("hetong_id"));
        holder.tv_property_name.setText(m.get("zichan_name"));
        holder.tv_contract_date.setText(m.get("hetong_date"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ContractDetailActivity.class);
                i.putExtra("hetong_id", m.get("hetong_id"));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
