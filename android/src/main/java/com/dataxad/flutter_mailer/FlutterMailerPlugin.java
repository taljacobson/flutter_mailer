package com.dataxad.flutter_mailer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    private @Nullable
    ActivityPluginBinding activityBinding;

    public static void registerWith(PluginRegistry.Registrar registrar) {
        final FlutterMailerPlugin plugin = new FlutterMailerPlugin();
        final MethodCallHandlerImpl methodCallHandler = new MethodCallHandlerImpl(registrar.context(), registrar.activity());
        registrar.addActivityResultListener(methodCallHandler);
        plugin.setupChannel(registrar.messenger(), methodCallHandler);
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        final MethodCallHandlerImpl methodCallHandler = new MethodCallHandlerImpl(binding.getApplicationContext(), null);
        setupChannel(binding.getBinaryMessenger(), methodCallHandler);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        teardown();
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activityBinding = binding;
        activityBinding.addActivityResultListener(handler);

        handler.setActivity(activityBinding.getActivity());
    }

    @Override
    public void onDetachedFromActivity() {
        handler.setActivity(null);
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    private void setupChannel(BinaryMessenger messenger, MethodCallHandlerImpl methodCallHandler) {
        channel = new MethodChannel(messenger, FLUTTER_MAILER);
        handler = methodCallHandler;
        channel.setMethodCallHandler(methodCallHandler);
    }

    private void teardown() {
        channel.setMethodCallHandler(null);
        if (activityBinding != null) {
            activityBinding.removeActivityResultListener(handler);
        }
        channel = null;
        handler = null;
        activityBinding = null;
    }
}