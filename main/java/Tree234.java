import java.util.ArrayList;

/***
 * Класс, являющийся представлением дерева 2-3-4
 */
class Tree234<E extends Comparable<E>> {
    private Node<E> root = new Node<>();
    /***
     * Метод для поиска элемента в дереве
     * @param value Элемент для поиска
     * @return Индекс элемента в массиве элементов узла / -1 в случае отсутствия элемента
     */
    public int find(E value) {
        Node<E> curNode = root;
        int childNumber;
        while (true) {
            // Для каждого объекта узла вызывается findItem(), чтобы узнать, находится ли предмет в этом узле
            if ((childNumber = curNode.findItem(value)) != -1) {
                return childNumber; // Да, он находится в этом узле. Элемент найден.
            } else if (curNode.isLeaf()) { // Если данный узел - лист, значит искать дальше смысла нет.
                return -1;
            } else { // С помошью метода getNextChild определяем, куда дальше двигаться в поисках элемента.
                curNode = getNextChild(curNode, value);
            }
        }
    }
    /***
     * Метод для поиска узла, содержащего элемент
     * @param value Элемент для поиска
     * @return Узел, содержащий элемент
     */
    private Node<E> findNode(E value) {
        Node<E> curNode = root;
        int childNumber;
        while (true) {
            // Для каждого объекта узла вызывается findItem(), чтобы узнать, находится ли предмет в этом узле
            if ((childNumber = curNode.findItem(value)) != -1) {
                return curNode; // Да, он находится в этом узле. Элемент найден.
            } else if (curNode.isLeaf()) { // Если данный узел - лист, значит искать дальше смысла нет.
                return null;
            } else { // С помошью метода getNextChild определяем, куда дальше двигаться в поисках элемента.
                curNode = getNextChild(curNode, value);
            }
        }
    }

    /***
     * Метод для вставки элемента в дерево
     * @param value Элемент для вставки
     */
    public void insert(E value) {
        Node<E> curNode = root;
        while(true) {
            if (curNode.isFull()) {
                // Если узел полон, то его нужно разбить.
                split(curNode);
                curNode = curNode.getParent();
                curNode = getNextChild(curNode, value);
            } else if (curNode.isLeaf()) {
                // Нашли лист - будем в него вставлять элемент
                break;
            } else {
                // Все еще не нашли лист, ищем дальше
                curNode = getNextChild(curNode, value);
            }
        }
        // Вставляем элемент
        curNode.insertItem(value);
    }

    /***
     * Разбивает узел
     * @param thisNode Узел, который необходимо разбить
     */
    public void split(Node<E> thisNode) {
        // Предполагается, что нода полная
        E itemB, itemC;
        Node<E> parent, child2, child3;
        int itemIndex;
        // Отсоединяем два правых элемента от узла, сохраняя их во временные переменные
        itemC = thisNode.removeItem();
        itemB = thisNode.removeItem();
        // Отсоединяем два правых потомка от узла, сохраняя их во временные переменные
        child2 = thisNode.disconnectChild(2);
        child3 = thisNode.disconnectChild(3);
        // Создаем новый узел, который будет размещен справа от разбиваемого узла
        Node<E> newRight = new Node<>();
        // Если разбиваемый узел корневой, создаем дополнительный корневой узел
        if (thisNode == root) {
            root = new Node<>();
            parent = root;
            // Задаем родителя, им является только что созданный узел.
            root.connectChild(0, thisNode);
        } else {
            // Задаем родителя, т.к узел не корневой, то родитель остается
            parent = thisNode.getParent();
        }
        // Элемент B вставляется в родительский узел
        itemIndex = parent.insertItem(itemB);
        int n = parent.getNumItems();
        // При необходимости существующие потомки родителя отсоединяются от него и присоединяются на одну позицию правее,
        // чтобы освободить место для нового элемента данных и новых связей
        for(int j = n - 1; j > itemIndex; j--) { // Двигаем соединения родителей
            Node<E> temp = parent.disconnectChild(j); // один ребенок
            parent.connectChild(j + 1, temp);        // вправо
        }
        // Присоединяем узел newRight к родителю
        parent.connectChild(itemIndex + 1, newRight);

        // Разбираемся с newRight
        // Вставляем элемент C в узел
        newRight.insertItem(itemC);
        // Присоединяем к нему потомки 2 и 3
        newRight.connectChild(0, child2);
        newRight.connectChild(1, child3);
    }

    /***
     * Метод, который определяем узел, в который нужно вставить значение
     * @param node Узел, потомка которого мы хотим получить
     * @param value Значение, которое мы хотим вставить
     * @return Узел, в который нужно вставить theValue
     */
    public Node<E> getNextChild(Node<E> node, E value) {
        int j;
        // Предполагается, что узел не пуст, не полон и не лист
        int numItems = node.getNumItems();
        // Обходим каждый элемент в узле
        for (j = 0; j < numItems; j++) {
            // Значение, которое мы хотим вставить, меньше?
            if (value.compareTo(node.getItem(j)) < 0) {
//            if (value < node.getItem(j)) {
                // Возвращаем левого потомка
                return node.getChild(j);
            }
        }
        // Возвращаем правого потомка
        return node.getChild(j);
    }
    /***
     * Метод для получения списка элементов, меньших element
     * @param element Элемент, меньше которого нужно искать
     * @return Список элементов, меньших element
     */
    public ArrayList<E> getListOfElementsSmallerThat(E element) {
        return new ArrayList<>(recFindSmallerItems(root, element));
    }

    /***
     * Рекурсивно обходит узлы в поисках элементов, меньших element
     * @param node Узел, с которого следуюет начать поиск
     * @param element Элемент, меньше которого нужно искать
     * @return Список элементов, меньших element
     */
    private ArrayList<E> recFindSmallerItems(Node<E> node, E element) {
        if (node == null) {
            return new ArrayList<>();
        }
        ArrayList<E> list = new ArrayList<>();
        ArrayList<Node<E>> nodes = new ArrayList<>();
        int j = 0;
        // Проверяем, есть ли в узле предметы, меньше element
        for (E el : node.getItemArray()) {
            if (el == null) {
                continue;
            }
            if (el.compareTo(element) < 0) {
                // Нашли такой элемент, добавляем его в список найденных
                list.add(el);
                // Добавляем узел-потомок, в котором будем продолжать поиск остальных, т.к element < A
                nodes.add(node.getChild(j));
                // Проверяем, пустой ли следующий элемент. Если да, то добавляем в поиск еще и следующего потомка
                if (!node.isLeaf() && node.getItem(j+1) == null) {
                    nodes.add(node.getChild(j+1));
                }
            }
            j++;
        }
        if (node.isLeaf()) {
            return list;
        }
        // В данном узле нет элементов, меньших element, поэтому он в 0 потомке
        if (nodes.size() == 0) {
            nodes.add(node.getChild(0));
        }
        for (Node<E> n : nodes) {
            list.addAll(recFindSmallerItems(n, element));
        }
        return list;
    }
    /***
     * Метод для получения списка элементов, больших element
     * @param element Элемент, больше которого нужно искать
     * @return Список элементов, больших element
     */
    public ArrayList<E> getListOfElementsBiggerThat(E element) {
        return new ArrayList<>(recFindBiggerItems(root, element));
    }
    /***
     * Рекурсивно обходит узлы в поисках элементов, больших element
     * @param node Узел, с которого следуюет начать поиск
     * @param element Элемент, больше которого нужно искать
     * @return Список элементов, больших element
     */
    private ArrayList<E> recFindBiggerItems(Node<E> node, E element) {
        if (node == null) {
            return new ArrayList<>();
        }
        ArrayList<E> list = new ArrayList<>();
        ArrayList<Node<E>> nodes = new ArrayList<>();
        int j = 0;
        // Проверяем, есть ли в узле предметы, больше element
        for (E el : node.getItemArray()) {
            if (el == null) {
                continue;
            }
            if (el.compareTo(element) > 0) {
                // Нашли такой элемент, добавляем его в список найденных
                list.add(el);
                // Добавляем узел-потомок, в котором будем продолжать поиск остальных, т.к element > A
                nodes.add(node.getChild(j));
                // Проверяем, пустой ли следующий элемент. Если да, то добавляем в поиск еще и следующего потомка
                if (!node.isLeaf() && node.getItem(j+1) == null) {
                    nodes.add(node.getChild(j+1));
                }
            }
            j++;
        }
        if (node.isLeaf()) {
            return list;
        }
        // В данном узле нет элементов, больших element, поэтому он в 1 потомке
        if (nodes.size() == 0) {
            nodes.add(node.getChild(1));
        }
        for (Node<E> n : nodes) {
            list.addAll(recFindBiggerItems(n, element));
        }
        return list;
    }
    /***
     * Рекурсивно обходит узлы в поисках элементов, находящихся в диапозоне от from до to (включительно)
     * @param from Начиная с какого элемента нужно искать
     * @param to Заканчивая каким элементом нужно искать
     * @return Список элементов, находящихся в диапозоне от from до to (включительно)
     */
    public ArrayList<E> getListOfElementsInRange(E from, E to) {
        return new ArrayList<>(recFindInRange(root, from, to));
    }
    /***
     * Рекурсивно обходит узлы в поисках элементов, находящихся в диапозоне от from до to (включительно)
     * @param node Узел, с которого следуюет начать поиск
     * @param from Начиная с какого элемента нужно искать
     * @param to Заканчивая каким элементом нужно искать
     * @return Список элементов, находящихся в диапозоне от from до to (включительно)
     */
    private ArrayList<E> recFindInRange(Node<E> node, E from, E to) {
        if (node == null) {
            return new ArrayList<>();
        }
        ArrayList<E> list = new ArrayList<>();
        ArrayList<Node<E>> nodes = new ArrayList<>();
        int j = 0;
        // Проверяем, есть ли в узле предметы, больше element
        for (E el : node.getItemArray()) {
            if (el == null) {
                continue;
            }
            if (from.compareTo(el) <= 0 && to.compareTo(el) >= 0) {
                // Нашли такой элемент, добавляем его в список найденных
                list.add(el);
                // Добавляем узел-потомок, в котором будем продолжать поиск остальных, т.к element > A
                nodes.add(node.getChild(j));
                // Проверяем, пустой ли следующий элемент. Если да, то добавляем в поиск еще и следующего потомка
                if (!node.isLeaf() && node.getItem(j+1) == null) {
                    nodes.add(node.getChild(j+1));
                }
            }
            j++;
        }
        if (node.isLeaf()) {
            return list;
        }
        // В данном узле нет нужных элементов, поэтому он в 1 потомке
        if (nodes.size() == 0) {
            nodes.add(node.getChild(1));
        }
        for (Node<E> n : nodes) {
            list.addAll(recFindInRange(n, from, to));
        }
        return list;
    }
    /***
     * Метод, возвращающий наименьший элемент в дереве
     * @return Наименьший элемент в дереве
     */
    public E getSmallest() {
        // Обходим дерево по левой ветке и возвращаем 0 элемент, по правилу построения 2-4 дерева, он будет наименьшим.
        Node<E> node = root;
        while (!node.isLeaf()) {
            node = node.getChild(0);
        }
        return node.getItem(0);
    }
    /***
     * Метод, возвращающий наибольший элемент в дереве
     * @return Наибольший элемент в дереве
     */
    public E getLargest() {
        // Обходим дерево по правой ветке и возвращаем последний элемент,
        // по правилу построения 2-4 дерева, он будет наибольшим.
        Node<E> node = root;
        while (!node.isLeaf()) {
            node = node.getChild(node.getNumItems());
        }
        return node.getItem(node.getNumItems() - 1);
    }

    public ArrayList<E> getAllElements() {
        return getAllElementsRec(root, 0);
    }

    public ArrayList<E> getAllElementsRec(Node<E> node, int level) {
        ArrayList<E> elements = new ArrayList<>(node.getItemArrayWithoutNull());
        // Рекурсивный вызов для каждого потомка текущего узла
        int numItems = node.getNumItems();
        for(int j = 0; j < numItems + 1; j++) {
            Node<E> nextNode = node.getChild(j);
            if (nextNode != null) {
                elements.addAll(getAllElementsRec(nextNode, level+1));
            } else {
                return elements;
            }
        }
        return elements;
    }


    @Override
    public String toString() {
        return recDisplayTree(root, 0, 0);
    }

    /***
     * Рекурсивный обход дерева
     * @param thisNode Узел, с которого следует начать обход
     * @param level Уровень вложенности узла
     * @param childNumber Порядковый номер потомка
     * @return Результат обхода в строке
     */
    private String recDisplayTree(Node<E> thisNode, int level, int childNumber) {
        StringBuilder builder = new StringBuilder();
        builder.append("level=").append(level).append(" child=").append(childNumber).append(" ");
        builder.append(thisNode).append("\n");
        // Рекурсивный вызов для каждого потомка текущего узла
        int numItems = thisNode.getNumItems();
        for(int j = 0; j < numItems + 1; j++) {
            Node<E> nextNode = thisNode.getChild(j);
            if (nextNode != null) {
                builder.append(recDisplayTree(nextNode, level+1, j));
            } else {
                return builder.toString();
            }
        }
        return builder.toString();
    }

    public Node<E> getRoot() {
        return root;
    }
}