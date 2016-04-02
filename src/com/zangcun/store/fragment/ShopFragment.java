package com.zangcun.store.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.zangcun.store.R;
import com.zangcun.store.activity.PayActivity;
import com.zangcun.store.adapter.ShopCarAdapter;
import com.zangcun.store.model.ShopCarModel;


import java.util.ArrayList;
import java.util.List;

// 购物车
public class ShopFragment extends BaseFragment implements
        ShopCarAdapter.PriceAndCountChangeListener {
    public static ShopFragment getInstance() {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }

    private ListView mListView;
    private TextView mShoudPay;
    private TextView mToPay;
    private LinearLayout mEmptyLayout;
    private LinearLayout mToBuyLayout;

    private List<ShopCarModel> mDatas;
    private ShopCarAdapter mAdapter;
    private int count;

    @Override
    protected int contentViewId() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void onFirstPreLoading() {
        mEmptyLayout = (LinearLayout) mView.findViewById(R.id.gwc_null);
        mToBuyLayout = (LinearLayout) mView.findViewById(R.id.gwc_layout);
        Button button = (Button) mView.findViewById(R.id.goshopping);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				 FragmentManager fm = mThis.getSupportFragmentManager();
//				 fm.beginTransaction().replace(R.id.shop_container,new
//				 SortFragment()).commit();
                // getFragmentManager().beginTransaction().replace(, new
                // SortFragment()).commit();
                // SortFragment second = new SortFragment();
                // FragmentManager fm = getFragmentManager();
                // FragmentTransaction ft = fm.beginTransaction();
                // ft.replace(R.id.sortfragment, second);
                // ft.addToBackStack("tag");
                // ft.commit();
            }
        });

        init();
        initData();

    }

    private void initData() {

        mDatas = new ArrayList<ShopCarModel>();

        //从网络获取数据
        //无用户token
        //这里做模拟数据

        // 模拟数据
        for (int i = 0; i < 5; i++) {
            mDatas.add(new ShopCarModel());
        }
        // 如果没有数据则显示为空
        if (mDatas == null || mDatas.size() == 0) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mToBuyLayout.setVisibility(View.GONE);
        } else {
            mAdapter = new ShopCarAdapter(getActivity(), mDatas,
                    R.layout.item_shop_car);
            mAdapter.setListener(this);
            mListView.setDividerHeight(20);
            mListView.setAdapter(mAdapter);
        }

    }

    private void init() {

        mListView = (ListView) mView.findViewById(R.id.gwc_listview);
        mToPay = (TextView) mView.findViewById(R.id.gwc_to_pay);
        mShoudPay = (TextView) mView.findViewById(R.id.gwc_should_pay);
        mToPay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (count <= 0) {
                    return;
                }
                startActivity(new Intent(getContext(), PayActivity.class));
            }
        });


    }

    @Override
    protected void onFirstLoadingData() {

    }

    @Override
    protected void onFirstLoadingFinish() {

    }

    @Override
    public void onPriceChanged(int total) {
        mShoudPay.setText("应付金额: ¥" + total);
    }

    @Override
    public void onCountCHanged(int count) {
        this.count = count;
        mToPay.setText("去结算(" + count + ")");
    }
}