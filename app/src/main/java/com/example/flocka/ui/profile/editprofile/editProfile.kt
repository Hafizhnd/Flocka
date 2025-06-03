package com.example.flocka.profile.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.viewmodel.auth.AuthViewModel
import com.example.flocka.R
import com.example.flocka.data.model.Interest
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.viewmodel.interest.InterestViewModel
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.example.flocka.ui.components.alexandriaFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    token: String,
    authViewModel: AuthViewModel = viewModel(),
    interestViewModel: InterestViewModel = viewModel(),
    profileImageUrl: String? = null,
) {
    val userProfileFromVM by authViewModel.userProfile.collectAsState()

    var name by remember { mutableStateOf("") }
    var usernameDisplay by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var age by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    var interestInput by remember { mutableStateOf("") }
    val selectedInterests = remember { mutableStateListOf<Interest>() }

    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isLoading = true
            authViewModel.uploadProfilePicture(context, token, it)
        }
    }

    LaunchedEffect(key1 = token) {
        if (token.isNotEmpty()) {
            authViewModel.fetchUserProfile(token)
        }
    }

    LaunchedEffect(userProfileFromVM) {
        userProfileFromVM?.let { profile ->
            name = profile.name ?: ""
            usernameDisplay = profile.username
            bio = profile.bio ?: ""
            selectedGender = profile.gender ?: "Male"
            age = profile.age?.toString() ?: ""
            profession = profile.profession ?: ""
            profileImageUrl = profile.profile_image_url
        }
    }

    LaunchedEffect(authViewModel.updateProfileResult) {
        authViewModel.updateProfileResult?.let { currentUpdateResult ->
            isLoading = false
            if (currentUpdateResult.isSuccess) {
                authViewModel.fetchUserProfile(token)
                onDoneClick()
            } else {
                Log.e("EditProfileScreen", "Update failed: ${currentUpdateResult.exceptionOrNull()?.message}")
            }
            authViewModel.updateProfileResult = null
        }
    }

    LaunchedEffect(authViewModel.uploadResult) {
        authViewModel.uploadResult?.let { currentUploadResult ->
            isLoading = false
            if (currentUploadResult.isSuccess) {
                authViewModel.fetchUserProfile(token)
            } else {
                Log.e("EditProfileScreen", "Upload failed: ${currentUploadResult.exceptionOrNull()?.message}")
            }
            authViewModel.uploadResult = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF002366)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF002366)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF4F9FF))
            )
        }
    ) { paddingValues ->
        if (userProfileFromVM == null && token.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF4F9FF))
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    if (!profileImageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "User Profile Picture",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.img_avatar),
                            error = painterResource(id = R.drawable.img_avatar)
                        )
                    }else {
                        Image(
                            painter = painterResource(id = R.drawable.img_avatar),
                            contentDescription = "User Profile Picture",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                    Text(
                        "Change profile picture",
                        color = OrangePrimary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { imagePickerLauncher.launch("image/*") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TypeField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Name",
                        placeholder = "Enter your name",
                    )

                    TypeField(
                        value = usernameDisplay,
                        onValueChange = { usernameDisplay = it },
                        label = "Username",
                        placeholder = "Enter your username",
                        enabled = true
                    )

                    TypeField(
                        value = profession,
                        onValueChange = { profession = it },
                        label = "Profession",
                        placeholder = "Enter your profession"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GenderDropdown(
                            value = selectedGender,
                            onValueChange = { selectedGender = it },
                            modifier = Modifier.weight(1f)
                        )

                        AgeFieldWithSuffix(
                            value = age,
                            onValueChange = { age = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TypeField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = "Bio",
                        placeholder = "Write something about you",
                        height = 78.dp,
                        multiline = true
                    )



                    InterestInputField(
                        token = token,
                        interest = interestInput,
                        onValueChange = { interestInput = it },
                        selectedInterests = selectedInterests,
                        viewModel = interestViewModel
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            isLoading = true
                            val interestIds = selectedInterests.map { it.id }
                            authViewModel.updateProfile(
                                token = token,
                                username = usernameDisplay,
                                name = name,
                                profession = profession,
                                gender = selectedGender,
                                age = age,
                                bio = bio,
                                interestIds = interestIds
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangePrimary,
                            contentColor = Color.White
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(
                                "Save Changes",
                                fontSize = 20.sp,
                                fontFamily = sansationFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun TypeField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    height: Dp = 50.dp,
    multiline: Boolean = false,
    enabled: Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = !multiline,
            enabled = enabled,
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            ),
            cursorBrush = SolidColor(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color.Transparent, RoundedCornerShape(15.dp))
                .border(1.dp, Color(0xFF1E1E1E), RoundedCornerShape(15.dp))
                .padding(horizontal = 16.dp)
                .padding(top = 18.dp),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = if (multiline) Alignment.Top else Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = if (multiline) 8.dp else 0.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 13.sp,
                                fontFamily = alexandriaFontFamily,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .offset(x = 10.dp, y = 6.dp)
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontFamily = sansationFontFamily,
                color = Color(0xFF808183)
            )
        }
    }
}

@Composable
fun GenderDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    val options = listOf("Male", "Female")

    Box(modifier = modifier) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Transparent, RoundedCornerShape(15.dp))
                    .border(1.dp, Color(0xFF1E1E1E), RoundedCornerShape(15.dp))
                    .clickable { expanded.value = true }
                    .padding(horizontal = 16.dp)
                    .padding(top = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = value,
                        color = if (value == "Select Gender") Color.Gray else Color.Black,
                        fontFamily = sansationFontFamily,
                        fontSize = 14.sp
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_dropdown),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = "Dropdown"
                    )
                }
            }

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                fontFamily = sansationFontFamily
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded.value = false
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(x = 10.dp, y = 6.dp)
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = "Gender",
                fontSize = 13.sp,
                fontFamily = sansationFontFamily,
                color = Color(0xFF808183)
            )
        }
    }
}

@Composable
fun AgeFieldWithSuffix(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }.take(2)
                onValueChange(filtered)
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            ),
            cursorBrush = SolidColor(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Transparent, RoundedCornerShape(15.dp))
                .border(1.dp, Color(0xFF1E1E1E), RoundedCornerShape(15.dp))
                .padding(horizontal = 16.dp)
                .padding(top = 18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = "Enter your age",
                                fontSize = 13.sp,
                                fontFamily = alexandriaFontFamily,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                    if (value.isNotEmpty()) {
                        Text(
                            text = if (value == "1") "year-old" else "years-old",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = sansationFontFamily
                        )
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .offset(x = 10.dp, y = 6.dp)
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = "Age",
                fontSize = 13.sp,
                fontFamily = sansationFontFamily,
                color = Color(0xFF808183)
            )
        }
    }
}

@Composable
fun InterestInputField(
    token: String,
    interest: String,
    onValueChange: (String) -> Unit,
    selectedInterests: MutableList<Interest>,
    viewModel: InterestViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val suggestions by viewModel.searchResults.collectAsState()
    var justSelected by remember { mutableStateOf(false) }
    val dropdownHeight = (suggestions.size.coerceAtMost(2) * 48).dp
    val listState = rememberLazyListState()

    LaunchedEffect(interest) {
        if (justSelected) {
            justSelected = false
            return@LaunchedEffect
        }

        if (interest.length >= 2) {
            viewModel.searchInterests(token, interest)
            expanded = true
        } else {
            expanded = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TypeField(
            value = interest,
            onValueChange = {
                onValueChange(it)
                if (it.length >= 2) expanded = true
            },
            label = "Interest",
            placeholder = "Type something you’re interested in",
            height = 78.dp,
            multiline = true
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .offset(y = -dropdownHeight)
                .offset(y = -78.dp)
        ) {
            if (expanded && suggestions.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 150.dp)
                        .background(Color.White)
                        .border(1.dp, Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                ) {
                    items(suggestions.filterNot { it in selectedInterests }) { suggestion ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = suggestion.name,
                                    fontFamily = sansationFontFamily,
                                    color = Color.Black
                                )
                            },
                            onClick = {
                                justSelected = true
                                onValueChange(suggestion.name)
                                expanded = false
                            }
                        )
                    }
                }
                val scrollbarHeightRatio =
                    (listState.layoutInfo.viewportSize.height.toFloat() /
                            listState.layoutInfo.totalItemsCount.coerceAtLeast(1) /
                            48f)

                val visibleItemCount = listState.layoutInfo.visibleItemsInfo.size
                val totalItems = listState.layoutInfo.totalItemsCount
                val firstVisibleIndex = listState.firstVisibleItemIndex

                if (totalItems > visibleItemCount) {
                    val scrollbarHeightFraction = visibleItemCount.toFloat() / totalItems
                    val scrollbarOffsetFraction = firstVisibleIndex.toFloat() / totalItems

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .fillMaxHeight()
                            .width(4.dp)
                            .padding(end = 2.dp)
                            .background(Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp * scrollbarHeightFraction.coerceIn(0.05f, 1f))
                                .offset(y = 150.dp * scrollbarOffsetFraction)
                                .background(Color.Gray, shape = RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }

        if (selectedInterests.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedInterests.forEach { interestItem ->
                    InputChip(
                        selected = true,
                        onClick = {},
                        label = { Text(interestItem.name, color = Color.White) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                modifier = Modifier.clickable {
                                    selectedInterests.remove(interestItem)
                                }
                            )
                        },
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = OrangePrimary
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    EditProfileScreen (
        onBackClick = {},
        token = "preview_token_123",
        onDoneClick = {}
    )
}