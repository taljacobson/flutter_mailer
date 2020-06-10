import 'package:flutter_mailer/flutter_mailer.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:e2e/e2e.dart';

void main() {
  E2EWidgetsFlutterBinding.ensureInitialized();

  testWidgets('Can get battery level', (WidgetTester tester) async {
    final MailOptions mailOptions = MailOptions();
    final Future<void> send = FlutterMailer.send(mailOptions);

    expect(send, completes);
  });
}
