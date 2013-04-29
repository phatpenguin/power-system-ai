/* Driver.java
 *
 * Created:  4/20/13 - Scott Leonard
 * Modified: 4/25/13 - Scott Leonard
 */

package org.cpsc5185.powersystem;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Scott Leonard
 */
public class Evolver {
    private static final Logger logger = Logger.getLogger(Evolver.class);

    private static final int YEARLY_INTERVALS = IntervalMaximum.values().length;
    private static final int MAX_GENERATIONS = 200;

    private final List<MaintenanceUnit> units = new ArrayList<>();
    private Population population;

    private int currentGenerationNumber = 0;

    private int totalInstalledCapacity = 0;  //calculated in constructor

    static final int MAX_POPULATION = 50;

    private static final int PROBABILITY_MAX = 10000; // lowest probability allowed is 0.000001

    private static final double CROSSOVER_PROBABILITY = 0.7;
    private static final double MUTATION_PROBABILITY = 0.001;

    public Evolver() {
        MaintenanceUnit m1 = new MaintenanceUnit(1, 20, 2);
        m1.addPotentialInterval(new boolean[]{true, true, false, false}); //1100
        m1.addPotentialInterval(new boolean[]{false, true, true, false}); //0110
        m1.addPotentialInterval(new boolean[]{false, false, true, true}); //0011
        totalInstalledCapacity += m1.getCapacity();
        units.add(m1);

        MaintenanceUnit m2 = new MaintenanceUnit(2, 15, 2);
        m2.addPotentialInterval(new boolean[]{true, true, false, false}); //1100
        m2.addPotentialInterval(new boolean[]{false, true, true, false}); //0110
        m2.addPotentialInterval(new boolean[]{false, false, true, true}); //0011
        totalInstalledCapacity += m2.getCapacity();
        units.add(m2);

        MaintenanceUnit m3 = new MaintenanceUnit(3, 35, 1);
        m3.addPotentialInterval(new boolean[]{true, false, false, false}); //1000
        m3.addPotentialInterval(new boolean[]{false, true, false, false}); //0100
        m3.addPotentialInterval(new boolean[]{false, false, true, false}); //0010
        m3.addPotentialInterval(new boolean[]{false, false, false, true}); //0001
        totalInstalledCapacity += m3.getCapacity();
        units.add(m3);

        MaintenanceUnit m4 = new MaintenanceUnit(4, 40, 1);
        m4.addPotentialInterval(new boolean[]{true, false, false, false}); //1000
        m4.addPotentialInterval(new boolean[]{false, true, false, false}); //0100
        m4.addPotentialInterval(new boolean[]{false, false, true, false}); //0010
        m4.addPotentialInterval(new boolean[]{false, false, false, true}); //0001
        totalInstalledCapacity += m4.getCapacity();
        units.add(m4);

        MaintenanceUnit m5 = new MaintenanceUnit(5, 15, 1);
        m5.addPotentialInterval(new boolean[]{true, false, false, false}); //1000
        m5.addPotentialInterval(new boolean[]{false, true, false, false}); //0100
        m5.addPotentialInterval(new boolean[]{false, false, true, false}); //0010
        m5.addPotentialInterval(new boolean[]{false, false, false, true}); //0001
        totalInstalledCapacity += m5.getCapacity();
        units.add(m5);

        MaintenanceUnit m6 = new MaintenanceUnit(6, 15, 1);
        m6.addPotentialInterval(new boolean[]{true, false, false, false}); //1000
        m6.addPotentialInterval(new boolean[]{false, true, false, false}); //0100
        m6.addPotentialInterval(new boolean[]{false, false, true, false}); //0010
        m6.addPotentialInterval(new boolean[]{false, false, false, true}); //0001
        totalInstalledCapacity += m6.getCapacity();
        units.add(m6);

        MaintenanceUnit m7 = new MaintenanceUnit(7, 10, 1);
        m7.addPotentialInterval(new boolean[]{true, false, false, false}); //1000
        m7.addPotentialInterval(new boolean[]{false, true, false, false}); //0100
        m7.addPotentialInterval(new boolean[]{false, false, true, false}); //0010
        m7.addPotentialInterval(new boolean[]{false, false, false, true}); //0001
        totalInstalledCapacity += m7.getCapacity();
        units.add(m7);

        createInitialPopulation();
    }

    public List<MaintenanceUnit> getUnits() {
        return units;
    }

    public int getTotalInstalledCapacity() {
        return totalInstalledCapacity;
    }

    public static int getYearlyIntervals() {
        return YEARLY_INTERVALS;
    }

    private void breed(Schedule parent1, Schedule parent2) {
        int crossoverChances = (int) (CROSSOVER_PROBABILITY * PROBABILITY_MAX);
        int mutationChances = (int) (MUTATION_PROBABILITY * PROBABILITY_MAX);

        boolean isCrossover = new Random().nextInt(PROBABILITY_MAX) <= crossoverChances; //only one crossover per breeding session

        int cutPoint = new Random().nextInt(units.size() - 1); //the cutpoint is where the genes start swapping.
        MaintenanceUnit[] keyArray = parent1.keySet().toArray(new MaintenanceUnit[]{});  //TODO: copy the keys into an array since we cant access TreeMap by index... is there a better way to do this??

        Schedule child1 = Schedule.cloneSchedule(parent1); //make child1 a copy of parent1
        Schedule child2 = Schedule.cloneSchedule(parent2); //make child2 a copy of parent2\

        for (int x = 0; x < units.size(); x++) {
            if (isCrossover && cutPoint >= x) {
                child1.put(keyArray[x], parent1.get(keyArray[x])); //replace the current gene at this position (x) with the gene from the other parent at the same location
                child2.put(keyArray[x], parent2.get(keyArray[x])); //replace the current gene at this position (x) with the gene from the other parent at the same location
            }

            if (new Random().nextInt(PROBABILITY_MAX) <= mutationChances) {
                child1.put(keyArray[x], keyArray[x].getRandomGene()); //replace the current gene at this position (x) with new random gene from the gene pool for this maintenance unit
                child2.put(keyArray[x], keyArray[x].getRandomGene()); //replace the current gene at this position (x) with new random gene from the gene pool for this maintenance unit
            }
        }

        population.add(child1);
        population.add(child2);
    }

    private void createInitialPopulation() {
        //If there is already a population and generation, we need to reset it
        population = new Population(totalInstalledCapacity);

        while (population.size() < MAX_POPULATION) {
            Schedule i = Schedule.randomScheduleFactory(this);
            i.setGeneration(currentGenerationNumber);
            population.add(i);
        }
    }

    @Override
    public String toString() {
        String evolver = "";
        evolver += population.toString() + "\n";
        evolver += "Maximum fitness: " + population.getMostFit().getFitness() + "\n";
        evolver += "Minimum fitness: " + population.getLeastFit().getFitness() + "\n";
        evolver += "Average fitness: " + population.getAverageFitness() + "\n";

        return evolver;
    }

    private void breedNewGeneration() {
        currentGenerationNumber++;
        List<Schedule> breedingPool = population.getBreedingPool();
        population = new Population(totalInstalledCapacity);

        while (population.size() < MAX_POPULATION) {
            Schedule parent1, parent2;

            Random rnd = new Random();
            parent1 = breedingPool.get(rnd.nextInt(breedingPool.size() - 1));
            parent2 = breedingPool.get(rnd.nextInt(breedingPool.size() - 1));

            if (parent1 == parent2) continue;

            breed(parent1, parent2);
        }
    }

    private String toCsv() {
        String csv = "";
        csv += currentGenerationNumber + ",";
        csv += population.getAverageFitness() + ",";
        csv += population.getMostFit().getFitness();
        return csv;
    }

    public static void main(String[] args) {
        Evolver evolver = new Evolver();

        do {
            if (evolver.currentGenerationNumber % 10 == 0) {
                System.out.println(evolver.toCsv());
                System.out.println(evolver.population.getMostFit().toString());
            }
            evolver.breedNewGeneration();
        } while (evolver.currentGenerationNumber <= MAX_GENERATIONS);
    }
}