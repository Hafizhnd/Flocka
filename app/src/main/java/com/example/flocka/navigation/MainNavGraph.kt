package com.example.flocka.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flocka.data.repository.CommunityRepository
import com.example.flocka.data.repository.EventRepository
import com.example.flocka.data.repository.OrderRepository
import com.example.flocka.data.repository.SpaceRepository
import com.example.flocka.data.repository.QuizRepository
import com.example.flocka.data.repository.TodoRepository
import com.example.flocka.profile.ui.EditProfileScreen
import com.example.flocka.profile.ui.ProfileScreen
import com.example.flocka.profile.ui.TicketScreen
import com.example.flocka.ui.PeoplePage
import com.example.flocka.ui.chat.AddContactUI
import com.example.flocka.ui.chat.ChatUIScreen
import com.example.flocka.ui.chat.MessageScreenGroup
import com.example.flocka.ui.chat.MessageScreenPrivate
import com.example.flocka.ui.chat.domain.groupChat
import com.example.flocka.ui.chat.domain.privateChat
import com.example.flocka.ui.event_workspace.WorkspaceUI
import com.example.flocka.ui.home.HomePage
import com.example.flocka.ui.home.communities.CommunitiesMain
import com.example.flocka.ui.home.communities.CommunityPage
import com.example.flocka.ui.home.event.EventUI
import com.example.flocka.ui.home.event.InfoEventUI
import com.example.flocka.ui.home.event.InfoSpaceUI
import com.example.flocka.ui.notification.FriendRequestUI
import com.example.flocka.ui.notification.NotificationUI
import com.example.flocka.ui.profile.mycommunities.MyCommunities
import com.example.flocka.ui.profile.subscription.SubscriptionMain
import com.example.flocka.ui.progress.ProgressMain

@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    token: String,
    communityRepository: CommunityRepository,
    quizRepository: QuizRepository,
    spaceRepository: SpaceRepository,
    orderRepository: OrderRepository,
    todoRepository: TodoRepository,
    eventRepository: EventRepository
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("home") {
            HomePage(
                onSpaceClick = { navController.navigate("workspace") },
                onEventClick = { navController.navigate("event") },
                onSeeCommunities = { navController.navigate("communities") },
                token = token,
                quizRepository = quizRepository
            )
        }

        composable("notification") {
            NotificationUI(
                onBackClick = { navController.popBackStack() },
                onFriendRequestClick= { navController.navigate("friendRequest") }
            )
        }

        composable("friendRequest") {
            FriendRequestUI(
                onBackClick = { navController.popBackStack() },
            )
        }

        composable("progress") {
            ProgressMain(
                token = token,
                todoRepository = todoRepository
            )
        }

        composable("communities") {
            CommunitiesMain(
                token = token,
                onBackClick = { navController.popBackStack() },
                onCommunityClick = { communityId ->
                    navController.navigate("community_page/$communityId")
                },
                onCommunityCreationComplete = { newCommunityId ->
                    navController.navigate("community_page/$newCommunityId")
                },
                communityRepository = communityRepository
            )
        }

        composable(
            route = "community_page/{communityId}",
            arguments = listOf(navArgument("communityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getString("communityId") ?: ""
            CommunityPage(
                communityId = communityId,
                token = token,
                onBackClick = { navController.popBackStack() },
                communityRepository = communityRepository
            )
        }

        composable("workspace") { WorkspaceUI(
            token = token,
            onBackClick = { navController.popBackStack() },
            spaceRepository = spaceRepository,
            onSpaceCardClick = { spaceId ->
                navController.navigate("space_detail/$spaceId")
            }
        )}

        composable(route = "space_detail/{spaceId}",
            arguments = listOf(navArgument("spaceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val spaceId = backStackEntry.arguments?.getString("spaceId") ?: ""
            InfoSpaceUI(
                spaceId = spaceId,
                token = token,
                spaceRepository = spaceRepository,
                orderRepository = orderRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("event") { EventUI(
            token = token,
            onBackClick = { navController.popBackStack() },
            onEventCardClick = { eventId ->
                navController.navigate("event_detail/$eventId")
            },
            eventRepository = eventRepository
        ) }

        composable(
            route = "event_detail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            Log.d("EventFlow", "Navigating to InfoEventUI. Received eventId: $eventId")
            InfoEventUI(
                eventId = eventId,
                token = token,
                onBackClick = { navController.popBackStack() },
                eventRepository = eventRepository
            )
        }

        composable("chat") {
            ChatUIScreen(
                onChatClick = { chat ->
                    if (chat.isGroup) {
                        navController.navigate("chatGroup")
                    } else {
                        navController.navigate("chatPrivate")
                    }
                },
                onAddContact = { navController.navigate("addContact")}
            )
        }

        composable ("addContact"){ AddContactUI(
            onBackClick = { navController.popBackStack() }
        )}

        composable("chatGroup") { MessageScreenGroup(
            messages = groupChat.messages,
            title = groupChat.title,
            subtitle = "UI Designer",
            onBackClick = { navController.popBackStack() }
        ) }
        composable("chatPrivate") { MessageScreenPrivate(
            messages = privateChat.messages,
            title = privateChat.title,
            subtitle = "UI Designer",
            onBackClick = { navController.popBackStack() }
        ) }

        composable("people") { PeoplePage() }
        composable("progress") { ProgressMain(
            token = token,
            todoRepository = todoRepository
        ) }

        composable("profile") { ProfileScreen(
            token = token,
            onEditProfileClick = { navController.navigate("editProfile") },
            onMyCommunityClick = { navController.navigate("myCommunity") },
            onOrderClick = { navController.navigate("order") },
            onSettingsClick = { /* Handle settings navigation */ },
            onHelpClick = { /* Handle help navigation */ },
            onLanguageClick = { /* Handle language navigation */ },
            onSubscriptionClick = { navController.navigate("subscriptionMain") },
            onLogoutClick = {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationRoute ?: "splash") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
        }

        composable("editProfile") {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                onDoneClick = {
                    navController.popBackStack()
                },
                token = token
            )
        }

        composable("myCommunity") { MyCommunities(
            onBackClick = { navController.popBackStack() },
            onCommunityClick = { navController.navigate("communityPage") }
        )}

        composable("order") {
            TicketScreen(
                token = token,
                orderRepository = orderRepository,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("subscriptionMain") { SubscriptionMain(
            onBackClick = { navController.popBackStack() }
        )}
    }
}