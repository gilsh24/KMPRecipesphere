import UIKit
import shared // The KMP framework name from :composeApp (set by Gradle); may be Recipes_cmp or composeApp depending config

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?

    override init() {
        super.init()
        // Ensure Koin starts on iOS too
        SharedKoinStarter().start() // see Ios bridge below
    }

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        let window = UIWindow(windowScene: windowScene)
        window.rootViewController = MainViewControllerKt.MainViewController()
        self.window = window
        window.makeKeyAndVisible()
    }
}
