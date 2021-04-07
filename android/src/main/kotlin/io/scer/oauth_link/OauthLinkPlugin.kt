package io.scer.oauth_link

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

/** OauthLinkPlugin */
class OauthLinkPlugin: FlutterPlugin, ActivityAware, MethodCallHandler, PluginRegistry.NewIntentListener {
  private var mActivity: Activity? = null
  private var appScheme: String? = null
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "oauth_link")
    channel.setMethodCallHandler(this)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
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
      val dataUri = intent.data as Uri
      if (dataUri.scheme == appScheme) {
        channel.invokeMethod("onLinkSuccess", dataUri.toString())
        return false
      }
    }

    return true
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
