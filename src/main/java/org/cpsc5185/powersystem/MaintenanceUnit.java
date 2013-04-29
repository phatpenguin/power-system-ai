/* MaintenanceUnit.java
 *
 * Created:  4/20/13 - Scott Leonard
 * Modified: 4/25/13 - Scott Leonard
 */

package org.cpsc5185.powersystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Scott Leonard
 */
public class MaintenanceUnit implements Comparable {
    private final int unitNumber;
    private int capacity;
    private int intervalRequirement;
    private List<boolean[]> genePool;

    public MaintenanceUnit(int unitNumber, int capacity, int intervalRequirement) {
        genePool = new ArrayList<>();
        this.unitNumber = unitNumber;
        this.capacity = capacity;
        this.intervalRequirement = intervalRequirement;
    }

    public boolean[] getRandomGene() {
        Random rnd = new Random();
        int index = rnd.nextInt(genePool.get(0).length - 1);

        return genePool.get(index);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getIntervalRequirement() {
        return intervalRequirement;
    }

    public void setIntervalRequirement(int intervalRequirement) {
        this.intervalRequirement = intervalRequirement;
    }

    public List<boolean[]> getGenePool() {
        return genePool;
    }

    public void setGenePool(List<boolean[]> genePool) {
        this.genePool = genePool;
    }

    public void addPotentialInterval(boolean[] interval) {
        this.genePool.add(interval);
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    @Override
    public String toString() {
        return "MaintenanceUnit{" +
                "capacity=" + capacity +
                ", intervalRequirement=" + intervalRequirement +
                ", genePool=" + genePool +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MaintenanceUnit && compareTo(o) == 0;
    }

    @Override
    public int hashCode() {
        return unitNumber;
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
        return this.hashCode() - o.hashCode();
    }
}
