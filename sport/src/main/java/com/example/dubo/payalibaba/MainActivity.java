package com.example.dubo.payalibaba;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import network.VickyConfig;

public class MainActivity extends AppCompatActivity {

    // 商户PID
    //public static final String PARTNER = "2018031902403741";
    public static final String PARTNER = "2088031540773893";
    // 商户收款账号
    public static final String SELLER = "897450146@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/G3IpD735C5VZ1Kw827qKzEdOxNmvO3U197/c"+
            "gyS2fC/lK6vXmQImlMRQUZD3PGOBzC1D8Wwn6U9BvNrtdBxWhy/zUdKMHNTOv2umylH4VnZUnRyE5bIJ9"+
            "0RgV5OfnXth1Xj98JZl2cRDvInl1t0sLhv+sNjHaqZ2Lct9SBIJTDRtL2d7p/Hzh8u3eqDz1bOmcbpHb"+
            "9qGX8oySzOkY9Nk4VhNOU/74W8lTKM+HYv2DuHhzdQkwTGZYKw3VUxJO1wcR+sFxGD0ayo+JU5my9AME"+
            "nuSj1P+0gYggTLba8EuqKwOLMYnpzQnwqqk6yIQxwdolt0ASHFBKM7G0JgncrljAgMBAAECggEBALoBE"+
            "aULkGCUuKG9UILPbonnd8Qf7qwSl+uWHqSW9CW1S7ynO9NVNnWsuEJgkVNB8KwFuUz27skN2TlngkI5j"+
            "9dcLvyJd/8tlKdCQpoNM6kdJoEGhhqHojj29RM8Sg/B7LP1XZT0FL4/LTqsiB8veQemPVMwd13o3OoLe"+
            "pKOkhd+5jgciRKVXM5l4SPB2ufKJHyTSWObDJ//kJBeNAz/Oi2DZI6SZId3i+9dI4sew130ATk7RsRy7"+
            "kjZxfKaEa9wuu9z5oSk8+tiLmTtGwpamfWi3F8cp7u5SA4ir7FfHOYhoBbm2N+lqx4XrRVu3ribXxsTk"+
            "aeEAbrtsNgrvmbEXfkCgYEA9mZB+L+bofPpma1pEoN6AIpJJT8hXXj3T1zMaYkBADYl0kOiNYlc8LKg"+
            "X9ByQfTjSfQ15wvLeGCPnko4Q8VwB2YYPfuqNrtReXbfpbI++sxQL2YvRkHrcsUlgXignQJ2g0M/JumT"+
            "THWieDgap0gGjaxiHP86wpkzZbUtqT0Qom0CgYEAxo2rPEpKsMcOEdRft8ckkSqJCAL0fO+QZPgvA6lm"+
            "zSzzymKUH/+VLgtx7wKp6yduoLuCPvqm9mPwncJpRhsTej5sn3XIXtY5DkoIhg9k1rFix5WMlBZnCkA6m"+
            "n1OeSqh9FwuDKL3pNmsmXvN8ETv0XgpMEkUGT9BqT4250e06Q8CgYEAgb9LEf9jpp5X7hKAd70/6zTmW"+
            "3Sozx25rNLC2PfJzSC2PchbjDFu/VgyVwHeiJi5buHn2Gg/ln3SYpnWkulOmrbKISl6ciOlPX65OflobIx"+
            "V1P/L+QURRHhHzauFd+gqG+i3QnhQljyDdyaj8l5dBRE3AOntOTeO9wm2p51+OukCgYAS7d6tWMfWIqQMvx"+
            "qBAtNRo6R8xwtX/d7eQWr6BmRumyKC4PMOOeeXBDBQgQiCCrhZSocYSf0w8EY4RBiVPm+U7+6bJTd9Ex9NZX"+
            "F6lm0eqrqoHHHSjHA2Yfk0UU3x70aRbVhWjBLf/ICgAVWY2nmilroWiGEKJyWzZOMHGab9sQKBgFUuNakxUk"+
            "GHCGAGM8n4rz3dXBLKGoE+60mDYoSFkAP3kpCObv0y9+t9cbMoLBInoPUrg3BAGbI+eV3xdS9pT/HbdZvyxf+"+
            "1StAQ5V/7OuEH5nVNO9fy4+Rqtwk4/u2oUXeJoMDRZmKmI2ferp+DSZgnRYyxV2QrOQPOtfqEihUw" ;
    // 支付宝公钥
    public static final String RSA_PUBLIC ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvxtyKQ+9+QuVWdSsPNu6isxHTsTZrzt1Nfe/3" +
            "IMktnwv5Sur15kCJpTEUFGQ9zxjgcwtQ/FsJ+lPQbza7XQcVocv81HSjBzUzr9rpspR+FZ2VJ0c" +
            "hOWyCfdEYFeTn517YdV4/fCWZdnEQ7yJ5dbdLC4b/rDYx2qmdi3LfUgSCUw0bS9ne6fx84fLt" +
            "3qg89WzpnG6R2/ahl/KMkszpGPTZOFYTTlP++FvJUyjPh2L9g7h4c3UJMExmWCsN1VMSTtcH" +
            "EfrBcRg9GsqPiVOZsvQDBJ7ko9T/tIGIIEy22vBLqisDizGJ6c0J8KqpOsiEMcHaJbdA" +
            "EhxQSjOxtCYJ3K5YwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    public MainHandler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mainHandler = new MainHandler(this.getMainLooper());
        //------------- 自己的项目
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "\"appid\",\"商户名\",\"商户私钥\" + \n 不能不空", Toast.LENGTH_SHORT).show();
                (new StephenThirdPayTool(MainActivity.this)).startAliPayPayment(PARTNER, SELLER, RSA_PRIVATE, "测试支付", "apple", 0.01);
            }
        });

        //-----------  学习签名在客户端
        //假设已经从服务端获取必要数据
        MyALipayUtils.ALiPayBuilder builder1 = new MyALipayUtils.ALiPayBuilder();
        builder1.setAppid("123")
                .setRsa("456")//根据情况设置Rsa2与Rsa
                .setMoney("0.01")//单位时分
                .setTitle("支付测试")
                .setOrderTradeId("487456")//从服务端获取
                .setNotifyUrl("fdsfasdf")//从服务端获取
                .build()
                .toALiPay(this);

        //-----------  学习签名在服务端
        String orderInfo = "";
        MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
        //builder.build().toALiPay(AlipayActivity.this, orderInfo);
    }

    //handler
    public class MainHandler extends Handler {
        public MainHandler(Looper looper) {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VickyConfig.msg_showLoading:
                    String hintStr = "努力加载数据中...";
                    if (null != msg.obj && msg.obj instanceof String) hintStr = msg.obj.toString();
                    showLoadingDialog(hintStr);
                    break;
                default:
                    disposeMainHandlerCallMethod(msg);
                    break;
            }//end of switch_
        }
    }

    //显示loading对话框
    public void showLoadingDialog(String hintStr) {
        if (null != mainHandler) {
            Message msg = Message.obtain();
            msg.what = VickyConfig.msg_showLoading;
            msg.obj = hintStr;
            mainHandler.sendMessage(msg);
        }//end of if
    }

    protected void disposeMainHandlerCallMethod(Message msg) {//处理handler消息
        switch (msg.what) {
            case VickyConfig.msg_alipay:
                (new StephenThirdPayTool(MainActivity.this)).alipayPaymentOperationResult((String) msg.obj, new StephenThirdPayTool.PaymentResultListener() {
                    @Override
                    public void paymentResult(boolean isSuccess) {
                        if (isSuccess) {
                            //paymentUserOrder();
                            Toast.makeText(MainActivity.this,"支付成功", Toast.LENGTH_SHORT).show();
                        } else {
                            //cancelUserOrder();
                            Toast.makeText(MainActivity.this,"支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }//end of switch
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mainHandler) mainHandler.removeCallbacksAndMessages(null);
    }
}
