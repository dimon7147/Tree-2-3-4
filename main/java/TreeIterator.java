import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/***
 * Итератор по обходу дерева 2-3-4
 * @param <E> Тип дерева
 */
public class TreeIterator<E extends Comparable<E>> implements Iterable<E> {
    private final Tree234<E> tree;
    private final ArrayList<E> list;
    private int passed = 0;

    TreeIterator(Tree234<E> tree) {
        this.tree = tree;
        this.list = tree.getAllElements();
        Collections.sort(this.list);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return list.size() > passed;
            }

            @Override
            public E next() {
                E el = list.get(passed);
                passed++;
                return el;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

