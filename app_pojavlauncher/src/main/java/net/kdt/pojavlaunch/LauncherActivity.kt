package net.kdt.pojavlaunch

import android.content.Context

import android.content.SharedPreferences

import android.os.Bundle

import android.widget.Toast

import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent

import androidx.compose.foundation.Image

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.blur

import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import net.kdt.pojavlaunch.prefs.LauncherPreferences

import net.kdt.pojavlaunch.utils.FileUtils

class MainActivity : ComponentActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)

// Инициализация папок (родной код MojoLauncher)

        try {

            FileUtils.initMinecraftDirectory()

        } catch (e: Exception) {

            e.printStackTrace()

        }

        setContent {

            KsuLauncherTheme()

        }

    }

    @Composable

    fun KsuLauncherTheme() {

        MaterialTheme {

            MainScreen()

        }

    }

    @Composable

    fun MainScreen() {

        val statusText = remember { mutableStateOf("Готов к запуску") }

        val logText = remember { mutableStateOf("") }

        val progressValue = remember { mutableStateOf(0f) }

        val displayName = prefs.getString("username", "")

        val token = prefs.getString("access_token", "")

        Box(modifier = Modifier.fillMaxSize()) {

// ЗАМЕНИ `R.drawable.bg` НА ИМЯ ТВОЕЙ КАРТИНКИ (например, my_bg)

            Image(

                    painter = painterResource(id = R.drawable.bg),

                    contentDescription = "Background",

                    modifier = Modifier.fillMaxSize(),

                    contentScale = ContentScale.Crop

            )

// Затемнение

            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

// Стеклянные эффекты

            Box(

                    modifier = Modifier

                            .size(300.dp)

                            .align(Alignment.TopStart)

                            .offset(x = (-100).dp, y = (-100).dp)

                            .background(Color(0xFF64FFDA).copy(alpha = 0.2f))

                            .blur(80.dp)

            )

            Box(

                    modifier = Modifier

                            .size(400.dp)

                            .align(Alignment.BottomEnd)

                            .offset(x = 150.dp, y = 150.dp)

                            .background(Color(0xFFE0E7FF).copy(alpha = 0.2f))

                            .blur(100.dp)

            )

            Column(

                    modifier = Modifier.fillMaxSize().padding(24.dp),

                    horizontalAlignment = Alignment.CenterHorizontally,

                    verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {

                GlassCard(

                        title = "KsuLauncher",

                        subtitle = "Ultimate Java Edition"

                )

                Spacer(modifier = Modifier.height(20.dp))

                if (displayName.isNotEmpty()) {

                    GlassCard(title = "Привет, $displayName!", subtitle = "")

                } else {

// Здесь можно вставить кнопку "Авторизация", но пока оставим текст

                    Text(text = "Авторизуйтесь в Ely.by", color = Color.Gray)

                }

// ГЛАВНАЯ КНОПКА ИГРАТЬ

                Button(

                        onClick = {

                if (token.isNotEmpty()) {

// Запуск Майнкрафт (родной код Mojo/Pojav)

                    LauncherPreferences.PREF_USERNAME = displayName

                    LauncherPreferences.PREF_ACCESS_TOKEN = token

                    PojavLauncher.startMinecraft()

                    Toast.makeText(this@MainActivity, "Запуск Майнкрафт...", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this@MainActivity, "Сначала авторизуйтесь!", Toast.LENGTH_SHORT).show()

                }

},

                modifier = Modifier.fillMaxWidth().height(60.dp),

                        shape = RoundedCornerShape(30.dp),

                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f))

) {

                    Text(text = "▶ ИГРАТЬ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                }

// Статус и прогресс

                GlassCard(title = statusText.value, subtitle = "") {

                    LinearProgressIndicator(

                            progress = { progressValue.value },

                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),

                            color = Color(0xFF64FFDA),

                            trackColor = Color.White.copy(alpha = 0.2f)

                    )

                }

// Лог

                Box(

                        modifier = Modifier

                                .fillMaxWidth()

                                .weight(1f)

                                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))

                                .padding(12.dp)

                ) {

                    LazyColumn {

                        item {

                            Text(

                                    text = logText.value,

                                    color = Color(0xFF00FF00),

                                    fontSize = 13.sp,

                                    lineHeight = 18.sp

                            )

                        }

                    }

                }

            }

        }

    }

    @Composable

    fun GlassCard(title: String, subtitle: String = "", content: @Composable (() -> Unit)? = null) {

        Surface(

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(24.dp),

                color = Color.White.copy(alpha = 0.08f),

                shadowElevation = 8.dp

        ) {

            Column(

                    modifier = Modifier.padding(24.dp),

                    horizontalAlignment = Alignment.CenterHorizontally

            ) {

                Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)

                if (subtitle.isNotEmpty()) {

                    Text(text = subtitle, fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))

                }

                content?.invoke()

            }

        }

    }

}