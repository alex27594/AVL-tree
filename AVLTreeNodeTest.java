import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Created by alexander on 23.06.17.
 */
public class AVLTreeNodeTest {
    private AVLTree<Integer, String> tree;

    @Before
    public void setUp() throws Exception {
        tree = new AVLTree<Integer, String>(Integer::compare);
    }

    @Test
    public void testAdding() throws Exception {
        tree.add(10, "Spider-man");
        tree.add(15, "Iron-man");
        tree.add(5, "Hawk-eye");
        assertEquals("Spider-man", tree.search(10));
        assertEquals("Iron-man", tree.search(15));
        assertEquals("Hawk-eye", tree.search(5));
    }

    @Test
    public void testDeletion() throws Exception {
        tree.add(10, "Spider-man");
        tree.add(15, "Iron-man");
        tree.delete(10);
        tree.delete(15);
        assertEquals(null, tree.search(10));
    }

    @Test
    public void testBalancing() throws Exception {
        tree.add(5, "Hawk-eye");
        tree.add(7, "Hawk");
        tree.add(8, "Ant-man");
        tree.add(10, "Spider-man");
        tree.add(11, "Capitan America");
        tree.add(14, "Black Panter");
        tree.add(15, "Iron-man");
        tree.add(17, "Ms Marvel");
        tree.add(20, "Halk");

        assertTrue(tree.getHeight() <= 4);
    }

    @Test
    public void testRepeatedUsing() throws Exception {
        tree.add(10, "Spider-man");
        tree.add(15, "Iron-man");
        tree.delete(15);
        tree.delete(10);
        tree.add(10, "Spider-man");
        tree.add(15, "Iron-man");
        assertEquals("Spider-man", tree.search(10));
        assertEquals("Iron-man", tree.search(15));
    }

}
