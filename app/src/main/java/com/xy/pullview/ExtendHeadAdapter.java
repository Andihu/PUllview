package com.xy.pullview;

import android.view.View;
import android.widget.TextView;


import com.xy.pullview.CommonAdapter;
import com.xy.pullview.R;
import com.xy.pullview.pullview.ViewHolder;

import java.util.List;

/**
 * Created by Renny on 2018/1/3.
 */

public class ExtendHeadAdapter extends CommonAdapter<String> {

    public ExtendHeadAdapter(List<String> datas) {
        super( R.layout.item_header, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final int position) {
        String data=getData(position);
        TextView tv = holder.getView(R.id.item_title);
        tv.setText(data);
        if (mItemClickListener != null) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(position, v);
                }
            });
        }
    }


}
