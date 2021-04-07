import 'package:flutter/material.dart';
import 'package:oauth_link/oauth_link.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();

    OAuthLink.instance.onLink(
      appScheme: 'test-scheme',
      onSuccess: (data) async {
        print('');
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Started'),
        ),
      ),
    );
  }
}
