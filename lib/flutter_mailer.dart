import 'dart:async';

import 'package:flutter/services.dart';

class FlutterMailer {
  static const MethodChannel _channel = const MethodChannel('flutter_mailer');

  static Future<void> send(MailOptions mailOptions) async {
    return _channel.invokeMethod('send', mailOptions.toJson());
  }
}

class MailOptions {
  static const String GMAIL = 'com.google.android.gm';

  /// Email Subject field
  final String subject;

  /// List of primary Recipients for the email
  final List<String> recipients;

  /// List of Carbon copy Recipents
  final List<String> ccRecipients;

  /// List of Blind carbon copy Recipents
  final List<String> bccRecipients;

  /// Email body field
  final String body;

  final bool isHTML;

  /// List of attachment file path
  final List<String> attachments;

  /// define a specific Email App to open
  ///
  /// this can be used to step over App choser sheet when sending an email with attachments.
  ///
  /// for Gmail on android `com.google.android.gm` or by `MailOptions.GMAIL`
  ///
  /// _android only_
  final String appSchema;
  MailOptions({
    this.subject = '',
    this.recipients = const <String>[],
    this.ccRecipients = const <String>[],
    this.bccRecipients = const <String>[],
    this.body = '',
    this.attachments,
    this.isHTML = false,
    this.appSchema,
  })  : assert(recipients != null),
        assert(ccRecipients != null),
        assert(bccRecipients != null),
        assert(isHTML != null);

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> map = <String, dynamic>{
      'subject': subject,
      'body': body,
      'recipients': recipients,
      'ccRecipients': ccRecipients,
      'bccRecipients': bccRecipients,
      'isHTML': isHTML,
      'appSchema': appSchema,
    };
    if (appSchema != null && appSchema.isNotEmpty) {
      map['appSchema'] = appSchema;
    }

    if (attachments != null && attachments.isNotEmpty) {
      final List<String> paths = <String>[];
      for (String path in attachments) {
        if (path != null && path.isNotEmpty) {
          paths.add(path);
        }
      }

      map['attachments'] = paths;
    }

    return map;
  }
}
