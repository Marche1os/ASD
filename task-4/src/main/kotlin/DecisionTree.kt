package org.example

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log2

class DecisionTree(
    private val dataSet: List<Map<String, String>>,
) {

    // считает энтропию конкретного критерия.
    fun calcEntropyByCriteria(criteria: String): Double {
        return calcTotalEntropy(
            dataSet.map { it.getValue(criteria) }
        )
    }

    fun calcTotalEntropy(criterias: List<String>): Double {
        val frequency = criterias.groupingBy { it }.eachCount()
        val total = criterias.size.toDouble()

        val sum = -frequency.values.sumOf { count ->
            val fraction = count / total
            fraction * log2(fraction)
        }

        return BigDecimal(sum).toDouble()
    }

    // считает энтропию заданного критерия с разбивкой по родительскому критерию
    fun calcCondEntropy(
        criteria: String,
        parentCriteria: String
    ): Double {
        val totalsByParent = dataSet.groupBy { it.getValue(parentCriteria) }
        val totalSize = dataSet.size.toDouble()

        return totalsByParent.entries
            .sumOf { (_, table) ->
                val size = table.size.toDouble()
                val valueEntropy = calcTotalEntropy(
                    table.map { it.getValue(criteria) }
                )
                (size / totalSize) * valueEntropy
            }
    }

    //  считает информационную выгоду заданного критерия с разбивкой по родительскому критерию
    fun calcGain(criteria: String, parentCriteria: String): Double {
        val parentEntropy = calcTotalEntropy(dataSet.map { it.getValue(parentCriteria) })
        val condEntropy = calcCondEntropy(parentCriteria, criteria)

        return BigDecimal(parentEntropy - condEntropy)
            .setScale(3, RoundingMode.DOWN)
            .toDouble()
    }

    fun build(
        criterias: MutableSet<String> = mutableSetOf(),
        target: String = "Play Tennis"
    ): Node {
        if (dataSet.map { it.getValue(target) }.distinct().size == 1) {
            return Node(
                dataSet
                    .first()
                    .getValue(target)
            )
        }

        val attributes = dataSet.first().keys - target - criterias

        if (attributes.isEmpty()) {
            return Node(
                dataSet
                    .groupBy { it.getValue(target) }
                    .maxBy { it.value.size }
                    .key
            )
        }

        val bestCriteria = attributes.maxBy { calcGain(it, target) }
        criterias.add(bestCriteria)

        val subsets = dataSet.groupBy { it.getValue(bestCriteria) }
        val node = Node(bestCriteria)

        subsets.forEach { (attrValue, subset) ->
            val child = DecisionTree(subset)
                .build(
                    criterias.toMutableSet(), target
                )

            node.branches[attrValue] = child
        }

        return node
    }

    data class Node(
        val name: String,
        val branches: MutableMap<String, Node> = mutableMapOf(),
    )
}

fun main() {
    val tree = DecisionTree(DATA_SET)

    val root = tree.build()
    println(root)
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
