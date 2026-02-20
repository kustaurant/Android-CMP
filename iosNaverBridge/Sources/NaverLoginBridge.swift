import Foundation
import NidThirdPartyLogin

@objc public final class NaverLoginBridge: NSObject {

    @objc public static let shared = NaverLoginBridge()
    @objc public func handleOpenURL(_ url: URL) -> Bool {
        return NidOAuth.shared.handleURL(url) == true
    }

    @objc public func login(
        onSuccess: @escaping (String, String) -> Void,
        onCancel: @escaping (String?) -> Void,
        onFailure: @escaping (String, String?) -> Void
    ) {
        NidOAuth.shared.requestLogin { result in
            switch result {
            case .success(let loginResult):
                let accessToken = loginResult.accessToken.tokenString

                NidOAuth.shared.getUserProfile(accessToken: accessToken) { profileResult in
                    switch profileResult {
                    case .success(let profile):
                        let providerId = (profile["id"] ?? "").trimmingCharacters(in: .whitespacesAndNewlines)

                        if providerId.isEmpty || accessToken.isEmpty {
                            onFailure("INVALID_DATA", "providerId or accessToken empty")
                            return
                        }
                        onSuccess(providerId, accessToken)

                    case .failure(let error):
                        onFailure("PROFILE_ERROR", error.localizedDescription)
                    }
                }

            case .failure(let error):
                let msg = error.localizedDescription
                if msg.lowercased().contains("cancel") {
                    onCancel(msg)
                } else {
                    onFailure("LOGIN_ERROR", msg)
                }
            }
        }
    }
}

