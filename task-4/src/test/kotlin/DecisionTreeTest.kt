import org.example.DecisionTree
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DecisionTreeTest {
    private val tree = DecisionTree(DATA_SET)

    /**
     * Итоговое дерево:
     *                  Outlook
     *                /     |      \
     *            Sunny   Overcast  Rain
     *           /          /          \
     *      Humidity      Yes           Wind
     *      /     \                     /   \
     *   High   Normal                Weak  Strong
     *   /          \                 /         \
     * No           Yes             Yes         No
     */
    @Test
    fun buildDecisionTreeTest() {
        val root = tree.build()

        assertEquals("Outlook", root.name)

        /**
         * Ветка Overcast
         *          \
         *          Yes
         */

        val overcastBranch = root.branches.getValue("Overcast")
        assertEquals("Yes", overcastBranch.name)
        assertTrue(overcastBranch.branches.isEmpty())

        /**
         * Ветка Sunny
         *        |
         *     Humidity
         *     |       \
         *    High     Normal
         *    |           \
         *    No          Yes
         */
        val sunnyBranch = root.branches.getValue("Sunny")
        assertEquals("Humidity", sunnyBranch.name)
        assertEquals(2, sunnyBranch.branches.size)

        val highBranch = sunnyBranch.branches.getValue("High")
        assertEquals("No", highBranch.name)
        assertTrue(highBranch.branches.isEmpty())

        val normalBranch = sunnyBranch.branches.getValue("Normal")
        assertEquals("Yes", normalBranch.name)
        assertTrue(normalBranch.branches.isEmpty())


        /**
         * Ветка Rain
         *        |
         *      Wind
         *     |    \
         *    Weak  Strong
         *    |        \
         *    Yes       No
         */

        val rainBranch = root.branches.getValue("Rain")
        assertEquals("Wind", rainBranch.name)
        assertEquals(2, rainBranch.branches.size)

        val weakBranch = rainBranch.branches.getValue("Weak")
        assertEquals("Yes", weakBranch.name)
        assertTrue(weakBranch.branches.isEmpty())

        val strongBranch = rainBranch.branches.getValue("Strong")
        assertEquals("No", strongBranch.name)
        assertTrue(strongBranch.branches.isEmpty())
    }
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