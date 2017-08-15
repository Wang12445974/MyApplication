package com.example.hoperun.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private RecyclerView reView;
    private List<String> args;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout swr;
    private Adapter madapter;
    private int lastVisibleItem;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initView();

    }

    private void initDatas() {
        args = new ArrayList<>();
        for (int i = 0; i < 20 ; i++) {
            args.add("第"+i+"个");
        }
    }
//    public void button(View view){
//        Intent intent=new Intent(MainActivity.this,glideActivity.class);
//        startActivity(intent);
//    }

    public void initView() {
        swr = (SwipeRefreshLayout) findViewById(R.id.refs);
        reView = (RecyclerView) findViewById(R.id.recyc);
        button = (Button) findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,glideActivity.class);
                 startActivity(intent);
            }
        });
        if (args.size()==0){
            return;
        }
        //设置刷新时动画的颜色，可以设置4个
        swr.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swr.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swr.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        reView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        manager = new LinearLayoutManager(this);
        reView.setLayoutManager(manager);
        reView.setHasFixedSize(true);
        //创建适配器

        madapter = new Adapter(args,MainActivity.this);
        reView.setAdapter(madapter);
        madapter.setOnitem(new recycleViewitem() {
            @Override
            public void setoNitem(View view, String dataModlo) {
                Toast.makeText(MainActivity.this,dataModlo,Toast.LENGTH_SHORT).show();
            }
        });
        delete();
        refush();


    }

    private void delete() {
        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(madapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(reView);
    }

    private void refush() {
        swr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> newDatas = new ArrayList<String>();
                        for (int i = 0; i <5; i++) {
                            int index = i + 1;
                            newDatas.add("new item" + index);
                        }
                        madapter.addItem(newDatas);
                        swr.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "更新了五条数据...", Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });
        reView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==madapter.getItemCount()) {
                    madapter.changeMoreStatus(Adapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i< 5; i++) {
                                int index = i +1;
                                newDatas.add("more item" + index);
                            }
                            madapter.addMoreItem(newDatas);
                            madapter.changeMoreStatus(Adapter.PULLUP_LOAD_MORE);
                        }
                    }, 2500);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx, dy);
                lastVisibleItem =manager.findLastVisibleItemPosition();
            }
        });
    }


    //声明点击接口
    public interface recycleViewitem{
         void setoNitem(View view,String dataModlo);
    }
    //创建接口
    public recycleViewitem recycleViewitem=null;




}
