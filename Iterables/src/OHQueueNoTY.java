import java.util.Iterator;

public class OHQueueNoTY extends OHQueue {
     public OHQueueNoTY(OHRequest queue) {
         super(queue);
     }

     @Override
     public Iterator<OHRequest> iterator() {
         return new TYIterator(q);
     }

    public static void testQueue() {
        OHRequest t5 = new OHRequest("halp", "halp!", null);
        OHRequest t4 = new OHRequest("help me", "help!", t5);
        OHRequest t3 = new OHRequest("im bored", "bore", t4);
        OHRequest t2 = new OHRequest("thank u", "tq2", t3);
        OHRequest t1 = new OHRequest("thank u", "tq1", t2);
        OHQueue qq = new OHQueueNoTY(t1);
        for (OHRequest rqq : qq) {
            System.out.println(rqq.name);
        }
    }
}
