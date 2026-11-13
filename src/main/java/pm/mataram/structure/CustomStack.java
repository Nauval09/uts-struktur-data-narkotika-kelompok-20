package pm.mataram.structure;

public class CustomStack<T> {
    private Object[] elements;
    private int top;

    public CustomStack(int capacity) {
        this.elements = new Object[capacity];
        this.top = -1;
    }

    public void push(T item) {
        if (top + 1 == elements.length) {
            Object[] newArr = new Object[elements.length * 2];
            for (int i = 0; i <= top; i++) newArr[i] = elements[i];
            elements = newArr;
        }
        elements[++top] = item;
        validateInvariant();
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) throw new IllegalStateException("Stack kosong");
        T item = (T) elements[top];
        elements[top--] = null;
        validateInvariant();
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Stack kosong");
        return (T) elements[top];
    }

    public boolean searchPreserve(T target) {
        CustomStack<T> temp = new CustomStack<>(size() + 1);
        boolean found = false;
        while (!isEmpty()) {
            T current = pop();
            if (current.equals(target)) found = true;
            temp.push(current);
        }
        restore(temp);
        validateInvariant();
        return found;
    }

    public void restore(CustomStack<T> backup) {
        while (!backup.isEmpty()) {
            push(backup.pop());
        }
    }

    public int size() { return top + 1; }
    public boolean isEmpty() { return top == -1; }

    public void validateInvariant() {
        if (top < -1 || top >= elements.length) {
            throw new IllegalStateException("Invariant Stack dilanggar: pointer top tidak valid");
        }
    }
}