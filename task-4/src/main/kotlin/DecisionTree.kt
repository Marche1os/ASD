package org.example

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.log2

class DecisionTree(
    private val dataSet: List<Map<String, String>>,
) {

    //  считает энтропию конкретного критерия.
    fun calcEntropyByCriteria(
        criteria: String,
        recordsCount: Int = dataSet.size,
    ): Double {
        val values = mutableSetOf<String>()

        dataSet.filter { params ->
            values.add(
                params.getValue(criteria)
            )
        }

        val fraction = DecimalFormat.getInstance().format(values.size.toDouble() / recordsCount).toDouble()

        return -(fraction * log2(fraction) + fraction * log2(fraction))
    }

    fun calcTotalEntropy(criterias: List<String>): Double {
        val fractions = criterias.groupBy { it }
            .map { it.value.size.toDouble() / criterias.size }

        return -fractions.sumOf { p -> p * log2(p) }
    }

    // считает информационную выгоду
    fun calcGain(
        parentCriteria: String,
        targetCriteria: String
    ): Double {
        val parentSubtrees = dataSet.groupBy { it.getValue(targetCriteria) }

        val weightedEntropy = parentSubtrees.map { (_, subset) ->
            val entropy = calcTotalEntropy(
                subset.map {
                    it.getValue(parentCriteria)
                }
            )

            subset.size.toDouble() / dataSet.size * entropy
        }.sum()

        val parentEntropy = calcTotalEntropy(dataSet.map { it.getValue(parentCriteria) })

        return BigDecimal(parentEntropy - weightedEntropy)
            .setScale(3, RoundingMode.DOWN)
            .toDouble()
    }

    fun calcEntropyByValue(
        criteria: String,
        value: String,
        parentCriteria: String,
    ): Double {
        var totalEntropy = 0.0
        val filteredData = dataSet.filter { it.getValue(criteria) == value }
            .groupBy { it.getValue(parentCriteria) }

        filteredData.forEach { _ ->
            val entropy = calcEntropyByCriteria(parentCriteria)

            totalEntropy += entropy
        }


        return BigDecimal(totalEntropy)
            .setScale(3, RoundingMode.DOWN)
            .toDouble()
    }

    fun conditionalEntropy(
        criteria: String,
        value: String,
        parentCriteria: String
    ): Double {
        val filteredData = dataSet.filter { it.getValue(criteria) == value }
            .groupBy { it.getValue(parentCriteria) }

        // Рассчитываем энтропию для каждой подгруппы и суммируем для получения средневзвешенной энтропии
        var totalEntropy = 0.0
        val totalSize = filteredData.size.toDouble()

        for ((_, subgroup) in filteredData) {
            val subgroupSize = subgroup.size.toDouble()
            val subgroupEntropy = calcEntropyByCriteria(parentCriteria)
            println(calcEntropyByCriteria(parentCriteria))
            totalEntropy += (subgroupSize / totalSize) * subgroupEntropy
        }

        return totalEntropy
    }

}

fun main() {
    val tree = DecisionTree(DATA_SET)

    val outlookGain = tree.calcGain("Play Tennis", "Outlook") //0.246
    val humidityGain = tree.calcGain("Play Tennis", "Humidity") //0.151
    val windGain = tree.calcGain("Play Tennis", "Wind") //0.048
    val temperatureGain = tree.calcGain("Play Tennis", "Temperature") //0.029

    val output = tree.conditionalEntropy("Outlook", "Sunny", "Play Tennis")
    val criteria = tree.calcEntropyByCriteria("Outlook")
    println(output)
}

val DATA_SET = listOf(
    mapOf(
        "Outlook" to "Sunny",
        "Temperature" to "Hot",
        "Humidity" to "High",
        "Wind" to "Weak",
        "Play Tennis" to "No"
    ),
    mapOf(
        "Outlook" to "Sunny",
        "Temperature" to "Hot",
        "Humidity" to "High",
        "Wind" to "Strong",
        "Play Tennis" to "No"
    ),
    mapOf(
        "Outlook" to "Overcast",
        "Temperature" to "Hot",
        "Humidity" to "High",
        "Wind" to "Weak",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Rain",
        "Temperature" to "Mild",
        "Humidity" to "High",
        "Wind" to "Weak",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Rain",
        "Temperature" to "Cool",
        "Humidity" to "Normal",
        "Wind" to "Weak",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Rain",
        "Temperature" to "Cool",
        "Humidity" to "Normal",
        "Wind" to "Strong",
        "Play Tennis" to "No"
    ),
    mapOf(
        "Outlook" to "Overcast",
        "Temperature" to "Cool",
        "Humidity" to "Normal",
        "Wind" to "Strong",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Sunny",
        "Temperature" to "Mild",
        "Humidity" to "High",
        "Wind" to "Weak",
        "Play Tennis" to "No"
    ),
    mapOf(
        "Outlook" to "Sunny",
        "Temperature" to "Cool",
        "Humidity" to "Normal",
        "Wind" to "Weak",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Rain",
        "Temperature" to "Mild",
        "Humidity" to "Normal",
        "Wind" to "Weak",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Sunny",
        "Temperature" to "Mild",
        "Humidity" to "Normal",
        "Wind" to "Strong",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Overcast",
        "Temperature" to "Mild",
        "Humidity" to "High",
        "Wind" to "Strong",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Overcast",
        "Temperature" to "Hot",
        "Humidity" to "Normal",
        "Wind" to "Weak",
        "Play Tennis" to "Yes"
    ),
    mapOf(
        "Outlook" to "Rain",
        "Temperature" to "Mild",
        "Humidity" to "High",
        "Wind" to "Strong",
        "Play Tennis" to "No"
    )
)
