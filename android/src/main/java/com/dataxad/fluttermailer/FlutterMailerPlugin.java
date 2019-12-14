package com.dataxad.fluttermailer;

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

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterMailerPlugin
 */
public class FlutterMailerPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
    private static final String IS_HTML = "isHTML";
    private static final String SUBJECT = "subject";
    private static final String BODY = "body";
    private static final String RECIPIENTS = "recipients";
    private static final String CCRecipients = "ccRecipients";
    private static final String BCCRecipients = "bccRecipients";
    private static final String ATTACHMENTS = "attachments";
    private static final String MAILTO_SCHEME = "mailto:";
    private static final String APP_SCHEMA = "appSchema";
    private final Registrar mRegistrar;

    private Result mResult;
    private Activity mActivty;
    private static final int MAIL_ACTIVTY_REQUEST_CODE = 564;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_mailer");
        FlutterMailerPlugin plugin = new FlutterMailerPlugin(registrar);
        channel.setMethodCallHandler(plugin);
        registrar.addActivityResultListener(plugin);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("send")) {
            mail(call, result);
        } else {
            result.notImplemented();
        }
    }

    private FlutterMailerPlugin(Registrar registrar) {
        this.mRegistrar = registrar;
        this.mActivty = registrar.activity();
    }


    private void mail(MethodCall options, Result callback) {
        Context context = mRegistrar.context();
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse(MAILTO_SCHEME));

//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (options.hasArgument(SUBJECT)) {
            String subject = options.argument(SUBJECT) ;
            intent.putExtra(Intent.EXTRA_SUBJECT, subject != null ? subject : "");
        }

        if (options.hasArgument(BODY)) {
            final String body = options.argument(BODY);
            CharSequence text =  body != null ? body: "" ;

            if (options.hasArgument(IS_HTML) && (boolean)options.argument(IS_HTML)) {
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
            final String[] r = readableArrayToStringArray( ccRecipients != null ? ccRecipients : new ArrayList<String>() );
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
            } else if (!attachments.isEmpty() ) {
                ArrayList<Uri> uris = new ArrayList<>();


                for (int j = 0; j < attachments.size(); j++) {
                    final String path = attachments.get(j);


                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    File file = new File(path);

                    final Uri p = FileProvider.getUriForFile(context, mRegistrar.context().getPackageName() + ".adv_provider", file);
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
                mActivty.startActivityForResult(intent, MAIL_ACTIVTY_REQUEST_CODE);
            } catch (Exception ex) {
                Log.e("Flutter_mailer Size==1", ex.getMessage());
                callback.error("error", ex.getMessage(), null);
            }
        } else if (options.hasArgument(APP_SCHEMA) && options.argument(APP_SCHEMA) != null && isAppInstalled((String) options.argument(APP_SCHEMA)) ) {
            mResult = callback;
            try {
                intent.setPackage((String)options.argument(APP_SCHEMA));
                mActivty.startActivityForResult(intent, MAIL_ACTIVTY_REQUEST_CODE);
            } catch (Exception ex) {
                Log.e("Flutter_mailer ERROR: ", ex.getMessage());
                callback.error("error", ex.getMessage(), null);
            }

        } else {

            // Intent chooser = Intent.createChooser(intent, "Send Mail");
            // chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mResult = callback;


            try {
                mActivty.startActivityForResult(intent, MAIL_ACTIVTY_REQUEST_CODE);
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
     * @param id    The app id.
     * @return      true if yes otherwise false.
     */
    private boolean isAppInstalled (String id) {

        try {
            mRegistrar.activeContext().getPackageManager().getPackageInfo(id, 0);
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
    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == MAIL_ACTIVTY_REQUEST_CODE && mResult != null){
            mResult.success(null);
            return false;
        }
        return false;
    }

}
