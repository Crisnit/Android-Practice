package com.example.androidpractice.content

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.R
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidpractice.DetailsActivity

@SuppressLint("LocalContextResourcesRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListActivityScreen(){
    val context = LocalContext.current
    val itemsArray = listOf(
        ItemData("Mercedes-Benz", "W210",
            "1995—2003", "бизнес-класс",
            "переднемоторная, заднеприводная\n" +
                    "переднемоторная, полноприводная",
            "Второе поколение легковых автомобилей E-класса немецкой торговой марки " +
                    "Mercedes-Benz. Пришло на смену Mercedes-Benz W124 и производилось с 1995 по 2003 " +
                    "год. Автомобиль выпускался в кузове седан (W210) по 2002 год и универсал (S210) " +
                    "по 2003 год. Впервые дизайнеры компании Mercedes-Benz использовали в серийных " +
                    "автомобилях двойные овальные фары, определившие облик целого ряда моделей фирмы. " +
                    "второе поколение легковых автомобилей E-класса немецкой торговой марки Mercedes-Benz. " +
                    "Пришло на смену Mercedes-Benz W124 и производилось с 1995 по 2003 год. " +
                    "Автомобиль выпускался в кузове седан (W210) по 2002 год и универсал (S210) по " +
                    "2003 год. Впервые дизайнеры компании Mercedes-Benz использовали в серийных автомобилях " +
                    "двойные овальные фары, определившие облик целого ряда моделей фирмы.",
            "w210.png"),
        ItemData("BMW AG", "E39", "1996—2004",
            "Бизнес-класс", "переднемоторная, заднеприводная",
            "Четвертое поколение легковых автомобилей немецкой марки BMW 5 серии, " +
                    "которое выпускалось с 1995 по 2004 год включительно. Предшественником данного " +
                    "поколения были автомобили поколения E34, а ему на смену пришло поколение Е60 " +
                    "(в 2003 году), и BMW F10 (в 2010 году) в совершенно новом стиле BMW. Базовой в " +
                    "семействе была модель 520i. Машина имела двигатель объемом 2.0 л и мощностью 150 л.с., " +
                    "после рестайлинга объем вырос до 2.2 литра, а мощность до 170 л.с. " +
                    "Спортивная версия BMW M5 была выпущена в 1998 году. Она оснащалась восьмицилиндровым " +
                    "двигателем S62 мощностью 400 л.с., который был сделан на базе двигателя предыдущего " +
                    "поколения BMW M62. Все автомобили, кроме BMW 535 и BMW M5, были доступны в кузовах " +
                    "седан и универсал. BMW 535 и BMW M5 выпускались только в кузове седан.",
            "e39.png"),
        ItemData("Volkswagen","Phaeton","2002—2016",
            " Представительский","переднемоторная, переднеприводная\n" +
                    "переднемоторная, полноприводная",
            "первый автомобиль представительского (F) класса за всю 70-летнюю историю " +
                    "Volkswagen. С 2002 по 2016 год производилась ручная сборка на заводе " +
                    "Gläserne Manufaktur в Дрездене в Германии. Прототип автомобиля, под рабочим " +
                    "названием Concept D, впервые был представлен в 1999 году на Франкфуртском " +
                    "автосалоне. С апреля 2007 года является первым автомобилем высшего класса, " +
                    "который с дизельным двигателем V6-TDI (V-образный турбодизель) выполнил нормы " +
                    "экологического стандарта Евро-5.",
            "phaeton.png")
    )
    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) { items(itemsArray) {
        item ->
            val context = LocalContext.current
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("CAR_MANUFACTURER", item.manufacturer)
            intent.putExtra("CAR_MODEL", item.model)
            intent.putExtra("CAR_YEARS", item.productionYears)
            intent.putExtra("CAR_CLASS", item.carClass)
            intent.putExtra("CAR_POWERTRAIN", item.powertrainLayout)
            intent.putExtra("CAR_DESCRIPTION", item.description)
            intent.putExtra("CAR_IMAGE_PATH", item.imagePath)
            Button( onClick = {
                context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF9393AB)),
                shape = RectangleShape,
            ) {
                    val carAsset = context.assets
                    val img = BitmapFactory.decodeStream(carAsset.open(item.imagePath))
                    Image(
                        bitmap = img.asImageBitmap(),
                        modifier = Modifier.width(100.dp),
                        contentDescription = "Car image"
                    )
                    Text(
                        modifier = Modifier.padding(0.dp)
                            .fillMaxWidth(),
                        text = "${item.manufacturer} ${item.model}",
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
            }
        }
    }
}









