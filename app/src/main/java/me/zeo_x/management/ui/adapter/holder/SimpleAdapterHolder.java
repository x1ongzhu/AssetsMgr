package me.zeo_x.management.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.zeo_x.management.R;

/**
 * Created by xiong on 2016/1/10.
 */
public class SimpleAdapterHolder extends RecyclerView.ViewHolder {
    public TextView tv;

    public SimpleAdapterHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.tv);
    }
}
