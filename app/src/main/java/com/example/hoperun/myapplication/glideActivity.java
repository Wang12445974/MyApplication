package com.example.hoperun.myapplication;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Hoperun on 2017/8/15.
 */

public class glideActivity extends Activity {

    private RecyclerView glide_view;
    private GankAdapter mAdapter;
    private List<String> mUrls=new ArrayList<>();
    private int page=1;
    private StaggeredGridLayoutManager stamanger;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    setAdapter();
                    break;
            }
        }
    };

    private void setAdapter() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glide);
        glide_view = (RecyclerView) findViewById(R.id.glide_rec);
        stamanger = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        glide_view.setLayoutManager(stamanger);



        mAdapter = new GankAdapter(glideActivity.this,mUrls);
        glide_view.setAdapter(mAdapter);
        //获取数据
        getImages(page);
        glide_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isScrollToEnd(recyclerView)){
                    Log.e("tag","============scroll to end");
                    page += 1;
                    getImages(page);
                }
            }
        });
    }
    public boolean isScrollToEnd(RecyclerView view){
        if (view == null) return false;
        if (view.computeVerticalScrollExtent() + view.computeVerticalScrollOffset() >= view.computeVerticalScrollRange())
            return true;
        return false;
    }

    public List<String> getImages(int item) {
        Request request = new Request.Builder().url("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/"+item).build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag","loading failure ");
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        JSONObject object=new JSONObject(response.body().string());
                        JSONArray array=new JSONArray(object.getString("results"));
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject ob = array.getJSONObject(i);
                            mUrls.add(ob.getString("url"));
                        }
                        mHandler.sendEmptyMessage(2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        return mUrls;
    }
}
