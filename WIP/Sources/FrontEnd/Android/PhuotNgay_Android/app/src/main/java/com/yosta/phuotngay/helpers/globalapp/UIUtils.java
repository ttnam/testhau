package com.yosta.phuotngay.helpers.globalapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yosta.phuotngay.helpers.validate.EmailValidate;
import com.yosta.phuotngay.helpers.validate.EmptyValidate;
import com.yosta.phuotngay.helpers.validate.IValidate;
import com.yosta.phuotngay.helpers.validate.LengthValidate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Phuc-Hau Nguyen on 9/29/2016.
 */

public class UIUtils {

    public static String FONT_LATO_BLACK = "fonts/Lato-Black.ttf";
    public static String FONT_LATO_BOLD = "fonts/Lato-Bold.ttf";
    public static String FONT_LATO_HEAVY = "fonts/Lato-Heavy.ttf";
    public static String FONT_LATO_LIGHT = "fonts/Lato-Light.ttf";
    public static String FONT_LATO_MEDIUM = "fonts/Lato-Medium.ttf";
    public static String FONT_LATO_THIN = "fonts/Lato-Thin.ttf";
    public static String FONT_LATO_ITALIC = "fonts/Lato-Italic.ttf";

    private static ArrayList<IValidate> emailValidates = new ArrayList<>(
            Arrays.asList(new EmptyValidate(), new EmailValidate()));

    private static ArrayList<IValidate> passWordValidates = new ArrayList<>(
            Arrays.asList(new EmptyValidate(), new LengthValidate(6)));

    private static ArrayList<IValidate> commentValidates = new ArrayList<>(
            Arrays.asList(new EmptyValidate(), new LengthValidate(1, 4096)));

    public static boolean IsEmailAccepted(TextInputEditText editText, boolean isShowErr) {
        for (IValidate email : emailValidates) {
            if (!email.IsValid(editText, isShowErr))
                return false;
        }
        return true;
    }

    public static boolean IsPasswordAccepted(TextInputEditText editText, boolean isShowErr) {
        for (IValidate pwd : passWordValidates) {
            if (!pwd.IsValid(editText, isShowErr))
                return false;
        }
        return true;
    }

    public static boolean IsCommentAccepted(String text) {
        for (IValidate cmt : commentValidates) {
            if (!cmt.IsValid(text))
                return false;
        }
        return true;
    }


    public static void setFont(Context context, String fontName, TextView... textView) {
        for (TextView aTextView : textView) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
            aTextView.setTypeface(font);
        }
    }
    public static void setFont(Context context, String fontName, AppCompatTextView... textViews) {
        for (AppCompatTextView aTextView : textViews) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
            aTextView.setTypeface(font);
        }
    }
    public static void setFont(Context context, String fontName, Button... buttons) {
        for (Button button : buttons) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
            button.setTypeface(font);
        }
    }

    public static void setFont(Context context, CheckBox checkBox, String fontName) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
        checkBox.setTypeface(font);
    }

}
