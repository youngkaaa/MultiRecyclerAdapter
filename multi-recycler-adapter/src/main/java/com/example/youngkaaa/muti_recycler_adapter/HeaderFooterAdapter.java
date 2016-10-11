package com.example.youngkaaa.muti_recycler_adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by : youngkaaa on 2016/10/11.
 * Contact me : 645326280@qq.com
 */

public class HeaderFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE + 1;

    private int mFooterId;
    private int mHeaderId;
    private Context mContext;
    private LayoutInflater mInflater;

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition + 1, toPosition + 1 + itemCount);
        }
    };

    public HeaderFooterAdapter(Context context, RecyclerView.Adapter adapter){
        this.mContext=context;
        setAdapter(adapter);
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER_VIEW){
            return new ViewHolder(mInflater.inflate(mHeaderId,parent,false));
        }else if(viewType==TYPE_FOOTER_VIEW){
            return new ViewHolder(mInflater.inflate(mFooterId,parent,false));
        }else{
            return mAdapter.onCreateViewHolder(parent,viewType);
        }
    }

    public void setHeaderResId(@LayoutRes int id) {
        this.mHeaderId = id;
    }

    public void setFooterResId(@LayoutRes int id) {
        this.mFooterId = id;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position>=1 && position<1+mAdapter.getItemCount()){
            mAdapter.onBindViewHolder(holder, position - 1);
        }
    }

    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (adapter != null) {
            if (!(adapter instanceof RecyclerView.Adapter))
                throw new RuntimeException("your adapter must be a RecyclerView.Adapter");
        }
        if (mAdapter != null) {
            notifyItemRangeRemoved(1, mAdapter.getItemCount());
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);
        notifyItemRangeInserted(1, mAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 1) {
            return TYPE_HEADER_VIEW;
        } else if (position >= 1 + mAdapter.getItemCount()) {
            return TYPE_FOOTER_VIEW + position - 1 - mAdapter.getItemCount();
        } else {
            return mAdapter.getItemViewType(position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return  2+mAdapter.getItemCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
