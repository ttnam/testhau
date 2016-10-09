package com.yosta.phuotngay.helpers;

import android.support.design.widget.TextInputEditText;

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
}
