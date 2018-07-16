import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_mailer/flutter_mailer.dart';
import 'package:image_picker/image_picker.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<String> attachment = [];
  TextEditingController _subjectController = new TextEditingController(text: 'the Subject');
  TextEditingController _bodyController = new TextEditingController(text: """
  <em>the body has <code>HTML</code></em> <br><br><br>
  <strong>Some Apps like Gmail might ignore it</strong>
  """);
  final GlobalKey<ScaffoldState> _scafoldKey = new GlobalKey<ScaffoldState>();
  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> send() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    final MailOptions mailOptions = MailOptions(
      body: _bodyController.text,
      subject: _subjectController.text,
      recipients: ['example@example.com'],
      isHTML: true,
      // bccRecipients: ['other@example.com'],
      ccRecipients: ['third@example.com'],
      attachments: attachment,
    );

    String platformResponse; 

    try {
      await FlutterMailer.send(mailOptions);
      platformResponse = 'success';
    } catch (error) {
      platformResponse = error.toString();
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
    _scafoldKey.currentState.showSnackBar(SnackBar(
      content: Text(platformResponse),
    ));
  }


  @override
  Widget build(BuildContext context) {
    final Widget imagePath = Column(
        children: attachment.map((file) => Text('$file')).toList());

    return new MaterialApp(
      theme: ThemeData(primaryColor: Colors.red),
      home: new Scaffold(
        key: _scafoldKey,
        appBar: new AppBar(
          title: const Text('Plugin example app'),
          actions: <Widget>[
            IconButton(
              onPressed: send,
              icon: Icon(Icons.send),
            )
          ],
        ),
        body: SingleChildScrollView(
          child: new Center(
            child: Padding(
              padding: EdgeInsets.all(8.0),
              child: Column(
                mainAxisSize: MainAxisSize.max,
                // mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      controller: _subjectController,
                      decoration: InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Subject',
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      controller: _bodyController,
                      maxLines: 10,
                      decoration: InputDecoration(
                          labelText: 'Body', border: OutlineInputBorder()),
                    ),
                  ),
                  imagePath,
                ],
              ),
            ),
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
      attachment.add(pick.path);
    });
  }
}
