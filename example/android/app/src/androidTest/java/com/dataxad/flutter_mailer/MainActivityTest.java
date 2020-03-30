package com.dataxad.flutter_mailer;

import androidx.test.rule.ActivityTestRule;
import dev.flutter.plugins.e2e.FlutterTestRunner;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(FlutterTestRunner.class)
public class MainActivityTest {

  @Rule
  public ActivityTestRule<io.flutter.embedding.android.FlutterActivity> rule = new ActivityTestRule<>(
      io.flutter.embedding.android.FlutterActivity.class);
}