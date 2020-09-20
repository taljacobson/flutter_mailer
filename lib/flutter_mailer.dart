library flutter_mailer;

import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

part 'src/mailerOptions.dart';
part 'src/FlutterMailerSendResponse.dart';

class FlutterMailer {
  static const MethodChannel _channel = const MethodChannel('flutter_mailer');

  static Future<MailerResponse> send(MailOptions mailOptions) async {
    final dynamic response =
        await _channel.invokeMethod<dynamic>('send', mailOptions.toJson());

    return _sendPlatformResponse(response);
  }

  /// returns true if an app schema is installed on the device
  /// other wise returns false
  ///
  /// ### _Android only_
  /// returns false on other platforms
  static Future<bool> isAppInstalled(String schema) async {
    if (!Platform.isAndroid) {
      return false;
    }

    return _channel
        .invokeMethod('isAppInstalled', <String, String>{'appSchema': schema});
  }

  /// returns true if can MFMailComposeViewController canSendMail is true
  /// other wise returns false
  ///
  /// ### _IOS only_
  /// returns false on other platforms
  static Future<bool> canSendMail() async {
    if (!Platform.isIOS) {
      return false;
    }

    return _channel.invokeMethod('canSendMail');
  }
}
