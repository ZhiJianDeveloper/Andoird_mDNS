package com.example.mdnsdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mxchip.jmdns.JmdnsAPI;
import com.mxchip.jmdns.JmdnsListener;

import org.json.JSONArray;

//        1、APP端启用EasyLink发送配网的数据包(SSID, PASSWORD)
//
//        2、设备收到数据包后会自动连接上WIFI路由器，并开启mDNS服务
//
//        3、APP在打开EasyLink的同时，打开mDNS，来搜索设备，根据设备Mac，判断有没有配网成功
public class MainActivity extends AppCompatActivity {
    private JmdnsAPI mdnsApi;
    private TextView textView;
    private Button button;
    private final int UPDATE_UI = 1;
    String s="搜索到的内容：";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    textView.setText(s);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.tv_content);
        button=(Button)findViewById(R.id.button);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingMdns();
            }
        });
    }
    //开启Mdns搜索设备 搜索会每三秒搜索一次
    public void loadingMdns() {
        mdnsApi = new JmdnsAPI(MainActivity.this);
        mdnsApi.startMdnsService("_zhijian._tcp.local.", new JmdnsListener() {
            @Override
            public void onJmdnsFind(JSONArray deviceJson) {
                 s=s+ deviceJson.toString()+"\n";
                Log.e("------OK------", s);
                //textview显示不直观，最好看log
                Message message = new Message();
                message.what = UPDATE_UI;
                mHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdnsApi.stopMdnsService();
    }
}
