abstract class Node(val name: String) {
    abstract fun getSize(): Int
}

class Directory(
    name: String,
    var parent: Directory? = null,
    val children: MutableList<Node> = mutableListOf()
) : Node(name) {
    override fun toString(): String {
        var level = 0
        var temp = parent
        while (temp != null) {
            temp = temp.parent
            level++
        }

        val prefix = "\t".repeat(level)
        var output = "$prefix- $name (dir)"
        children.forEach { output += if (it is File) "\n$prefix\t$it" else "\n$it" }
        return output
    }

    fun addDirectory(name: String) {
        val directory = Directory(name, this)
        children.add(directory)
    }

    fun addFile(name: String, size: Int) {
        children.add(File(name, size))
    }

    override fun getSize(): Int {
        return children.sumOf(Node::getSize)
    }

    fun getPath(): String {
        var path = name
        var temp = this.parent
        while (temp != null) {
            path = if (temp.name != "/") "${temp.name}/$path" else "/$path"
            temp = temp.parent
        }
        return path
    }
}

class File(name: String, private val size: Int) : Node(name) {
    override fun getSize(): Int {
        return size
    }

    override fun toString(): String {
        return "- $name (file, size=$size)"
    }
}

fun main() {
    fun pareInput(input: List<String>): Directory {
        val root = Directory("/")
        var currentDirectory = root

        val cdCommand = """\$ cd (.+)""".toRegex()
        val filePattern = """(\d+) (.+)""".toRegex()
        val directoryPattern = """dir (.+)""".toRegex()

        for (command in input) {
            cdCommand.matchEntire(command)?.destructured?.let { (directory) ->
                currentDirectory = when (directory) {
                    "/" -> root
                    ".." -> currentDirectory.parent!!
                    else -> currentDirectory.children.filterIsInstance<Directory>().first { it.name == directory }
                }
            }

            filePattern.matchEntire(command)?.destructured?.let { (size, name) ->
                currentDirectory.addFile(name, size.toInt())
            }

            directoryPattern.matchEntire(command)?.destructured?.let { (name) -> currentDirectory.addDirectory(name) }
        }

        return root
    }

    fun getSizes(directory: Directory, sizes: MutableMap<String, Int>): MutableMap<String, Int> {
        sizes[directory.getPath()] = directory.getSize()
        for (dir in directory.children.filterIsInstance<Directory>()) {
            sizes.putAll(getSizes(dir, sizes))
        }
        return sizes
    }

    fun part1(input: List<String>): Int {
        val root = pareInput(input)
        // println(root)

        val directorySizes = getSizes(root, mutableMapOf())
        // println(directorySizes)
        return directorySizes.filter { it.value <= 100000 }.values.sum()
    }

    fun part2(input: List<String>): Int {
        val root = pareInput(input)

        val totalSpace = 70000000
        val updateSize = 30000000

        val directorySizes = getSizes(root, mutableMapOf())

        val freeSpace = totalSpace - directorySizes["/"]!!
        val neededSpace = updateSize - freeSpace

        val directoryToDelete = directorySizes.filter { it.value >= neededSpace }.minBy { it.value }
        // println(directoryToDelete)
        return directoryToDelete.value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println("Part 1: " + part1(input))
    println("Part 2: " + part2(input))
}
