package competition.cig.andreacastegnaro.ga_an;
import competition.cig.andreacastegnaro.ga_an.ann.*;

import java.util.*;
import java.io.IOException;
import java.security.InvalidParameterException;
/**
 * This class will be the responsible for changing the weights to the neural network.
 * It will take just information about species (population, population size and their genes)
 * @author Andrea
 *
 */
public class GeneticAlgorithm {
	
	protected int population_size;

	protected List<Chromosome> population;
	protected List<NeuralNetwork> genes;
	protected int[] layers;
		
	protected int weightsCount; //viene utile
	
	protected FileSaver annSavedFile;
	
	/**
	 * Constructors
	 */	
	public GeneticAlgorithm(int pop_size, int nnetNeuronsPerLayer[] )
	{
		if(pop_size < 20)
		{
			throw new InvalidParameterException("Invalid population_size. Cannot evolve such few population");
		}
		population_size = pop_size;
		population = new ArrayList<Chromosome>();
		layers = nnetNeuronsPerLayer;
		genes = new ArrayList<NeuralNetwork>();
		
		Init();
		
		weightsCount = genes.get(0).GetNumberTotalWeights();
	}

	public GeneticAlgorithm(int pop_size, int nnetNeuronsPerLayer[], String trainedFile )
	{
		this(pop_size,nnetNeuronsPerLayer);
		annSavedFile = new FileSaver(trainedFile);
	}
	
	private void Init() 
	{	
		for(int i = 0; i < this.population_size; i++)
		{
			genes.add(new NeuralNetwork(layers, 0.1f));
			//Taking form the just created gene(neural net) all the weights to be considered as a chromosome
			population.add(new Chromosome(genes.get(i).GetTotalWeights()));
		}			
	}
	/**
	 * @param popsize population size
	 * @param chromesize number of genes componing the population member
	 * @return List of chromosome for the current population
	 */
	private List<Chromosome> InitRandom(int popsize)
	{
		if(this.weightsCount == 0)
			throw new InvalidParameterException("Chromosome must be already be set");
		List<Float> newGenes;
		List<Chromosome> retPop = new ArrayList<Chromosome>();
		
		Random r_float = new Random(new Random().nextLong());
		
		for(int i=0; i< popsize ;i++)
		{
			newGenes = new ArrayList<Float>();
			
			for(int j=0; j< this.weightsCount ;j++)
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
		
		List<Chromosome> newPopPart1 = CloneChromosomeList(population.subList(0, 10));//Total 10
		List<Chromosome> newPopPart2 = CloneChromosomeList(population.subList(0, 40));//Total 25
		List<Chromosome> newPopPart21 = CloneChromosomeList(population.subList(0,30));//Total 40
				
		newPopulation.addAll(newPopPart1);
		newPopulation.addAll(MultiCrossOver(newPopPart2,true));
		newPopulation.addAll(MultiMutateCrossOver(newPopPart21,true));
		//Repopulating
		int populationLeft = population.size() - newPopulation.size();//10
		System.out.println("New random population added: " + populationLeft);
		
		if(populationLeft>0)
		{
			List<Chromosome> newPopPart3 = CloneChromosomeList(population.subList(0, populationLeft));
			newPopulation.addAll(MultiCrossOverWithNewRandom(newPopPart3, true));
		}
		this.population = newPopulation;
		copyPopulationToNet();		
	}
	
	private void copyPopulationToNet()
	{
		for(int i = 0; i < genes.size(); i++)
			genes.get(i).SetAllWeights(population.get(i).getGenes());
	}
	
	private List<Chromosome> MultiCrossOver(List<Chromosome> listToCrossover, boolean shuffle)
	{
		if(listToCrossover.size()%2 != 0)
			throw new InvalidParameterException("MultiCrossOver has to be even");
		
		if(shuffle)
			Collections.shuffle(listToCrossover);
				
		List<Chromosome> retList = new ArrayList<Chromosome>();
		for(int i = 0; i < listToCrossover.size(); i+=2)
		{
			retList.add(CrossOver(listToCrossover.get(i),listToCrossover.get(i+1)));
		}	
		return retList;
	}
	
	private List<Chromosome> MultiMutateCrossOver(List<Chromosome> listToMutate, boolean shuffle)
	{
		if(listToMutate.size()%2 != 0)
			throw new InvalidParameterException("MultiMutateCrossOver has to be even");
		
		listToMutate = MultiMutate(listToMutate);
		
		if(shuffle)
			Collections.shuffle(listToMutate);
		
		List<Chromosome> retList = new ArrayList<Chromosome>();
		for(int i = 0; i < listToMutate.size(); i += 2)
			retList.add(CrossOver(listToMutate.get(i),listToMutate.get(i+1)));
		
		return retList;

	}
	
	private List<Chromosome> MultiCrossOverWithNewRandom(List<Chromosome> listtocross, boolean shuffle)
	{
		listtocross = MultiMutate(listtocross);
		
		if(shuffle)
			Collections.shuffle(listtocross);
		
		List<Chromosome> randomCrossList = InitRandom(listtocross.size());
		listtocross.addAll(randomCrossList);
		
		List<Chromosome> retList = new ArrayList<Chromosome>();
		for(int i = 0; i<listtocross.size(); i+=2)
			retList.add(CrossOver(listtocross.get(i),listtocross.get(i+1))); 

		if(retList.size() != listtocross.size()/2)
			throw new InvalidParameterException("ERROR in MultiMutateCrossOverWithRandomWeights: The number of chromosome should be equal in both input and output");
		
		return retList;
	}
	
	private List<Chromosome> MultiMutate(List<Chromosome> listtomutate)
	{
		for(int i = 0; i < listtomutate.size();i++)
		{
			listtomutate.get(i).MutateGaussian(0.0f, 0.1f);
		}
		return listtomutate;
	}
	
	private Chromosome CrossOver(Chromosome c1, Chromosome c2)
	{
		List<Float> newGenePart1 = new ArrayList<Float>();
		List<Float> newGenePart2 = new ArrayList<Float>();
		List<Float> tmpList = new ArrayList<Float>();
		Chromosome newCromosome = new Chromosome();
		Random r = new Random(new Random().nextLong());
		
		int splitSeparator = r.nextInt(this.weightsCount - 1) + 1;
		int combineMode = r.nextInt(2); //TODO check behavior here
		
		int geneLength = c2.getGenes().size();
		
		if(combineMode==0)
		{
			newGenePart1 = c1.getGenes().subList(0, splitSeparator);
			newGenePart2 = c2.getGenes().subList(splitSeparator, geneLength);
		}
		else
		{
			newGenePart1 = c2.getGenes().subList(0, splitSeparator);
			newGenePart2 = c1.getGenes().subList(splitSeparator, geneLength);
		}
		
		tmpList.addAll(newGenePart1);
		tmpList.addAll(newGenePart2);
				
		newCromosome.SetGene(tmpList);
		
		if(c1.getGenes().size() != newCromosome.getGenes().size())
			throw new InvalidParameterException("Invalid number of weights in one of the chromosomes in the Crossover.");
		
		return newCromosome;
	}
	
	private List<Chromosome> CloneChromosomeList(List<Chromosome> listToClone)
	{
		List<Chromosome> retList = new ArrayList<Chromosome>();
		
		for(Chromosome c : listToClone)
			retList.add((Chromosome)c.clone());
		
		return retList;
	}
	
	public List<Chromosome> GetPopulation()
	{
		return population;
	}
	
	public List<NeuralNetwork> GetNeuralNetworks()
	{
		return this.genes;
	}
	
	public void SaveANN()
	{
    	try 
    	{
    		this.annSavedFile.save(this.genes.get(0));
		} 
    	catch (IOException e) {e.printStackTrace();}
	}
}
