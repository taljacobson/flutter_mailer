#import "FlutterMailerPlugin.h"
#import <UIKit/UIKit.h>
#import <MessageUI/MessageUI.h>

@implementation FlutterMailerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"flutter_mailer"
                                     binaryMessenger:[registrar messenger]];
    FlutterMailerPlugin* instance = [[FlutterMailerPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSString *method = call.method;
    if ([@"send" isEqualToString:method]) {
        // result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
        //[self send:call.arguments:result];
        if ([MFMailComposeViewController canSendMail])
        {
            MFMailComposeViewController *mail = [[MFMailComposeViewController alloc] init];
            mail.mailComposeDelegate = self;
            
            NSDictionary *options = call.arguments;
            if (options[@"subject"]){
                NSString *subject =  options[@"subject"];
                [mail setSubject:subject];
            }
            
            bool isHTML = false;
            
            if (options[@"isHTML"]){
                isHTML = [options[@"isHTML"] boolValue];
            }
            
            if (options[@"body"]){
                NSString *body = options[@"body"];
                [mail setMessageBody:body isHTML:isHTML];
            }
            
            if (options[@"recipients"]){
                NSArray *recipients = options[@"recipients"];
                [mail setToRecipients:recipients];
            }
            
            if (options[@"ccRecipients"]){
                NSArray *ccRecipients = options[@"ccRecipients"];
                [mail setCcRecipients:ccRecipients];
            }
            
            if (options[@"bccRecipients"]){
                NSArray *bccRecipients = options[@"bccRecipients"];
                [mail setBccRecipients:bccRecipients];
            }
            if (options[@"attachments"]){
                NSArray *attachments =  options[@"attachments"];
                for (NSString *attachmentPath in attachments) {
                    
                    
                    NSString *attachmentType = [[attachmentPath lastPathComponent] pathExtension];
                    // Get the resource path and read the file using NSData
                    NSData *fileData = [NSData dataWithContentsOfFile:attachmentPath];

                    // Determine the MIME type
                    NSString *mimeType;
                    
                    NSString *name = [attachmentPath lastPathComponent];
                    
                    
                    /*
                     * Add additional mime types and PR if necessary. Find the list
                     * of supported formats at http://www.iana.org/assignments/media-types/media-types.xhtml
                     */
                    if ([attachmentType isEqualToString:@"jpg"]) {
                        mimeType = @"image/jpeg";
                    } else if ([attachmentType isEqualToString:@"png"]) {
                        mimeType = @"image/png";
                    } else if ([attachmentType isEqualToString:@"doc"]) {
                        mimeType = @"application/msword";
                    } else if ([attachmentType isEqualToString:@"ppt"]) {
                        mimeType = @"application/vnd.ms-powerpoint";
                    } else if ([attachmentType isEqualToString:@"html"]) {
                        mimeType = @"text/html";
                    } else if ([attachmentType isEqualToString:@"csv"]) {
                        mimeType = @"text/csv";
                    } else if ([attachmentType isEqualToString:@"pdf"]) {
                        mimeType = @"application/pdf";
                    } else if ([attachmentType isEqualToString:@"vcard"]) {
                        mimeType = @"text/vcard";
                    } else if ([attachmentType isEqualToString:@"json"]) {
                        mimeType = @"application/json";
                    } else if ([attachmentType isEqualToString:@"zip"]) {
                        mimeType = @"application/zip";
                    } else if ([attachmentType isEqualToString:@"text"] || [attachmentType isEqualToString:@"txt"]) {
                        mimeType = @"text/*";
                    } else if ([attachmentType isEqualToString:@"mp3"]) {
                        mimeType = @"audio/mpeg";
                    } else if ([attachmentType isEqualToString:@"wav"]) {
                        mimeType = @"audio/wav";
                    } else if ([attachmentType isEqualToString:@"aiff"]) {
                        mimeType = @"audio/aiff";
                    } else if ([attachmentType isEqualToString:@"flac"]) {
                        mimeType = @"audio/flac";
                    } else if ([attachmentType isEqualToString:@"ogg"]) {
                        mimeType = @"audio/ogg";
                    } else if ([attachmentType isEqualToString:@"xls"]) {
                        mimeType = @"application/vnd.ms-excel";
                    } else {
                        mimeType = @"application/octet-stream";
                    }
                    
                    
                    // Add attachment
                    [mail addAttachmentData:fileData mimeType:mimeType fileName:name];
                }
            }
            
            UIViewController *root = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
            
            while (root.presentedViewController) {
                root = root.presentedViewController;
            }
            [root presentViewController:mail animated:YES completion:nil];
            result(nil);
        } else {
            result([FlutterError errorWithCode:@"UNAVAILABLE"
                                 message:@"default mail app not available"
                                 details:nil]);
        }
        
    } else {
        result(FlutterMethodNotImplemented);
    }
}


#pragma mark MFMailComposeViewControllerDelegate Methods

- (void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error
{
    // NSString *key = RCTKeyForInstance(controller);
    // RCTResponseSenderBlock callback = _callbacks[key];
    // if (callback) {
    //     switch (result) {
    //         case MFMailComposeResultSent:
    //             callback(@[[NSNull null] , @"sent"]);
    //             break;
    //         case MFMailComposeResultSaved:
    //             callback(@[[NSNull null] , @"saved"]);
    //             break;
    //         case MFMailComposeResultCancelled:
    //             callback(@[[NSNull null] , @"cancelled"]);
    //             break;
    //         case MFMailComposeResultFailed:
    //             callback(@[@"failed"]);
    //             break;
    //         default:
    //             callback(@[@"error"]);
    //             break;
    //     }
    // [_callbacks removeObjectForKey:key];
    // } else {
    //  NSLog(@"No callback registered for mail: %@", controller.title);
    // }
    UIViewController *ctrl = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    while (ctrl.presentedViewController && ctrl != controller) {
        ctrl = ctrl.presentedViewController;
    }
    [ctrl dismissViewControllerAnimated:YES completion:nil];
}


@end
