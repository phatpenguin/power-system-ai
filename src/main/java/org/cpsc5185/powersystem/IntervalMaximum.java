package org.cpsc5185.powersystem;

/* IntervalMaximum.java
 *
 * Created:  4/22/13 - Scott Leonard
 * Modified: 4/25/13 - Scott Leonard
 */
public enum IntervalMaximum {
    INTERVAL1_MAX(0, 80), INTERVAL2_MAX(1, 90), INTERVAL3_MAX(2, 65), INTERVAL4_MAX(3, 70);

    private final int index, max;

    private IntervalMaximum(int index, int max) {
        this.index = index;
        this.max = max;
    }

    public int getIndex() {
        return index;
    }

    public int getMax() {
        return max;
    }

    public static IntervalMaximum findByIndex(int index) {
        for (IntervalMaximum i : IntervalMaximum.values()) {
            if (i.getIndex() == index) return i;
        }
        return null;
    }
}
