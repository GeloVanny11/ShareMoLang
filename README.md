# ShareMoLang for Android

Phone-to-phone file sharing, ShareIt style. Uses Google's Nearby Connections:
phones find each other over Bluetooth, then files move over a direct Wi-Fi link,
with no internet, no router and no size limit.

## Get the APK from GitHub
1. Push this folder to a GitHub repo on the `main` branch.
2. Open **Actions**, run **Build Android APK**.
3. Download `sharemolang-debug-apk`, copy `ShareMoLang.apk` to both phones and install it
   (allow "install unknown apps" when asked).

## Use it
1. Open ShareMoLang on both phones and allow the nearby devices permission
   (on Android 12 or older, also turn on Location).
2. On the sending phone tap **Choose files**, then tap the other phone on the radar.
3. The receiving phone taps **Accept**. Files save to `Downloads/ShareMoLang`.

## Build locally
Open the folder in Android Studio (JDK 17) and run the `app` configuration.

## Notes
- Needs Android 10+ and Google Play Services on both phones.
- No app icon yet: add one under `app/src/main/res/` and set `android:icon`.
- Bluetooth only finds phones; Nearby switches to Wi-Fi for the transfer, which is why it is fast.
