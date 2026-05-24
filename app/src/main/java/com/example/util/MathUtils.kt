package com.example.util

import java.math.BigDecimal
import java.math.RoundingMode

object MathUtils {
    fun evaluate(expression: String): String {
        return try {
            val tokens = tokenize(expression.replace(" ", "").replace("×", "*").replace("÷", "/"))
            val result = parseExpression(tokens)
            formatResult(result)
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun formatResult(result: Double): String {
        if (result.isNaN() || result.isInfinite()) return "Error"
        val dec = BigDecimal(result).setScale(8, RoundingMode.HALF_UP).stripTrailingZeros()
        return dec.toPlainString()
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var current = ""
        for (char in expr) {
            if (char.isDigit() || char == '.') {
                current += char
            } else {
                if (current.isNotEmpty()) {
                    tokens.add(current)
                    current = ""
                }
                tokens.add(char.toString())
            }
        }
        if (current.isNotEmpty()) tokens.add(current)
        return tokens
    }

    private fun parseExpression(tokens: List<String>): Double {
        if (tokens.isEmpty()) return 0.0
        val tokenList = tokens.toMutableList()

        fun parsePrimary(): Double {
            if (tokenList.isEmpty()) return 0.0
            val token = tokenList.removeAt(0)
            if (token == "-") return -parsePrimary()
            if (token == "+") return parsePrimary()
            if (token == "(") {
                val value = parseExpression(tokenList)
                if (tokenList.isNotEmpty() && tokenList.first() == ")") tokenList.removeAt(0)
                return value
            }
            return token.toDoubleOrNull() ?: 0.0
        }

        fun parseTerm(): Double {
            var left = parsePrimary()
            while (tokenList.isNotEmpty() && (tokenList.first() == "*" || tokenList.first() == "/")) {
                val op = tokenList.removeAt(0)
                val right = parsePrimary()
                left = if (op == "*") left * right else left / right
            }
            return left
        }

        var left = parseTerm()
        while (tokenList.isNotEmpty() && (tokenList.first() == "+" || tokenList.first() == "-")) {
            val op = tokenList.removeAt(0)
            val right = parseTerm()
            left = if (op == "+") left + right else left - right
        }
        return left
    }
}
