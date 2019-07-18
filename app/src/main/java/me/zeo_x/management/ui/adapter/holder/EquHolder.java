package me.zeo_x.management.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.zeo_x.management.R;

/**
 * Created by xiong on 2016/1/10.
 */
public class EquHolder extends RecyclerView.ViewHolder {
    public TextView tv_equ_id, tv_equ_name;

    public EquHolder(View itemView) {
        super(itemView);
        tv_equ_id = (TextView) itemView.findViewById(R.id.tv_equ_id);
        tv_equ_name = (TextView) itemView.findViewById(R.id.tv_equ_name);
    }
}
