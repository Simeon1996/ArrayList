package Coding.List;

import java.util.Collection;

public interface List<T> extends Iterable<T> {

    boolean isEmpty();

    int size();

    void add(T element);

    void add(T element, int position);

    void addAll(Collection<? super T> elements);

    boolean remove(int position);

    void clear();

    T get(int position);
}
