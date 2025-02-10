package com.dataxad.flutter_mailer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler, PluginRegistry.ActivityResultListener {
    static class FlutterMailerException extends Exception {
        final String errorMessage;
        final Object errorDetails;
        final String errorCode;

        FlutterMailerException(String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {
            super();
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.errorDetails = errorDetails;
        }
    }

    private static final String TAG = "FLUTTER_MAILER";
    private static final String IS_HTML = "isHTML";
    private static final String SUBJECT = "subject";
    private static final String BODY = "body";
    private static final String RECIPIENTS = "recipients";
    private static final String CCRecipients = "ccRecipients";
    private static final String BCCRecipients = "bccRecipients";
    private static final String ATTACHMENTS = "attachments";
    private static final String MAILTO_SCHEME = "mailto:";
    private static final String APP_SCHEMA = "appSchema";
    private static final int MAIL_ACTIVITY_REQUEST_CODE = 564;

    private final Context context;
    private Activity activity;
    private MethodChannel.Result mResult;

    MethodCallHandlerImpl(@NonNull Context context, @Nullable Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if (call.method.equals("send")) {
            mResult = result;
            try {
                final Intent intent = mail(call);
                activity.startActivityForResult(intent, MAIL_ACTIVITY_REQUEST_CODE);
            } catch (FlutterMailerException e) {
                result.error(e.errorCode, e.errorMessage, e.errorDetails);
                mResult = null;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                result.error("UNKNOWN", e.getMessage(), null);
                mResult = null;
            }
        } else if (call.method.equals("isAppInstalled")) {
            if (call.hasArgument(APP_SCHEMA) && call.argument(APP_SCHEMA) != null && isAppInstalled((String) call.argument(APP_SCHEMA))) {
                result.success(true);
            } else {
                result.success(false);
            }
        } else {
            result.notImplemented();
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MAIL_ACTIVITY_REQUEST_CODE && mResult != null) {
            mResult.success("android");
            mResult = null;
            return false;
        }
        return false;
    }


    private Intent mail(MethodCall options) throws FlutterMailerException {

        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse(MAILTO_SCHEME));


        if (options.hasArgument(SUBJECT)) {
            String subject = options.argument(SUBJECT);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject != null ? subject : "");
        }

        if (options.hasArgument(BODY)) {
            final String body = options.argument(BODY);
            CharSequence text = body != null ? body : "";

            if (options.hasArgument(IS_HTML) && (boolean) options.argument(IS_HTML)) {
                text = fromHtml((String) text);
            }
            intent.putExtra(Intent.EXTRA_TEXT, text);

        }
        if (options.hasArgument(RECIPIENTS)) {
            ArrayList<String> recipients = options.argument(RECIPIENTS);
            final String[] r = readableArrayToStringArray(recipients != null ? recipients : new ArrayList<String>());
            intent.putExtra(Intent.EXTRA_EMAIL, r);
        }

        if (options.hasArgument(CCRecipients)) {
            ArrayList<String> ccRecipients = options.argument(CCRecipients);
            final String[] r = readableArrayToStringArray(ccRecipients != null ? ccRecipients : new ArrayList<String>());
            intent.putExtra(Intent.EXTRA_CC, r);
        }

        if (options.hasArgument(BCCRecipients)) {
            ArrayList<String> bccRecipients = options.argument(BCCRecipients);
            final String[] r = readableArrayToStringArray(bccRecipients != null ? bccRecipients : new ArrayList<String>());
            intent.putExtra(Intent.EXTRA_BCC, r);
        }

        if (options.hasArgument(ATTACHMENTS)) {
            ArrayList<String> attachments = options.argument(ATTACHMENTS);
            if (attachments == null) {
                throw new FlutterMailerException("Attachments_null", "Attachments cannot be null", null);
            } else if (!attachments.isEmpty()) {
                ArrayList<Uri> uris = new ArrayList<>();

                for (int j = 0; j < attachments.size(); j++) {
                    final String path = attachments.get(j);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    File file = new File(path);

                    final Uri p = FileProvider.getUriForFile(context, context.getPackageName() + ".adv_provider", file);
                    uris.add(p);
                }

                intent.setAction(Intent.ACTION_SEND_MULTIPLE)
                        .setType(null) // if we're using a selector, then clear the type to null
                        .putExtra(Intent.EXTRA_STREAM, uris)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // restrict to just mail apps
                final Intent restrictIntent = new Intent(Intent.ACTION_SENDTO);
                restrictIntent.setData(Uri.parse(MAILTO_SCHEME));
                intent.setSelector(restrictIntent);
            }
        }

        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(intent, 0);

        if (list == null || list.size() == 0) {
            Log.e(TAG, "size is null or Zero");
            throw new FlutterMailerException("not_available", "no email Managers available", null);
        }

        if (list.size() == 1) {
            return intent;
        } else if (options.hasArgument(APP_SCHEMA) && options.argument(APP_SCHEMA) != null && isAppInstalled((String) options.argument(APP_SCHEMA))) {
            intent.setPackage((String) options.argument(APP_SCHEMA));
            return intent;
        }
        return intent;
    }

    /**
     * Converts a ReadableArray to a String array
     *
     * @param r the ReadableArray instance to convert
     * @return array of strings
     */
    private String[] readableArrayToStringArray(ArrayList<String> r) {
        int length = r.size();
        String[] strArray = new String[length];

        for (int keyIndex = 0; keyIndex < length; keyIndex++) {
            strArray[keyIndex] = r.get(keyIndex);
        }

        return strArray;
    }


    /**
     * Ask the package manager if the app is installed on the device.
     *
     * @param id The app id.
     * @return true if yes otherwise false.
     */
    private boolean isAppInstalled(String id) {
        try {
            context.getPackageManager().getPackageInfo(id, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}