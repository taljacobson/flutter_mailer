# flutter_mailer

[![pub package](https://img.shields.io/pub/v/flutter_mailer.svg)](https://pub.dartlang.org/packages/flutter_mailer)

#### Share email content via device Email Client - supports multiple Attachments

Simple & quick plugin for cross application data sharing of email fields in mobile development. 

**Flutter Mailer** [example app](https://github.com/JaysQubeXon/flutter_mailer/blob/master/example/README.md)

## Supports:

- [x] android
- [x] ios

## Getting Started

Add to your pubspec dependencies, like so: 

```yaml
dependencies:
  flutter:
    sdk: flutter
  flutter_mailer: ^1.0.0

```
Instantiate mail options as [follows](https://github.com/JaysQubeXon/flutter_mailer/blob/master/example/lib/main.dart#L29):

### send email

```dart
import 'package:flutter_mailer/flutter_mailer.dart';

...
...

final MailOptions mailOptions = MailOptions(
  body: 'a long body for the email <br> with a subset of HTML',
  subject: 'the Email Subject',
  recipients: ['example@example.com'],
  isHTML: true,
  bccRecipients: ['other@example.com'],
  ccRecipients: ['third@example.com'],
  attachments: [ 'path/to/image.png', ],
);

final MailerResponse response = await FlutterMailer.send(mailOptions);
switch (response) {
  case MailerResponse.saved: /// ios only
    platformResponse = 'mail was saved to draft';
    break;
  case MailerResponse.sent: /// ios only
    platformResponse = 'mail was sent';
    break;
  case MailerResponse.cancelled: /// ios only
    platformResponse = 'mail was cancelled';
    break;
  case MailerResponse.android:
    platformResponse = 'intent was successful';
    break;
  default:
    platformResponse = 'unknown';
    break;
}

```

**note** _gmail_ and other apps Might parse HTML out of the body.


### [Android] check if app is installed.
use full if you want to send the intent to a specific App.
_returns false on [IOS]_

```dart
const GMAIL_SCHEMA = 'com.google.android.gm';

final bool gmailinstalled =  await isAppInstalled(GMAIL_SCHEMA);

if(gmailinstalled) {
  final MailOptions mailOptions = MailOptions(
    body: 'a long body for the email <br> with a subset of HTML',
    subject: 'the Email Subject',
    recipients: ['example@example.com'],
    isHTML: true,
    bccRecipients: ['other@example.com'],
    ccRecipients: ['third@example.com'],
    attachments: [ 'path/to/image.png', ],
    appSchema: GMAIL_SCHEMA,
  );
  await FlutterMailer.send(mailOptions);
}

```


### [IOS] check if device has the ability to send email
this package uses [MFMailComposeViewController](https://developer.apple.com/documentation/messageui/mfmailcomposeviewcontroller) for [IOS] which requires the default mail App.
if none is installed you might want to revert to use [url_launcher](https://pub.dev/packages/url_launcher)
_returns false on [Android]_
```dart

  final bool canSend = await canSendMail();

  if(!canSend && Platform.isIos) {
    final url = 'mailto:$recipient?body=$body&subject=$subject';
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      throw 'Could not launch $url';
    }
  }
}

```

For help getting started with Flutter, view official online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).

**based off of [react-native-mail](https://github.com/chirag04/react-native-mail)**
