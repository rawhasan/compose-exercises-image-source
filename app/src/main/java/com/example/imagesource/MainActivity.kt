package com.example.imagesource

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {
    private var imageUriState = mutableStateOf<Uri?>(null)

    private val selectImageLauncher = registerForActivityResult(GetContent()) { uri ->
        imageUriState.value = uri
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageSourceActivityScreen()
        }
    }

    @Composable
    fun ImageSourceActivityScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            // Solution - 1 by Philip (SO) - works after adding (.value)
//            Image(
//                painter = if (imageUriState.value != null) {
//                    rememberImagePainter(
//                        imageUriState.value
//                    )
//                } else {
//                    painterResource(id = R.drawable.blank_profile_picture)
//                },
//                contentDescription = "profile image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxWidth()
//            )


            // Solution - 2 by Philip (SO) - Doesn't work
//            Image(
//                painter = rememberImagePainter(
//                    imageUriState.value,
//                    builder = {
//                        placeholder(R.drawable.blank_profile_picture)
//                    }
//                ),
//                contentDescription = "profile image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxWidth()
//            )


//            Solution by Gabriele (SO) - Doesn't work
//            val painter = rememberImagePainter(
//                imageUriState.value,
//                builder = {
//                    placeholder(R.drawable.blank_profile_picture)
//                }
//            )
//            Image(
//                painter = painter,
//                contentDescription = "profile image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxWidth()
//            )


//            Bug fixed (.value): Works
//            if (imageUriState.value != null) {
//                Image(
//                    painter = rememberImagePainter(imageUriState.value),
//                    contentDescription = "Profile image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//            } else {
//                Image(
//                    painter = rememberImagePainter(R.drawable.blank_profile_picture),
//                    contentDescription = "Profile image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }


//                if (imageUriState != null) {
//                    painter = rememberImagePainter(imageUriState.value)
//                } else {
//                    painter = rememberImagePainter( R.drawable.blank_profile_picture)
//                },
//
//


            // Bug fixed: Added ".value" - Works now!
            Image(
                painter = rememberImagePainter(
                    if (imageUriState.value != null) {
                        imageUriState.value
                    } else {
                        R.drawable.blank_profile_picture
                    }
                ),
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { }) {
                    Text("Web")
                }
                Button(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = { selectImageLauncher.launch("image/*") }) {
                    Text("Gallery")
                }
                Button(onClick = { }) {
                    Text("Camera")
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //ImageSourceActivityScreen()
}