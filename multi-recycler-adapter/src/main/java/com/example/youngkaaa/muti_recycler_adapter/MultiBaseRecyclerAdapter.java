package com.example.youngkaaa.muti_recycler_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by : youngkaaa on 2016/10/9.
 * Contact me : 645326280@qq.com
 */

public abstract class MultiBaseRecyclerAdapter<K, V> extends RecyclerView.Adapter {
    private static final String TAG = "MultiBaseAdapter";
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_OTHER = 2;

    public static final int TOP_BORDER = 3;
    public static final int BOTTOM_BORDER = 4;
    public static final int OTHER_BORDER = 5;


    public class ItemResult {
        private int type;
        private Object data;
        private int border;

        public ItemResult(int type, Object data, int border) {
            this.type = type;
            this.data = data;
            this.border = border;
        }

        public int getBorder() {
            return border;
        }

        public int getType() {
            return type;
        }

        public Object getData() {
            return data;
        }
    }

    private Map<K, List<V>> mData;
    private List<K> mKeys = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public MultiBaseRecyclerAdapter(Context context, Map<K, List<V>> data) {
        this.mContext = context;
        this.mData = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        for (Map.Entry<K, List<V>> entry : mData.entrySet()) {
            K key = entry.getKey();
            mKeys.add(key);
        }
    }

    public MultiBaseRecyclerAdapter(Context context, List<K> keys, Map<K, List<V>> data) {
        this.mContext = context;
        this.mData = data;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mKeys = keys;
        for (Map.Entry<K, List<V>> entry : mData.entrySet()) {
            K key = entry.getKey();
        }
        if (mData.entrySet().size() != mKeys.size()) {
            throw new RuntimeException("Key's size should be equal to the Map's key size!");
        }
    }

    public abstract int getTitleLayoutId();

    public abstract int getContentLayoutId();

    public abstract void bindData(BaseViewHolder holder, ItemResult itemResult);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new BaseViewHolder(mInflater.inflate(getTitleLayoutId(), parent, false));
        } else {
            return new BaseViewHolder(mInflater.inflate(getContentLayoutId(), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_TITLE) {
            bindData((BaseViewHolder) holder, new ItemResult(TYPE_TITLE, mKeys.get(getKeyIndexByPosition(position)), OTHER_BORDER));
//            bindData(TYPE_TITLE, (BaseViewHolder) holder, mKeys.get(getKeyIndexByPosition(position)));
        } else {
            int borderType = checkPositionBorderType(position);
            bindData((BaseViewHolder) holder, new ItemResult(TYPE_OTHER, getValueByPosition(position), borderType));
//            bindData(TYPE_OTHER, (BaseViewHolder) holder, getValueByPosition(position));
        }
    }

    private int checkPositionBorderType(int position) {
        for (int i = 0; i < mKeys.size(); i++) {
            if (position == getTitlePositionByItemIndex(i) + 1) {
                Log.d(TAG, "getTitlePositionByItemIndex+1=> #" + (getTitlePositionByItemIndex(i) + 1) + "#  TOP_BORDER on #" + position);
                return TOP_BORDER;
            }

            if (position == (getTitlePositionByItemIndex(i) - 1)) {
                Log.d(TAG, "getTitlePositionByItemIndex-1=> #" + (getTitlePositionByItemIndex(i) - 1) + "# BOTTOM_BORDER on " + position);
                return BOTTOM_BORDER;
            }

            if (getTotalItemCount() - 1 == position) {
                return BOTTOM_BORDER;
            }


        }
        return OTHER_BORDER;
    }

    @Override
    public int getItemViewType(int position) {
        return judgeTypeByPosition(position);
    }

    @Override
    public int getItemCount() {
        return getTotalItemCount();
    }

    public boolean isTitleView(int position) {
        for (int i = 0; i < mKeys.size(); i++) {
            if (position == getTitlePositionByItemIndex(i)) {
                return true;
            }
        }
        return false;
    }


    public Context getContext() {
        return mContext;
    }

    private int judgeTypeByPosition(int position) {
        for (int i = 0; i < mKeys.size(); i++) {
            if (getTitlePositionByItemIndex(i) == position) {
                return TYPE_TITLE;
            }
        }
        return TYPE_OTHER;
    }

    //传入title的index，递归得到它在所有item里面的position
    //比如你传入0，那么就是获得第一个title的position，传入1就是获得第2个title的position
    private int getTitlePositionByItemIndex(int itemIndex) {
        if (itemIndex == 0) {
            return 0;
        } else {
            return getTitlePositionByItemIndex(itemIndex - 1) + getItemListCountByKey(mKeys.get(itemIndex - 1)) + 1;
        }
    }

    private V getValueByPosition(int position) {
        int trueIndex = 0;
        for (int i = 1; i < mKeys.size(); i++) {
            if (position > getTitlePositionByItemIndex(i - 1) && position < getTitlePositionByItemIndex(i)) {
                trueIndex = position - getTitlePositionByItemIndex(i - 1) - 1;
                return getListValuesByKey(mKeys.get(i - 1)).get(trueIndex);
            }
        }
        if (position > getTitlePositionByItemIndex(mKeys.size() - 1)) {
            trueIndex = position - getTitlePositionByItemIndex(mKeys.size() - 1) - 1;
            return getListValuesByKey(mKeys.get(mKeys.size() - 1)).get(trueIndex);
        }
        return getListValuesByKey(mKeys.get(0)).get(0); //默认返回第一个key对应的list中的第一个
    }

    private int getKeyIndexByPosition(int position) {
        for (int i = 0; i < mKeys.size(); i++) {
            if (getTitlePositionByItemIndex(i) == position) {
                return i;
            }
        }
        return 0;  //找不到的话就返回第一个key来绑定
    }

    private int getItemListCountByKey(K key) {
        return getListValuesByKey(key).size();
    }

    private int getKeyCount() {
        return mKeys.size();
    }


    private List<V> getListValuesByKey(K key) {
        return this.mData.get(key);
    }

    private int getTotalItemCount() {
        int count = 0;
        for (int i = 0; i < mKeys.size(); i++) {
            count += getItemListCountByKey(mKeys.get(i));
        }
        return count + mKeys.size();
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        private SparseArray<View> views = new SparseArray<>();

        public BaseViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }

        //such as findViewById()
        public <T extends View> T getViewById(int id) {
            View v = views.get(id);
            if (null == v) {
                v = rootView.findViewById(id);
                views.put(id, v);
            }
            return (T) v;
        }

    }

    public List<K> getKeys() {
        return mKeys;
    }

    public Map<K, List<V>> getData() {
        return mData;
    }
}
