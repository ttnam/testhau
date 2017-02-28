package com.yosta.phuotngay.helpers.validate;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Phuc-Hau Nguyen on 9/29/2016.
 */

public class ValidateHelper {

    private static ArrayList<IValidate> emailValidates = new ArrayList<>(
            Arrays.asList(new EmptyValidate(), new EmailValidate()));

    private static ArrayList<IValidate> passWordValidates = new ArrayList<>(
            Arrays.asList(new EmptyValidate(), new LengthValidate(6)));

    private static ArrayList<IValidate> commentValidates = new ArrayList<>(
            Arrays.asList(new EmptyValidate(), new LengthValidate(1, 4096)));

    public static boolean isEmailAccepted(TextInputEditText editText, boolean isShowErr) {
        for (IValidate email : emailValidates) {
            if (!email.IsValid(editText, isShowErr))
                return false;
        }
        return true;
    }

    public static boolean isPasswordAccepted(TextInputEditText editText, boolean isShowErr) {
        for (IValidate pwd : passWordValidates) {
            if (!pwd.IsValid(editText, isShowErr))
                return false;
        }
        return true;
    }

    public static boolean isCommentAccepted(String text) {
        for (IValidate cmt : commentValidates) {
            if (!cmt.IsValid(text))
                return false;
        }
        return true;
    }

    public static boolean canUse(String... values) {
        for (String str : values) {
            if (str == null || str.equals("") || str.length() < 1) {
                return false;
            }
        }
        return true;
    }

}
