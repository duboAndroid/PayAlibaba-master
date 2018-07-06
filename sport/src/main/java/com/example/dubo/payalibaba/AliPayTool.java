package com.example.dubo.payalibaba;

import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import singnUtils.AliPayRSAUtil;
import singnUtils.AliPayResult;

//自已项目的
public class AliPayTool {
    private MainAliPayActivity baseActivity;
    private String outTradeNoStr;
    public static final int msg_alipay = -6;

    public AliPayTool(MainAliPayActivity baseActivity) {
        this.baseActivity = baseActivity;
        //生成默认订单号
        String key = getSystemCurDateTime("MMddHHmmss");
        key += (new Random()).nextInt();
        this.outTradeNoStr = key.substring(0, 15);
    }

    public AliPayTool(MainAliPayActivity baseActivity, String outTradeNoStr) {
        this.baseActivity = baseActivity;
        this.outTradeNoStr = outTradeNoStr;
    }

    public void startAliPayPayment(String alipayPARTNER, String SELLER, String RSA_PRIVATE, String productName, String productDesc, double sumPrice) {
        try {
            String orderInfo = getOrderInfo(alipayPARTNER, SELLER, productName, productDesc, "0.01");//String.valueOf(sumPrice));//这里测试,用的一分钱,正式请改回
            final String payInfo = orderInfo + "&sign=\"" + URLEncoder.encode(AliPayRSAUtil.alipaySign(orderInfo, RSA_PRIVATE), "UTF-8") + "\"&" + AliPayRSAUtil.getSignType();//完整的符合支付宝参数规范的订单信息
            Thread payThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = msg_alipay;//支付宝接收handler
                    msg.obj = (new PayTask(baseActivity)).pay(payInfo);//传的是支付信息,调用支付接口,获取支付结果
                    baseActivity.mainHandler.sendMessage(msg);
                }
            });//必须异步调用
            payThread.start();
        } catch (Exception e) {
            //StephenToolUtils.showHintInfoDialog(baseActivity, "调用支付宝支付异常!"+e.getMessage());
            e.printStackTrace();
        }
    }

    //创建支付宝订单信息
    private String getOrderInfo(String PARTNER, String SELLER, String subject, String body, String price) {
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";//签约合作者身份PID
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";//签约卖家支付宝账号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNoStr + "\"";//商户网站唯一订单号
        orderInfo += "&subject=" + "\"" + subject + "\"";//商品名称
        orderInfo += "&body=" + "\"" + body + "\"";//商品详情
        orderInfo += "&total_fee=" + "\"" + price + "\"";//商品金额
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";//服务器异步通知页面路径
        orderInfo += "&service=\"mobile.securitypay.pay\"";//服务接口名称， 固定值
        orderInfo += "&payment_type=\"1\"";//支付类型， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";//参数编码， 固定值
        orderInfo += "&it_b_pay=\"30m\"";//设置未付款交易的超时时间,默认30分钟，一旦超时，该笔交易就会自动被关闭。,取值范围：1m～15d。,m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。,该参数数值不接受小数点，如1.5h，可转换为90m。
        //orderInfo += "&extern_token=" + "\"" + extern_token + "\"";//extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        orderInfo += "&return_url=\"m.alipay.com\"";//支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        //orderInfo += "&paymethod=\"expressGateway\"";//调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        return orderInfo;
    }

    //支付宝结果
    public void alipayPaymentOperationResult(String payRetStr, PaymentResultListener paymentResultListener) {
        AliPayResult payResult = new AliPayResult(payRetStr);
        String resultStatus = payResult.getResultStatus();
        if (TextUtils.equals(resultStatus, "9000")) {//判断resultStatus为"9000"则代表支付成功
            //StephenToolUtils.showLongHintInfo(baseActivity, "恭喜,支付宝支付成功!");
            if (null != paymentResultListener) paymentResultListener.paymentResult(true);
        } else {//判断resultStatus为非'9000"则代表可能支付失败
            if (TextUtils.equals(resultStatus, "8000")) {//"8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认,最终交易是否成功以服务端异步通知为准(小概率状态)
                //.showLongHintInfo(baseActivity,"请等待,支付宝支付结果确认中!");
            } else {//其他值就可以判断为支付失败,包括用户主动取消支付,或者系统返回的错误
                //.showLongHintInfo(baseActivity,"抱歉,支付宝支付失败,请重试!");
            }
            if (null != paymentResultListener) paymentResultListener.paymentResult(false);
        }//end of else
    }

    public interface PaymentResultListener {//支付成功回调

        void paymentResult(boolean isSuccess);
    }

    //得到当前日期
    public static String getSystemCurDateTime(String dateFormat) {//24小时制:yyyy-MM-dd HH:mm:ss,12小时制:yyyy-MM-dd hh:mm:ss
        return (new SimpleDateFormat(dateFormat, Locale.getDefault())).format(new Date());
    }
}
