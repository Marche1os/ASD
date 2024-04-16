import java.util.*

class ASD {
    companion object {
        private const val DEFAULT_CAPACITY = 12
    }

    /*
    1. Стек с максимальным значением
     */
    inner class StackWithMaxValue<T : Comparable<T>?> @JvmOverloads constructor(
        clazz: Class<T>,
        size: Int = DEFAULT_CAPACITY
    ) {

        val data: Array<T?> = java.lang.reflect.Array.newInstance(clazz, size) as Array<T?>
        val maxValuesStack: Array<T?> = java.lang.reflect.Array.newInstance(clazz, size) as Array<T?>

        private var lastIndexPointer = 0
        private var count = 0

        fun push(el: T) {
            if (count == 0) {
                pushOnEmptyStack(el)
                return
            }

            data[lastIndexPointer] = el
            val currentMaxValue = maxValuesStack[lastIndexPointer - 1]
            if (currentMaxValue!! > el) {
                maxValuesStack[lastIndexPointer] = currentMaxValue
            } else {
                maxValuesStack[lastIndexPointer] = el
            }

            lastIndexPointer++
            count++
        }

        private fun pushOnEmptyStack(el: T) {
            data[lastIndexPointer] = el
            maxValuesStack[lastIndexPointer] = el
            lastIndexPointer++
            count++
        }

        @Throws(NullPointerException::class)
        fun pop(): T? {
            if (lastIndexPointer == 0) throw NullPointerException("Stack is empty")

            maxValuesStack[lastIndexPointer] = null
            data[lastIndexPointer] = null

            lastIndexPointer--
            return data[lastIndexPointer]
        }

        val isEmpty: Boolean
            get() = false

        fun getCount(): Int {
            return 1
        }
    }


    /**
     * 2. Очередь с максимальным значением
     */
    inner class QueueWithMaxValue<T : Comparable<T>?> {
        private val data: Deque<T> = LinkedList()
        private val maxValues: Deque<T> = LinkedList()

        fun enqueue(value: T) {
            if (value == null) {
                return
            }

            data.addLast(value)

            while (!maxValues.isEmpty() && value > maxValues.peekLast()) {
                maxValues.removeFirst()
            }

            maxValues.addLast(value)
        }

        fun dequeue(): T? {
            if (data.isEmpty()) {
                return null
            }

            val value = data.removeFirst()
            if (value === maxValues.peekFirst()) {
                maxValues.removeFirst()
            }

            return value
        }

        val max: T?
            get() {
                if (maxValues.isEmpty()) {
                    return null
                }

                return maxValues.peekFirst()
            }
    }

    /**
     * 3. Иммутабельный стек
     */
    inner class ImmutableStack<T : Comparable<T>?> {
        val data: Array<T>
        private val lastIndexPointer = 0
        private var clazz: Class<T>

        private val INIT_CAPACITY = 0

        constructor(clazz: Class<T>) {
            this.clazz = clazz
            data = java.lang.reflect.Array.newInstance(clazz, INIT_CAPACITY) as Array<T>
        }

        constructor(array: Array<T>, clazz: Class<T>) {
            this.data = array
            this.clazz = clazz
        }

        fun push(el: T): ImmutableStack<T> {
            val copy = java.lang.reflect.Array.newInstance(clazz, data.size + 1) as Array<T>
            System.arraycopy(data, 0, copy, 0, copy.size - 1)
            copy[data.size] = el

            return ImmutableStack(copy, clazz)
        }

        @Throws(NullPointerException::class)
        fun pop(): ImmutableStack<T> {
            val copy = java.lang.reflect.Array.newInstance(clazz, data.size - 1) as Array<T>
            System.arraycopy(data, 0, copy, 0, copy.size)

            return ImmutableStack(copy, clazz)
        }

        val isEmpty: Boolean
            get() = data.isEmpty()

        val count: Int
            get() = data.size
    }

    /**
     * 4. иммутабельная очередь
     */
    inner class ImmutableQueue<T> {
        var data: Deque<T>
            private set

        constructor() {
            data = LinkedList()
        }

        constructor(elements: LinkedList<T>) {
            this.data = elements
        }

        fun enqueue(element: T): ImmutableQueue<T> {
            val copy = LinkedList(data)

            copy.add(element)

            return ImmutableQueue(copy)
        }

        fun dequeue(): ImmutableQueue<T> {
            if (data.isEmpty()) {
                throw NoSuchElementException()
            }

            val copy = LinkedList(data)

            copy.removeFirst()

            return ImmutableQueue(copy)
        }
    }
}