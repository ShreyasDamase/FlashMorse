package com.vanguard.flashmorse.presentation.guide

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.flashmorse.R
import com.vanguard.flashmorse.ui.theme.FlashOrange
import kotlinx.coroutines.launch

data class MorseItem(val char: String, val morse: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MorseGuideScreen(
    onBackClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()
    
    val tabs = listOf(
        stringResource(R.string.guide_tab_letters),
        stringResource(R.string.guide_tab_numbers),
        stringResource(R.string.guide_tab_symbols)
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val letters = listOf(
        MorseItem("A", "• -"), MorseItem("B", "- • • •"), MorseItem("C", "- • - •"),
        MorseItem("D", "- • •"), MorseItem("E", "•"), MorseItem("F", "• • - •"),
        MorseItem("G", "- - •"), MorseItem("H", "• • • •"), MorseItem("I", "• •"),
        MorseItem("J", "• - - -"), MorseItem("K", "- • -"), MorseItem("L", "• - • •"),
        MorseItem("M", "- -"), MorseItem("N", "- •"), MorseItem("O", "- - -"),
        MorseItem("P", "• - - •"), MorseItem("Q", "- - • -"), MorseItem("R", "• - •"),
        MorseItem("S", "• • •"), MorseItem("T", "-"), MorseItem("U", "• • -"),
        MorseItem("V", "• • • -"), MorseItem("W", "• - -"), MorseItem("X", "- • • -"),
        MorseItem("Y", "- • - -"), MorseItem("Z", "- - • •")
    )

    val numbers = listOf(
        MorseItem("1", "• - - - -"), MorseItem("2", "• • - - -"), MorseItem("3", "• • • - -"),
        MorseItem("4", "• • • • -"), MorseItem("5", "• • • • •"), MorseItem("6", "- • • • •"),
        MorseItem("7", "- - • • •"), MorseItem("8", "- - - • •"), MorseItem("9", "- - - - •"),
        MorseItem("0", "- - - - -")
    )

    val symbols = listOf(
        MorseItem(".", "• - • - • -"), MorseItem(",", "- - • • - -"), MorseItem("?", "• • - - • •"),
        MorseItem("'", "• - - - - •"), MorseItem("!", "- • - • - -"), MorseItem("/", "- • • - •"),
        MorseItem("(", "- • - - •"), MorseItem(")", "- • - - • -"), MorseItem("&", "• - • • •"),
        MorseItem(":", "- - - • • •"), MorseItem(";", "- • - • - •"), MorseItem("=", "- • • • -"),
        MorseItem("+", "• - • - •"), MorseItem("-", "- • • • • -"), MorseItem("_", "• • - - • -"),
        MorseItem("\"", "• - • • - •"), MorseItem("$", "• • • - • • -"), MorseItem("@", "• - - • - •")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.guide_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back), tint = colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onBackground
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = colorScheme.background,
                contentColor = colorScheme.onBackground,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                        color = FlashOrange
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                        selectedContentColor = FlashOrange,
                        unselectedContentColor = colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(colorScheme.background)
            ) { page ->
                val itemsList = when (page) {
                    0 -> letters
                    1 -> numbers
                    else -> symbols
                }
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(itemsList) { item ->
                        MorseGridItem(item, colorScheme.surface, colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
fun MorseGridItem(
    item: MorseItem,
    cardBgColor: Color,
    charTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = cardBgColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.char,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = charTextColor,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.morse,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
