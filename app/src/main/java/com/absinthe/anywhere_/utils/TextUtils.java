package com.absinthe.anywhere_.utils;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Patterns;

import com.absinthe.anywhere_.constants.AnywhereType;
import com.absinthe.anywhere_.constants.Const;
import com.absinthe.anywhere_.constants.GlobalValues;
import com.absinthe.anywhere_.model.AnywhereEntity;
import com.absinthe.anywhere_.utils.handler.URLSchemeHandler;
import com.absinthe.anywhere_.utils.manager.URLManager;
import com.absinthe.anywhere_.view.editor.SwitchShellEditor;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class TextUtils {

    public static boolean isEmpty(CharSequence text) {
        return android.text.TextUtils.isEmpty(text);
    }

    /**
     * process and obtain adb result
     *
     * @param result return result
     */
    public static String[] processResultString(String result) {
        String packageName, className;
        int length = result.length();

        if (!result.contains(" u0 ") || result.indexOf(" u0 ") + 4 >= length - 1) {
            return null;
        }

        packageName = result.substring(result.indexOf(" u0 ") + 4, result.indexOf("/"));
        className = result.substring(result.indexOf("/") + 1, result.indexOf(" ", result.indexOf("/") + 1));

        Timber.d("packageName = %s", packageName);
        Timber.d("className = %s", className);

        return new String[]{packageName, className};
    }

    /**
     * get launch command of a item
     *
     * @param item the item
     */
    public static String getItemCommand(AnywhereEntity item) {
        StringBuilder cmd = new StringBuilder();
        int type = item.getAnywhereType();

        if (type == AnywhereType.ACTIVITY) {
            String packageName = item.getParam1();
            String className = item.getParam2();
            String extras = item.getParam3();
            Timber.d("packageName = %s, className = %s, extras = %s", packageName, className, extras);

            if (className.startsWith(".")) {
                className = packageName + className;
            }
            cmd.append(String.format(Const.CMD_OPEN_ACTIVITY_FORMAT, packageName, className));

            if (!TextUtils.isEmpty(extras)) {
                String[] extrasList = extras.split("\n");
                for (String eachLine : extrasList) {
                    cmd.append(" ").append(eachLine);
                }
            }
        } else if (type == AnywhereType.URL_SCHEME) {
            String urlScheme = item.getParam1();
            Timber.d("urlScheme = %s", urlScheme);

            if (!isEmpty(item.getParam3())) {
                cmd.append(String.format(AnywhereType.DYNAMIC_PARAMS_PREFIX_FORMAT, item.getParam3()));
            }

            if (GlobalValues.INSTANCE.getWorkingMode().equals(Const.WORKING_MODE_URL_SCHEME)) {
                cmd.append(urlScheme);
            } else {
                cmd.append(String.format(Const.CMD_OPEN_URL_SCHEME_FORMAT, urlScheme));
            }
        } else if (type == AnywhereType.QR_CODE) {
            cmd.append(AnywhereType.QRCODE_PREFIX).append(item.getParam2());
        } else if (type == AnywhereType.SHELL) {
            cmd.append(AnywhereType.SHELL_PREFIX).append(item.getParam1());
        } else if (type == AnywhereType.SWITCH_SHELL) {
            cmd.append(AnywhereType.SHELL_PREFIX);

            if (item.getParam3().equals(SwitchShellEditor.SWITCH_SHELL_OFF_STATUS)) {
                cmd.append(item.getParam1());
            } else if (item.getParam3().equals(SwitchShellEditor.SWITCH_SHELL_ON_STATUS)) {
                cmd.append(item.getParam2());
            }
        } else {
            Timber.d("AnywhereType has problem.");
        }
        Timber.d(cmd.toString());
        return cmd.toString();
    }

    /**
     * Get current date
     *
     * @return date string
     */
    public static String getCurrFormatDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * Judge that whether a string contains the other string **ignore case**
     *
     * @param str       main string
     * @param subString our target
     * @return true if contains
     */
    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

    /**
     * Get package name by adb command
     *
     * @param cmd adb command
     * @return package name
     */
    public static String getPkgNameByCommand(String cmd) {
        if (cmd.contains("am start -n")) {
            cmd = cmd.replace("am start -n ", "");
            return cmd.split("/").length > 1 ? cmd.split("/")[0] : "";
        } else if (cmd.contains("am start -a")) {
            cmd = cmd.replace(Const.CMD_OPEN_URL_SCHEME, "");
            return getPkgNameByUrlScheme(cmd);
        } else {
            return "";
        }
    }

    /**
     * Get package name by URL Scheme
     *
     * @param url URL Scheme
     * @return package name
     */
    public static String getPkgNameByUrlScheme(String url) {
        List<ResolveInfo> resolveInfo =
                Utils.getApp().getPackageManager()
                        .queryIntentActivities(URLSchemeHandler.handleIntent(url), PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() != 0) {
            return resolveInfo.get(0).activityInfo.packageName;
        } else {
            return "";
        }
    }

    /**
     * Parse URL from a sharing text
     *
     * @param sharing original text
     * @return URL
     */
    public static String parseUrlFromSharingText(String sharing) {
        if (android.text.TextUtils.isEmpty(sharing)) {
            return "Error";
        }

        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(sharing);

        if (matcher.find()) {
            return matcher.group().split("\\?")[0];
        } else {
            return null;
        }
    }

    /**
     * Judge that whether the url is an image url
     *
     * @param s url
     * @return true if is an image url
     */
    public static boolean isImageUrl(String s) {
        List<String> list = new ArrayList<>();
        list.add(".jpg");
        list.add(".jpeg");
        list.add(".png");
        list.add("webp");
        list.add("gif");
        list.add("bmp");
        list.add("tif");
        list.add("tiff");

        for (String suffix : list) {
            if (s.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Judge that whether it is a gift code
     *
     * @param code code str
     * @return true if is a gift code
     */
    public static boolean isGiftCode(String code) {
        return code.matches("^([A-Z0-9]{5}-){3}[A-Z0-9]{5}$");
    }

    /**
     * Get card sharing URL
     *
     * @param ae Card entity
     * @return URL
     */
    public static String genCardSharingUrl(AnywhereEntity ae) {
        String json = new Gson().toJson(ae, AnywhereEntity.class);
        String encrypted = CipherUtils.encrypt(json);
        if (encrypted != null) {
            encrypted = encrypted.replaceAll("\n", "");
        }
        return URLManager.ANYWHERE_SCHEME + URLManager.CARD_SHARING_HOST + "/" + encrypted;
    }
}
