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