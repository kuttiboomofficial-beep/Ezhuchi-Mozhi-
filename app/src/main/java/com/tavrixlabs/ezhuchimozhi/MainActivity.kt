package com.tavrixlabs.ezhuchimozhi

import android.content.ClipData
import android.content.Intent
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF63347E)
private val DeepPurple = Color(0xFF281843)
private val Cream = Color(0xFFFFF7EF)
private val Gold = Color(0xFFFFD580)
private val Pink = Color(0xFFE84A83)

private data class Quote(
    val text: String,
    val category: String
)

private val categories = listOf(
    "அனைத்தும்", "காதல்", "காதல் தோல்வி", "வாழ்க்கை",
    "தன்னம்பிக்கை", "நட்பு", "குடும்பம்", "ஊக்கம்", "தத்துவம்"
)

private val quotes = listOf(
    Quote("உன்னை உண்மையாக நேசிப்பவர்கள், உன் மௌனத்தையும் புரிந்துகொள்வார்கள்.", "காதல்"),
    Quote("நேசித்தவர் பிரிந்தாலும், நேசித்த நினைவுகள் பிரிவதில்லை.", "காதல் தோல்வி"),
    Quote("விழுவது தோல்வியல்ல; விழுந்த இடத்தில் கிடப்பதுதான் தோல்வி.", "தன்னம்பிக்கை"),
    Quote("வாழ்க்கை எப்போதும் எளிதல்ல; ஆனால் ஒவ்வொரு நாளும் ஒரு பாடம்.", "வாழ்க்கை"),
    Quote("உன் கனவுகளைச் சிறியதாக நினைக்காதே; உன் முயற்சியைப் பெரிதாக்கு.", "ஊக்கம்"),
    Quote("உண்மையான நட்பு தூரத்தால் குறைவதில்லை; காலத்தால் அழிவதில்லை.", "நட்பு"),
    Quote("உலகம் மாறினாலும், உனக்காகக் காத்திருக்கும் அன்பு குடும்பம்.", "குடும்பம்"),
    Quote("மௌனம் சில நேரங்களில் ஆயிரம் வார்த்தைகளைவிட வலிமையானது.", "தத்துவம்"),
    Quote("உன் கண்ணீரை மறைத்தாலும், உன் மனதை உன்னால் ஏமாற்ற முடியாது.", "காதல் தோல்வி"),
    Quote("மற்றவர்களுடன் போட்டி வேண்டாம்; நேற்றைய உன்னை விட இன்று உயர்ந்திரு.", "தன்னம்பிக்கை"),
    Quote("சில பிரிவுகள் வலிக்கும்; ஆனால் அவை நம்மை நமக்கே அறிமுகப்படுத்தும்.", "வாழ்க்கை"),
    Quote("நல்ல மனதைத் தேடாதே; முதலில் நீ நல்ல மனமாக இரு.", "தத்துவம்"),
    Quote("உன் மதிப்பை அறிந்தவனிடம் உன்னை நிரூபிக்க வேண்டியதில்லை.", "தன்னம்பிக்கை"),
    Quote("அன்பு என்பது அருகில் இருப்பதல்ல; தொலைவில் இருந்தும் துணையாக இருப்பது.", "காதல்"),
    Quote("கடினமான நாட்கள் உன்னை உடைக்க வரவில்லை; உன்னை உருவாக்க வருகின்றன.", "ஊக்கம்"),
    Quote("நண்பன் என்பவன் சிரிப்பில் உடன் இருப்பவன் மட்டும் அல்ல; கண்ணீரிலும் விலகாதவன்.", "நட்பு"),
    Quote("வீடு என்பது சுவர் அல்ல; உன்னை ஏற்றுக்கொள்ளும் இதயங்கள்.", "குடும்பம்"),
    Quote("காலம் பதில் சொல்லும்; அதுவரை பொறுமை உன் பலமாக இருக்கட்டும்.", "தத்துவம்"),
    Quote("உன் பயம் சொல்வதைக் கேட்காதே; உன் கனவு சொல்வதைக் கேள்.", "ஊக்கம்"),
    Quote("சில நினைவுகள் மறையாது; அவற்றுடன் வாழக் கற்றுக்கொள்கிறோம்.", "காதல் தோல்வி"),
    Quote("உன்னை நீ நம்பும் நாள் முதல் உன் பயணம் மாறத் தொடங்கும்.", "தன்னம்பிக்கை"),
    Quote("அன்பு கேட்டு வாங்குவது அல்ல; மனதாரக் கொடுக்கப்படுவது.", "காதல்"),
    Quote("வெற்றி மெதுவாக வந்தாலும் பரவாயில்லை; முயற்சி நின்றுவிடக்கூடாது.", "ஊக்கம்"),
    Quote("உண்மையான உறவுக்கு தினமும் விளக்கம் தேவையில்லை.", "குடும்பம்"),
    Quote("மன அமைதியை இழந்து பெறும் வெற்றி, முழுமையான வெற்றியல்ல.", "வாழ்க்கை"),
    Quote("உன் பாதை தனியாக இருந்தாலும், உன் இலக்கை மறந்துவிடாதே.", "ஊக்கம்"),
    Quote("பேசும் வார்த்தையைவிட, காயப்படுத்தாத மனம் அழகானது.", "தத்துவம்"),
    Quote("நல்ல நண்பர்கள் வாழ்க்கையின் அழகான வரம்.", "நட்பு"),
    Quote("உன் முயற்சியை இன்று யாரும் பார்க்காமல் இருக்கலாம்; நாளை அதன் பலனைப் பார்ப்பார்கள்.", "தன்னம்பிக்கை"),
    Quote("காதலில் தோற்றாலும், அன்பு காட்டிய உன் இதயம் தோற்றுவிடவில்லை.", "காதல் தோல்வி")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EzhuchiApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EzhuchiApp() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("ezhuchi_settings", android.content.Context.MODE_PRIVATE)
    }

    var page by remember { mutableStateOf("Home") }
    var selectedCategory by remember { mutableStateOf("அனைத்தும்") }
    var search by remember { mutableStateOf("") }
    var createText by remember { mutableStateOf("") }
    var darkTheme by remember { mutableStateOf(false) }

    val favorites = remember {
        mutableStateListOf<String>().apply {
            addAll(prefs.getStringSet("favorites", emptySet()) ?: emptySet())
        }
    }

    val background = if (darkTheme) Color(0xFF17121D) else Cream
    val cardColor = if (darkTheme) Color(0xFF2B2333) else Color.White
    val textColor = if (darkTheme) Color.White else DeepPurple

    fun toggleFavorite(text: String) {
        if (favorites.contains(text)) {
            favorites.remove(text)
        } else {
            favorites.add(text)
        }
        prefs.edit().putStringSet("favorites", favorites.toSet()).apply()
    }

    fun copyText(text: String) {
        val clipboard =
            context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("கவிதை", text))
        android.widget.Toast.makeText(
            context, "கவிதை நகலெடுக்கப்பட்டது", android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$text\n\n— எழுச்சி மொழி")
        }
        context.startActivity(Intent.createChooser(intent, "கவிதையைப் பகிர்"))
    }

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            containerColor = background,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("எழுச்சி மொழி", fontWeight = FontWeight.Bold)
                            Text(
                                "உன் உணர்வுகளுக்கு ஓர் எழுத்து 💜",
                                fontSize = 11.sp
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = { page = "Settings" }) {
                            Text("⚙️", fontSize = 22.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = background,
                        titleContentColor = textColor
                    )
                )
            },
            bottomBar = {
                NavigationBar(containerColor = cardColor) {
                    val destinations = listOf(
                        Triple("Home", "⌂", "Home"),
                        Triple("Favorites", "♡", "Favorites"),
                        Triple("Create", "✎", "Create"),
                        Triple("Premium", "♛", "Premium")
                    )

                    destinations.forEach { (destination, symbol, label) ->
                        NavigationBarItem(
                            selected = page == destination,
                            onClick = { page = destination },
                            icon = { Text(symbol, fontSize = 23.sp) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { padding ->
            when (page) {
                "Settings" -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text("அமைப்புகள்", fontSize = 24.sp, color = textColor)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dark theme", color = textColor)
                            Switch(
                                checked = darkTheme,
                                onCheckedChange = { darkTheme = it }
                            )
                        }
                        Text("எழுச்சி மொழி — Tamil Status & Quotes", color = textColor)
                        Text(
                            "கவிதைகளை நகலெடுத்து நண்பர்களுடன் பகிரலாம்.",
                            color = textColor
                        )
                        Button(onClick = { page = "Home" }) {
                            Text("முகப்புக்குத் திரும்பு")
                        }
                    }
                }

                "Premium" -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("👑", fontSize = 54.sp)
                        Text(
                            "Ezhuchi Premium",
                            fontSize = 26.sp,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Premium வசதி விரைவில் அறிமுகமாகும்.",
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "இப்போது எந்த payment-மும் இணைக்கப்படவில்லை.",
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                "Create" -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "உன் கவிதையை எழுது ✍️",
                            fontSize = 24.sp,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = createText,
                            onValueChange = { createText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            placeholder = { Text("உன் மனதில் இருப்பதை எழுது...") },
                            minLines = 6
                        )
                        Button(
                            onClick = { if (createText.isNotBlank()) copyText(createText) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📋 நகலெடு")
                        }
                        OutlinedButton(
                            onClick = { if (createText.isNotBlank()) shareText(createText) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("↗ பகிர்")
                        }
                    }
                }

                else -> {
                    val visibleQuotes = when (page) {
                        "Favorites" -> quotes.filter { favorites.contains(it.text) }
                        else -> quotes.filter { quote ->
                            (selectedCategory == "அனைத்தும்" ||
                                quote.category == selectedCategory) &&
                                quote.text.contains(search, ignoreCase = true)
                        }
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
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                listOf(DeepPurple, Purple, Color(0xFFB65D83))
                                            ),
                                            RoundedCornerShape(26.dp)
                                        )
                                        .padding(22.dp)
                                ) {
                                    Text(
                                        "🌅 இன்றைய நாள் உனக்கானது ✨",
                                        color = Gold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        "எழுச்சி மொழி",
                                        color = Color.White,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        "உன் உணர்வுகளுக்கு ஓர் எழுத்து 💜",
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                    Spacer(Modifier.height(14.dp))
                                    Text(
                                        "🌄 இயற்கை அழகுடன் உன் நாளைத் தொடங்கு",
                                        color = Color.White
                                    )
                                }
                            }

                            item {
                                OutlinedTextField(
                                    value = search,
                                    onValueChange = { search = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("🔍 கவிதையைத் தேடு...") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(28.dp)
                                )
                            }

                            item {
                                Text(
                                    "✨ இன்றைய கவிதை",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }

                            item {
                                QuoteCard(
                                    quote = quotes.first(),
                                    isFavorite = favorites.contains(quotes.first().text),
                                    cardColor = Brush.linearGradient(
                                        listOf(DeepPurple, Purple)
                                    ),
                                    textColor = Color.White,
                                    onFavorite = { toggleFavorite(quotes.first().text) },
                                    onCopy = { copyText(quotes.first().text) },
                                    onShare = { shareText(quotes.first().text) }
                                )
                            }

                            item {
                                Text(
                                    "▦ வகைகளைத் தேர்ந்தெடு",
                                    fontSize = 21.sp,
                                    color = textColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    categories.drop(1).chunked(2).forEach { pair ->
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            pair.forEach { category ->
                                                Card(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(76.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = cardColor
                                                    ),
                                                    onClick = { selectedCategory = category }
                                                ) {
                                                    Box(
                                                        Modifier
                                                            .fillMaxSize()
                                                            .padding(8.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            category,
                                                            color = textColor,
                                                            fontWeight = FontWeight.Bold,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                            if (pair.size == 1) {
                                                Spacer(Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Text(
                                    "🔥 பிரபலமான கவிதைகள்",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                        }

                        if (page == "Favorites") {
                            item {
                                Text(
                                    "❤️ பிடித்த கவிதைகள்",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                        }

                        items(visibleQuotes, key = { it.text }) { quote ->
                            QuoteCard(
                                quote = quote,
                                isFavorite = favorites.contains(quote.text),
                                cardColor = Brush.linearGradient(
                                    listOf(cardColor, cardColor)
                                ),
                                textColor = textColor,
                                onFavorite = { toggleFavorite(quote.text) },
                                onCopy = { copyText(quote.text) },
                                onShare = { shareText(quote.text) }
                            )
                        }

                        if (visibleQuotes.isEmpty()) {
                            item {
                                Text(
                                    if (page == "Favorites")
                                        "இன்னும் பிடித்த கவிதைகள் சேமிக்கப்படவில்லை."
                                    else
                                        "கவிதைகள் கிடைக்கவில்லை. வேறு வார்த்தையைத் தேடு.",
                                    color = textColor,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuoteCard(
    quote: Quote,
    isFavorite: Boolean,
    cardColor: Brush,
    textColor: Color,
    onFavorite: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(cardColor)
                .padding(18.dp)
        ) {
            Text(
                "✨ ${quote.category}",
                color = if (textColor == Color.White) Gold else Purple,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "“${quote.text}”",
                color = textColor,
                fontSize = 19.sp,
                lineHeight = 29.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "— எழுச்சி மொழி",
                color = textColor.copy(alpha = 0.75f),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onFavorite) {
                    Text(
                        if (isFavorite) "❤️ பிடித்தது" else "♡ பிடித்தவை",
                        color = if (textColor == Color.White) Color.White else Pink
                    )
                }
                TextButton(onClick = onCopy) {
                    Text("📋 நகலெடு", color = textColor)
                }
                TextButton(onClick = onShare) {
                    Text("↗ பகிர்", color = textColor)
                }
            }
        }
    }
}
