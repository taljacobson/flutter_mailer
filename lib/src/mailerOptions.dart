part of flutter_mailer;

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
  final String? appSchema;
  // ignore: sort_constructors_first
  MailOptions({
    this.subject = '',
    this.recipients = const <String>[],
    this.ccRecipients = const <String>[],
    this.bccRecipients = const <String>[],
    this.body = '',
    this.attachments = const <String>[],
    this.isHTML = false,
    this.appSchema,
  });

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
    if (appSchema != null && appSchema!.isNotEmpty) {
      map['appSchema'] = appSchema;
    }

    if (attachments.isNotEmpty) {
      final List<String> paths = <String>[];
      for (String path in attachments) {
        if (path.isNotEmpty) {
          paths.add(path);
        }
      }

      map['attachments'] = paths;
    }

    return map;
  }
}
