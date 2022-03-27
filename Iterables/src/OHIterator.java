import java.util.Iterator;
import java.util.NoSuchElementException;

public class OHIterator implements Iterator<OHRequest> {
    public OHRequest curr;

    public OHIterator(OHRequest queue) {
        curr = queue;
    }

    @Override
    public boolean hasNext() {
        return curr != null;
    }

    @Override
    public OHRequest next() {
        while(hasNext() && !isGood(curr.description)) {
            curr = curr.next;
        }

        if (hasNext() && isGood(curr.description)) {
            OHRequest res = curr;
            curr = curr.next;
            return res;
        } else {
            throw new NoSuchElementException("Out of requests!");
        }
    }

    public boolean isGood(String description) {
        return description != null && description.length() > 5;
    }

}