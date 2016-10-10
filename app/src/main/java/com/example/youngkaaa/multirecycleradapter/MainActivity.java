package com.example.youngkaaa.multirecycleradapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.youngkaaa.muti_recycler_adapter.OnItemClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyMultiAdapter mMutiAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return mMutiAdapter.isTitleView(position)?gridLayoutManager.getSpanCount():1;
//            }
//        });
        mMutiAdapter = new MyMultiAdapter(this, initListKeys(), initMapData());

        mRecyclerView.setAdapter(mMutiAdapter);


        mMutiAdapter.setItemClickedListener(new OnItemClickedListener() {
            @Override
            public void onClick(int type, Object o, View v) {
                Log.d("kklog","v.getId==>"+v.getId());
                //方法1()
                switch (v.getId()){
                    case R.id.textViewTitleLeft:
                        Toast.makeText(MainActivity.this, "Title类左边点击了 ==> " + ((TitleBean) o).getLeftTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.textViewTitleRight:
                        Toast.makeText(MainActivity.this, "Title类右边点击了 ==> " + ((TitleBean) o).getRightTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.imageViewContentIco:
                        Toast.makeText(MainActivity.this, "非Title类 ==> " + ((ContentBean) o).getContent(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public List<String> initData(String data, int num) {
        List<String> dataTemp = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            dataTemp.add(data + i);
        }
        return dataTemp;
    }

    public List<ContentBean> initData(int num,int kind) {
        List<ContentBean> dataTemp = new ArrayList<>();
        String resUrl=null;
        String content=null;
        if(kind==1){
            resUrl="http://d.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=139697f9ac18972ba33a07ccdef61cb4/9213b07eca806538cf570ffe90dda144ac34829a.jpg";
            content="齐木楠雄的灾难(日播版)";
        }else if(kind==2){
            resUrl="http://img0.imgtn.bdimg.com/it/u=3852624731,2910808955&fm=21&gp=0.jpg";
            content="刀剑乱舞";
        }else if(kind==3){
            resUrl="http://box.bfimg.com/img/67/787567/51_270*340.jpg";
            content="民间鬼术";
        }else if(kind==4){
            resUrl="http://img0.imgtn.bdimg.com/it/u=3854742867,2477250643&fm=21&gp=0.jpg";
            content="求求你吃我吧";
        }else{
            resUrl="http://cdn.duitang.com/uploads/item/201411/26/20141126210156_thKZ3.jpeg";
            content="火影忍者";
        }
        for (int i = 0; i < num; i++) {
            ContentBean bean=new ContentBean(resUrl,content);
            dataTemp.add(bean);
        }
        return dataTemp;
    }


    public List<TitleBean> initListKeys() {
        List<TitleBean> keys = new ArrayList<>();
        TitleBean bean1 = new TitleBean("新番连载", "所有连载>");
        TitleBean bean2 = new TitleBean("国产动画", "更多>");
        TitleBean bean3 = new TitleBean("7月新番", "分季列表>");
        TitleBean bean4 = new TitleBean("8月新番", "分季列表>");
        TitleBean bean5 = new TitleBean("9月新番", "分季列表>");

        keys.add(bean1);
        keys.add(bean2);
        keys.add(bean3);
        keys.add(bean4);
        keys.add(bean5);
        return keys;
    }

    public Map<TitleBean, List<ContentBean>> initMapData() {
        Map<TitleBean, List<ContentBean>> map = new HashMap<>();
        List<TitleBean> keys = initListKeys();
        map.put(keys.get(0), initData(3,1));
        map.put(keys.get(1), initData(6,2));
        map.put(keys.get(2), initData(6,3));
        map.put(keys.get(3), initData(3,4));
        map.put(keys.get(4), initData(3,5));
        return map;
    }

}
