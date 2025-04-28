package com.example.flocka.ui.home

import android.R.style
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R

@Composable
fun HomePage(onSpaceClick: () -> Unit, onEventClick: () -> Unit ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFedf1f7))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ads
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.width(12.dp))
                Image(
                    modifier = Modifier
                        .width(330.dp)
                        .height(185.dp),
                    painter = painterResource(id = R.drawable.img_ads_1),
                    contentDescription = "home"
                )
                Image(
                    modifier = Modifier
                        .width(330.dp)
                        .height(185.dp),
                    painter = painterResource(id = R.drawable.img_ads_2),
                    contentDescription = "home"
                )
                Image(
                    modifier = Modifier
                        .width(330.dp)
                        .height(185.dp),
                    painter = painterResource(id = R.drawable.img_ads_3),
                    contentDescription = "home"
                )
                Box(modifier = Modifier.width(12.dp))
            }

            // Community
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            "Communities",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Discover your people. Join the conversation!",
                            style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF172D9D))
                        )
                    }
                    Text(
                        "See All",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFFFF7F00),
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(95.dp),
                            painter = painterResource(id = R.drawable.img_community_1),
                            contentDescription = "group chat"
                        )
                        Column(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Column {
                                Text(
                                    "WE LOVE THE EARTH, IT IS OUR PLANET",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 15.sp,
                                        letterSpacing = 0.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "1,200 Members",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                                )
                            }
                            Text(
                                "A chill space for geology enthusiasts who care about the planet. Let’s explore, learn, and protect the Earth together!",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xffA4A4A6),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 8.sp,
                                    lineHeight = 12.sp
                                )
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(95.dp),
                            painter = painterResource(id = R.drawable.img_community_2),
                            contentDescription = "group chat"
                        )
                        Column(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Column {
                                Text(
                                    "Pixel People",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 15.sp,
                                        letterSpacing = 0.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "6,312 Members",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                                )
                            }
                            Text(
                                "A vibrant space for UI/UX and digital design lovers to share ideas, get inspired, and grow together. From pixels to prototypes — let’s create cool stuff!",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xffA4A4A6),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 8.sp,
                                    lineHeight = 12.sp
                                )
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(95.dp),
                            painter = painterResource(id = R.drawable.img_community_3),
                            contentDescription = "group chat"
                        )
                        Column(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Column {
                                Text(
                                    modifier = Modifier.padding(end = 8.dp),
                                    text = "Digi Marketing MAKES MORE MONEY",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 15.sp,
                                        letterSpacing = 0.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "4,416 Members",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                                )
                            }
                            Text(
                                "A space for digital marketing minds to connect, share strategies, and stay ahead of the trends. From SEO to social — let’s grow together!",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xffA4A4A6)
                                    ,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 8.sp,
                                    lineHeight = 12.sp
                                )
                            )
                        }
                    }
                }
            }

            // Announcement
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            "Announcements",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Discover upcoming events and more!",
                            style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF172D9D))
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .width(220.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        Text(
                            "Upcoming Seminar : Master Your Public Speaking",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF7F00),
                                lineHeight = 16.sp
                            )
                        )
                        Text(
                            "SmartPath",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF172D9D),
                                fontWeight = FontWeight.Black,
                                fontSize = 8.sp
                            )
                        )
                        Text(
                            "Join us this Saturday at 10 AM for a live seminar with professional speaker Jane Armanda.\n" +
                                    "\n" +
                                    "Here’s what you get :\n" +
                                    "✅ Boost your confidence\n" +
                                    "✅ Learn proven techniques\n" +
                                    "\n" +
                                    "This seminar will held online via Zoom. Reserve your spot now-limited seat!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 9.sp,
                                lineHeight = 12.sp
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .width(220.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        Text(
                            "Mastering Business Strategies",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF7F00),
                                lineHeight = 16.sp
                            )
                        )
                        Text(
                            "SmartPath",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF172D9D),
                                fontWeight = FontWeight.Black,
                                fontSize = 8.sp
                            )
                        )
                        Text(
                            "Unlock the secrets to building smart, scalable, and sustainable business strategies with expert Ahmed Selam. Perfect for aspiring entrepreneurs and business professionals looking to level up their game.\n" +
                                    "\n" +
                                    "\uD83C\uDFAF What you'll learn:\n" +
                                    "Strategic planning essentials\n" +
                                    "Market positioning tactics\n" +
                                    "Real-world case studies\n" +
                                    "\uD83C\uDF9F\uFE0F Limited slots available — reserve yours now!\n" +
                                    "\n" +
                                    "\uD83D\uDDD3\uFE0F November 20 | \uD83D\uDD53 16.00 – 17.30\n" +
                                    "\uD83D\uDCBB Zoom Meeting",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 9.sp,
                                lineHeight = 12.sp
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .width(220.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        Text(
                            "Design Rethink with Tola Alabi",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF7F00),
                                lineHeight = 16.sp
                            )
                        )
                        Text(
                            "SmartPath",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF172D9D),
                                fontWeight = FontWeight.Black,
                                fontSize = 8.sp
                            )
                        )
                        Text(
                            "Rethink the way you approach design with industry expert Tola Alabi. This session will challenge conventional thinking and spark fresh creative perspectives.\n" +
                                    "\n" +
                                    "What to expect:\n" +
                                    "✅ Human-centered design insights\n" +
                                    "✅ Real-world design critiques\n" +
                                    "✅ Q&A with Tola Alabi\n" +
                                    "\n" +
                                    "This seminar will held online via Zoom Meeting, at November 20th. Spots are limited — reserve yours today!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 9.sp,
                                lineHeight = 12.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable{onSpaceClick()},
                    painter = painterResource(id = R.drawable.img_space),
                    contentDescription = "space"
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable{onEventClick()},
                    painter = painterResource(id = R.drawable.img_event),
                    contentDescription = "event"
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview(){
    HomePage(
        onEventClick = {},
        onSpaceClick = {}
    )
}