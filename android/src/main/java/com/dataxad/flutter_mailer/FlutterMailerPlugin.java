package com.dataxad.flutter_mailer;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/**
 * FlutterMailerPlugin
 */
public class FlutterMailerPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
    private static final String FLUTTER_MAILER = "flutter_mailer";
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

    private Context context;
    private Result mResult;
    private Activity mActivity;
    private MethodChannel channel;
    private ActivityPluginBinding activityPluginBinding;


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), FLUTTER_MAILER);
        channel.setMethodCallHandler(new FlutterMailerPlugin());

        context = flutterPluginBinding.getApplicationContext();

    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), FLUTTER_MAILER);

        FlutterMailerPlugin plugin = new FlutterMailerPlugin();
        plugin.context = registrar.context();
        registrar.addActivityResultListener(plugin);
        channel.setMethodCallHandler(plugin);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("send")) {
            mail(call, result);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        context = null;
        channel.setMethodCallHandler(null);
        channel = null;
    }


    private void mail(MethodCall options, Result callback) {

        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse(MAILTO_SCHEME));

//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
                callback.error("Attachments_null", "Attachments cannot be null", null);
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
                        .setType("message/rfc822")
                        .putExtra(Intent.EXTRA_STREAM, uris)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (attachments.size() == 1) {

                    intent.setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, uris.get(0));
                }
            }
        }

        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(intent, 0);

        if (list == null || list.size() == 0) {
            Log.e("Flutter_mailer ERROR: ", "size is null or Zero");
            callback.error("not_available", "no email Managers available", null);
            return;
        }

        if (list.size() == 1) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mResult = callback;
            try {
                mActivity.startActivityForResult(intent, MAIL_ACTIVITY_REQUEST_CODE);
            } catch (Exception ex) {
                Log.e("Flutter_mailer Size==1", ex.getMessage());
                callback.error("error", ex.getMessage(), null);
            }
        } else if (options.hasArgument(APP_SCHEMA) && options.argument(APP_SCHEMA) != null && isAppInstalled((String) options.argument(APP_SCHEMA))) {
            mResult = callback;
            try {
                intent.setPackage((String) options.argument(APP_SCHEMA));
                mActivity.startActivityForResult(intent, MAIL_ACTIVITY_REQUEST_CODE);
            } catch (Exception ex) {
                Log.e("Flutter_mailer ERROR: ", ex.getMessage());
                callback.error("error", ex.getMessage(), null);
            }

        } else {

            // Intent chooser = Intent.createChooser(intent, "Send Mail");
            // chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mResult = callback;


            try {
                mActivity.startActivityForResult(intent, MAIL_ACTIVITY_REQUEST_CODE);
            } catch (Exception ex) {
                Log.e("Flutter_mailer ERROR: ", ex.getMessage());
                callback.error("error", ex.getMessage(), null);
            }
        }
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
            activityPluginBinding.getActivity().getApplicationContext().getPackageManager().getPackageInfo(id, 0);
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

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        mActivity = binding.getActivity();

        activityPluginBinding = binding;
        activityPluginBinding.addActivityResultListener(this);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        mActivity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        mActivity = binding.getActivity();
        activityPluginBinding = binding;
        activityPluginBinding.addActivityResultListener(this);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromActivity() {
        mActivity = null;
        channel.setMethodCallHandler(null);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MAIL_ACTIVITY_REQUEST_CODE && mResult != null) {
            mResult.success(null);
            return false;
        }
        return false;
    }
}
