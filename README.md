# flutter_mailer

#### Share an email to device Email Client supports multiple Attachments


**based off of [react-native-mail](https://github.com/chirag04/react-native-mail)**



- [x] android
- [ ] ios


``` dart

    final MailOptions mailOptions = MailOptions(
      body: 'a long body for the email <br> with a subset of HTML',
      subject: 'the Email Subject',
      recipients: ['example@example.com'],
      isHTML: true,
      bccRecipients: ['other@example.com'],
      ccRecipients: ['third@example.com'],
      attachments: [ new File('path/to/image.png'), ],
    );
    
    await FlutterMailer.send(mailOptions);

```

**note** _gmail_ and other apps parse HTML out of the body.


## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).
