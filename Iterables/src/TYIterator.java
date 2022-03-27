public class TYIterator extends OHIterator {
     public TYIterator(OHRequest queue) {
         super(queue); // Must override ctor, since OHIterator has no default CTOR
     }

    @Override
    public boolean isGood(String description) {
        return  super.isGood(description)
                && !description.contains("thank u");
    }
}
