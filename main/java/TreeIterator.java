import java.util.Iterator;

public class TreeIterator implements Iterable<String> {
    private final Tree234 tree;

    TreeIterator(Tree234 tree) {
        this.tree = tree;
    }

    private Node getLeftLeafNode() {
        Node curNode = tree.getRoot();
        while (true) {
            if (curNode.isLeaf()) {
                return curNode;
            } else {
                curNode = curNode.getChild(0);
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int lastElementId = 0;
            private String currentElement;
            private Node currentNode = getLeftLeafNode();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                return "";
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

