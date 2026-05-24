package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.RoundingMode
import java.text.DecimalFormat

val categories = listOf(
    "Площадь", "Длина", "Температура", "Объем", "Масса",
    "Данные", "Скорость", "Время", "Чаевые"
)

val unitsMap = mapOf(
    "Площадь" to listOf("Кв. Метры", "Кв. Километры", "Акр", "Гектар"),
    "Длина" to listOf("Метры", "Километры", "Сантиметры", "Миллиметры", "Миля"),
    "Температура" to listOf("Цельсий", "Фаренгейт", "Кельвин"),
    "Объем" to listOf("Литры", "Милилитры", "Галлоны"),
    "Масса" to listOf("Килограммы", "Граммы", "Тонны", "Фунты"),
    "Данные" to listOf("Бит", "Байт", "Кб", "Мб", "Гб", "Тб"),
    "Скорость" to listOf("Км/ч", "М/с", "Мили/ч"),
    "Время" to listOf("Секунды", "Минуты", "Часы", "Дни"),
    "Чаевые" to listOf("Сумма счета", "Процент чаевых", "Количество персон")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen() {
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedUnitFrom by remember { mutableStateOf(unitsMap[selectedCategory]!![0]) }
    var selectedUnitTo by remember { mutableStateOf(unitsMap[selectedCategory]!![1]) }
    var inputValue by remember { mutableStateOf("") }
    
    val outputValue = remember(inputValue, selectedCategory, selectedUnitFrom, selectedUnitTo) {
        convert(inputValue, selectedCategory, selectedUnitFrom, selectedUnitTo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Конвертер", color = MaterialTheme.colorScheme.onBackground) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DropdownMenuSelector(
                label = "Категория",
                items = categories,
                selected = selectedCategory,
                onSelected = { 
                    selectedCategory = it
                    selectedUnitFrom = unitsMap[it]!![0]
                    if (unitsMap[it]!!.size > 1) {
                         selectedUnitTo = unitsMap[it]!![1]
                    } else {
                         selectedUnitTo = unitsMap[it]!![0]
                    }
                    inputValue = ""
                }
            )
            
            if (selectedCategory == "Чаевые") {
                var bill by remember { mutableStateOf("") }
                var tipPercent by remember { mutableStateOf("10") }
                var persons by remember { mutableStateOf("1") }
                
                val totalTip = (bill.toDoubleOrNull() ?: 0.0) * (tipPercent.toDoubleOrNull() ?: 0.0) / 100.0
                val totalBill = (bill.toDoubleOrNull() ?: 0.0) + totalTip
                val perPerson = if ((persons.toIntOrNull() ?: 1) > 0) totalBill / (persons.toIntOrNull() ?: 1) else 0.0
                
                OutlinedTextField(
                    value = bill,
                    onValueChange = { bill = it },
                    label = { Text("Сумма счета", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tipPercent,
                    onValueChange = { tipPercent = it },
                    label = { Text("Процент чаевых (%)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = persons,
                    onValueChange = { persons = it },
                    label = { Text("Количество персон", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Чаевые: ${formatResult(totalTip)}", color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Итого: ${formatResult(totalBill)}", color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("На человека: ${formatResult(perPerson)}", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        DropdownMenuSelector("Из", unitsMap[selectedCategory]!!, selectedUnitFrom) { selectedUnitFrom = it }
                    }
                    IconButton(
                        onClick = {
                            val temp = selectedUnitFrom
                            selectedUnitFrom = selectedUnitTo
                            selectedUnitTo = temp
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.SwapHoriz,
                            contentDescription = "Поменять местами",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        DropdownMenuSelector("В", unitsMap[selectedCategory]!!, selectedUnitTo) { selectedUnitTo = it }
                    }
                }
                
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Ввод", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = outputValue,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Результат", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun formatResult(num: Double): String {
    val df = DecimalFormat("#.####")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(num)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuSelector(label: String, items: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun convert(valueStr: String, category: String, from: String, to: String): String {
    val param = valueStr.toDoubleOrNull() ?: return ""
    if (from == to) return formatResult(param)
    
    val baseValue = when (category) {
        "Площадь" -> when (from) {
            "Кв. Метры" -> param
            "Кв. Километры" -> param * 1_000_000
            "Акр" -> param * 4046.86
            "Гектар" -> param * 10000
            else -> param
        }
        "Длина" -> when (from) {
            "Метры" -> param
            "Километры" -> param * 1000
            "Сантиметры" -> param / 100
            "Миллиметры" -> param / 1000
            "Миля" -> param * 1609.34
            else -> param
        }
        "Температура" -> when (from) {
            "Фаренгейт" -> (param - 32) * 5.0 / 9.0
            "Кельвин" -> param - 273.15
            else -> param // Celsius
        }
        "Объем" -> when(from) {
            "Литры" -> param
            "Милилитры" -> param / 1000
            "Галлоны" -> param * 3.78541
            else -> param
        }
        "Масса" -> when(from) {
            "Килограммы" -> param
            "Граммы" -> param / 1000
            "Тонны" -> param * 1000
            "Фунты" -> param * 0.453592
            else -> param
        }
        "Данные" -> when(from) {
            "Бит" -> param / 8.0
            "Байт" -> param
            "Кб" -> param * 1024
            "Мб" -> param * 1024 * 1024
            "Гб" -> param * 1024 * 1024 * 1024
            "Тб" -> param * 1024 * 1024 * 1024 * 1024
            else -> param
        }
        "Скорость" -> when(from) {
            "Км/ч" -> param / 3.6
            "М/с" -> param
            "Мили/ч" -> param * 0.44704
            else -> param
        }
        "Время" -> when(from) {
            "Секунды" -> param
            "Минуты" -> param * 60
            "Часы" -> param * 3600
            "Дни" -> param * 86400
            else -> param
        }
        else -> param
    }
    
    val result = when (category) {
        "Площадь" -> when (to) {
            "Кв. Метры" -> baseValue
            "Кв. Километры" -> baseValue / 1_000_000
            "Акр" -> baseValue / 4046.86
            "Гектар" -> baseValue / 10000
            else -> baseValue
        }
        "Длина" -> when (to) {
            "Метры" -> baseValue
            "Километры" -> baseValue / 1000
            "Сантиметры" -> baseValue * 100
            "Миллиметры" -> baseValue * 1000
            "Миля" -> baseValue / 1609.34
            else -> baseValue
        }
        "Температура" -> when (to) {
            "Фаренгейт" -> (baseValue * 9.0 / 5.0) + 32
            "Кельвин" -> baseValue + 273.15
            else -> baseValue // Celsius base
        }
        "Объем" -> when(to) {
            "Литры" -> baseValue
            "Милилитры" -> baseValue * 1000
            "Галлоны" -> baseValue / 3.78541
            else -> baseValue
        }
        "Масса" -> when(to) {
            "Килограммы" -> baseValue
            "Граммы" -> baseValue * 1000
            "Тонны" -> baseValue / 1000
            "Фунты" -> baseValue / 0.453592
            else -> baseValue
        }
        "Данные" -> when(to) {
            "Бит" -> baseValue * 8.0
            "Байт" -> baseValue
            "Кб" -> baseValue / 1024
            "Мб" -> baseValue / (1024 * 1024)
            "Гб" -> baseValue / (1024 * 1024 * 1024)
            "Тб" -> baseValue / (1024 * 1024 * 1024 * 1024)
            else -> baseValue
        }
        "Скорость" -> when(to) {
            "Км/ч" -> baseValue * 3.6
            "М/с" -> baseValue
            "Мили/ч" -> baseValue / 0.44704
            else -> baseValue
        }
        "Время" -> when(to) {
            "Секунды" -> baseValue
            "Минуты" -> baseValue / 60
            "Часы" -> baseValue / 3600
            "Дни" -> baseValue / 86400
            else -> baseValue
        }
        else -> baseValue
    }
    
    return formatResult(result)
}
