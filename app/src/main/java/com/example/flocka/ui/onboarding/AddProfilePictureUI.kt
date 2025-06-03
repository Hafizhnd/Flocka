package com.example.flocka.ui.onboarding

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.viewmodel.auth.AuthViewModel
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import java.io.File
import java.util.Objects

@Composable
fun AddProfilePictureUI(
    token: String,
    authViewModel: AuthViewModel = viewModel(),
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    fun createImageUri(): Uri {
        val imageFile = File(context.cacheDir, "temp_camera_image_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            context.packageName + ".provider",
            imageFile
        )
    }

    var tempCameraUri: Uri? = null

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                galleryLauncher.launch("image/*")
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri = tempCameraUri
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                tempCameraUri = createImageUri()
                cameraLauncher.launch(tempCameraUri)
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Choose an option") },
            text = {
                Column {
                    Text(
                        text = "Take Photo with Camera",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                showDialog = false
                            }
                            .padding(vertical = 12.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Choose from Gallery",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    Manifest.permission.READ_MEDIA_IMAGES
                                } else {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                                galleryPermissionLauncher.launch(permission)
                                showDialog = false
                            }
                            .padding(vertical = 12.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(authViewModel.uploadResult) {
        authViewModel.uploadResult?.let { result ->
            isLoading = false
            if (result.isSuccess) {
                onSaveSuccess()
            }
            authViewModel.uploadResult = null
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BluePrimary)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(18.dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start){
            Text(
                "Set up your account",
                fontFamily = alexandriaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
            )
        }
        Text(
            "Complete your account set up by adding an engaging profile picture so you will leave a lasting impression in the online community!",
            fontFamily = alexandriaFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 11.sp,
            color = Color(0xFFD1D0D0),
            modifier = Modifier.padding(start = 3.dp, top = 8.dp)
        )
        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(Color(0xFFCED1D7))
                .clickable {
                    showDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_photo_camera),
                    contentDescription = "Upload Picture",
                    modifier = Modifier.size(180.dp)
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                imageUri?.let {
                    isLoading = true
                    authViewModel.uploadProfilePicture(context, token, it)
                }
            },
            enabled = imageUri != null && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary, contentColor = Color.White)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Save", fontSize = 20.sp, fontFamily = sansationFontFamily, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview
@Composable
fun AddProfilePicturePreview(){
    AddProfilePictureUI(
        token = "preview_token",
        onSaveSuccess = {}
    )
}