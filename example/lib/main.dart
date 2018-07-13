import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_mailer/flutter_mailer.dart';
import 'package:image_picker/image_picker.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  File _imagePickered;
  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> send() async {
    String platformVersion;
    List<File> attachment = [];
    if (_imagePickered != null) {
      attachment.add(_imagePickered);
    }
    // Platform messages may fail, so we use a try/catch PlatformException.
    final MailOptions mailOptions = MailOptions(
        body:
            'this is the body <br> break <a href="www.google.com">google</a> ',
        subject: 'this is the subject',
        recipients: ['example@example.com'],
        isHTML: true,
        // bccRecipients: ['other@example.com'],
        ccRecipients: ['third@example.com'],
        attachments: attachment);

    print(mailOptions.recipients);
    try {
      await FlutterMailer.send(mailOptions);
      platformVersion = 'success';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    } catch (error) {
      platformVersion = error.toString();
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child: Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: <Widget>[
              new Text('Running on: $_platformVersion\n'),
              RaisedButton(
                color: Colors.red,
                shape: StadiumBorder(),
                colorBrightness: Brightness.dark,
                padding: EdgeInsets.all(8.0),
                child: Text('Send Mail'),
                onPressed: send,
              )
            ],
          ),
        ),
        floatingActionButton: FloatingActionButton.extended(
          icon: Icon(Icons.camera),
          label: Text('Add Image'),
          onPressed: _picker,
        ),
      ),
    );
  }

  void _picker() async {
    File pick = await ImagePicker.pickImage(source: ImageSource.gallery);
    setState(() {
      _imagePickered = pick;
    });
  }
}
