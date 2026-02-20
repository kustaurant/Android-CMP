import SwiftUI
import NidThirdPartyLogin
import NaverBridge

@main
struct iOSApp: App {

    init() {
        guard
            let clientId = Bundle.main.object(forInfoDictionaryKey: "NAVER_LOGIN_CLIENT_ID") as? String,
            let clientSecret = Bundle.main.object(forInfoDictionaryKey: "NAVER_LOGIN_CLIENT_SECRET") as? String,
            let urlScheme = Bundle.main.object(forInfoDictionaryKey: "NAVER_LOGIN_URL_SCHEMA") as? String
        else {
            fatalError("❌ NAVER credentials missing in Info.plist")
        }

        let appName = Bundle.main.object(forInfoDictionaryKey: "CFBundleDisplayName") as? String ?? "iOSApp"

        NidOAuth.shared.initialize(
            appName: appName,
            clientId: clientId,
            clientSecret: clientSecret,
            urlScheme: urlScheme
        )

        print("✅ Naver SDK initialized")
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    _ = NaverLoginBridge.shared.handleOpenURL(url)
                }
        }
    }
}
