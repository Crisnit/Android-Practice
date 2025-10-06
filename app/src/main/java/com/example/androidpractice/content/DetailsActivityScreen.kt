package com.example.androidpractice.content

import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.ScrollView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidpractice.DetailsActivity
import com.example.androidpractice.MainActivity
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
import org.intellij.lang.annotations.JdkConstants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsActivityScreen (carData: ItemData,
                           onBackClick: () -> Unit) {
    AndroidPracticeTheme {
        val context = LocalContext.current
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Row (verticalAlignment = Alignment.CenterVertically) {  val intent = Intent(context, MainActivity::class.java)
                            IconButton( onClick =  onBackClick ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                            Text(text = "Детали")
                        }
                    },
                    colors = TopAppBarColors(Color(0xFF81A681),
                        Color(0xFF81A681),
                        Color(0xFFFFFFFF),
                        Color(0xFFFFFFFF),
                        Color(0xFFFFFFFF)),
                    actions = {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_SUBJECT, "Карточка")
                                    .putExtra(Intent.EXTRA_TEXT, "")
                                context.startActivity(intent)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share"
                            )
                        }
                    }
                )
            }){ innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    val carAsset = context.assets
                    val img = BitmapFactory.decodeStream(carAsset.open(carData.imagePath))
                    Image(
                        bitmap = img.asImageBitmap(),
                        modifier = Modifier.height(200.dp).width(300.dp),
                        contentDescription = "Car image"
                    )
                    CarParameterText("Название автомобмля","${carData.manufacturer} ${carData.model}")

                    CarParameterText("Годы производства",carData.productionYears)

                    CarParameterText("Класс",carData.carClass)

                    CarParameterText("Компоновка",carData.powertrainLayout)

                    CarParameterText("Описание",carData.description)
                }
            }
    }
}

@Composable
fun CarParameterText (title: String, paragraph: String) {
    Column{
        Text(modifier = Modifier.padding(10.dp,10.dp,10.dp, 5.dp)
            .fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = Bold)
        Text(
            modifier = Modifier.padding(10.dp, 0.dp, 10.dp,10.dp)
                .fillMaxWidth(),
            text = paragraph,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }

}