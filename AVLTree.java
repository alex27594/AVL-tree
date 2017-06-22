    import java.util.Comparator;
import java.lang.Math;

/**
 * Created by alexander on 18.06.17.
 */
public class AVLTree<K, V> {
    private AVLTreeNode<K, V> root;
    private Comparator<K> comparator;

    public AVLTree (Comparator<K> comparator) {
        this.comparator = comparator;
        root = null;
    }

    public AVLTree (K key, V value, Comparator<K> comparator) {
        this.comparator = comparator;
        root = AVLTreeNode.makeRoot(key, value, comparator);
    }

    public void add (K key, V value) {
        if (root == null) {
            root = AVLTreeNode.makeRoot(key, value, comparator);
        }
        else {
            root.add(key, value);
            //if the root was changed by rotation
            root = root.searchRoot();
        }
    }

    public void delete (K key) {
        if (root != null) {
            if (root.getLeftChild() == null && root.getRightChild() == null && root.getKey() == key) {
                root = null;
            }
            else {
                root.delete(key);
                //if the root was changed by rotation
                root = root.searchRoot();
            }
        }
    }

    public V search(K key) {
        if (root != null) {
            return root.search(key);
        }
        else {
            return null;
        }
    }

    public void paintTree() {
        if (root != null) {
            root.paintTree();
        }
        else {
            System.out.println("_");
        }
    }

    public int getHeight() {
        return root.getH();
    }
}
