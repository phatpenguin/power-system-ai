/* Population.java
 *
 * Created:  4/27/13 - Scott Leonard
 * Modified: 4/27/13 - Scott Leonard
 */

package org.cpsc5185.powersystem;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Scott Leonard
 */
public class Population extends ArrayList<Schedule> {
    private static final Logger logger = Logger.getLogger(Population.class);

    private int fitnessSum;
    private final int MAX_POPULATION;
    private final int MIN_FITNESS;
    private final int MAX_FITNESS = 0;

    public Population(int minFitness) {
        this.MAX_POPULATION = Evolver.MAX_POPULATION;
        this.MIN_FITNESS = minFitness;
        fitnessSum = 0;
    }

    public Schedule getMostFit() {
        Schedule mostFit = null;
        int maxFitness = MAX_FITNESS;
        for (Schedule i : this) {
            int fitness = i.getFitness();
            if (this.indexOf(i) == 0 || fitness > maxFitness) {
                maxFitness = fitness;
                mostFit = i;
            }
        }
        return mostFit;
    }

    public Schedule getLeastFit() {
        int minFitness = MIN_FITNESS; //reset minFitness
        Schedule leastFit = null;
        for (Schedule i : this) {
            int fitness = i.getFitness();
            if (this.indexOf(i) == 0 || fitness < minFitness) {
                minFitness = fitness;
                leastFit = i;
            }
        }
        return leastFit;
    }

    public List<Schedule> getBreedingPool() {
        List<Schedule> pool = new ArrayList<>();

        for (Schedule i : this) {
            double breedOdds = Math.round(((double) i.getFitness() / (double) fitnessSum) * 100d);
            logger.debug("Schedule: " + i);
            logger.debug("individualFitness: " + i.getFitness() + " totalFitness: " + fitnessSum + " Odds: " + breedOdds + "%");
            for (int x = 0; x < breedOdds; x++) {
                pool.add(i);
            }
        }
        return pool;
    }

    public double getAverageFitness() {
        return (double) fitnessSum / (double) size();
    }

    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            fitnessSum -= ((Schedule) o).getFitness();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean add(Schedule i) {
        if (contains(i) || (size() >= MAX_POPULATION && i.getFitness() < getLeastFit().getFitness())) {
            return false;  //if the population is full, and i is less fit, don't bother adding it.
        } else {
            logger.debug("fitnessSum: " + fitnessSum + ", fitness: " + i.getFitness());
            fitnessSum += i.getFitness();
            return super.add(i);
        }
    }

    @Override
    public String toString() {
        String population = "";
        for (Schedule i : this) {
            population += i.toString() + "\n";
        }
        return population;
    }
}
