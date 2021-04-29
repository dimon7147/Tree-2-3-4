import java.util.ArrayList;
import java.util.Collections;

/***
 * Класс, являющийся представлением узла дерева 2-3-4
 */
class Node<E extends Comparable<E>> {
    private static final int ORDER = 4;
    private int numItems;
    private Node<E> parent;
    private final ArrayList<Node<E>> childArray = new ArrayList<>(ORDER);
    private ArrayList<E> itemArray = new ArrayList<>(ORDER-1);

    public Node() {
        // Заполняем ArrayList нуллами, чтобы тот не выбрасывал исключения
        for (int i = 0; i < ORDER - 1; i++) {
            childArray.add(null);
            itemArray.add(null);
        }
        childArray.add(null);
    }

    /***
     * Метод для получения левого брата узла
     * @return левого брат узла
     */
    public Node<E> getLeftBrother() {
        Node<E> parent = this.getParent();
        int i = 0;
        for (i = 0; i < parent.getNumItems() + 1; i++) {
            if (parent.getChild(i) == this) {
                break;
            }
        }
        // Мы самые левые, у нас нет левого брата
        if (i == 0) {
            return null;
        }
        return parent.getChild(i-1);
    }
    /***
     * Метод для получения правого брата узла
     * @return правый брат узла
     */
    public Node<E> getRightBrother() {
        Node<E> parent = this.getParent();
        int i = 0;
        for (i = 0; i < parent.getNumItems() + 1; i++) {
            if (parent.getChild(i) == this) {
                break;
            }
        }
        // Мы самые правые, нет у нас правого брата
        if (i == parent.getNumItems()) {
            return null;
        }
        return parent.getChild(i+1);
    }

    /***
     * Метод, связывающий узел с потомком
     * @param childNum Порядковый номер потомка
     * @param child Потомок, который необходимо присоединить к узлу
     */
    public void connectChild(int childNum, Node<E> child) {
        childArray.set(childNum, child);
        if (child != null) {
            child.parent = this;
        }
    }
    /***
     * Метод, отсоединяющий потомка от узла
     * @param childNum Порядковый номер потомка
     * @return Отсоединенный узел
     */
    public Node<E> disconnectChild(int childNum) {
        Node<E> tempNode = childArray.get(childNum);
        childArray.set(childNum, null);
        return tempNode;
    }

    /***
     * Метод, возвращающий потомка
     * @param childNum Порядковый номер потомка
     * @return Узел-потомок
     */
    public Node<E> getChild(int childNum) {
        return childArray.get(childNum);
    }

    /***
     * Метод, возвращающий родителя текущего узла
     * @return Узел-родитель
     */
    public Node<E> getParent() {
        return parent;
    }

    /***
     * Метод, определяющий является ли узел листом (не имеет потомков)
     * @return true - узел является листом, false - узел не лист
     */
    public boolean isLeaf() {
        return childArray.get(0) == null;
    }
    public int getNumItems() {
        return numItems;
    }
    public E getItem(int index) {
        return itemArray.get(index);
    }

    /***
     * Метод, определяющий заполнен ли узел
     * @return true - узел заполнен, false - узел не полон
     */
    public boolean isFull() {
        return numItems == ORDER - 1;
    }

    /***
     * Определение индекса элемента в пределах узла
     * @param element Элемент для поиска
     * @return Индекс элемента / -1, если он не найден
     */
    public int findItem(E element) {
        for (int j = 0; j < ORDER - 1; j++) {
            if (itemArray.get(j) == null) {
                break;
            } else if (itemArray.get(j).equals(element)) {
                return j;
            }
        }
        return -1;
    }

    /***
     * Вставка нового элемента в узел
     * @param newItem Элемент для вставки
     * @return Индекс вставленного элемента в узле
     */
    public int insertItem(E newItem) {
        // Предполагается, узел не пуст
        numItems++;
        // Начиная справа проверяем элементы
        for(int j = ORDER - 2; j >= 0; j--) {
            // Если ячейка пуста, переходим на одну ячейку влево
            if(itemArray.get(j) == null) {
                continue;
                // Иначе, получаем ее ключ
            } else {
                E itsKey = itemArray.get(j);
                // Если новый элемент больше, сдвигаем его вправо
                if (newItem.compareTo(itsKey) < 0) {
                    itemArray.set(j+1, itemArray.get(j));
                } else {
                    // Вставляем новый элемент
                    itemArray.set(j+1, newItem);
                    // Возвращаем его индекс
                    return j + 1;
                }
            }
        }
        itemArray.set(0, newItem);
        return 0;
    }

    /***
     * Удаление наибольшего элемента в узле
     * @return Удаленный элемент
     */
    public E removeItem() {
        // Предполагается, узел не пуст
        E temp = itemArray.get(numItems-1);
        itemArray.set(numItems-1, null);
        numItems--;
        return temp;
    }
    /***
     * Удаление элемента в узле и сортировка
     * @param id Порядковый номер элемента для удаления
     * @return Удаленный элемент
     */
    public E removeItemAndSort(int id) {
        // Предполагается, узел не пуст
        E temp = itemArray.get(id);
        itemArray.set(id, null);
        numItems--;
        ArrayList<E> list = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            list.add(itemArray.get(i));
        }
        Collections.sort(list);
        while (list.size() != 3) {
            list.add(null);
        }
        itemArray = list;
        return temp;
    }
    /***
     * Удаление элемента в узле
     * @param id Порядковый номер элемента для удаления
     * @return Удаленный элемент
     */
    public E removeItem(int id) {
        // Предполагается, узел не пуст
        E temp = itemArray.get(id);
        itemArray.set(id, null);
        numItems--;
        return temp;
    }

    public ArrayList<E> getItemArray() {
        return itemArray;
    }

    public ArrayList<E> getItemArrayWithoutNull() {
        ArrayList<E> list = new ArrayList<>();
        for(int j = 0; j < numItems; j++) {
            list.add(itemArray.get(j));
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int j = 0; j < numItems; j++) {
            builder.append("|").append(itemArray.get(j));
        }
        builder.append("|");
        return builder.toString();
    }
}
