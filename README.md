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
  flutter_mailer: ^0.4.1+1

```
Instantiate mail options as [follows](https://github.com/JaysQubeXon/flutter_mailer/blob/master/example/lib/main.dart#L29):

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

await FlutterMailer.send(mailOptions);

```

**note** _gmail_ and other apps Might parse HTML out of the body.

For help getting started with Flutter, view official online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).

**based off of [react-native-mail](https://github.com/chirag04/react-native-mail)**
