package com.example.dubo.weiXinPay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.dubo.StephenApplication;
import com.example.dubo.payalibaba.R;

public class MainWeiXinPayActivity extends AppCompatActivity {

    public static final String alipayPARTNER = "2088421963297225", openIdForWeiXin = "wx86383cba0aa85e01",SELLERWeiXin = "277627117@163.com",
            RSA_PRIVATE_WeiXin = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALW5OybGf+32Jo34ucgKbiLvnKn9zYzV7WDZwIC7gq/scBVMrjh0DPoHGSM/fH/x3NYTop/cLJHnD872Uteo2JCH4OTR0ao/JxAftkyqMqotg+1G5nUlhh+wP71mqPYsaj5yEUlV98Jt/NTSdbD3KDt+PtYJ6OZ/pVMTtQ4U9JC5AgMBAAECgYEAp66RF1mIyFIfB1fgTebZ8+C35d/tIKxxyrGDQQZFNAHu9qh4Bl2Hb9rG5lB5mDHcPamGoQ3wDn1wh9+hV6BUm5DLS3280mHDGzgrE0UQawJdjlGNjT5fKy1lvWLeowhfWuZDDWtV/gqg+KG2dUTLQJRC8k0sr7KyS2rGcC5XUsUCQQDuX8XvjZf1EBVsqF2WaSwLA4UQfE2AxOIESzIZlu2rLmpXdgvCGVL72uPkpRzjSpjiu+6mI/QXgoMkV4IRh+NbAkEAwykb94ucAA9hb6NSHVfLahr5de1ldEgpgZFP+jH099crCUec+BG4WEI13pdpf4DnPnHCyK7BqtCL0RkVAPM8ewI/GwC+a2Se0iQASU7JS0CevKCpcjSikvO6psHd6wgt5DZd1YqJynMT83PYXQ5XN2g76XNglXF9NSyTroo7McEzAkAo5eaMe99XS+HZaVp997YTKE/63KMUOUMpefBOuEVj4t2L9kv42I3PQSDU4toi9DqBpYlyUN0ZFNcYHK+AIXkdAkBMVTGsE8EKis237pwaYw+q58BnUxnY5IKPyiKuxtPDHvFRBRX0XiPygvjwaH1eFvSQsKdUsa4dXQJPwUadObEy";//商户私钥，pkcs8格式 这里是密钥

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weixin);
        //------------- 自己的项目
        findViewById(R.id.pay1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainAliPayActivity.this, "\"appid\",\"商户名\",\"商户私钥\" + \n 不能不空", Toast.LENGTH_SHORT).show();
                (new WeiXinPayTool(MainWeiXinPayActivity.this)).startWeiXinPayment(openIdForWeiXin, SELLERWeiXin, RSA_PRIVATE_WeiXin, "", 0.1);
            }
        });

        /*//在服务端签名
        findViewById(R.id.pay1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //假装请求了服务器 获取到了所有的数据
                WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                builder.setAppId("123")
                        .setPartnerId("56465")
                        .setPrepayId("41515")
                        .setPackageValue("5153")
                        .setNonceStr("5645")
                        .setTimeStamp("56512")
                        .setSign("54615")
                        .build().toWXPayNotSign(MainWeiXinPayActivity.this, "123");
            }
        });
        //在客户端签名
        findViewById(R.id.pay2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //假装请求了服务端信息，并获取了appid、partnerId、prepayId
                WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                builder.setAppId("123")
                        .setPartnerId("213")
                        .setPrepayId("3213")
                        .setPackageValue("Sign=WXPay")
                        .build()
                        .toWXPayAndSign(MainWeiXinPayActivity.this, "123", "key");
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        String weiXinFlag = ((StephenApplication) getApplication()).getWeiXinFlag();
        if (!TextUtils.isEmpty(weiXinFlag)) {//微信支付回调
            ((StephenApplication) getApplication()).setWeiXinFlag(null);
            if(WXPayEntryActivity.PaySuccess.equals(weiXinFlag)){
                Toast.makeText(MainWeiXinPayActivity.this,"success pay",Toast.LENGTH_SHORT).show();
                //paymentUserOrder();
            }else{
                Toast.makeText(MainWeiXinPayActivity.this,"fail pay",Toast.LENGTH_SHORT).show();
                //cancelUserOrder();
            }
        } else {
            //mainHandler.sendEmptyMessage(VickyConfig.msg_closeLoading);//将loading关闭
        }
    }
}
