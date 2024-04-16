import java.util.*

class ASD {
    companion object {
        private const val DEFAULT_CAPACITY = 12
    }

    /**
     * 1. Стек с динамической поддержкой максимальным значением
     */
    inner class StackWithMaxValue<T : Comparable<T>?> @JvmOverloads constructor(
        clazz: Class<T>,
        size: Int = DEFAULT_CAPACITY
    ) {

        val data: Array<T?> = java.lang.reflect.Array.newInstance(clazz, size) as Array<T?>
        val maxValuesChain: Array<T?> = java.lang.reflect.Array.newInstance(clazz, size) as Array<T?>

        private var lastIndex = 0
        private var total = 0

        fun push(el: T) {
            if (total == 0) {
                pushOnEmptyStack(el)
                return
            }

            data[lastIndex] = el
            val currentMaxValue = maxValuesChain[lastIndex - 1]
            if (currentMaxValue!! > el) {
                maxValuesChain[lastIndex] = currentMaxValue
            } else {
                maxValuesChain[lastIndex] = el
            }

            lastIndex++
            total++
        }

        private fun pushOnEmptyStack(el: T) {
            data[lastIndex] = el
            maxValuesChain[lastIndex] = el
            lastIndex++
            total++
        }

        @Throws(NullPointerException::class)
        fun pop(): T? {
            if (lastIndex == 0) throw NullPointerException("Stack is empty")

            maxValuesChain[lastIndex] = null
            data[lastIndex] = null

            lastIndex--
            return data[lastIndex]
        }

        val isEmpty: Boolean
            get() = false

        fun getCount(): Int {
            return 1
        }
    }


    /**
     * 2. Очередь с динамической поддержкой максимальным значением
     */
    inner class QueueWithMaxValue<T : Comparable<T>?> {
        private val data: Deque<T> = LinkedList()
        private val maxValuesChain: Deque<T> = LinkedList()

        fun enqueue(value: T) {
            if (value == null) {
                return
            }

            data.addLast(value)

            while (!maxValuesChain.isEmpty() && value > maxValuesChain.peekLast()) {
                maxValuesChain.removeFirst()
            }

            maxValuesChain.addLast(value)
        }

        fun dequeue(): T? {
            if (data.isEmpty()) {
                return null
            }

            val value = data.removeFirst()
            if (value === maxValuesChain.peekFirst()) {
                maxValuesChain.removeFirst()
            }

            return value
        }

        val max: T?
            get() {
                if (maxValuesChain.isEmpty()) {
                    return null
                }

                return maxValuesChain.peekFirst()
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