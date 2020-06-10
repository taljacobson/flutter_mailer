import 'package:flutter_mailer/flutter_mailer.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:e2e/e2e.dart';

void main() {
  E2EWidgetsFlutterBinding.ensureInitialized();

  // works but opens an external app and needs to come back into the app to succeed.
  testWidgets('can open mail', (WidgetTester tester) async {
    final MailOptions mailOptions = MailOptions();
    final Future<void> send = FlutterMailer.send(mailOptions);

    expect(send, completes);
  }, skip: true);

  testWidgets('isAppInstalled', (WidgetTester tester) async {
    final bool send = await FlutterMailer.isAppInstalled('com.empty.app');

    expect(send, false);
  });
}
