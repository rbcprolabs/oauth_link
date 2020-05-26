#import "OauthLinkPlugin.h"
#if __has_include(<oauth_link/oauth_link-Swift.h>)
#import <oauth_link/oauth_link-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "oauth_link-Swift.h"
#endif

@implementation OauthLinkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftOauthLinkPlugin registerWithRegistrar:registrar];
}
@end
