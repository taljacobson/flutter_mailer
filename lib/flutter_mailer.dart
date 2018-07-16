import 'dart:async';

import 'package:flutter/services.dart';

class FlutterMailer {
  static const MethodChannel _channel = const MethodChannel('flutter_mailer');

  static Future<void> send(MailOptions mailOptions) async {
    return _channel.invokeMethod('send', mailOptions.toJson());
  }
}

class MailOptions {
  final String subject;
  final List<String> recipients;
  final List<String> ccRecipients;
  final List<String> bccRecipients;
  final String body; // '<b>A Bold Body</b>',
  final bool isHTML; // true
  final List<String> attachments;
  MailOptions(
      {this.subject = '',
      this.recipients = const [],
      this.ccRecipients = const [],
      this.bccRecipients = const [],
      this.body = '',
      this.attachments,
      this.isHTML = false});

  Map<String, dynamic> toJson() {
    Map<String, dynamic> map = {
      "subject": subject,
      "body": body,
      "recipients": recipients,
      "ccRecipients": ccRecipients,
      "bccRecipients": bccRecipients,
      "isHTML": isHTML,
    };
    if (attachments != null && attachments.isNotEmpty) {
      List<String> paths = <String>[];
      for (String path in attachments) {
        paths.add(path);
      }

      map["attachments"] = paths;
    }

    return map;
  }
}
