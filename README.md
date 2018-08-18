# flutter_mailer

[![pub package](https://img.shields.io/pub/v/flutter_mailer.svg)](https://pub.dartlang.org/packages/flutter_mailer)

#### Share an email to device Email Client - supports multiple Attachments

**based off of [react-native-mail](https://github.com/chirag04/react-native-mail)**

- [x] android
- [x] ios - _work in progress_.

```dart
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

## Getting Started

just add it to your pubspec dependencies; like so 

```
dependencies:
  flutter:
    sdk: flutter
  flutter_mailer: ^0.1.0

```

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).
