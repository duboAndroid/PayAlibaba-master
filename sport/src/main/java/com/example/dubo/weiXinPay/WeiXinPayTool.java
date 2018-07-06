package com.example.dubo.weiXinPay;

import android.text.TextUtils;
import android.util.Xml;

import com.example.dubo.weiXinPay.MainWeiXinPayActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import network.StephenRequestAsyncTask;
import network.StephenRequestMyCallback;
import network.VickyConfig;
import singnUtils.MD5Util;

public class WeiXinPayTool {

    private MainWeiXinPayActivity baseActivity;
    private String outTradeNoStr;

    public WeiXinPayTool(MainWeiXinPayActivity baseActivity) {
        this.baseActivity = baseActivity;
        //生成默认订单号
        String key = getSystemCurDateTime("MMddHHmmss");
        key += (new Random()).nextInt();
        this.outTradeNoStr = key.substring(0, 15);
    }

    public WeiXinPayTool(MainWeiXinPayActivity baseActivity, String outTradeNoStr) {
        this.baseActivity = baseActivity;
        this.outTradeNoStr = outTradeNoStr;
    }

    /*public static final String APP_ID = "wxf2f565574a968187";//appid,请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String MCH_ID = "1233848001";//商户号
    public static final  String API_KEY="412fde4e9c2e2bb619514ecea142e449";//API密钥，在商户平台设置
    productDesc:APP名字-实际商品名称*/
    public void startWeiXinPayment(final String openIdForWeiXin, final String openMCHForWeiXin, final String openKeyForWeiXin, String productDesc, double sumPrice) {
        final IWXAPI WXApi = WXAPIFactory.createWXAPI(baseActivity, openIdForWeiXin, true);
        WXApi.registerApp(openIdForWeiXin);
        if (WXApi.isWXAppInstalled() && WXApi.isWXAppSupportAPI()) {
            String productArgsStr = genProductArgs(openIdForWeiXin, openMCHForWeiXin, openKeyForWeiXin, productDesc, sumPrice + "");
            if (!TextUtils.isEmpty(productArgsStr)) {
                (new StephenRequestAsyncTask(baseActivity, /*baseActivity.mainHandler, */"https://api.mch.weixin.qq.com/pay/unifiedorder", VickyConfig.RequestType_Post, true, new StephenRequestMyCallback() {//生成prepay_id
                    @Override
                    public void onCompleted(String returnMsg) {
                        if (!TextUtils.isEmpty(returnMsg)) {
                            Map<String, String> returnXMLMap = decodeXml(returnMsg);
                            if (null != returnXMLMap) {
                                PayReq wxReq = new PayReq();
                                wxReq.appId = openIdForWeiXin;
                                wxReq.partnerId = openMCHForWeiXin;
                                wxReq.prepayId = returnXMLMap.get("prepay_id");
                                wxReq.packageValue = "Sign=WXPay";
                                wxReq.nonceStr = MD5Util.getMessageDigest(String.valueOf((new Random()).nextInt(10000)).getBytes());
                                wxReq.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

                                List<NameValuePair> signParams = new LinkedList<>();
                                signParams.add(new BasicNameValuePair("appid", wxReq.appId));
                                signParams.add(new BasicNameValuePair("noncestr", wxReq.nonceStr));
                                signParams.add(new BasicNameValuePair("package", wxReq.packageValue));
                                signParams.add(new BasicNameValuePair("partnerid", wxReq.partnerId));
                                signParams.add(new BasicNameValuePair("prepayid", wxReq.prepayId));
                                signParams.add(new BasicNameValuePair("timestamp", wxReq.timeStamp));
                                wxReq.sign = genAppSign(signParams, openKeyForWeiXin);

                                if (WXApi.sendReq(wxReq)) {
                                    //StephenToolUtils.showLongHintInfo(baseActivity, "发送微信支付请求成功,请等待微信响应...");
                                } else {
                                    //StephenToolUtils.showLongHintInfo(baseActivity, "抱歉,微信通信失败,请重试!");
                                }
                            } else {
                                //StephenToolUtils.showLongHintInfo(baseActivity,"抱歉,解析微信预付订单信息失败!");
                            }
                        } else {
                            //StephenToolUtils.showLongHintInfo(baseActivity,"抱歉,生成微信预付订单信息失败!");
                        }
                    }
                })).execute(productArgsStr);
            } else {
                //StephenToolUtils.showLongHintInfo(baseActivity, "抱歉,生成微信商品请求信息失败,请重试!");
            }
        } else {
            //StephenToolUtils.showLongHintInfo(baseActivity, "抱歉,您未安装微信,不能使用微信支付!");
        }
    }

    //解析XML
    private Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            //StephenToolUtils.showHintInfoDialog(baseActivity, "解析微信预付订单信息异常!"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //生成微信签名
    private String genAppSign(List<NameValuePair> params, String API_KEY) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);
        System.out.println("sign str\n" + sb.toString() + "\n\n");
        return MD5Util.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    }

    //生成微信预付单信息
    private String genProductArgs(String APP_ID, String MCH_ID, String API_KEY, String productDesc, String sumPrice) {
        try {
            List<NameValuePair> packageParams = new LinkedList<>();
            packageParams.add(new BasicNameValuePair("appid", APP_ID));
            packageParams.add(new BasicNameValuePair("body", productDesc));
            packageParams.add(new BasicNameValuePair("mch_id", MCH_ID));//商户Id
            packageParams.add(new BasicNameValuePair("nonce_str", MD5Util.getMessageDigest(String.valueOf((new Random()).nextInt(10000)).getBytes())));
            packageParams.add(new BasicNameValuePair("notify_url", "http://www.weixin.qq.com/wxpay/pay.php"));//通知地址
            packageParams.add(new BasicNameValuePair("out_trade_no", outTradeNoStr));//商户订单号
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));//用户端实际ip
            packageParams.add(new BasicNameValuePair("total_fee", sumPrice));//总金额,支付金额单位为分
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));//支付类型
            packageParams.add(new BasicNameValuePair("sign", genAppSign(packageParams, API_KEY)));//设置微信签名
            //生成XML
            StringBuilder xmlSb = new StringBuilder();
            xmlSb.append("<xml>");
            for (int i = 0; i < packageParams.size(); i++) {
                xmlSb.append("<" + packageParams.get(i).getName() + ">");
                xmlSb.append(packageParams.get(i).getValue());
                xmlSb.append("</" + packageParams.get(i).getName() + ">");
            }//end of for
            xmlSb.append("</xml>");
            return xmlSb.toString();
        } catch (Exception e) {
            //StephenToolUtils.showHintInfoDialog(baseActivity, "生成微信商品请求信息异常!"+e.getMessage());
            return null;
        }
    }

    //得到当前日期
    public static String getSystemCurDateTime(String dateFormat) {//24小时制:yyyy-MM-dd HH:mm:ss,12小时制:yyyy-MM-dd hh:mm:ss
        return (new SimpleDateFormat(dateFormat, Locale.getDefault())).format(new Date());
    }
}
