class EulerGraph {
    private val adjacency = mutableMapOf<String, MutableList<String>>()
    private val path = mutableListOf<String>()

    fun addRib(u: String, v: String) {
        adjacency.computeIfAbsent(u) { mutableListOf() }.add(v)
        adjacency.computeIfAbsent(v) { mutableListOf() }.add(u)
    }

    fun findPath(): List<String> {
        val startNode = findStartNode()
        dfs(startNode)
        return path
    }

    private fun findStartNode(): String {
        return adjacency.filter { it.value.size % 2 == 0 }
            .map { it.key }
            .firstOrNull()
            ?: adjacency.keys.first()
    }

    private fun dfs(node: String) {
        val neighbours = adjacency[node]
        while (!neighbours.isNullOrEmpty()) {
            val nextNode = neighbours.removeLast()
            adjacency[nextNode]?.remove(node)
            dfs(nextNode)
        }
        path.add(node)
    }

    // метод построения эйлерова графа из заданного дерева.
    fun buildGrapdFromTree(tree: Map<String, List<String>>) {
        for ((node, neighbours) in tree) {
            for (neighbour in neighbours) {
                addRib(node, neighbour)
            }
        }
    }

    // метод построения эйлерова пути для графа.
    fun buildPath(graph: MutableMap<String, MutableList<String>>, startNode: String): List<String> {
        adjacency.clear()
        path.clear()

        adjacency.putAll(graph)
        dfs(startNode)

        return path
    }

    // метод изменения корня графа (меняет только эйлеров путь).
    fun changeRoot(
        oldRoot: String,
        newRoot: String
    ): List<String> {
        val oldPath = buildPath(adjacency, oldRoot)
        val index = oldPath.indexOf(newRoot)
        val newPath = oldPath.subList(index, oldPath.size - 1).toMutableList()

        newPath.addAll(oldPath.subList(1, index))

        return newPath
    }

    //метод связывания ребром двух эйлеровых графов и формирования нового графа с изменением множества.
    fun bindTwoGraphsByRib(
        graph1: Map<String, List<String>>,
        graph2: Map<String, List<String>>
    ): EulerGraph {
        val newGraph = EulerGraph()

        newGraph.buildGrapdFromTree(graph1)
        newGraph.buildGrapdFromTree(graph2)

        return newGraph
    }

    // метод проверки принадлежности двух вершин одному графу.
    fun isNodesConnected(node1: String, node2: String): Boolean = adjacency[node1]?.contains(node2) ?: false

    private fun clear() {
        adjacency.clear()
        path.clear()
    }
}