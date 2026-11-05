package com.socialquest.app.ui
import com.socialquest.app.ui.theme.SocialquestTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.socialquest.app.AppViewModel
import com.socialquest.app.ui.screens.AgeGateScreen
import com.socialquest.app.ui.screens.AuthScreen
import com.socialquest.app.ui.screens.ConsentScreen
import com.socialquest.app.ui.screens.HomeScreen
import com.socialquest.app.ui.screens.ModeScreen
import com.socialquest.app.ui.screens.OnboardingScreen
import com.socialquest.app.ui.screens.PrivacyScreen
import com.socialquest.app.ui.screens.ProfileScreen
import com.socialquest.app.ui.screens.WelcomeScreen


// Open this file in Android Studio and use the Split / Design view to see every screen without running the app.
@Composable
private fun Frame(content: @Composable () -> Unit) =
    SocialquestTheme {
        Surface(
            color = com.socialquest.app.ui.theme.TextPrimary
        ) {
            content()
        }
    }

private fun demoVm() = AppViewModel().apply {
    nickname = "Sam"; goal = "Ask one question in a meeting"
    situations.add("Job interviews"); difficulties.add("My mind goes blank")
}

private const val W = 360
private const val H = 780

@Preview(name = "Welcome", widthDp = W, heightDp = H) @Composable fun PWelcome() = Frame { WelcomeScreen({}, {}) }
@Preview(name = "Age gate", widthDp = W, heightDp = H) @Composable fun PAge() = Frame { AgeGateScreen({}, {}) }
@Preview(name = "Sign up", widthDp = W, heightDp = H) @Composable fun PSignUp() = Frame { AuthScreen(true, {}, { _, _ -> }, {}) }
@Preview(name = "Sign in", widthDp = W, heightDp = H) @Composable fun PSignIn() = Frame { AuthScreen(false, {}, { _, _ -> }, {}) }
@Preview(name = "Consent", widthDp = W, heightDp = H) @Composable fun PConsent() = Frame { ConsentScreen({}, {}) }
@Preview(name = "Onboarding", widthDp = W, heightDp = H) @Composable fun POnboarding() = Frame { OnboardingScreen(demoVm(), {}, {}) }
@Preview(name = "Mode", widthDp = W, heightDp = H) @Composable fun PMode() = Frame { ModeScreen(demoVm(), "Start", {}, {}) }
@Preview(name = "Home", widthDp = W, heightDp = 900) @Composable fun PHome() = Frame { HomeScreen(demoVm()) {} }
@Preview(name = "Profile", widthDp = W, heightDp = 900) @Composable fun PProfile() = Frame { ProfileScreen(demoVm(), {}, {}, {}) }
@Preview(name = "Privacy", widthDp = W, heightDp = H) @Composable fun PPrivacy() = Frame { PrivacyScreen(demoVm(), {}, {}) }
