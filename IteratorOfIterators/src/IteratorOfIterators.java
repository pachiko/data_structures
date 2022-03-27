import java.util.*;

public class IteratorOfIterators implements Iterator<Integer> {
    List<Iterator<Integer>> listOfIters;
    Iterator<Iterator<Integer>> listIterator;
    Iterator<Integer> iter;
    boolean emptyPass;

    public IteratorOfIterators(List<Iterator<Integer>> listOfIters) {
        this.listOfIters = listOfIters;
        listIterator = listOfIters.iterator();
        emptyPass = true;
    }

    @Override
    public boolean hasNext() {
        while(true) {
            while (listIterator.hasNext()) { // Loop through iterators
                iter = listIterator.next();
                if (iter.hasNext()) { // get item in iterator
                    emptyPass = false;
                    return true;
                }
            }
            if (!emptyPass) { // reset to check if an empty pass
                listIterator = listOfIters.iterator();
                emptyPass = true;
            } else { // quit if empty pass
                return false;
            }
        }
    }

    @Override
    public Integer next() {
        return iter.next();
    }

    public static void main(String[] args) {
        // Part 2
        ArrayList<Integer> a = new ArrayList<>(Arrays.asList(1, 3, 4, 5));
        ArrayList<Integer> b = new ArrayList<>();
        ArrayList<Integer> c = new ArrayList<>();
        c.add(2);

        List<Iterator<Integer>> l = new ArrayList<>();
        l.add(a.iterator());
        l.add(b.iterator());
        l.add(c.iterator());

        IteratorOfIterators ioi = new IteratorOfIterators(l);
        while (ioi.hasNext()) {
            System.out.println(ioi.next());
        }
    }
}
