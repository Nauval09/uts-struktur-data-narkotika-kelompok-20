package pm.mataram.structure;

public class DynamicArray<T> implements StrukturData<T> {
    private Object[] data;
    private int size;

    public DynamicArray() {
        this.data = new Object[10];
        this.size = 0;
    }

    private void resize() {
        int newCapacity = data.length * 2;
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    @Override
    public void add(T value) {
        if (value == null) throw new IllegalArgumentException("Nilai tidak boleh null");
        if (size == data.length) resize();
        data[size++] = value;
        validateInvariant();
    }

    public void insert(int position, T value) {
        if (position < 0 || position > size) throw new IndexOutOfBoundsException("Posisi tidak valid");
        if (value == null) throw new IllegalArgumentException("Nilai tidak boleh null");
        if (size == data.length) resize();
        for (int i = size; i > position; i--) {
            data[i] = data[i - 1];
        }
        data[position] = value;
        size++;
        validateInvariant();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int position) {
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException("Indeks di luar batas");
        return (T) data[position];
    }

    public void set(int position, T value) {
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException("Indeks di luar batas");
        data[position] = value;
        validateInvariant();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T remove(int position) {
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException("Indeks di luar batas");
        T removed = (T) data[position];
        for (int i = position; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
        validateInvariant();
        return removed;
    }

    public int search(T value) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(value)) return i;
        }
        return -1;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(T value) { return search(value) != -1; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
        validateInvariant();
    }

    public void validateInvariant() {
        if (data.length < size || size < 0) {
            throw new IllegalStateException("Invariant dilanggar: kapasitas < size");
        }
        for (int i = 0; i < size; i++) {
            if (data[i] == null) {
                throw new IllegalStateException("Invariant dilanggar: elemen aktif bernilai null pada indeks " + i);
            }
        }
    }
}