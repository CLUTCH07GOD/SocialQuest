package com.socialquest.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import com.socialquest.app.ui.components.SocialQuestLoadingScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import com.socialquest.app.ui.components.SocialQuestLoadingScreen
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.socialquest.app.AppViewModel
import com.socialquest.app.ui.screens.AgeGateScreen
import com.socialquest.app.ui.screens.AuthScreen
import com.socialquest.app.ui.screens.ConsentScreen
import com.socialquest.app.ui.screens.HomeScreen
import com.socialquest.app.ui.screens.ModeScreen
import com.socialquest.app.ui.screens.OnboardingScreen
import com.socialquest.app.ui.screens.PlaceholderScreen
import com.socialquest.app.ui.screens.PrivacyScreen
import com.socialquest.app.ui.screens.ProfileScreen
import com.socialquest.app.ui.screens.WelcomeScreen
import com.socialquest.app.ui.theme.Coral
import com.socialquest.app.ui.theme.Paper

object Routes {
    const val WELCOME = "welcome"
    const val AGE_GATE = "age_gate"
    const val SIGN_UP = "sign_up"
    const val SIGN_IN = "sign_in"
    const val CONSENT = "consent"
    const val ONBOARDING = "onboarding"
    const val MODE = "mode"
    const val MODE_EDIT = "mode_edit"
    const val HOME = "home"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
    const val PRIVACY = "privacy"
    // Module entry points owned by P2, P3, P4. Replace each PlaceholderScreen below with the real screen.
    const val MIND = "mind"
    const val PRACTISE = "practise"
    const val COURAGE = "courage"
    const val SUPPORT = "support"
}

private data class Tab(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppNav(vm: AppViewModel = viewModel()) {
    val nav = rememberNavController()
    var showJourneyLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(showJourneyLoading) {
        if (showJourneyLoading) {
            delay(6500)
            showJourneyLoading = false
        }
    }


    val route = nav.currentBackStackEntryAsState().value?.destination?.route

    val tabs = listOf(
        Tab(Routes.HOME, "Home", Icons.Outlined.Home),
        Tab(Routes.PROGRESS, "Progress", Icons.Outlined.Insights),
        Tab(Routes.PROFILE, "Profile", Icons.Outlined.Person),
    )
    val showBar = route in tabs.map { it.route }
    // "Need help now" stays reachable everywhere after sign-in except where it already has a button.
    val showHelp = route in setOf(
        Routes.PROGRESS, Routes.PROFILE, Routes.PRIVACY, Routes.MODE_EDIT,
        Routes.MIND, Routes.PRACTISE, Routes.COURAGE,
    )

    fun toTab(r: String) = nav.navigate(r) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
    fun enterApp() = nav.navigate(Routes.HOME) { popUpTo(Routes.WELCOME) { inclusive = true } }
    LaunchedEffect(showJourneyLoading) {
        if (showJourneyLoading) {
            delay(6500)

            showJourneyLoading = false
            enterApp()
        }
    }
    fun restart() = nav.navigate(Routes.WELCOME) { popUpTo(nav.graph.id) { inclusive = true } }
    val back: () -> Unit = { nav.popBackStack() }
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (showBar) {
                    NavigationBar(containerColor = Paper) {
                        tabs.forEach { t ->
                            NavigationBarItem(
                                selected = route == t.route,
                                onClick = { toTab(t.route) },
                                icon = { Icon(t.icon, contentDescription = null) },
                                label = { Text(t.label) },
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (showHelp) {
                    ExtendedFloatingActionButton(
                        onClick = { nav.navigate(Routes.SUPPORT) },
                        containerColor = Coral,
                        contentColor = Color.White,
                        icon = { Icon(Icons.Outlined.Spa, contentDescription = null) },
                        text = { Text("Need help now") },
                    )
                }
            },
        ) { pad ->
            NavHost(
                navController = nav,
                startDestination = Routes.WELCOME,
                modifier = Modifier.padding(pad).consumeWindowInsets(pad),
            ) {
                composable(Routes.WELCOME) {
                    WelcomeScreen(
                        onStart = { nav.navigate(Routes.AGE_GATE) },
                        onSignIn = { nav.navigate(Routes.SIGN_IN) })
                }
                composable(Routes.AGE_GATE) {
                    AgeGateScreen(onConfirm = { nav.navigate(Routes.SIGN_UP) }, onBack = back)
                }
                composable(Routes.SIGN_UP) {
                    AuthScreen(
                        register = true,
                        onBack = back,
                        // TODO: POST /auth/register with age_confirmed = true.
                        onSubmit = { email, _ ->
                            if (!showJourneyLoading) {
                                vm.email = email
                                showJourneyLoading = true
                            }
                        },
                        onSwitch = {
                            nav.navigate(Routes.SIGN_IN) {
                                popUpTo(Routes.SIGN_UP) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
                composable(Routes.SIGN_IN) {
                    AuthScreen(
                        register = false,
                        onBack = back,
                        // TODO: POST /auth/login.
                        onSubmit = { email, _ ->
                            if (!showJourneyLoading) {
                                vm.email = email
                                showJourneyLoading = true
                            }
                        },
                        // New accounts always pass the 18+ gate first.
                        onSwitch = {
                            nav.navigate(Routes.AGE_GATE) {
                                popUpTo(Routes.SIGN_IN) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
                composable(Routes.CONSENT) {
                    ConsentScreen(
                        onBack = back,
                        onAgree = { nav.navigate(Routes.ONBOARDING) })
                }
                composable(Routes.ONBOARDING) {
                    OnboardingScreen(vm, onBack = back, onFinished = { nav.navigate(Routes.MODE) })
                }
                composable(Routes.MODE) {
                    ModeScreen(vm, doneLabel = "Start", onBack = back, onDone = { enterApp() })
                }
                composable(Routes.MODE_EDIT) {
                    ModeScreen(vm, doneLabel = "Save", onBack = back, onDone = back)
                }
                composable(Routes.HOME) { HomeScreen(vm, onOpen = { nav.navigate(it) }) }
                composable(Routes.PROGRESS) {
                    PlaceholderScreen(
                        "Progress",
                        "Person 4",
                        "Sessions, quests, reflections, goals and milestones will appear here.",
                        null
                    )
                }
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        vm,
                        onMode = { nav.navigate(Routes.MODE_EDIT) },
                        onPrivacy = { nav.navigate(Routes.PRIVACY) },
                        onSignOut = { vm.reset(); restart() },
                    )
                }
                composable(Routes.PRIVACY) {
                    PrivacyScreen(vm, onBack = back, onDeleted = { vm.reset(); restart() })
                }
                composable(Routes.MIND) {
                    PlaceholderScreen(
                        "Understand your mind",
                        "Person 2",
                        "Fear Map, thought reflection, Courage Ladder and Prediction vs Reality go here.",
                        back
                    )
                }
                composable(Routes.PRACTISE) {
                    PlaceholderScreen(
                        "Practise conversations",
                        "Person 3",
                        "The voice simulation and AI characters go here.",
                        back
                    )
                }
                composable(Routes.COURAGE) {
                    PlaceholderScreen(
                        "Build your courage",
                        "Person 4",
                        "Real-world Courage Quests go here.",
                        back
                    )
                }
                composable(Routes.SUPPORT) {
                    PlaceholderScreen(
                        "Instant support",
                        "Person 4",
                        "Short, optional exercises and professional-support resources go here.",
                        back
                    )
                }
            }
        }
    }
    if (showJourneyLoading) {
        SocialQuestLoadingScreen(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f),
            message = "Your next chapter begins..."
        )
    }

}
