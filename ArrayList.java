package Coding.List;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Simple single-threaded representation of a generic ArrayList.
 *
 * @param <T>
 */
public class ArrayList<T> implements List<T> {

    /**
     * Initial array size, when such is not specified.
     */
    private final static int DEFAULT_SIZE = 10;

    /**
     * The internal elements counter.
     */
    private int index = 0;

    /**
     * The factor by which a resizing will be trigger when required.
     */
    private final static int RESIZING_FACTOR = 2;

    /**
     * Represents the percentage of elements occupying the array space,
     * if that percentage if bigger than 75%, a resizing should take place, multiplying
     * the current size with <code>RESIZING_FACTOR</code>, aiming to achieve occupancy less
     * than it.
     *
     * @see #RESIZING_FACTOR
     */
    private final static int LOAD_FACTOR = 75;

    /**
     * The backing array.
     */
    private T[] arr;

    /**
     * Default constructor initializing the backing array with default size.
     *
     * @see #DEFAULT_SIZE
     */
    @SuppressWarnings("unchecked")
    public ArrayList() {
        arr = (T[]) (new Object[DEFAULT_SIZE]);
    }

    /**
     * Constructor with one parameter, allowing users to customize their desired size,
     * avoiding any further costly resizes along the way.
     *
     * @param initialSize represents the desired size that will be initialized at first.
     * @throws InvalidParameterException when the initialSize is less than 0.
     */
    @SuppressWarnings("unchecked")
    public ArrayList(int initialSize) {
        if (initialSize < 0) {
            throw new InvalidParameterException("Size cannot be less than 0.");
        }

        arr = (T[]) new Object[initialSize];
    }

    /**
     * Constructor that gives the ability to provide another collection with various number of elements,
     * which is being accepted as an initial version, making sure that after the elements are added
     * the occupancy should still be less than <code>LOAD_FACTOR</code> percentages.
     *
     * @see #LOAD_FACTOR
     *
     * @param collection is the data-structure that will be passed and accepted as initial version.
     * @throws InvalidParameterException when <code>NULL</code> is provided, or the size of the collection is 0.
     */
    @SuppressWarnings("unchecked")
    public ArrayList(Collection<T> collection) {
        if (collection == null) {
            throw new InvalidParameterException("NULL cannot be provided as an argument.");
        }

        int collSize = collection.size();

        if (collSize == 0) {
            throw new InvalidParameterException("Collection size should not be 0.");
        }

        int size = allocateEnoughSpace(DEFAULT_SIZE, collSize);

        arr = (T[]) new Object[size];

        System.arraycopy(collection.toArray(), 0, arr, 0, collSize);
        updateIndex(index + collSize);
    }

    /**
     * Returns the elements from the array.
     *
     * @return int representing their count.
     */
    public int size() {
        return index;
    }

    /**
     * Check if the ArrayList doesn't have any elements.
     *
     * @return boolean value indicating if the data-structure is empty.
     */
    public boolean isEmpty() {
        return index == 0;
    }

    /**
     * Adds an element and allocates more space if necessary.
     *
     * @param element represents the entity that will be added in the list.
     */
    public void add(T element) {
        arr[index++] = element;

        probeResizing();
    }

    /**
     * Adds an element in a specified position.
     *
     * <p>
     *     <i>
     *         If there are elements after the position at which the element will be added,
     *         all of them will be shifted with 1 position to the right.
     *     </i>
     * </p>
     *
     * @param element represents the entity that will be added in the list.
     * @param position is the specified index at which it will be added.
     * @throws IndexOutOfBoundsException when the position to be added is out of bounds.
     */
    public void add(T element, int position) {
        if (position < 0 || position > index) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }

        if (position != index) {
            shiftElementsRight(position, 1);
        }

        arr[position] = element;
        updateIndex(index + 1);
    }

    /**
     * Adds a collection of elements.
     *
     * <p>
     *     <i>
     *         If there's not enough space to add the provided collection,
     *         a resize is being triggered allocating enough space so that they can fit
     *         and preserve <code>LOAD_FACTOR</code> value.
     *     </i>
     * </p>
     *
     * @param collection is the set of element that will be provided as an argument.
     * @throws InvalidParameterException if the collection is NULL
     * @see #LOAD_FACTOR
     */
    public void addAll(Collection<? super T> collection) {
        if (collection == null) {
            throw new InvalidParameterException("NULL cannot be passed as collection argument.");
        }

        int collSize = collection.size();

        if (collSize <= 0) {
            return;
        }

        int size = allocateEnoughSpace(arr.length, collSize);

        if (size != arr.length) {
            resizeArrayAndCopyContent(size);
        }

        System.arraycopy(collection.toArray(), 0, arr, index, collSize);
        updateIndex(index + collSize);
    }

    public T remove(T element) {
        // @TODO try to find a better way, instead of linear search
        return null;
    }

    /**
     * Remove an element corresponding to the provided position.
     *
     * @param position is the index that holds the element to be removed.
     * @return true if the element was deleted, false otherwise
     * @throws IndexOutOfBoundsException if the provided position is not in the defined range.
     */
    public boolean remove(int position) {
        if (isIndexOutOfBounds(position)) {
            return false;
        }

        int lastElementPos = --index;

        if (position == lastElementPos) {
            arr[position] = null;
        } else {
            shiftElementsLeft(position);
            arr[lastElementPos] = null;
        }

        return true;
    }

    /**
     * Clears all elements within the list.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        arr = (T[]) new Object[arr.length];
        updateIndex(0);
    }

    /**
     * Get an element corresponding to a certain position.
     *
     * @param position is the index from which the element will be taken.
     * @return the element
     * @throws IndexOutOfBoundsException if the position is not in the defined range.
     */
    public T get(int position) {
        if (position < 0 || position >= arr.length) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }

        return arr[position];
    }

    @Override
    public String toString() {
        return Arrays.toString(arr);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            int idx = 0;

            @Override
            public boolean hasNext() {
                return idx < index;
            }

            @Override
            public T next() {
                return arr[idx++];
            }
        };
    }

    /**
     * Check if the provided index is out of bounds.
     *
     * @param index the value that will be checked against the defined constraints.
     * @return true if it's out of bounds, false otherwise.
     */
    private boolean isIndexOutOfBounds(int index) {
        return index < 0 || index >= this.index;
    }

    /**
     * Check the occupancy of the array and resize (along with copying all current content) accordingly, if necessary.
     *
     * @see #LOAD_FACTOR
     * @see #RESIZING_FACTOR
     */
    private void probeResizing() {
        if (getPercentageOccupied(index, arr.length) >= LOAD_FACTOR) {
            resizeArrayAndCopyContent(arr.length * RESIZING_FACTOR);
        }
    }

    /**
     * Switch the current backing array with one which has more space, along with copying the content.
     *
     * @param size is the newly calculated size, enough to fit a specific number of elements.
     */
    @SuppressWarnings("unchecked")
    private void resizeArrayAndCopyContent(int size) {
        T[] resizedArr = (T[]) new Object[size];
        System.arraycopy(arr, 0, resizedArr, 0, index);
        arr = resizedArr;
    }

    /**
     * Get how many percentages are occupied from the list.
     *
     * @param elements represents the number of already taken positions in the list.
     * @param totalElements is the allocated size, including the empty spaces of the list.
     * @return the percentages occupied.
     */
    private long getPercentageOccupied(int elements, int totalElements) {
        return Math.round(((float) elements / (float) totalElements) * 100);
    }

    /**
     * Shift elements 1 position to the left.
     *
     * @param fromIndex is the position from which it's started.
     */
    private void shiftElementsLeft(int fromIndex) {
        System.arraycopy(arr, fromIndex + 1, arr, fromIndex, index - fromIndex);
    }

    private int allocateEnoughSpace(int currentLength, int collectionSize) {
        int arrSpace = currentLength;

        // @TODO Optimize
        while ((arrSpace - index) < collectionSize || getPercentageOccupied(index + collectionSize, arrSpace) > LOAD_FACTOR) {
            arrSpace *= RESIZING_FACTOR;
        }

        return arrSpace;
    }

    /**
     * Shift elements starting from <b>fromIndex</b> - <b>positionsRight</b> to the right.
     *
     * @param fromIndex represents the position from which all values forward will be moved.
     * @param positionsRight represents how many positions those values will be moved to the right.
     */
    private void shiftElementsRight(int fromIndex, int positionsRight) {
        if (index - fromIndex >= 0) {
            System.arraycopy(arr, fromIndex, arr, fromIndex + positionsRight, index - fromIndex);
        }
    }

    /**
     * Update the internal index, indicating how many elements are occupying the backing array.
     *
     * @param size is the new value, taking into account the newly added element/s.
     */
    private void updateIndex(int size) {
        index = size;
    }
}
