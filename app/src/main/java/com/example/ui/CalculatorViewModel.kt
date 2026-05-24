package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Calculation
import com.example.data.CalculationRepository
import com.example.util.MathUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val repository: CalculationRepository
) : ViewModel() {

    private val _expression = MutableStateFlow("")
    val expression: StateFlow<String> = _expression.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    val history: StateFlow<List<Calculation>> = repository.allCalculations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAction(action: String) {
        when (action) {
            "C" -> {
                _expression.value = ""
                _result.value = ""
            }
            "⌫" -> {
                if (_expression.value.isNotEmpty()) {
                    _expression.value = _expression.value.dropLast(1)
                    updatePreview()
                }
            }
            "=" -> calculateResult()
            else -> {
                _expression.value += action
                updatePreview()
            }
        }
    }

    private fun updatePreview() {
        if (_expression.value.isEmpty()) {
            _result.value = ""
            return
        }
        val evaluated = MathUtils.evaluate(_expression.value)
        if (evaluated != "Error") {
            _result.value = evaluated
        } else {
            _result.value = ""
        }
    }

    private fun calculateResult() {
        if (_expression.value.isEmpty()) return
        val evaluated = MathUtils.evaluate(_expression.value)
        if (evaluated != "Error") {
            viewModelScope.launch {
                repository.insert(Calculation(expression = _expression.value, result = evaluated))
            }
            _expression.value = evaluated
            _result.value = ""
        } else {
            _result.value = "Error"
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
