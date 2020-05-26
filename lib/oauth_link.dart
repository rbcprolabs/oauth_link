import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef OnLinkSuccessCallback = Future<dynamic> Function(String link);

class OAuthLink {
  OAuthLink._();

  static final OAuthLink instance = OAuthLink._();
  static const MethodChannel _channel = MethodChannel('oauth_link');

  OnLinkSuccessCallback _onLinkSuccess;

  void onLink({String appScheme, OnLinkSuccessCallback onSuccess}) {
    _onLinkSuccess = onSuccess;
    _channel
      ..invokeMethod('setScheme', appScheme)
      ..setMethodCallHandler(_handleMethod);
  }

  Future<dynamic> _handleMethod(MethodCall call) async {
    debugPrint('OauthLink: $call');
    switch (call.method) {
      case 'onLinkSuccess':
        return _onLinkSuccess(call.arguments);
    }
  }
}
