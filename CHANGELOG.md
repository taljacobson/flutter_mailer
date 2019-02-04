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
