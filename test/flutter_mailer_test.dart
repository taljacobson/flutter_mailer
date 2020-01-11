import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_mailer/flutter_mailer.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_mailer');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  // test('send', () async {
  //   final MailOptions mailOptions = MailOptions(
  //     subject: "Subject",
  //     body: "Body",
  //     recipients: ["example@domain.com"]
  //   );

  //   expect(await FlutterMailer.send(mailOptions), true);
  // });
}
