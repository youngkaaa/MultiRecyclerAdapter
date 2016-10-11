# 本库包括两个Adapter适配

1. MultiRecyclerAdapter
MultiRecyclerAdapter for RecyclerView
2. HeaderFooterAdapter
Add Footer and Header for your adapter!

下面先介绍第一个`MultiRecyclerAdapter`

模仿b站列表的Adapter，适用于RecyclerView.

先看一下原样式效果：

像这样:

![](https://github.com/youngkaaa/MultiRecyclerAdapter/blob/master/app/pics/bilibili1.png)

还有这样：

![](https://github.com/youngkaaa/MultiRecyclerAdapter/blob/master/app/pics/bilibili2.png)


## 用法
继承项目library中的抽象类`MultiBaseRecyclerAdapter<K, V>`.就像下面这样：

```
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
```

这里我传入的`K`、`V`分别为：`TitleBean`和`ContentBean`。这两个分别是自定义的类。
注意：前者`K`是用于Adapter中的每个`Title`布局中的，即将用于`Title`布局的赋值，下面是我的`TitleBean`的代码：

```
/**
 * Created by : youngkaaa on 2016/10/9.
 * Contact me : 645326280@qq.com
 */

public class TitleBean {
    private String leftTitle;
    private String rightTitle;

    public TitleBean() {
    }

    public TitleBean(String left, String right) {
        this.leftTitle = left;
        this.rightTitle = right;
    }

    public void setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public void setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TitleBean person = (TitleBean) obj;

        if (leftTitle != null && person.leftTitle != null && rightTitle != null && person.rightTitle != null
                && leftTitle.equals(person.leftTitle) && person.rightTitle.equals(rightTitle)) {
            return true;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 7 * leftTitle.hashCode() + 13 * rightTitle.hashCode();
    }
```

注意，要重写`equals()`和`hashCode()`方法哦，因为我们这里用到了`Map<>`，而我们知道：当`Map<>`的键是一个自定义类（在我的这个demo中是TitleBean）时，是需要重写上面这两个方法的，因为`Map<>`中是通过上面那两个方法来确定键是不是一样的。所以你需要重写他们俩。而至于如何重写就不是这里的重点了，去找java有关书籍看看吧。

然后就是`ContentBean`类，这个类是用来给非`title`布局的布局来赋值的。这个类不需要重写上面那两个方法，因为一个是值，一个是键，只有键是自定义类时才需要重写那两个方法！

然后继承实现`MultiBaseRecyclerAdapter`中的方法，下面逐个解释：

*  MultiBaseRecyclerAdapter(Context context, Map<TitleBean, List<ContentBean>> data)

构造方法之一，第一个参数不用讲了吧，第二个参数就是一个`Map<>`参数，其中该`Map<>`的键用来给`Title`布局赋值，然后每个键对应的值用来给指定的非`Title`布局赋值。这样的话可以简便的实现最终的效果，但是由于`Map<>`的一个特点：遍历取出来的值每次顺序都是不一样的，即不能指定顺序，所以我还提供了另外一个重载构造方法，你可以按需继承，即当你不关心最终的列表顺序，只需要显示出来内容就行，那么你就继承实现该构造方法，如果你需要指定顺序，那么你就继承下一个构造方法吧！

*  MultiBaseRecyclerAdapter(Context context, List<TitleBean> keys, Map<TitleBean, List<ContentBean>> data)

构造方法之二，第一个参数不讲了。第二个参数就是你单独传入的键，用来给`Title`布局赋值，然后后面的`Map<>`中的对应值来给指定的非`Title`布局赋值。与上面的构造方法唯一不同的就是本构造方法可以指定列表顺序，即按你传入的第二个参数的顺序来放置布局。两种常用的供不同需求使用！

*  getTitleLayoutId()

重写本方法来返回`Title`布局的布局资源id。所以说你可以给`Title`部分添加任何你想要的样式，而不只是上面b站的样式！

*  getContentLayoutId()

重写本方法来返回非`Title`布局的布局资源id。所以说你可以给非`Title`部分添加任何你想要的样式，而不只是上面b站的样式！

*  bindData(int type, BaseViewHolder holder, Object data)

该方法就可以让你来绑定布局数据。第一个参数`type`有两个可选的：`TYPE_TITLE`和`TYPE_OTHER`。你可以通过判断来决定具体现在是要给那个布局绑定数据，第二个参数`holder`就是指定的`BaseViewHolder`，你可以用它来获得指定`view`,通过其`getViewById()`方法。第三个参数是该布局应该绑定的数据，当`type`为`TYPE_TITLE`时，该`data`其实就是一个`TitleBean`类型的数据，即你上面重写时设置的`K`的类型，你可以放心的使用类型转换，当`type`为`TYPE_OTHER`时，该`data`其实就是一个`ContentBean`类型的数据，即你上面重写时设置的`V`的类型，你可以放心的使用类型转换来绑定。这里没有牵扯到`position`，这些我在`MultiBaseRecyclerAdapter`中都做好了，哪个位置应该绑定`Map<>`中的哪个位置的数据我已经处理好直接返回给你了，你就光绑定就行了！示例代码如下：

```
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
```

注意：

1. 如果你使用第二个构造方法，那么要注意传入的键的数量和`Map<>`中对应的值的数量要相同，否则会抛出一个`RuntimeException`，内容为：Key's size should be equal to the Map's key size!

2. 你如果要设置点击事件的话，你可以仿照我在项目中的实现，我内部现成的提供了一个点击事件的接口，你可以按照我的样子来设置点击事件，这样很轻松。当然你也可以自己定义使用自己定义的接口。

3. 上面使用的是`GridLayoutManager`，当然你也可以使用`LinearLayout`，其中`LinearLayout`布局比较简单，这里就不再解释了。需要注意的是`GridLayoutManager`，在使用`GridLayoutManager`时，你需要做下面这一步：

```
gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
    @Override
    public int getSpanSize(int position) {
        return mMutiAdapter.isTitleView(position)?gridLayoutManager.getSpanCount():1;
    }
});
```

即给`RecyclerView`中每个`Item`设置span，显然`Title`布局要占据一整行的，而非`Title`就按应有的一个占用一个格子，所以上面的`mMutiAdapter.isTitleView(position)?gridLayoutManager.getSpanCount():1`就不难懂了吧。而至于上面用到的`isTitleView()`方法你光使用就行了，我帮你做好了判断的，不要乱继承实现该方法哦！

写的差不多了，够详细了，没写到位的请查看我提供的demo吧，里面的代码都比较简单，所以注释就没写多少，个别重要的地方有注释。

下面贴一下运行图：

![](https://github.com/youngkaaa/MultiRecyclerAdapter/blob/master/app/pics/screens.gif)


![](https://github.com/youngkaaa/MultiRecyclerAdapter/blob/master/app/pics/screens_linear.gif)

更多样式请自行实现！



下面介绍第二个`HeaderFooterAdapter`

下面先贴出图：

![]()

然后，用法很简单，就像下面这样(只贴出部分有关代码)：

```
mMutiAdapter = new MyMultiAdapter(this, initListKeys(), initMapData());
        headerFooterAdapter=new HeaderFooterAdapter(this,mMutiAdapter);
        headerFooterAdapter.setFooterResId(R.layout.footer_layout);
        headerFooterAdapter.setHeaderResId(R.layout.header_layout);
        mRecyclerView.setAdapter(headerFooterAdapter);
```

就是先调用构造方法，将你已经实现了的 `Adapter`传入，这样做的优点就是几乎不用修改你原来的逻辑，只需要给你原先的`RecyclerAdapter`外面再包裹一个`Adapter`就行了，然后设置该`Adapter`给你的`RecyclerView`，这样原来的逻辑都在，就多了上面两三行代码就让你的`RecyclerView`有了`Header`和`Footer`。更多用法请查看项目内的Demo.



欢迎大神提意见，你有任何好的pr都欢迎！

如果该项目对你有用的话，给个`star`以示鼓励吧！你也可以顺便围观一下我的其他项目谢谢！
