package com.example.jjhu.pulltorefreshdemo;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    ArrayList<Music> musics = new ArrayList<>();
    PullToRefreshListView listView;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        adapter = new MyAdapter(this,musics);
        loadData();
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadDataAsyncTask(MainActivity.this).execute();
                Toast.makeText(MainActivity.this,"上拉", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(MainActivity.this,"下拉",Toast.LENGTH_LONG).show();
                new LoadDataAsyncTask(MainActivity.this).execute();
            }
        });
        listView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private int count = 1;
    public void loadData(){
        for(int i=0;i<2;i++){
            musics.add(new Music("歌名——"+count,"歌手——"+count));
            count ++;
        }
    }

    class LoadDataAsyncTask extends AsyncTask<Void,Void,String> {
        private MainActivity mainActivity;
        public LoadDataAsyncTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(200);
//                musics.add(new Music("歌名——"+3,"歌手——"+3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainActivity.loadData();
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if("success".equals(s)){
                mainActivity.adapter.notifyDataSetChanged();
                mainActivity.listView.onRefreshComplete();
            }
        }
    }

    static class MyAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<Music> musics;
        public MyAdapter(Context context,ArrayList<Music> musics){
            this.context = context;
            this.musics = musics;
        }
        @Override
        public int getCount() {
            return musics.size();
        }

        @Override
        public Object getItem(int position) {
            return musics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView= LayoutInflater.from(context).inflate(R.layout.listview_layout,null);
                vh = new ViewHolder();
                vh.textViewsinger = (TextView)convertView.findViewById(R.id.textView_singer);
                vh.textViewtitle = (TextView) convertView.findViewById(R.id.textView_title);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            Music m = musics.get(position);
            vh.textViewtitle.setText(m.getTitle());
            vh.textViewsinger.setText(m.getSinger());
            return convertView;
        }

        static class ViewHolder{
            TextView textViewtitle;
            TextView textViewsinger;
        }
    }
}
