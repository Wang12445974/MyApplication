package com.example.hoperun.myapplication;

/**
 * Created by Hoperun on 2017/8/10.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition,int toPosition);
    //数据删除
    void onItemDissmiss(int position);
}
