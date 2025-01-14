package com.zangcun.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zangcun.store.R;
import com.zangcun.store.model.LinCollectionModel;

import java.util.List;

//待收货适配器
public class LinCollectionAdapter extends BaseAdapter {
    private Context mContext;
    private List<LinCollectionModel> mDataList;

    public LinCollectionAdapter(Context mContext, List<LinCollectionModel> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public LinCollectionModel getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lindo, null);
            holder = new ViewHolder();
            holder.tv_lin_time = (TextView) convertView.findViewById(R.id.tv_lin_time);
            holder.lin_img = (ImageView) convertView.findViewById(R.id.lin_img);
            holder.lin_number = (TextView) convertView.findViewById(R.id.lin_number);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.btn_wuliu = (Button) convertView.findViewById(R.id.btn_go_pay);
            holder.btn_tuikuan = (Button) convertView.findViewById(R.id.btn_lin_cancle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tv_lin_time;//下单时间
        private ImageView lin_img;//商品图片
        private TextView lin_number;//商品数量
        private TextView money;//总价
        private Button btn_wuliu;//查看物流按钮
        private Button btn_tuikuan;//申请退款按钮
    }
}
