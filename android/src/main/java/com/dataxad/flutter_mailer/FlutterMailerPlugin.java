package com.dataxad.flutter_mailer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.BinaryMessenger;


public class FlutterMailerPlugin implements FlutterPlugin, ActivityAware {
    private static final String FLUTTER_MAILER = "flutter_mailer";

    private MethodChannel channel;
    private MethodCallHandlerImpl handler;

    public static void registerWith(PluginRegistry.Registrar registrar) {
        final FlutterMailerPlugin plugin = new FlutterMailerPlugin();
        plugin.setupChannel(registrar.messenger(), registrar.context(), registrar.activity());
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        setupChannel(binding.getBinaryMessenger(), binding.getApplicationContext(), null);
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        teardownChannel();
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        handler.setActivity(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivity() {
        handler.setActivity(null);
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    private void setupChannel(BinaryMessenger messenger, Context context, Activity activity) {
        channel = new MethodChannel(messenger, FLUTTER_MAILER);
        handler = new MethodCallHandlerImpl(context, activity);
        channel.setMethodCallHandler(handler);
    }

    private void teardownChannel() {
        channel.setMethodCallHandler(null);
        channel = null;
        handler = null;
    }
}