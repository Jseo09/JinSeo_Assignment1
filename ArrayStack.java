public class ArrayStack<T> implements StackInterface{
	private T[] stack;
	private static final int DEFAULT_CAPACITY = 5000;
	private static final int MAX_CAPACITY = 100000;
	private int topIndex;

	public ArrayStack(){
		this(DEFAULT_CAPACITY);
	}

	public ArrayStack(int initialCapacity){
		checkCapacity(initialCapacity);
		@SuppressWarnings("unchecked")
		T[] tempStack = (T[]) new Object[initialCapacity];
		stack = tempStack;
		topIndex = -1;
	}

	private void checkCapacity(int capacity){
		if (capacity < 1 || capacity > MAX_CAPACITY){
			throw new IllegalArgumentException("Capacity does not work");
		}
	}
	public T peek(){
		if (isEmpty())
			throw null;
		T item = stack[topIndex];
		return item;

	}

	public boolean contains(T item) {
		for (int i = 0; i <= topIndex; i++) {
			if (stack[i].equals(item)) {
				return true;
			}
		}
		return false;
	}

	public T pop(){
		if (isEmpty()) {
			throw null;
		}
		T item = stack[topIndex];
		stack[topIndex] = null; // Remove reference to the popped item to prevent memory leaks
		topIndex--;
		return item;
	}

	public void push(Object input){
		if (topIndex == stack.length - 1) {
			throw new IllegalStateException("Stack is full");
		}
		topIndex++;
		stack[topIndex] = (T) input;
	}

	public boolean isEmpty(){
		return topIndex == -1;
	}

	public int getSizeOfWords(){
		return topIndex + 1;
	}

	public T getIndex(int index){
		if (index < 0 || index > topIndex) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return stack[index];
	}
	public void empty(){
		for(int i = 0; i < stack.length; i++){
			stack[i] = null;
		}
		topIndex = -1;
	}

	public void setIndex(int index, T value){
		stack[(int) index] = value;

	}
}
