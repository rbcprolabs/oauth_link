import Flutter
import UIKit

public class SwiftOauthLinkPlugin: NSObject, FlutterPlugin {
  private var oauthChannel: FlutterMethodChannel?
  private var appScheme: String = "";
  
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "oauth_link", binaryMessenger: registrar.messenger())
    let instance = SwiftOauthLinkPlugin(channel: channel)
    registrar.addMethodCallDelegate(instance, channel: channel)
    registrar.addApplicationDelegate(instance)
  }
  
  init(channel: FlutterMethodChannel) {
    oauthChannel = channel;
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if call.method == "setScheme" {
      guard let scheme = call.arguments as? String else {
        return;
      }
      appScheme = scheme;
    }
  }
  
  public func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
    NSLog("application open url with url: " + url.debugDescription)
    return handleLink(url)
  }
  
  private func handleLink(_ url: URL) -> Bool {
    if (url.scheme == appScheme) {
      oauthChannel?.invokeMethod("onLinkSuccess", arguments: url.absoluteString)
      return true
    }
    
    return false
  }
}
