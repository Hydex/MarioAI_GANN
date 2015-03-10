package competition.cig.andreacastegnaro.ga_an;

import competition.cig.andreacastegnaro.ga_an.ann.*;
import java.util.*;
import java.security.InvalidParameterException;
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
	protected int[] layers;
	
	/**
	 * Constructors
	 */
	
	public GeneticAlgorithm(int pop_size, int nnetNeuronsPerLayer[] )
	{
		if(population_size < 20)
		{
			throw new InvalidParameterException("Invalid population_size. Cannot evolve such few population");
		}
		population_size = pop_size;
		population = new ArrayList<Chromosome>();

		layers = nnetNeuronsPerLayer;
		genes = new ArrayList<NeuralNetwork>();
		
		Init();
	}
	
	/**
	 * Functions
	 */
	/**
	 * Initialize the population in GA.
	 */
	private void Init() 
	{	
		for(int i = 0; i < this.population_size; i++)
		{
			genes.add(new NeuralNetwork(layers, 0.1f));
			population.add(new Chromosome(genes.get(i).GetTotalWeights()));
		}			
	}
	/**
	 * Maybe to start training I will use 
	 * @param popsize population size
	 * @param chromesize number of genes componing the population member
	 * @return List of chromosome for the current population
	 */
	private List<Chromosome> InitRandom(int popsize, int chromesize)
	{
		List<Float> newGenes;
		List<Chromosome> retPop = new ArrayList<Chromosome>();
		
		Random r_float = new Random(new Random().nextLong());
		
		for(int i=0; i<popsize;i++)
		{
			newGenes = new ArrayList<Float>();
			for(int j=0; j<chromesize;j++)
			{
				newGenes.add(r_float.nextFloat()*1000000 % 2.0f - 1.0f); //weights are number between -1 and 1
			}
			retPop.add(new Chromosome(newGenes));
		}	
		return retPop; 
	}
	
	/**
	 * Get Population from each Neural Network
	 * This method is used before starting the evolution
	 */
	public void GetPopulationFromNetSet()
	{
		for (int i = 0; i < genes.size(); i++)
			population.get(i).SetGene(genes.get(i).GetTotalWeights());
	}
	
	public void SortPopulationAfterFitness()
	{
		//Powerful Java here: da paura!
		Collections.sort(population);
		
		PrintPopulationStatistics();
	}
	
	private void PrintPopulationStatistics()
	{
		System.out.println("Best: " + population.get(0).GetFitness() + " FIRST WEIGHT: " + population.get(0).getGenes().get(0) + "Last WEIGHT: " + population.get(0).getGenes().get(population.get(0).getGenes().size()-1));
		System.out.println("Worst: " + population.get(population.size()-1).GetFitness() + " " + population.get(population.size()-1).getGenes().get(0));
	}
	
	/**
	 * Repopulating the population.  Saving the best 20 chromosome and MultiMutateCrossOver the remaining.
	 */
	public void Repopulate()
	{
		SortPopulationAfterFitness();
		
		List<Chromosome> newPopulation = new ArrayList<Chromosome>();
		
		List<Chromosome> newPopPart1 = CloneChromosomeList(population.subList(0, 20));//Total 20
		List<Chromosome> newPopPart2 = CloneChromosomeList(population.subList(0, 90));//Total 65
		List<Chromosome> newPopPart3 = CloneChromosomeList(population.subList(0, 60));//Total 95
		
		newPopulation.addAll(newPopPart1);
		
		//Starting with implementing a multicrossover function for repopulation
		
	}
	
	private List<Chromosome> CloneChromosomeList(List<Chromosome> listToClone)
	{
		List<Chromosome> retList = new ArrayList<Chromosome>();
		
		for(Chromosome c : listToClone)
			retList.add((Chromosome)c.clone());
		
		return retList;
	}
}
