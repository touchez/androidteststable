package com.example.a13162.activitytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class jump extends AppCompatActivity {
    String xcxpath="pages/index/index";
    String xcxid="xcx:gh_f4803b06a633";
    String appId = "wx22f60e47bd1eb936"; // 填应用AppId
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump);
        IWXAPI api = WXAPIFactory.createWXAPI(jump.this, appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = xcxid;
        req.path = xcxpath;
        req.miniprogramType = 2;// 可选打开 开发版，体验版和正式版
        System.out.println("跳转到了");
        api.sendReq(req);
    }
}
