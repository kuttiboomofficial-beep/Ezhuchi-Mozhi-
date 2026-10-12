package com.tavrixlabs.ezhuchimozhi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.json.JSONArray

data class Quote(val text: String, val category: String)

private val quoteList = listOf(
    Quote("உன்னை நேசிப்பது என் விருப்பம்… உன் நினைவில் வாழ்வது என் பழக்கம்.", "காதல்"),
    Quote("சில மௌனங்கள் ஆயிரம் வார்த்தைகளைவிட வலிமையானவை.", "காதல் தோல்வி"),
    Quote("விழுந்த இடம் முக்கியமில்லை; மீண்டும் எழும் துணிவுதான் முக்கியம்.", "தன்னம்பிக்கை"),
    Quote("நேரம் எல்லாவற்றையும் மாற்றும்; பொறுமை நம்மை வலிமையாக்கும்.", "வாழ்க்கை"),
    Quote("உண்மையான அன்பு உரிமை கேட்காது; மனதைப் புரிந்துகொள்ளும்.", "காதல்"),
    Quote("உன்னை இழந்த வலி, உன்னை நீயே கண்டுபிடிக்க உதவட்டும்.", "காதல் தோல்வி"),
    Quote("மெதுவாக சென்றாலும் பரவாயில்லை; நின்றுவிடாதே.", "தன்னம்பிக்கை"),
    Quote("ஒவ்வொரு நாளும் ஒரு புதிய தொடக்கத்திற்கான வாய்ப்பு.", "வாழ்க்கை"),
    Quote("சொல்லாத உணர்வுகளுக்கும் இதயத்தில் ஒரு மொழி உண்டு.", "காதல்"),
    Quote("பிரிவு முடிவல்ல; சில நேரம் அது நம்மை நமக்கே திருப்பித் தரும்.", "காதல் தோல்வி"),
    Quote("உன் முயற்சி அமைதியாக இருக்கட்டும்; வெற்றி பேசட்டும்.", "தன்னம்பிக்கை"),
    Quote("சிறிய மகிழ்ச்சிகளையும் ரசிக்கத் தெரிந்தால் வாழ்க்கை அழகாகும்.", "வாழ்க்கை")
) + extraQuotes

private val purple = Color(0xFF63347E)
private val deepPurple = Color(0xFF281B43)
private val cream = Color(0xFFFFF7EF)
private val gold = Color(0xFFFFD580)
private val categories = listOf(
    "அனைத்தும்",
    "காதல்",
    "காதல் தோல்வி",
    "வாழ்க்கை",
    "தன்னம்பிக்கை",
    "நட்பு",
    "குடும்பம்",
    "ஊக்கம்",
    "தத்துவம்"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EzhuchiApp(
                onShare = { text ->
                    startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, text)
                            },
                            "கவிதையை பகிரவும்"
                        )
                    )
                },
                onYouTube = {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://youtube.com/@ezhuchitamilpk")
                        )
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EzhuchiApp(
    onShare: (String) -> Unit,
    onYouTube: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboard = LocalClipboardManager.current

    val prefs = remember {
        context.getSharedPreferences("ezhuchi_prefs", 0)
    }

    val saved = remember {
        mutableStateListOf<String>().apply {
            val arr = runCatching {
                JSONArray(prefs.getString("favorites", "[]"))
            }.getOrNull()

            if (arr != null) {
                for (i in 0 until arr.length()) {
                    add(arr.optString(i))
                }
            }
        }
    }

    var page by remember { mutableStateOf("Home") }
    var selected by remember { mutableStateOf("அனைத்தும்") }
    var search by remember { mutableStateOf("") }
    var createText by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = purple,
            secondary = gold,
            background = cream,
            surface = Color.White
        )
    ) {
        Scaffold(
            containerColor = cream,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = cream
                    ),
                    title = {
                        Column {
                            Text(
                                "எழுச்சி மொழி",
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                "உன் உணர்வுகளுக்கு ஓர் எழுத்து 💜",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            page = "Settings"
                        }) {
                            Text("⚙️")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    val destinations = listOf(
                        Triple("Home", "🏠", "Home"),
                        Triple("Favorites", "♡", "Favorites"),
                        Triple("Create", "✍️", "Create"),
                        Triple("Premium", "👑", "Premium")
                    )

                    destinations.forEach { (name, symbol, label) ->
                        NavigationBarItem(
                            selected = page == name,
                            onClick = { page = name },
                            icon = { Text(symbol) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { padding ->

            when (page) {
                "Create" -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "✍️ உன் கவிதையை உருவாக்கு",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = createText,
                            onValueChange = { createText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            placeholder = {
                                Text("உன் மனதில் உள்ளதை இங்கே எழுது...")
                            },
                            minLines = 6
                        )

                        Button(
                            onClick = {
                                if (createText.isNotBlank()) {
                                    clipboard.setText(
                                        AnnotatedString(createText)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = createText.isNotBlank()
                        ) {
                            Text("📋 நகலெடு (Copy)")
                        }

                        OutlinedButton(
                            onClick = {
                                if (createText.isNotBlank()) {
                                    onShare(createText)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = createText.isNotBlank()
                        ) {
                            Text("↗️ பகிர் (Share)")
                        }
                    }
                }

                "Premium" -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("👑", style = MaterialTheme.typography.displayLarge)
                        Text(
                            "Ezhuchi Premium",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "விளம்பரமில்லா அனுபவம், கூடுதல் வடிவமைப்புகள் " +
                                "மற்றும் Status உருவாக்கும் வசதிகள் " +
                                "எதிர்காலத்தில் இங்கே சேர்க்கப்படும்.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("Premium வாங்கும் வசதி இன்னும் இணைக்கப்படவில்லை.")
                    }
                }

                "Settings" -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "⚙️ அமைப்புகள்",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text("எழுச்சி மொழி — Tamil Status & Quotes")
                        Text("மொழித் தேர்வு அடுத்த கட்டத்தில் சேர்க்கப்படும்.")
                        Text("தற்போதைய கவிதைகள்: ${quoteList.size}")
                        TextButton(onClick = { page = "Home" }) {
                            Text("முகப்புக்குத் திரும்பு")
                        }
                    }
                }

                else -> {
                    val filtered = quoteList.filter { quote ->
                        val matchesPage =
                            if (page == "Favorites") {
                                saved.contains(quote.text)
                            } else {
                                selected == "அனைத்தும்" ||
                                    quote.category == selected
                            }

                        matchesPage &&
                            quote.text.contains(search, ignoreCase = true)
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (page == "Home") {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                listOf(deepPurple, purple)
                                            ),
                                            RoundedCornerShape(26.dp)
                                        )
                                        .padding(22.dp)
                                ) {
                                    Text(
                                        "🌱 இன்றைய நாள் உனக்கானது ✨",
                                        color = gold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        "எழுச்சி மொழி",
                                        color = Color.White,
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        "உன் உணர்வுகளுக்கு ஓர் எழுத்து 💜",
                                        color = Color.White
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    Button(
                                        onClick = onYouTube,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = gold,
                                            contentColor = deepPurple
                                        )
                                    ) {
                                        Text("▶ Ezhuchi Tamil வீடியோக்கள்")
                                    }
                                }
                            }
                        } else {
                            item {
                                Text(
                                    "❤️ பிடித்த கவிதைகள்",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = search,
                                onValueChange = { search = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text("🔍 கவிதையைத் தேடு...")
                                },
                                singleLine = true
                            )
                        }

                        if (page == "Home") {
                            item {
                                Text(
                                    "🗂️ வகைகளைத் தேர்ந்தெடு",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            item {
                                Row(
                                    modifier = Modifier.horizontalScroll(
                                        rememberScrollState()
                                    ),
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {
                                    categories.forEach { category ->
                                        FilterChip(
                                            selected = selected == category,
                                            onClick = { selected = category },
                                            label = { Text(category) }
                                        )
                                    }
                                }
                            }

                            item {
                                Text(
                                    "🔥 கவிதைகள்  ·  ${filtered.size}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (filtered.isEmpty()) {
                            item {
                                Text(
                                    if (page == "Favorites") {
                                        "இன்னும் பிடித்த கவிதைகள் சேர்க்கப்படவில்லை."
                                    } else {
                                        "கவிதைகள் கிடைக்கவில்லை. வேறு சொல்லைத் தேடு."
                                    },
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        items(filtered, key = { it.text }) { quote ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                )
                            ) {
                                Column(
                                    Modifier.padding(18.dp)
                                ) {
                                    Text(
                                        "✨ ${quote.category}",
                                        color = purple,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    Text(
                                        quote.text,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement =
                                            Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextButton(
                                            onClick = {
                                                if (saved.contains(quote.text)) {
                                                    saved.remove(quote.text)
                                                } else {
                                                    saved.add(quote.text)
                                                }

                                                val arr = JSONArray()
                                                saved.forEach { arr.put(it) }
                                                prefs.edit()
                                                    .putString(
                                                        "favorites",
                                                        arr.toString()
                                                    )
                                                    .apply()
                                            }
                                        ) {
                                            Text(
                                                if (saved.contains(quote.text)) {
                                                    "❤️ பிடித்தது"
                                                } else {
                                                    "♡ பிடித்தவை"
                                                }
                                            )
                                        }

                                        TextButton(
                                            onClick = {
                                                clipboard.setText(
                                                    AnnotatedString(quote.text)
                                                )
                                            }
                                        ) {
                                            Text("📋 நகலெடு")
                                        }

                                        TextButton(
                                            onClick = { onShare(quote.text) }
                                        ) {
                                            Text("↗️ பகிர்")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
