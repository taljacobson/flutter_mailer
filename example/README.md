
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
