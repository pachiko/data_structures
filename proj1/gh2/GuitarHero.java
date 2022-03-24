package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;


public class GuitarHero {
    public static void main(String[] args) {
        /* create 37 notes */
        GuitarString[] strings = new GuitarString[37];
        for (int i = 0; i < 37; i++)  {
            strings[i] = new GuitarString(440.0*Math.pow(2.0, (i - 24.0)/12.0));
        }
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                try {
                    strings[keyboard.indexOf(key)].pluck();
                } catch(ArrayIndexOutOfBoundsException e) {
                    // Ignore wrong keys
                }
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (GuitarString s : strings) {
                sample += s.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString s : strings) s.tic();
        }
    }
}
