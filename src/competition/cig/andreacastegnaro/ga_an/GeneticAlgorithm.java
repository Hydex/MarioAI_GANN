package competition.cig.andreacastegnaro.ga_an;

import competition.cig.andreacastegnaro.ga_an.ann.*;
import java.util.*;
/**
 * This class will be the responsible for changing the weights to the neural network.
 * It will take just information about species (population, population size and their genes)
 * @author Andrea
 *
 */
public class GeneticAlgorithm {
	
	protected int population_size;
	
	//we will use List because they will be very easy to sort
	protected List<Chromosome> population;
	protected List<NeuralNetwork> genes;
}
