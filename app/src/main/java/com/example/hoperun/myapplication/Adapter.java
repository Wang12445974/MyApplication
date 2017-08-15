package com.example.hoperun.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Hoperun on 2017/8/10.
 */

class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,ItemTouchHelperAdapter{
    private List strings;


    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;
    //上拉加载更多状态-默认为0
    private int load_more_status=0;

    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private Context context;
    private MainActivity.recycleViewitem recycleViewitem;

    public Adapter(List ss, Context context){
        this.strings=ss;
        this.context=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM) {
            View view = View.inflate(context, R.layout.text, null);
            Mhoder mhoder = new Mhoder(view);;
            view.setOnClickListener(this);
            return mhoder;
        }else if(viewType==TYPE_FOOTER){
            View foot_view=View.inflate(context,R.layout.foot,null);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder=new FootViewHolder(foot_view);;
            return footViewHolder;
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Adapter.Mhoder) {
            ((Adapter.Mhoder)holder).textView.setText((CharSequence) strings.get(position));
            holder.itemView.setTag(strings.get(position));
        }else if(holder instanceof Adapter.FootViewHolder){
            Adapter.FootViewHolder footViewHolder=(Adapter.FootViewHolder)holder;
            switch (load_more_status){
                case PULLUP_LOAD_MORE:
                    footViewHolder.textView.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.textView.setText("正在加载更多数据...");
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return strings.size()+1;
    }

    @Override
    public void onClick(View view) {
        if (recycleViewitem!=null){
            recycleViewitem.setoNitem(view,(String)view.getTag());
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(strings,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        //移除数据
        strings.remove(position);
        notifyItemRemoved(position);
    }

    class Mhoder extends RecyclerView.ViewHolder{
        public TextView textView;
        public Mhoder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.itemtext);
        }
    }
    class FootViewHolder  extends RecyclerView.ViewHolder{
        public TextView textView;
        public FootViewHolder (View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.foottext);
        }
    }
    public void setOnitem(MainActivity.recycleViewitem onitem){
        this.recycleViewitem=onitem;
    }
    //添加数据
    public void addItem(List<String> newDatas) {
        newDatas.addAll(strings);
        strings.removeAll(strings);
        strings.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<String> newDatas) {
        strings.addAll(newDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }

}
