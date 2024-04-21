import java.util.PriorityQueue

class HuffmanEncoder {

    private fun buildPriorityQueue(frequencyMap: Map<Char, Int>): PriorityQueue<Node> =
        PriorityQueue<Node>().apply {
            frequencyMap.forEach { (char, freq) ->
                add(
                    Node(
                        character = char,
                        frequency = freq,
                        left = null,
                        right = null
                    )
                )
            }
        }

    private fun buildHuffmanTree(priorityQueue: PriorityQueue<Node>): Node? {
        while (priorityQueue.size > 1) {
            val left = priorityQueue.poll()
            val right = priorityQueue.poll()

            val merged = Node(
                character = null,
                frequency = left.frequency + right.frequency,
                left = left,
                right = right,
            )

            priorityQueue.add(merged)
        }

        return priorityQueue.poll()
    }

    private fun buildCodesTable(
        root: Node?,
        code: String,
        codesTable: MutableMap<Char, String>
    ): MutableMap<Char, String> {

        root?.let { root ->
            root.character?.let { c -> codesTable[c] = code }

            buildCodesTable(root.left, code + "0", codesTable)
            buildCodesTable(root.right, code + "1", codesTable)
        }

        return codesTable
    }

    fun encode(text: String): Map<Char, String> {
        val frequencyMap = text
            .replace(' ', '_')
            .groupingBy { it }
            .eachCount()

        val priorityQueue = buildPriorityQueue(frequencyMap)
        val rootHuffmanNode = buildHuffmanTree(priorityQueue)

        return buildCodesTable(rootHuffmanNode, "", mutableMapOf())
            .toList()
            .asReversed()
            .toMap()
    }

    data class Node(
        val character: Char?,
        val frequency: Int,
        val left: Node?,
        val right: Node?
    ) : Comparable<Node> {

        override fun compareTo(other: Node): Int = frequency.compareTo(other.frequency)
    }
}
