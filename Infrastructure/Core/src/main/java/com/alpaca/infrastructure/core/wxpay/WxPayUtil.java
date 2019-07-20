package com.alpaca.infrastructure.core.wxpay;

import com.alpaca.infrastructure.core.config.WxmpConfig;
import com.alpaca.infrastructure.core.utils.HttpHelper;
import com.alpaca.infrastructure.core.utils.RandomStringGenerator;
import com.alpaca.infrastructure.core.wxpay.domain.PayRequestInfo;
import com.alpaca.infrastructure.core.wxpay.domain.PayResponseInfo;
import com.alpaca.infrastructure.core.wxpay.domain.SignInfo;
import com.alpaca.infrastructure.core.wxpay.domain.SignInfoResponse;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lichenw
 * Created on 2019/6/1
 */
@Component
public class WxPayUtil {
    @Autowired
    private WxmpConfig wxmpConfig;

    @Autowired
    private Signature signature;

    /**
     * @param openId
     * @param total_fee
     * @param poNo
     * @param poName
     * @return
     * @throws Exception
     */
    public SignInfoResponse pay(String openId, int total_fee, String poNo, String poName) throws Exception {

        PayRequestInfo requestInfo = new PayRequestInfo();
        requestInfo.setAppid(wxmpConfig.getAppid());
        requestInfo.setMch_id(wxmpConfig.getPayMchId());
        requestInfo.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        requestInfo.setBody(poName);
        requestInfo.setOut_trade_no(poNo);
        requestInfo.setTotal_fee(total_fee);
        requestInfo.setSpbill_create_ip(HttpHelper.getIpAddress());
        requestInfo.setNotify_url(wxmpConfig.getPayNotifyUrl());
        requestInfo.setTrade_type("JSAPI");
        requestInfo.setOpenid(openId);
        requestInfo.setSign_type("MD5");
        String sign = signature.getSign(requestInfo);
        requestInfo.setSign(sign);

        System.out.println(sign);

        String xml = HttpHelper.sendPostXml("https://api.mch.weixin.qq.com/pay/unifiedorder", requestInfo);
        XStream xStream = new XStream();
        xStream.alias("xml", PayResponseInfo.class);
        PayResponseInfo payResponseInfo = (PayResponseInfo) xStream.fromXML(xml);

        System.out.println(xml);

        if ("SUCCESS".equals(payResponseInfo.getReturn_code()) && payResponseInfo.getResult_code().equals(payResponseInfo.getReturn_code())) {
            SignInfo signInfo = new SignInfo();
            signInfo.setAppId(requestInfo.getAppid());
            long time = System.currentTimeMillis() / 1000;
            signInfo.setTimeStamp(String.valueOf(time));
            signInfo.setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
            signInfo.setRepay_id("prepay_id=" + payResponseInfo.getPrepay_id());
            signInfo.setSignType("MD5");

            String sign1 = signature.getSign(signInfo);

            SignInfoResponse result = new SignInfoResponse();
            result.setPaySign(sign1);
            result.setAppId(signInfo.getAppId());
            result.setNonceStr(signInfo.getNonceStr());
            result.setRepay_id(signInfo.getRepay_id());
            result.setSignType(signInfo.getSignType());
            result.setTimeStamp(signInfo.getTimeStamp());

            return result;
        }

        return null;

    }
}
