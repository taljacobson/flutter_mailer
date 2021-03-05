import 'dart:io';

// ignore: import_of_legacy_library_into_null_safe
import 'package:flutter_driver/flutter_driver.dart';

Future<void> main() async {
  final FlutterDriver driver = await FlutterDriver.connect();
  final String result =
      await driver.requestData(null, timeout: const Duration(minutes: 1));
  await driver.close();
  exit(result == 'pass' ? 0 : 1);
}
