package com.example.flocka.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.flocka.ui.event_workspace.EventUI
import com.example.flocka.ui.event_workspace.WorkspaceUI
import com.example.flocka.ui.home.HomePage
import com.example.flocka.ui.home.communities.CommunitiesMain
import com.example.flocka.ui.home.communities.CommunityPage
import com.example.flocka.ui.home.event.InfoEventUI
import com.example.flocka.ui.home.event.InfoSpaceUI
import com.example.flocka.ui.profile.mycommunities.MyCommunities
import com.example.flocka.ui.profile.subscription.SubscriptionMain
import com.yourpackage.ui.progress.ProgressMain

@Composable
fun MainNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues)

    ) {
        composable("home") {
            HomePage(
                onSpaceClick = { navController.navigate("workspace") },
                onEventClick = { navController.navigate("event") },
                onSeeCommunities = { navController.navigate("communities") }
            )
        }

        composable("communities") { CommunitiesMain(
            onBackClick = { navController.popBackStack()},
            onCommunityCardClick = { navController.navigate("communityPage") },
            onCommunityCreationComplete = { navController.navigate("communityPage") } // Assuming this leads to communityPage
        )}

        composable("communityPage") {
            CommunityPage(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("workspace") { WorkspaceUI(
            onBackClick = { navController.popBackStack() },
            onSpaceCardClick = {navController.navigate("infoSpace")}
        )}

        composable("infoSpace") { InfoSpaceUI(
            onBackClick = { navController.popBackStack() }
        ) }

        composable("event") { EventUI(
            onBackClick = { navController.popBackStack() },
            onEventCardClick = {navController.navigate("infoEvent")}
        ) }

        composable("infoEvent") { InfoEventUI {
        } }

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

        composable("chatGroup") { MessageScreenGroup(messages = groupChat.messages) }
        composable("chatPrivate") { MessageScreenPrivate(messages = privateChat.messages) }

        composable("people") { PeoplePage() }
        composable("progress") { ProgressMain() }

        composable("profile") { ProfileScreen(
            onEditProfileClick = { navController.navigate("editProfile") },
            onMyCommunityClick = { navController.navigate("myCommunity") },
            onOrderClick = { navController.navigate("order") },
            onSettingsClick = { /* Handle settings navigation */ },
            onHelpClick = { /* Handle help navigation */ },
            onLanguageClick = { /* Handle language navigation */ },
            onSubscriptionClick = { navController.navigate("subscriptionMain") }
        )
        }

        composable("editProfile") { EditProfileScreen(
            onBackClick = { navController.popBackStack() }
        )}

        composable("myCommunity") { MyCommunities(
            onBackClick = { navController.popBackStack() },
            onCommunityCardClick = { navController.navigate("communityPage") }
        )}

        composable("order") { TicketScreen(
            onBackClick = { navController.popBackStack() }
        )}

        // SubscriptionMain is still a regular route
        composable("subscriptionMain") { SubscriptionMain(
            onBackClick = { navController.popBackStack() }
        )}
    }
}