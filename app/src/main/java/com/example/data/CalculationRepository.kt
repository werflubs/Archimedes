package com.example.data

import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val dao: CalculationDao) {
    val allCalculations: Flow<List<Calculation>> = dao.getAllCalculations()

    suspend fun insert(calculation: Calculation) {
        dao.insertCalculation(calculation)
    }

    suspend fun clearAll() {
        dao.clearHistory()
    }
}
