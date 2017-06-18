import java.util.Comparator;
import java.lang.Math;

/**
 * Created by alexander on 18.06.17.
 */
public class Tree<K, V> {
    private TreeNode<K, V> root;

    public Tree (K key, V value, Comparator<? super K> comparator) {
        root = TreeNode.makeRoot(key, value, comparator);
    }

    public void add (K key, V value) {
        root.add(key, value);
        //if the root was changed by rotation
        root = root.searchRoot();
    }

    public void delete (K key) {
        root.delete(key);
        //if the root was changed by rotation
        root = root.searchRoot();
    }

    public V search(K key) {
        return root.search(key);
    }

    public void paintTree() {
        root.paintTree();
    }
}
class TreeNode<K, V> {
    private int h;
    private TreeNode parent;
    private TreeNode leftChild;
    private TreeNode rightChild;
    private K key;
    private V value;
    private Comparator<? super K> comparator;

    private TreeNode(int h, TreeNode parent, TreeNode leftChild, TreeNode rightChild, K key, V value, Comparator<? super K> comparator) {
        this.h = h;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.key = key;
        this.value = value;
        this.comparator = comparator;
    }

    public static <K, V> TreeNode<K, V> makeRoot(K key, V value, Comparator<? super K> comparator) {
        return new TreeNode<>(1, null, null, null, key, value, comparator);
    }

    private TreeNode<K, V> makeChild(K key, V value) {
        return new TreeNode<>(1, this, null, null, key, value, this.comparator);
    }

    public void add(K key, V value) {
        //equal
        if (comparator.compare(key, this.key) == 0) {
            this.value = value;
        }
        //less
        else if (comparator.compare(key, this.key) < 0) {
            //have left node
            if (leftChild != null) {
                leftChild.add(key, value);
            }
            //have not left node
            else {
                TreeNode<K, V> node = this.makeChild(key, value);
                leftChild = node;
                //balance
                balancing();
            }
        }
        //greater
        else {
            //have right node
            if (rightChild != null) {
                rightChild.add(key, value);
            }
            //have not right node
            else {
                TreeNode<K, V> node = this.makeChild(key, value);
                rightChild = node;
                //balance
                balancing();
            }
        }
    }

    private void recountH() {
        if (leftChild != null && rightChild != null) {
            h = Math.max(leftChild.h, rightChild.h) + 1;
        }
        else if (leftChild != null) {
            h = leftChild.h + 1;
        }
        else if (rightChild != null) {
            h = rightChild.h + 1;
        }
        else {
            h = 1;
        }
    }

    private void balancing() {
        int leftH;
        int rightH;
        if (leftChild != null && rightChild != null) {
            leftH = leftChild.h;
            rightH = rightChild.h;
        }
        else if (leftChild != null) {
            leftH = leftChild.h;
            rightH = 0;
        }
        else if (rightChild != null){
            leftH = 0;
            rightH = rightChild.h;
        }
        else {
            leftH = 0;
            rightH = 0;
            h = 1;
            if (parent != null) {
                parent.balancing();
            }
        }
        //right case
        if (rightH - leftH > 1) {
            //small rotation
            if (rightChild.rightChild != null && rightChild.rightChild.h + 1 - leftH > 1) {
                TreeNode<K, V> p = this.parent;
                TreeNode<K, V> beta = this.rightChild;
                TreeNode<K, V> gamma = this.rightChild.leftChild;

                //this <--> gamma
                this.rightChild = gamma;
                if (gamma != null) {
                    gamma.parent = this;
                }

                //beta <--> this
                beta.leftChild = this;
                this.parent = beta;

                //p <--> beta
                if (p != null) {
                    if (p.leftChild == this) {
                        p.leftChild = beta;
                    }
                    else {
                        p.rightChild = beta;
                    }
                }
                beta.parent = p;

                //recounting h
                this.recountH();
                beta.recountH();

                //balance parent
                if (p != null) {
                    p.balancing();
                }
            }
            //big rotation
            else {
                TreeNode<K, V> p = parent;
                TreeNode<K, V> beta = rightChild;
                TreeNode<K, V> gamma = rightChild.leftChild;
                TreeNode<K, V> gamma1 = gamma.leftChild;
                TreeNode<K, V> gamma2 = gamma.rightChild;

                //this <--> gamma1
                this.rightChild = gamma1;
                if (gamma1 != null) {
                    gamma1.parent = this;
                }

                //beta <--> gamma2
                beta.leftChild = gamma2;
                if (gamma2 != null) {
                    gamma2.parent = beta;
                }

                //gamma <--> this
                gamma.leftChild = this;
                this.parent = gamma;

                //gamma <--> beta
                gamma.rightChild = beta;
                beta.parent = gamma;

                //p <--> gamma
                if (p != null) {
                    if (p.leftChild == this) {
                        p.leftChild = gamma;
                    }
                    else {
                        p.rightChild = gamma;
                    }
                }
                gamma.parent = p;

                //recounting h
                this.recountH();
                beta.recountH();
                gamma.recountH();

                //balance parent
                if (p != null) {
                    p.balancing();
                }
            }
        }
        //left case
        else if (leftH - rightH > 1) {
            //small rotation
            if (leftChild.leftChild != null && leftChild.leftChild.h + 1 - rightH > 1) {
                TreeNode<K, V> p = this.parent;
                TreeNode<K, V> beta = this.leftChild;
                TreeNode<K, V> gamma = this.leftChild.rightChild;

                //this <--> gamma
                this.leftChild = gamma;
                if (gamma != null) {
                    gamma.parent = this;
                }

                //beta <--> this
                beta.rightChild = this;
                this.parent = beta;

                //p <--> beta
                if (p != null) {
                    if (p.leftChild == this) {
                        p.leftChild = beta;
                    }
                    else {
                        p.rightChild = beta;
                    }
                }
                beta.parent = p;

                //recounting h
                this.recountH();
                beta.recountH();

                //balance parent
                if (p != null) {
                    p.balancing();
                }
            }
            //big rotation
            else {
                TreeNode<K, V> p = parent;
                TreeNode<K, V> beta = leftChild;
                TreeNode<K, V> gamma = leftChild.rightChild;
                TreeNode<K, V> gamma1 = gamma.rightChild;
                TreeNode<K, V> gamma2 = gamma.leftChild;

                //this <--> gamma1
                this.leftChild = gamma1;
                if (gamma1 != null) {
                    gamma1.parent = this;
                }

                //beta <--> gamma2
                beta.rightChild = gamma2;
                if (gamma2 != null) {
                    gamma2.parent = beta;
                }

                //gamma <--> this
                gamma.rightChild = this;
                this.parent = gamma;

                //gamma <--> beta
                gamma.leftChild = beta;
                beta.parent = gamma;

                //p <--> gamma
                if (p != null) {
                    if (p.leftChild == this) {
                        p.leftChild = gamma;
                    }
                    else {
                        p.rightChild = gamma;
                    }
                }
                gamma.parent = p;

                //recounting h
                beta.recountH();
                this.recountH();
                gamma.recountH();

                //balance parent
                if (p != null) {
                    p.balancing();
                }
            }
        }
        else {
            recountH();
            if (parent != null) {
                parent.balancing();
            }
        }
    }


    public V search(K key) {
        //equal
        if (comparator.compare(key, this.key) == 0) {
            return value;
        }
        //less
        else if (comparator.compare(key, this.key) < 0) {
            //have left node
            if (leftChild != null) {
                return (V)leftChild.search(key);
            }
            //have not left node
            else {
                return null;
            }
        }
        //greater
        else {
            //have right node
            if (rightChild != null) {
                return (V)rightChild.search(key);
            }
            //have not right node
            else {
                return null;
            }
        }
    }

    public void delete(K key) {
        //equal
        if (comparator.compare(key, this.key) == 0) {
            deleteNode();
        }
        //less
        else if (comparator.compare(key, this.key) < 0) {
            //have left node
            if (leftChild != null) {
                leftChild.delete(key);
            }
        }
        //greater
        else {
            //have right node
            if (rightChild != null) {
                rightChild.delete(key);
            }
        }
    }

    private void deleteNode() {
        if (leftChild == null && rightChild == null) {
            if (parent != null) {
                if (parent.leftChild == this) {
                    parent.leftChild = null;
                }
                else {
                    parent.rightChild = null;
                }
                parent.balancing();
            }
            else {
                throw new LastTreeNodeDeletionException();
            }
        }
        else if (leftChild != null && rightChild == null) {
            key = (K)leftChild.key;
            value = (V)leftChild.value;
            leftChild = null;
            recountH();
            if (parent != null) {
                parent.balancing();
            }
        }
        else if (leftChild == null && rightChild != null) {
            key = (K)rightChild.key;
            value = (V)rightChild.value;
            rightChild = null;
            recountH();
            if (parent != null) {
                parent.balancing();
            }
        }
        //leftChild != null && rightChild != null
        else {
            TreeNode<K, V> maxLeftNode = leftChild.searchMax();
            K key1 = maxLeftNode.key;
            V value1 = maxLeftNode.value;

            K key2 = this.key;
            V value2 = this.value;

            maxLeftNode.key = key2;
            maxLeftNode.value = value2;

            this.key = key1;
            this.value = value1;

            maxLeftNode.deleteNode();
        }
    }

    private TreeNode<K, V> searchMax() {
        if (rightChild != null) {
            return rightChild.searchMax();
        }
        else {
            return this;
        }
    }

    public void paintTree() {
        System.out.print("(");
        if (leftChild != null) {
            leftChild.paintTree();
        }
        else {
            System.out.print("_");
        }
        System.out.print(")<-");
        System.out.print("[");
        System.out.print(key);
        System.out.print(", ");
        System.out.print(value);
        System.out.print(", h=");
        System.out.print(h);
        System.out.print("]");
        System.out.print("->(");
        if (rightChild != null) {
            rightChild.paintTree();
        }
        else {
            System.out.print("_");
        }
        System.out.print(")");
    }

    TreeNode<K, V> searchRoot() {
        if (parent == null) {
            return this;
        }
        else {
            return parent.searchRoot();
        }
    }
}

class LastTreeNodeDeletionException extends RuntimeException {}
