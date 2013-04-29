/* Individual.java
 *
 * Created:  4/22/13 - Scott Leonard
 * Modified: 4/26/13 - Scott Leonard
 */

package org.cpsc5185.powersystem;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * @author Scott Leonard
 */
public class Schedule extends TreeMap<MaintenanceUnit, boolean[]> implements Comparable {
    private final Evolver evolver;

    private int generation;

    public static Schedule cloneSchedule(Schedule parent) {
        Schedule clone = new Schedule(parent.evolver);

        clone.putAll(parent);
        clone.generation = parent.generation + 1;

        return clone;
    }

    public static Schedule randomScheduleFactory(Evolver evolver) {
        Schedule schedule = new Schedule(evolver);

        do {
            for (MaintenanceUnit mu : evolver.getUnits()) {
                schedule.put(mu, mu.getRandomGene());
            }
        } while (schedule.getFitness() < 0);

        return schedule;
    }

    private Schedule(Evolver evolver) {
        this.evolver = evolver;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getFitness() {
        int fitness = evolver.getTotalInstalledCapacity();  //the fitness value will represent the lowest net reserve of the schedule

        for (int x = 0; x < Evolver.getYearlyIntervals(); x++) {
            int intervalLoad = 0;
            for (MaintenanceUnit mu : keySet()) {
                boolean[] gene = get(mu);
                if (gene[x]) {
                    intervalLoad += mu.getCapacity();
                }
            }
            int netReserves = evolver.getTotalInstalledCapacity() - intervalLoad; //net reserve = total installed capacity minus the total lost power due to unit maintenance
            netReserves -= IntervalMaximum.findByIndex(x).getMax();
            netReserves = netReserves < 0 ? 0 : netReserves; //if the net reserves value is negative, set it to zero
            if (netReserves < fitness)
                fitness = netReserves; //if the net reserves are lower than the previous fitness, then set the new fitness
        }
        return fitness;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p/>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object o) {
        return this.getFitness() - ((Schedule) o).getFitness();
    }

    @Override
    public boolean equals(Object i) {
        if (super.equals(i)) {
            return true;
        } else if (compareTo(i) == 0) {
            for (MaintenanceUnit mu : keySet()) {
                if (!Arrays.equals(((Schedule) i).get(mu), this.get(mu))) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private String getGeneSequence() {
        String geneSequence = "";
        for (MaintenanceUnit mu : keySet()) {
            for (int x = 0; x < get(mu).length; x++) {
                geneSequence += (get(mu)[x]) ? 1 : 0;
            }
            geneSequence += " ";
        }

        return geneSequence.trim();
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "generation=" + generation +
                ", geneSequence=" + getGeneSequence() +
                ", fitness=" + getFitness() +
                '}';
    }
}
