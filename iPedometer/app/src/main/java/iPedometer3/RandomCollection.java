package iPedometer3;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * Source: http://stackoverflow.com/questions/6409652/random-weighted-selection-in-java
 * Created by Hans-Christiaan on 26-5-2015.
 */
public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public void add(double weight, E result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public E takeWeightedSample() {
        double value = random.nextDouble() * total;
        return map.ceilingEntry(value).getValue();
    }
}
