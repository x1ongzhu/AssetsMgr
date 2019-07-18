package me.zeo_x.management.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.zeo_x.management.R;

/**
 * Created by xiong on 2016/1/21.
 */
public class ContractHolder extends RecyclerView.ViewHolder {
    public TextView tv_contract_id, tv_property_name,tv_contract_date;

    public ContractHolder(View itemView) {
        super(itemView);
        tv_contract_id=(TextView)itemView.findViewById(R.id.tv_contract_id);
        tv_property_name=(TextView)itemView.findViewById(R.id.tv_property_name);
        tv_contract_date=(TextView)itemView.findViewById(R.id.tv_contract_date);

    }
}
