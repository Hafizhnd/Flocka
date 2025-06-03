package com.example.flocka.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flocka.viewmodel.auth.AuthViewModel
import com.example.flocka.R
import com.example.flocka.data.model.Interest
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.interest.InterestViewModel


@Composable
fun SetUpAccountUI (
    username: String,
    onDoneClick: () -> Unit,
    token: String,
    authViewModel: AuthViewModel = viewModel(),
    interestViewModel: InterestViewModel = viewModel()
){

    var firstname by remember { mutableStateOf("") }
    var lastname by remember {mutableStateOf("") }
    var profession by remember {mutableStateOf("") }
    var gender by remember {mutableStateOf("Select Gender") }
    var age by remember {mutableStateOf("") }
    var bio by remember {mutableStateOf("") }
    var interest by remember { mutableStateOf("") }
    val selectedInterests = remember { mutableStateListOf<Interest>() }

    var showError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.updateProfileResult) {
        authViewModel.updateProfileResult?.let { result ->
            isLoading = false
            result.onSuccess {
                onDoneClick()
            }
            result.onFailure { exception ->
                showError = exception.message ?: "An unknown error occurred."
            }
            authViewModel.updateProfileResult = null // Reset state
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BluePrimary)
    ){
        Column(
            modifier = Modifier
                .padding(vertical = 45.dp, horizontal = 30.dp)
                .fillMaxSize()
        ) {
            Text(
                "Set up your account",
                fontFamily = alexandriaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 3.dp, top = 8.dp)
            )
            Text(
                "Complete your account set up by providing your proper biography info",
                fontFamily = alexandriaFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 11.sp,
                color = Color(0xFFD1D0D0),
                modifier = Modifier.padding(start = 3.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.padding(top = 10.dp))

            TypeField(
                value = username,
                onValueChange = { },
                label = "Username",
                placeholder = "",
                enabled = false
            )
            TypeField(
                value = firstname,
                onValueChange = { firstname = it },
                label = "First Name",
                placeholder = "Enter your first name"
            )
            TypeField(
                value = lastname,
                onValueChange = { lastname = it },
                label = "Last Name",
                placeholder = "Enter your last name"
            )
            TypeField(
                value = profession,
                onValueChange = { profession = it },
                label = "Profession",
                placeholder = "Choose your profession"
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GenderDropdown(
                    value = gender,
                    onValueChange = { gender = it },
                    modifier = Modifier.weight(1f)
                )

                AgeFieldWithSuffix(
                    value = age,
                    onValueChange = { age = it },
                    modifier = Modifier.weight(1f)
                )
            }
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
                interest = interest,
                onValueChange = { interest = it },
                selectedInterests = selectedInterests,
                viewModel = interestViewModel
            )

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Button(
                onClick = {
                    isLoading = true
                    val fullName = "$firstname $lastname".trim()
                    val interestIds = selectedInterests.map { it.id }
                    authViewModel.updateProfile(
                        token = token,
                        username = username,
                        name = fullName,
                        profession = profession,
                        gender = gender,
                        age = age,
                        bio = bio,
                        interestIds = interestIds
                    )
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary, contentColor = Color.White)
            ) {
                Text("Done", fontSize = 20.sp, fontFamily = sansationFontFamily, fontWeight = FontWeight.Bold)
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
                color = Color.White
            ),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color.Transparent, RoundedCornerShape(15.dp))
                .border(1.dp, Color.White, RoundedCornerShape(15.dp))
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
                color = Color.White
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
                    .border(1.dp, Color.White, RoundedCornerShape(15.dp))
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
                        color = if (value == "Select Gender") Color.Gray else Color.White,
                        fontFamily = sansationFontFamily,
                        fontSize = 14.sp
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_dropdown),
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
                color = Color.White
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
                color = Color.White
            ),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Transparent, RoundedCornerShape(15.dp))
                .border(1.dp, Color.White, RoundedCornerShape(15.dp))
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
                color = Color.White
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
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
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

@Preview
@Composable
fun SetUpAccountPreview(){
    SetUpAccountUI(
        username = "preview_user",
        token = "preview_token",
        onDoneClick = {}
    )
}