package com.example.youngkaaa.multirecycleradapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.youngkaaa.muti_recycler_adapter.MultiBaseRecyclerAdapter;
import com.example.youngkaaa.muti_recycler_adapter.OnItemClickedListener;

import java.util.List;
import java.util.Map;

/**
 * Created by : youngkaaa on 2016/10/9.
 * Contact me : 645326280@qq.com
 */

public class MyMultiAdapter extends MultiBaseRecyclerAdapter<TitleBean,ContentBean> {
    private OnItemClickedListener mItemClickedListener;
    private Context mContext;

    public MyMultiAdapter(Context context, Map<TitleBean, List<ContentBean>> data) {
        super(context, data);
        mContext=context;
    }

    public MyMultiAdapter(Context context, List<TitleBean> keys, Map<TitleBean, List<ContentBean>> data) {
        super(context, keys, data);
        mContext=context;
    }

    public void setItemClickedListener(OnItemClickedListener itemClickedListener) {
        mItemClickedListener = itemClickedListener;
    }

    @Override
    public int getTitleLayoutId() {
        return R.layout.title_layout;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.recycler_item;
    }

    @Override
    public void bindData(int type, BaseViewHolder holder, Object data) {
        TextView textViewLeft=holder.getViewById(R.id.textViewTitleLeft);
        TextView textViewRight=holder.getViewById(R.id.textViewTitleRight);
        TextView textViewContent=holder.getViewById(R.id.textViewRecyclerItemContent);
        ImageView imageView=holder.getViewById(R.id.imageViewContentIco);
        if(type==TYPE_TITLE){
            final TitleBean bean= (TitleBean) data;
            textViewLeft.setText(bean.getLeftTitle());
            textViewRight.setText(bean.getRightTitle());
            textViewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mItemClickedListener!=null){
                        mItemClickedListener.onClick(TYPE_TITLE,bean,view);
                    }
                }
            });
            textViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mItemClickedListener!=null){
                        mItemClickedListener.onClick(TYPE_TITLE,bean,view);
                    }
                }
            });

        }else{
            final ContentBean bean= (ContentBean) data;
            textViewContent.setText(bean.getContent());
            Glide.with(mContext).load(bean.getPicUrl())
                    .centerCrop().into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mItemClickedListener!=null){
                        mItemClickedListener.onClick(TYPE_TITLE,bean,view);
                    }
                }
            });
        }
    }
}
