package org.example

import java.util.*

class HaffmanCode {
    fun encode(line: String): Map<Char, Int> {
        val frequencies = mutableMapOf<Char, Int>()

        line.replace(' ', '_')
            .forEach { item -> frequencies[item] = frequencies.getOrDefault(item, 0) + 1 }

        val root = buildTree(frequencies)
        val huffmanCodes = buildTableCodes(root)

        return huffmanCodes.map { it.key to it.value.toInt() }
            .asReversed()
            .toMap()
    }

    private fun buildTree(frequencies: MutableMap<Char, Int>): Node {
        val priorityQueue = PriorityQueue<Node>()

        frequencies.forEach { (char, freq) ->
            priorityQueue.offer(
                Node(
                    character = char,
                    frequency = freq,
                )
            )
        }

        while (priorityQueue.size > 1) {
            val left = priorityQueue.poll()
            val right = priorityQueue.poll()

            val frequency = left.frequency + right.frequency

            val node = Node(
                character = null,
                frequency = frequency,
                left = left,
                right = right
            )

            priorityQueue.offer(node)
        }

        return priorityQueue.poll()
    }

    private fun buildTableCodes(root: Node): Map<Char, String> {
        val codes = mutableMapOf<Char, String>()

        traverse(root, "", codes)

        return codes
    }

    private fun traverse(root: Node, code: String, codes: MutableMap<Char, String>) {
        val process: () -> Unit = {
            traverse(root.left!!, code + "0", codes)
            traverse(root.right!!, code + "1", codes)
        }

        root.character
            ?.let { c -> codes[c] = code }
            ?: process()
    }

    data class Node(
        val character: Char?,
        val frequency: Int,
        val left: Node? = null,
        val right: Node? = null
    ) : Comparable<Node> {
        override fun compareTo(other: Node): Int {
            return frequency.compareTo(other.frequency)
        }
    }
}
