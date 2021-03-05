import 'package:flutter_mailer/flutter_mailer.dart';
import 'package:flutter_test/flutter_test.dart';
// ignore: import_of_legacy_library_into_null_safe
import 'package:integration_test/integration_test.dart';

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  testWidgets('Can get battery level', (WidgetTester tester) async {
    final MailOptions mailOptions = MailOptions();
    final Future<void> send = FlutterMailer.send(mailOptions);

    expect(send, completes);
  });
}
