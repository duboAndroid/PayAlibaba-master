package com.example.dubo.weiXinPay;

import android.app.Activity;
import android.os.Bundle;

import com.example.dubo.StephenApplication;
import com.example.dubo.payalibaba.R;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String PaySuccess = "weChatPaySuccess",PayFailure = "weChatPayFailure";
    private IWXAPI WXApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            WXApi = WXAPIFactory.createWXAPI(WXPayEntryActivity.this, MainWeiXinPayActivity.openIdForWeiXin, false);
            WXApi.handleIntent(getIntent(), WXPayEntryActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReq(BaseReq arg0) {}

    @Override
    public void onResp(BaseResp resp) {
        if (null != resp) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK://成功
                    ((StephenApplication)getApplication()).setWeiXinFlag(PaySuccess);
                    //StephenToolUtils.showShortHintInfo(WXPayEntryActivity.this,"微信支付成功!");
                    break;
                default:
                    ((StephenApplication)getApplication()).setWeiXinFlag(PayFailure);
                    //StephenToolUtils.showShortHintInfo(WXPayEntryActivity.this,"抱歉,微信支付失败,请重试:");
                    break;
            }//end of switch
        }//end of if
        //backToPrevActivity();//将微信启动的这个界面关闭掉会导致使用微信的界面触发onResume
        finish();
    }
}