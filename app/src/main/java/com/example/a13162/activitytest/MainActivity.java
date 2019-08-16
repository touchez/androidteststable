package com.example.a13162.activitytest;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseNfcActivity {
    String mTagText;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private MenuItem menuItem;
    protected NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private FragmentTag tag;
    private FragamentNfc f;

    private boolean startFlag=false;     //用于判断是否是在外面扫的小程序
    public boolean isStartFlag=true;    //用于判断是否应该读取数据
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.QRcode:
                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification notification=new Builder(getApplicationContext())
                        .setContentTitle("共享单车")
                        .setContentText("点击跳转")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                        .build();
                manager.notify(1,notification);
                // 创建IntentIntegrator对象
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                //设置自定义的扫描
                intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
                // 开始扫描
                intentIntegrator.initiateScan();

                Toast.makeText(this,"You clicker Backuo",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    @Override
    public void onNewIntent(Intent intent) {
        Log.d("abcd","a new intent");
        resolveIntent(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        SharedPreferences pref=getSharedPreferences("nfc", MODE_PRIVATE);
           String string=pref.getString("pages/activeCheck/activeCheck?type=exsanguinate","");
           if(string!=null&&!Data.getnfclist().contains(string)) {
               Data.getnfclist().add(string);
           }
        String string1=pref.getString("pages/activeCheck/activeCheck?type=ct","");
        if(string1!=null&&!Data.getnfclist().contains(string1)) {
            Data.getnfclist().add(string1);
        }
        String string2=pref.getString("pages/medicineList/medicineList","");
        if(string2!=null&&!Data.getnfclist().contains(string2)) {
            Data.getnfclist().add(string2);
        }
        String string3=pref.getString("pages/activeCheck/activeCheck?type=xray","");
        if(string3!=null&&!Data.getnfclist().contains(string3)) {
            Data.getnfclist().add(string3);
        }
        String string4=pref.getString("pages/detail/detail?departmentid=2","");
        if(string4!=null&&!Data.getnfclist().contains(string4)) {
            Data.getnfclist().add(string4);
        }
        String string5=pref.getString("pages/jiuzheng/jiuzheng","");
        if(string5!=null&&!Data.getnfclist().contains(string5)) {
            Data.getnfclist().add(string5);
        }
        String string6=pref.getString("pages/index/index","");
        if(string6!=null&&!Data.getnfclist().contains(string6)) {
            Data.getnfclist().add(string6);
        }
        String string7=pref.getString("pages/daoyouji/daoyouji","");
        if(string7!=null&&!Data.getnfclist().contains(string7)) {
            Data.getnfclist().add(string7);
        }
        String string8=pref.getString("pages/daoyouji2/daoyouji2?guideMachineName=甘肃彩陶文化简介","");
        if(string8!=null&&!Data.getnfclist().contains(string8)) {
            Data.getnfclist().add(string8);
        }
        String string9=pref.getString("pages/daoyouji2/daoyouji2?guideMachineName=安特生考察路线图","");
        if(string9!=null&&!Data.getnfclist().contains(string9)) {
            Data.getnfclist().add(string9);
        }
        for(int i=0;i<Data.getnfclist().size();i++)
        {
            System.out.println(Data.getnfclist().get(i));
        }

     /*   NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        *//** 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*//*
        Intent intent = new Intent(this,jump.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//给通知添加点击意图
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentInfo("Content info")
                .setContentText("点击跳转")//设置通知内容
                .setContentTitle("共享单车")//设置通知标题
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher_round)//不能缺少的一个属性
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001","my_channel",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }
        builder.setContentIntent(pi);
        Notification n = builder.build();
        manager.notify(0,n);*/
       // builder.setContentIntent(pi);
       /* NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification=new Builder(MainActivity.this)
                .setContentTitle("共享单车")
                .setContentText("点击跳转")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();
        manager.notify(0,notification);

        NotificationChannel channel = new NotificationChannel("1",
                "my_channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN); //小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        manager.createNotificationChannel(channel);

//同时，Notification.Builder需要多设置一个*/


        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        f=new FragamentNfc();
        tag=new FragmentTag();
        List<Fragment> list = new ArrayList<>();
        list.add(TestFragment.newInstance("首页"));
        list.add(f);//nfc页面
        list.add(tag);//标签页面
        //list.add(TestFragment.newInstance(""));
        viewPagerAdapter.setList(list);
        startFlag=true;
        //直接跳转小程序
        resolveIntent(getIntent());
        startFlag=false;
        //viewPager.setCurrentItem(1);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
                /*case R.id.navigation_person:
                    viewPager.setCurrentItem(3);
                    return true;*/
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 获取解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "取消扫描", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描内容:" + result.getContents(), Toast.LENGTH_LONG).show();
                viewPager.setCurrentItem(2);
                tag.showInputDialog(result.getContents());
                //tag.showInputDialog(mTagText);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * 读取NFC标签文本数据
     */
    private void readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    String textRecord = parseTextRecord(record);
                    mTagText = textRecord ;
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     *
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData =launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
        }
    }

    private void resolveIntent(Intent intent){
        String action=intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){

            //1.获取Tag对象
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //2.获取Ndef的实例
            Ndef ndef = Ndef.get(detectedTag);
            //mTagText = ndef.getType() + "\nmaxsize:" + ndef.getMaxSize() + "bytes\n\n";
            readNfcTag(intent);
            if(mTagText!=null)
                Log.d("abcd",mTagText);
            else
            {
                mTagText="This is a null tag!!!";
                Log.d("abcd","msg is null");
            }


            String xcx=null;
            String path=null;
            int i=0;
            if(mTagText!=null&&mTagText.indexOf("xcx:")==0){
                i=mTagText.indexOf("path:");
                if(i<0){
                    xcx=mTagText.substring(4);
                }else{
                    xcx=mTagText.substring(4,i);
                    i=i+5;
                    path=mTagText.substring(i);
                }
                Log.d("xcx","xcx id is "+xcx+" path is "+path);

            }
            //在外面扫到小程序直接跳
            if(startFlag==true&&xcx!=null){
                startFlag=false;
                jumpToXCX(xcx,path);
                return;
            }

            if(Data.getIsactive()==0){
                viewPager.setCurrentItem(2);
                tag.showInputDialog(mTagText);
            }else if(Data.getIsactive()==1){
                if(xcx!=null){
                    final String str=xcx;
                    final String str1=path;
                    AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("发现一个小程序");
                    dialog.setMessage("是否跳转？");
                    dialog.setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            jumpToXCX(str,str1);
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("NFC标签内容");
                    dialog.setMessage(mTagText);
                    dialog.show();
                }


            }
        }
    }
    //跳转小程序
    private void jumpToXCX(String xcxid,String xcxpath){
        String appId = "wx22f60e47bd1eb936"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = xcxid; // 填小程序原始id
        if(xcxpath!=null){
            if(Data.getnfclist().contains(xcxpath)) {
                System.out.println("找到了");

                if(Data.getnfclist().contains("pages/index/index")&&xcxpath.equals("pages/index/index")) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setContentInfo("Content info")
                            .setContentText("点击跳转")//设置通知内容
                            .setContentTitle("共享单车")//设置通知标题
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .setSmallIcon(R.drawable.bike)//不能缺少的一个属性
                            .setSubText("Subtext")
                            .setTicker("滚动消息......")
                            .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("001", "my_channel", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.enableLights(true); //是否在桌面icon右上角展示小红点
                        channel.setLightColor(Color.GREEN); //小红点颜色
                        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                        manager.createNotificationChannel(channel);
                        builder.setChannelId("001");
                    }
                    //builder.setContentIntent(pi);
                    Notification n = builder.build();
                    manager.notify(0, n);
                }
                req.path = xcxpath;
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
                api.sendReq(req);
            }
            else
            {
                System.out.println("没找到");
            }
        }
        //                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
       /* req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);*/
    }

}

