package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> aListNoResizing = new AListNoResizing<>();
        BuggyAList <Integer> buggyAList = new BuggyAList <>();

        for (int i = 4; i < 7; i++) {
            aListNoResizing.addLast(i);
            buggyAList.addLast(i);
        }

        while (aListNoResizing.size() > 0) {
            aListNoResizing.removeLast();
            buggyAList.removeLast();

            for (int i = 0; i < aListNoResizing.size(); i++) {
                assertEquals("Test elements at index: "+ i,
                        aListNoResizing.get(i), buggyAList.get(i));
            }
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList <Integer> buggyAList = new BuggyAList <>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                buggyAList.addLast(randVal);
                assertEquals(L.getLast(), buggyAList.getLast());
            } else if (operationNumber == 1) {
                // size
                int lSize = L.size();
                int buggySize = buggyAList.size();
                assertEquals(lSize, buggySize);
            } else if (operationNumber == 2) {
                if (L.size() > 0 && buggyAList.size() > 0) {
                    int lLast = L.getLast();
                    int buggyLast = buggyAList.getLast();
                    assertEquals(lLast, buggyLast);
                }
            } else if (operationNumber == 3) {
                if (L.size() > 0) {
                    int lLast = L.removeLast();
                    int buggyLast = buggyAList.removeLast();
                    assertEquals(lLast, buggyLast);
                    assertEquals(L.size(), buggyAList.size());
                }
            }
        }
    }
}
