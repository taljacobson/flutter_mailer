## 1.0.1

* allow the user to attach files from the application documents without having to copy them to the cache first.

## 1.0.0

* _*[breaking change]*_ `send` method will now return a `Future<MailerResponse>`, #20<br/>
  this should not really be breaking since it used to return a `Future<void>` and its response would just be ignored.<br/> 
  on [ios] uses [mfmailcomposeresult](https://developer.apple.com/documentation/messageui/mfmailcomposeresult), on android returns `android`.


## 0.5.1

* [ios] fix discrepancy, between android and ios, where `send` future would return immediately #26


## 0.5.0

* [Android] Compatibility with Flutters Android Embedding V2
* [Android] expose `isAppInstalled` java method to dart - use this when passing a custom app schema.
* [IOS] expose `MFMailComposeViewController canSendMail` method - default mail app availablity. 


## 0.4.3

* [Androind] remove unneeded check for attachment size [PR 23](https://github.com/taljacobson/flutter_mailer/pull/23). 

## 0.4.2

* [Androind] return `send` future when app is returned to. 


## 0.4.1+2

* fix typo in ios `UNAVAILABLE` return

## 0.4.1+1

* replace androidx appcompat with core dependency for simpler classpath

## 0.4.1

* return platform error if no mail app available in ios

## 0.4.0+1

* Set Android `compileSdkVersion` to 28 in order to prevent build errors with AndroidX.

## 0.4.0

* **Breaking change**. Migrate from the deprecated original Android Support
  Library to AndroidX. This shouldn't result in any functional changes, but it
  requires any Android apps using this plugin to [also
  migrate](https://developer.android.com/jetpack/androidx/migrate) if they're
  using the original support library.

## 0.3.1
 * ios warning fixes
 * fix crash when attachment has unknow mimeType
 * display full file name with extention.

## 0.3.0
  * android - change to use "just once/always" menu instead of choser.
  _this is a super small change but changes the flow, hence it's not a patch._

## 0.2.0
  * add `appSchema` prop to `MailOptions` for by passing choser and picking specific app. _android only_
  * add comments
  * android null checks

## 0.1.2
  * minor change in android
  * expand example with temp file upload

## 0.1.1
  * update android dependencies

## 0.1.0
  * remove the need to edit `androidManifest.xml` for file sharing

## 0.0.4
 * change attachment type from `List<File>` to `List<String>`. 

## 0.0.3
* fix homepage spelling mistake in pubspec

## 0.0.2

* add initial ios support
**still work in progress, i'm not a ios devepoler, its the first time i wrote objective C code**


## 0.0.1

* TODO: Describe initial release.
