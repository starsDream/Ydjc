package com.zangcun.store.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.android.volley.VolleyError;
import com.zangcun.store.BaseActivity;
import com.zangcun.store.R;
import com.zangcun.store.adapter.GoodsViewPagerAdapter;
import com.zangcun.store.model.FsypModel;
import com.zangcun.store.model.FxModel;
import com.zangcun.store.model.TkModel;
import com.zangcun.store.model.XdModel;
import com.zangcun.store.net.CommandBase;
import com.zangcun.store.net.Http;
import com.zangcun.store.net.Net;
import com.zangcun.store.other.Const;
import com.zangcun.store.utils.ToastUtils;
import com.zangcun.store.widget.AdapterIndicator;
import com.zangcun.store.widget.MyScrollView;
import com.zangcun.store.widget.MyScrollView.RealScrollView.OnScroll;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 商品详情
public class DetailActivity extends BaseActivity implements OnClickListener, Http.INetWork {

    private ImageView mBack;
    private TextView mTitle;

    private ViewPager mViewPager;
    private AdapterIndicator mIndicator;
    private TextView mPrice;
    private TextView mMarketPrice;
    private TextView mExpressTime;
    private TextView mGoodsDesc;
    private ImageView mCollect;
    private ImageView mToShopCar;
    private ImageView mTitleCollect;
    private ImageView mTitleToShopCar;
    private Button mAddToShopCar;
    private Button mAddToGoPay;
    private Http mHttp;
    private List<String> mGoodUrls = new ArrayList<String>();
    private List<String> mGoodContentUrls = new ArrayList<String>();
    private FxModel fxModel;
    private XdModel xdModel;
    private TkModel tkModel;
    private FsypModel fsypModel;

    private RelativeLayout mChooseKindOption;
    private LinearLayout mGoodShowLayout;
    private LinearLayout chooseChicun;
    private LinearLayout chooseColor;
    private LinearLayout mRightLayout;

    private MyScrollView mScrollView;
    private String kind;

    private PopupWindow mPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        init();
        initEvent();
        initData();
    }

    private void init() {
        mScrollView = (MyScrollView) findViewById(R.id.detail_scroll);
        mGoodShowLayout = (LinearLayout) findViewById(R.id.detail_good_show_layout);
        mRightLayout = (LinearLayout) findViewById(R.id.title_right_layout);
        mChooseKindOption = (RelativeLayout) findViewById(R.id.detail_choose_option);
        mBack = (ImageView) findViewById(R.id.goods_back);
        mTitle = (TextView) findViewById(R.id.tv_top_title);

        mIndicator = (AdapterIndicator) findViewById(R.id.goods_indicator);
        mViewPager = (ViewPager) findViewById(R.id.goods_pager);
        mPrice = (TextView) findViewById(R.id.price);
        mMarketPrice = (TextView) findViewById(R.id.market_price);
        mExpressTime = (TextView) findViewById(R.id.express_time);
        mGoodsDesc = (TextView) findViewById(R.id.goods_desc);
        mCollect = (ImageView) findViewById(R.id.collection);
        mToShopCar = (ImageView) findViewById(R.id.to_shop_car);
        mTitleCollect = (ImageView) findViewById(R.id.title_collection);
        mTitleToShopCar = (ImageView) findViewById(R.id.title_to_shop_car);
        mAddToShopCar = (Button) findViewById(R.id.add_to_shopcar);
        mAddToGoPay = (Button) findViewById(R.id.add_to_gopay);
    }

    private void initEvent() {
//		// 模拟数据
//		mGoodUrls
//				.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=4882823172c6a7efa626ae20cdfbafe9/f9dcd100baa1cd11dd1855cebd12c8fcc2ce2db5.jpg");
//		mGoodUrls
//				.add("http://d.hiphotos.baidu.com/image/h%3D360/sign=e0a211de5eafa40f23c6c8db9b65038c/562c11dfa9ec8a13f075f10cf303918fa1ecc0eb.jpg");
//		mGoodUrls
//				.add("http://e.hiphotos.baidu.com/image/h%3D360/sign=ea96ce4c0e7b020813c939e752d8f25f/14ce36d3d539b600be63e95eed50352ac75cb7ae.jpg");
//		mGoodUrls
//				.add("http://b.hiphotos.baidu.com/image/h%3D360/sign=4966caee48086e0675a8394d320a7b5a/023b5bb5c9ea15cec72cb6d6b2003af33b87b22b.jpg");
//		mGoodUrls
//				.add("http://a.hiphotos.baidu.com/image/h%3D360/sign=0acd05e7552c11dfc1d1b92553276255/e850352ac65c10383b570cc6b0119313b07e898d.jpg");


        mScrollView.setOnScroll(new OnScroll() {
            @Override
            public void onScrollY(int y) {
                //如果滑动出 收藏图标之外 则 显示titlebar的图标
                Log.d("debug", "y:" + y);
                if (y > 650) {
                    mRightLayout.setVisibility(View.VISIBLE);
                } else {
                    mRightLayout.setVisibility(View.INVISIBLE);
                }

            }
        });
        mMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mBack.setOnClickListener(this);
        mCollect.setOnClickListener(this);
        mToShopCar.setOnClickListener(this);
        mAddToShopCar.setOnClickListener(this);
        mAddToGoPay.setOnClickListener(this);
        mTitleCollect.setOnClickListener(this);
        mTitleToShopCar.setOnClickListener(this);
        mChooseKindOption.setOnClickListener(this);

    }

    private void addShop() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("good_id", fxModel.getGoods_id() + "");
        map.put("count", "1");
        map.put("option_id ", fxModel.getOptions_id() + "");
        CommandBase.requestDataMap(getApplicationContext(), Const.URL_CARTS, handler, map);

    }


    private void initData() {


        //根据不同的activity进入
        //设置不同种类的数据
        kind = getIntent().getStringExtra("kind");
        if (kind == null) {
            return;
        }
        switch (kind) {
            case "fx":
                fxModel = (FxModel) getIntent().getSerializableExtra("fx");
                if (fxModel == null) {
                    return;
                }
                mGoodsDesc.setText(fxModel.getGoods_name());
                mPrice.setText("¥" + fxModel.getPrice());
                mMarketPrice.setText("市场价：" + "¥" + fxModel.getMarket_price());
                mGoodUrls = fxModel.getGood_image_urls();
                mGoodContentUrls = fxModel.getContents();
                addGoodsContent();
                break;
            case "tk":
                tkModel = (TkModel) getIntent().getSerializableExtra("tk");
                if (tkModel == null) {
                    return;
                }
                mGoodsDesc.setText(tkModel.getGoods_name());
                mPrice.setText("¥" + tkModel.getPrice());
                mMarketPrice.setText("市场价：" + "¥" + tkModel.getMarket_price());
                mGoodUrls = tkModel.getGood_image_urls();
                mGoodContentUrls = tkModel.getContents();
                addGoodsContent();
                break;
            case "fsyp":
                fsypModel = (FsypModel) getIntent().getSerializableExtra("fsyp");
                if (fsypModel == null) {
                    return;
                }
                mGoodsDesc.setText(fsypModel.getGoods_name());
                mPrice.setText("¥" + fsypModel.getPrice());
                mMarketPrice.setText("市场价：" + "¥" + fsypModel.getMarket_price());
                mGoodUrls = fsypModel.getGood_image_urls();
                mGoodContentUrls = fsypModel.getContents();
                addGoodsContent();
                break;
            case "xd":
                xdModel = (XdModel) getIntent().getSerializableExtra("xd");
                if (xdModel == null) {
                    return;
                }
                mGoodsDesc.setText(xdModel.getGoods_name());
                mPrice.setText("¥" + xdModel.getPrice());
                mMarketPrice.setText("市场价：" + "¥" + xdModel.getMarket_price());
                mGoodUrls = xdModel.getGood_image_urls();
                mGoodContentUrls = xdModel.getContents();
                addGoodsContent();
                break;
        }

        mIndicator.bindViewPager(mViewPager);
        mIndicator.setPointCount(mGoodUrls.size());
        mViewPager.setAdapter(new GoodsViewPagerAdapter(mGoodUrls));


        if (mHttp == null) {
            mHttp = new Http(this);
        }

//		mHttp.get(Net.URL_GOODS_DEAIL, this, Const.REQUEST_DETAIL);
        // to do
    }


    private void addGoodsContent() {
//		for (int i = 0; i < mGoodContentUrls.size(); i++) {
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(this);
            Picasso.with(this).load(Net.DOMAIN + mGoodContentUrls.get(i)).into(imageView);
            mGoodShowLayout.addView(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_back:
                finish();
                break;

            case R.id.detail_choose_option:
                //如果有选择种类 选项
                chooseOption();
                break;
            case R.id.collection:
                collect();
                break;
            case R.id.to_shop_car:
                toShopCar();
                startActivity(new Intent(getApplicationContext(), ShopCarActivity.class));
                break;
            case R.id.title_collection://标题栏的收藏按钮
                collect();
                break;
            case R.id.title_to_shop_car://标题栏加入购物按钮
                toShopCar();
                break;
            case R.id.add_to_shopcar:
                addToShopCar();
                chooseOption();
                ToastUtils.show(this, "加入购物车成功~");
                break;
            case R.id.add_to_gopay:
//                chooseOption();//选择尺寸颜色后购买
                Intent intent = new Intent(getApplication(), PayActivity.class);
                startActivity(intent);
                break;
            case R.id.goods_choose_del://pupup弹窗内的"x"
                mPopWindow.dismiss();
                break;
        }
    }

    /**
     * 如果特殊商品有选择种类选项
     */
    private void chooseOption() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_choosekind, null);
        mPopWindow = new PopupWindow(contentView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setAnimationStyle(R.style.popwin_anim_style);
        chooseChicun = (LinearLayout) contentView.findViewById(R.id.choose_chicun_layout);
        chooseColor = (LinearLayout) contentView.findViewById(R.id.choose_color_layout);
        ImageView less = (ImageView) contentView.findViewById(R.id.choose_less);
        ImageView more = (ImageView) contentView.findViewById(R.id.choose_more);
        ImageView del = (ImageView) contentView.findViewById(R.id.goods_choose_del);
        del.setOnClickListener(this);
        final TextView count = (TextView) contentView.findViewById(R.id.choose_count);
        less.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(count.getText().toString()) <= 0) {
                    return;
                }
                count.setText((Integer.valueOf(count.getText().toString()) - 1) + "");
            }
        });
        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count.setText((Integer.valueOf(count.getText().toString()) + 1) + "");
            }
        });
        //获得数据 动态加载到 俩个layout
        //模拟 3个 到时候是动态的变量
        for (int i = 0; i < 5; i++) {
            TextView chicun = new TextView(this);
            //通过给textView设置 selector背景来选择
            chicun.setBackgroundResource(R.drawable.item_filter_selector);
            chicun.setText("" + i);
            chicun.setPadding(10, 10, 10, 10);
            chicun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //因为是单选 所以要先把状态初始化
                    for (int j = 0; j < 5; j++) {
                        chooseChicun.getChildAt(j).setSelected(false);
                    }
                    v.setSelected(!v.isSelected());
                }
            });
            chooseChicun.addView(chicun);
        }

        //模拟两个种类
        for (int i = 0; i < 4; i++) {
            TextView color = new TextView(this);
            color.setBackgroundResource(R.drawable.item_filter_selector);
            color.setPadding(10, 10, 10, 10);
            color.setText("" + i);
            color.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //因为是单选 所以要先把状态初始化
                    for (int j = 0; j < 4; j++) {
                        chooseColor.getChildAt(j).setSelected(false);
                    }

                    v.setSelected(!v.isSelected());
                }
            });
            chooseColor.addView(color);
        }
        //显示PopupWindow
        View rootview = LayoutInflater.from(this).inflate(R.layout.dialog_choosekind, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    private void addToShopCar() {
        //接口访问 操作购物车数据
        //to-do
    }

    private void toShopCar() {
//         startActivity(new Intent(this, ShopCarActivity.class));
        Map<String, String> map = new HashMap<String, String>();
        switch (kind) {
            case "fx":
                map.put("good_id ", String.valueOf(fxModel.getGoods_id()));
                map.put("count", "1");
                map.put("option_id ", fxModel.getOptions_id() + "");
                CommandBase.requestDataNoMap(getApplicationContext(), Net.URL_GOODS_CARTS, handler, map);
                break;
            case "tk":
                map.put("good_id ", String.valueOf(tkModel.getGoods_id()));
                map.put("count", "1");
                map.put("option_id ", tkModel.getOptions_id() + "");
                CommandBase.requestDataNoMap(getApplicationContext(), Net.URL_GOODS_CARTS, handler, map);
                break;
            case "fsyp":
                map.put("good_id ", String.valueOf(fsypModel.getGoods_id()));
                map.put("count", "1");
                map.put("option_id ", fsypModel.getOptions_id() + "");
                CommandBase.requestDataNoMap(getApplicationContext(), Net.URL_GOODS_CARTS, handler, map);
                break;
            case "xd":
                map.put("good_id ", String.valueOf(xdModel.getGoods_id()));
                map.put("count", "1");
                map.put("option_id ", xdModel.getOptions_id() + "");
                CommandBase.requestDataNoMap(getApplicationContext(), Net.URL_GOODS_CARTS, handler, map);
                break;
        }

    }

    private void collect() {
        mCollect.setSelected(!mCollect.isSelected());
        mTitleCollect.setSelected(!mTitleCollect.isSelected());
        //处理收藏数据

    }

    @Override
    public void onNetSuccess(String response, int requestCode) {

    }

    @Override
    public void onNetError(VolleyError error, int requestCode) {
        if (this != null) {
            ToastUtils.show(this, "网络连接失败");
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Const.SUCCESS) {
                startActivity(new Intent(getApplicationContext(), ShopCarActivity.class));
            } else if (msg.what == Const.ERROR) {

            }
        }
    };
}
