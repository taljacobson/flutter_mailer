part of flutter_mailer;

/// ### Mailer Send response.
/// only [ios] can return `sent | saved | cancelled`
/// [android] will return `android` there is no way of knowing on android if the intent was sent saved or even cancelled.
enum MailerResponse {
  /// [ios] only - mail was sent
  sent,

  /// [ios] only - mail was saved as draft
  saved,

  /// [ios] only - mail was cancelled
  cancelled,

  /// [android] only.
  android,
  unknown,
}

MailerResponse _sendPlatformResponse(String response) {
  switch (response) {
    case 'sent':
      return MailerResponse.sent;
    case 'saved':
      return MailerResponse.saved;
    case 'cancelled':
      return MailerResponse.cancelled;
    case 'android':
      return MailerResponse.android;
    default:
      return MailerResponse.unknown;
  }
}
