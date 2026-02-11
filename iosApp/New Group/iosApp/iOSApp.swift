import SwiftUI

@main
struct iOSApp: App {
    init() {
        NidThirdPartyLoginConnection.getSharedInstance().setOnlyPortraitMode(true)

    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
