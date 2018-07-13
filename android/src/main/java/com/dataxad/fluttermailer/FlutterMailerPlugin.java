package com.dataxad.fluttermailer;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import 	java.util.ArrayList;
import 	java.util.List;
import java.io.File;
import java.util.HashMap;

/** FlutterMailerPlugin */
public class FlutterMailerPlugin implements MethodCallHandler {
  private final Registrar mRegistrar;
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_mailer");
    channel.setMethodCallHandler(new FlutterMailerPlugin(registrar));
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
  }


  private void mail(MethodCall  options, Result callback) {
    Context context = mRegistrar.context();
    Intent i = new Intent(Intent.ACTION_SENDTO);
    i.setData(Uri.parse("mailto:"));

    if (options.hasArgument("subject")) {
      String subject = options.argument("subject");
      i.putExtra(Intent.EXTRA_SUBJECT, subject);
    }

    if (options.hasArgument("body")) {
      String body = options.argument("body");
      if (options.hasArgument("isHTML") && (Boolean)options.argument("isHTML")) {
        i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
      } else {
        i.putExtra(Intent.EXTRA_TEXT, body);
      }
    }
    if (options.hasArgument("recipients")) {
      ArrayList<String> recipients = options.argument("recipients");
      final String[] r = readableArrayToStringArray(recipients);
      i.putExtra(Intent.EXTRA_EMAIL, r);
    }

    if (options.hasArgument("ccRecipients")) {
      ArrayList<String> ccRecipients = options.argument("ccRecipients");
      final String[] r = readableArrayToStringArray(ccRecipients);
      i.putExtra(Intent.EXTRA_CC, r);
    }

    if (options.hasArgument("bccRecipients")) {
      ArrayList<String> bccRecipients = options.argument("bccRecipients");
      final String[] r = readableArrayToStringArray(bccRecipients);
      i.putExtra(Intent.EXTRA_BCC, r);
    }

    if (options.hasArgument("attachments")) {
      ArrayList<String> attachments  = options.argument("attachments");
      if (attachments != null && !attachments.isEmpty()) {

        for (int j = 0; j < attachments.size(); j++) {
          final String path = attachments.get(j);
            File file = new File(path);
            Uri p = Uri.fromFile(file);
            i.putExtra(Intent.EXTRA_STREAM, p);
            
        }
      }
    }

    PackageManager manager = context.getPackageManager();
    List<ResolveInfo> list = manager.queryIntentActivities(i, 0);

    if (list == null || list.size() == 0) {
      callback.error("not_available", "no email Managers available", null);
      return;
    }

    if (list.size() == 1) {
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      try {
        context.startActivity(i);
      } catch (Exception ex) {
        callback.error("error", ex.getMessage(), null);
      }
    } else {
      Intent chooser = Intent.createChooser(i, "Send Mail");
      chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      try {
        context.startActivity(chooser);
        callback.success(null);
      } catch (Exception ex) {
        callback.error("error", ex.getMessage(), null);
      }
    }
  }

  /**
   * Converts a ReadableArray to a String array
   *
   * @param r the ReadableArray instance to convert
   *
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
}
