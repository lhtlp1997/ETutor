package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseUser;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class EaseUserUtils {

    static private EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username 用户名
     * @return EaseUser
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username 用户名
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                Glide.with(context).load(user.getAvatar()).signature(new StringSignature(String.valueOf(user.getMills()))).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
            } catch (Exception e) {
                Glide.with(context).load(user.getAvatar()).signature(new StringSignature(String.valueOf(user.getMills()))).bitmapTransform(new CropCircleTransformation(context)).into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.fluidicon).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

}
