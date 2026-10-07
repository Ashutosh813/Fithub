package com.example.ui.calai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MealType
import com.example.model.sampleFoodDatabase
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalAiAddFoodSheet(
    targetMealType: MealType,
    onDismiss: () -> Unit,
    onAddFood: (name: String, mealType: MealType, calories: Int, protein: Int, carbs: Int, fat: Int, portion: String) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    // Custom food state
    var customName by remember { mutableStateOf("") }
    var customCalories by remember { mutableStateOf("") }
    var customProtein by remember { mutableStateOf("") }
    var customCarbs by remember { mutableStateOf("") }
    var customFat by remember { mutableStateOf("") }
    var customPortion by remember { mutableStateOf("1 serving") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("cal_ai_add_food_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Add to ${targetMealType.displayName}",
                        fontFamily = InterFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Text(
                        text = "Search database or log custom food",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF2F2F7),
                contentColor = AccentPurple,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AccentPurple
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Food Database", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Custom Entry", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == 0) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search foods (e.g. salad, oats, paneer...)", fontFamily = InterFontFamily, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = BorderColor
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                val filtered = sampleFoodDatabase.filter {
                    it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered) { preset ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(CardBackground)
                                .border(0.5.dp, BorderColor, RoundedCornerShape(14.dp))
                                .clickable {
                                    onAddFood(
                                        preset.name,
                                        targetMealType,
                                        preset.calories,
                                        preset.protein,
                                        preset.carbs,
                                        preset.fat,
                                        preset.portion
                                    )
                                    onDismiss()
                                }
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = preset.name,
                                    fontFamily = InterFontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextMain
                                )
                                Text(
                                    text = "${preset.portion} • P: ${preset.protein}g • C: ${preset.carbs}g • F: ${preset.fat}g",
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${preset.calories} kcal",
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurple
                                )
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(AccentPurple.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Rounded.Add, contentDescription = "Add", tint = AccentPurple, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            } else {
                // Custom food form
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Food Name", fontFamily = InterFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = customCalories,
                            onValueChange = { customCalories = it },
                            label = { Text("Calories", fontFamily = InterFontFamily) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = customPortion,
                            onValueChange = { customPortion = it },
                            label = { Text("Portion Size", fontFamily = InterFontFamily) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = customProtein,
                            onValueChange = { customProtein = it },
                            label = { Text("Protein (g)", fontFamily = InterFontFamily) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = customCarbs,
                            onValueChange = { customCarbs = it },
                            label = { Text("Carbs (g)", fontFamily = InterFontFamily) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = customFat,
                            onValueChange = { customFat = it },
                            label = { Text("Fat (g)", fontFamily = InterFontFamily) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (customName.isNotBlank()) {
                                onAddFood(
                                    customName,
                                    targetMealType,
                                    customCalories.toIntOrNull() ?: 200,
                                    customProtein.toIntOrNull() ?: 10,
                                    customCarbs.toIntOrNull() ?: 20,
                                    customFat.toIntOrNull() ?: 5,
                                    customPortion
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Save & Log Meal", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
