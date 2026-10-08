package com.example.wallpaper.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.filled.RocketLaunch
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.components.InterstitialAdHelper
import com.example.ui.components.StartAppBannerView
import com.example.wallpaper.data.WallpaperRepository
import com.example.wallpaper.model.AnimeWallpaper
import com.example.wallpaper.model.WallpaperCategory
import com.example.wallpaper.ui.components.AppUpdateDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeWallpaperScreen(
    repository: WallpaperRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val wallpapers by repository.wallpapers.collectAsState()
    val favorites by repository.favoriteIds.collectAsState()

    var selectedCategory by remember { mutableStateOf<String>(WallpaperCategory.ALL.displayName) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedWallpaper by remember { mutableStateOf<AnimeWallpaper?>(null) }
    var isSettingWallpaper by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(true) }

    val filteredList = wallpapers.filter { wp ->
        val matchesCategory = (selectedCategory == WallpaperCategory.ALL.displayName) || (wp.category == selectedCategory)
        val matchesSearch = searchQuery.isBlank() ||
                wp.title.contains(searchQuery, ignoreCase = true) ||
                wp.animeTitle.contains(searchQuery, ignoreCase = true) ||
                wp.characterName.contains(searchQuery, ignoreCase = true) ||
                wp.tags.any { it.contains(searchQuery, ignoreCase = true) }
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F111A))
    ) {
        // App Top Bar / Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "WALLPAPERS 4K",
                        color = Color(0xFFFF5376),
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF00E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "1,500+ UHD",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Text(
                    text = "Anime • KTM Duke 250/390 • Supercars • Animals • Fruits",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Update Badge Pill Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF5376), Color(0xFFFF9800))
                            )
                        )
                        .clickable { showUpdateDialog = true }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("open_update_dialog_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "Update Available",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "v2.0",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Favorites Count
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E2235))
                        .padding(horizontal = 9.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = Color(0xFFFF5376),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${favorites.size}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Duke 390, GT-R, Tiger, Strawberry, Gojo...", color = Color.Gray, fontSize = 12.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFFFF5376)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF5376),
                unfocusedBorderColor = Color(0xFF282E47),
                focusedContainerColor = Color(0xFF181C2B),
                unfocusedContainerColor = Color(0xFF181C2B),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("wallpaper_search_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categories Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            items(WallpaperCategory.values()) { cat ->
                val isSelected = selectedCategory == cat.displayName
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat.displayName },
                    label = {
                        Text(
                            text = "${cat.icon} ${cat.displayName}",
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFF5376),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF1A1F30),
                        labelColor = Color.White.copy(alpha = 0.8f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("category_chip_${cat.name}")
                )
            }
        }

        // Wallpapers Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 70.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(filteredList, key = { it.id }) { item ->
                val isFav = favorites.contains(item.id)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181C2B)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedWallpaper = item
                            InterstitialAdHelper.showAdEveryXActions(context, interval = 2)
                        }
                        .testTag("wallpaper_card_${item.id}")
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.68f)
                                .clip(RoundedCornerShape(16.dp))
                        )

                        // Bottom Gradient with Info
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xDD000000), Color(0xFF000000))
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = item.title,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.animeTitle,
                                    color = Color(0xFFFF5376),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Download,
                                            contentDescription = null,
                                            tint = Color.LightGray,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Text(
                                            text = " ${item.downloads}",
                                            color = Color.LightGray,
                                            fontSize = 9.sp
                                        )
                                    }
                                    Text(
                                        text = "4K UHD",
                                        color = Color(0xFF00E676),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Favorite Button
                        IconButton(
                            onClick = { repository.toggleFavorite(item.id) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFav) Color(0xFFFF5376) else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Integrated Start.io Banner Ad at bottom
        StartAppBannerView(modifier = Modifier.fillMaxWidth())
    }

    // Fullscreen Wallpaper Preview & Set Modal
    selectedWallpaper?.let { wp ->
        val isFav = favorites.contains(wp.id)
        Dialog(
            onDismissRequest = { selectedWallpaper = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // High-Res Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(wp.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = wp.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Header Overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)))
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { selectedWallpaper = null },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }

                    Row {
                        IconButton(
                            onClick = { repository.toggleFavorite(wp.id) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFav) Color(0xFFFF5376) else Color.White
                            )
                        }
                    }
                }

                // Bottom Action Bar Overlay
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xCC000000), Color(0xFF000000))
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = wp.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${wp.characterName} • ${wp.animeTitle} • ${wp.resolution}",
                        color = Color(0xFFFF5376),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isSettingWallpaper) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = Color(0xFFFF5376), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Applying 4K Wallpaper...", color = Color.White, fontSize = 14.sp)
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Set Home Screen
                            Button(
                                onClick = {
                                    isSettingWallpaper = true
                                    coroutineScope.launch {
                                        val success = repository.setWallpaper(wp, 1)
                                        isSettingWallpaper = false
                                        val msg = if (success) "✅ Home Screen Wallpaper Applied!" else "Failed to apply wallpaper"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (success) InterstitialAdHelper.showAd(context)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2235)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = Color(0xFFFF5376), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Home", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            // Set Lock Screen
                            Button(
                                onClick = {
                                    isSettingWallpaper = true
                                    coroutineScope.launch {
                                        val success = repository.setWallpaper(wp, 2)
                                        isSettingWallpaper = false
                                        val msg = if (success) "✅ Lock Screen Wallpaper Applied!" else "Failed to apply wallpaper"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (success) InterstitialAdHelper.showAd(context)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2235)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFF5376), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Lock", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            // Set Both
                            Button(
                                onClick = {
                                    isSettingWallpaper = true
                                    coroutineScope.launch {
                                        val success = repository.setWallpaper(wp, 3)
                                        isSettingWallpaper = false
                                        val msg = if (success) "🎉 4K Wallpaper Applied to Both Screens!" else "Failed to apply wallpaper"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (success) InterstitialAdHelper.showAd(context)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5376)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Icon(Icons.Default.Wallpaper, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Set Both", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // In-App Update Dialog Popup
    if (showUpdateDialog) {
        AppUpdateDialog(
            versionName = "v2.0",
            onDismiss = { showUpdateDialog = false }
        )
    }
}
