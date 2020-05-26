package com.example.oauth_link

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar

/** OauthLinkPlugin */
class OauthLinkPlugin: FlutterPlugin, ActivityAware, MethodCallHandler, PluginRegistry.NewIntentListener {
  private var mActivity: Activity? = null
  private var methodChannel: MethodChannel? = null
  private var appScheme: String? = null

  constructor() {}

  constructor(activity: Activity, channel: MethodChannel) {
    mActivity = activity
    methodChannel = channel
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "oauth_link");
      val plugin = OauthLinkPlugin(registrar.activity(), channel)
      registrar.addNewIntentListener(plugin)
      channel.setMethodCallHandler(plugin)
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "setScheme") {
      if (call.arguments !is String) {
        return
      }
      appScheme = call.arguments as String?
    }
  }

  override fun onNewIntent(intent: Intent?): Boolean {
    if (intent?.data is Uri && appScheme != null) {
      var dataUri = intent?.data as Uri
      if (dataUri.scheme == appScheme) {
        methodChannel?.invokeMethod("onLinkSuccess", dataUri.toString())
        return false
      }
    }

    return true
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel = MethodChannel(flutterPluginBinding.flutterEngine.dartExecutor, "oauth_link")
    methodChannel?.setMethodCallHandler(this);
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel?.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    mActivity = binding.activity
    binding.addOnNewIntentListener(this)
  }

  override fun onDetachedFromActivity() {
    mActivity = null;
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    mActivity = binding.activity
    binding.addOnNewIntentListener(this)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    mActivity = null;
  }
}
