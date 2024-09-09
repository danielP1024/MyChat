package com.danielpasser.mychat.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.danielpasser.mychat.compose.chat.ChatScreen
import com.danielpasser.mychat.compose.chatlist.ChatListScreen
import com.danielpasser.mychat.compose.edituser.EditUserProfileScreen
import com.danielpasser.mychat.compose.loginscreen.LoginScreen
import com.danielpasser.mychat.compose.registerscreen.RegisterScreen
import com.danielpasser.mychat.compose.userprofile.UserProfileScreen
import com.danielpasser.mychat.models.Country
import com.danielpasser.mychat.utils.CustomNavType
import com.danielpasser.mychat.viewmodels.MyChatAppViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object Login

@Serializable
data class Registration(val country: Country, val phone: String)

@Serializable
data class Chat(val userName: String)

@Serializable
object UserProfile

@Serializable
object UserEditor

@Serializable
object ChatList

@Composable
fun MyChatApp(viewModel: MyChatAppViewModel = hiltViewModel()) {
    val isLogin = viewModel.isLogin.collectAsState().value
    val navController = rememberNavController()
    MyChatNavController(navController, isLogin, onLogoff = { viewModel.logOff() })


}

@Composable
fun MyChatNavController(navController: NavHostController, isLogin: Boolean, onLogoff: () -> Unit) {
    NavHost(navController = navController, startDestination = if (isLogin) ChatList else Login)
    {
        composable<Chat>
        { navBackStackEntry ->

            val chat: Chat = navBackStackEntry.toRoute()
            ChatScreen(
                onProfileClicked = { navController.navigate(UserProfile) },
                onLogOffClicked = onLogoff,
                onEditProfileClicked = { navController.navigate(UserEditor) },
                userName = chat.userName,
                onBackClicked = {navController.popBackStack()}
            )
        }
        composable<ChatList>
        {
            ChatListScreen(
                onProfileClicked = { navController.navigate(UserProfile) },
                onMessageClicked = { navController.navigate(route = Chat(it)) },
                onEditProfileClicked = { navController.navigate(UserEditor) },
                onLogOffClicked = onLogoff,
            )
        }

        composable<UserEditor>
        {
            EditUserProfileScreen(navigateBack = {
                navController.popBackStack()
            })
        }

        composable<UserProfile>
        {
            UserProfileScreen(onEditProfileClicked = { navController.navigate(UserEditor) },onBackClicked = {navController.popBackStack()})
        }
        composable<Login>
        {
            LoginScreen(onRegisterNewUserClicked = {
                navController.navigate(route = it)

            }
//                , onLogin = {
//              //  navController.navigate(Chat)
//            }
            )
        }
        composable<Registration>(
            typeMap = mapOf(
                typeOf<Country>() to CustomNavType(
                    Country::class.java,
                    Country.serializer()
                )
            )
        ) { navBackStackEntry ->
            val registration: Registration = navBackStackEntry.toRoute()
            RegisterScreen(reg = registration)
            //onRegister = { navController.navigate(Chat) })
        }
    }
}