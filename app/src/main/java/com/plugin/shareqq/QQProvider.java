package com.plugin.shareqq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * qq 提供商
 * Created by killer on 15/9/21..
 */
public class QQProvider {

    private static Tencent tencent;


    public static void init(Context context) {
        if (tencent != null) {
            return;
        }
        tencent = Tencent.createInstance("1104783970", context.getApplicationContext());
    }

    public void login(final Activity activity, IUiListener listener) {
        if (tencent == null) {
            return;
        }

        tencent.login(activity, "all", listener);
    }

    public void handleResultData(Intent intent, IUiListener listener) {
        Tencent.handleResultData(intent, listener);
    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                tencent.setAccessToken(token, expires);
                tencent.setOpenId(openId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserInfo(Context context) {

        final String openId = tencent.getOpenId();
        if (tencent.isSessionValid() && openId != null) {
            UserInfo userInfo = new UserInfo(context, tencent.getQQToken());
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {

                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    public void shareAppToQQ(final Activity activity, final IUiListener listener) {

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://pp.myapp.com/ma_icon/0/icon_12139880_1440998124/96");

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                tencent.shareToQQ(activity, params, listener);
            }
        });
    }


    public void shareObjToQZone(final Activity activity, final IUiListener listener) {
        final Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, Integer.toString(QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT));
        ArrayList<String> icons = new ArrayList<>();
        icons.add("http://pp.myapp.com/ma_icon/0/icon_12139880_1440998124/96");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, icons);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                tencent.shareToQzone(activity, params, listener);
            }
        });
    }
}
