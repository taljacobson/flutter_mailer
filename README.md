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

# Android setup
this plugin uses FileProvider for android
if you are using a conflicting plugin such as `image_picker` you'll need to edit your applications `AndroidManifest.xml` file with the following.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.company.appId"
  xmlns:tools="http://schemas.android.com/tools" <!--  add this --> 
  >
  ....
  <application 
  ....
  >
  <!-- vv   add this   vv  -->
  <provider 
    tools:replace="android:authorities"
    android:authorities="${applicationId}.adv_provider"
    android:name="android.support.v4.content.FileProvider" >
    <meta-data
        tools:replace="android:resource"
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/flutter_mailer_paths"
        />
  </provider>
  ...
```

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).
