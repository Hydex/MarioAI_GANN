package competition.cig.andreacastegnaro.ga_an;

import java.util.*;
/**
 * This class will output the fitness function value and will stored the weights (called genes when having key lecture of geentic algoriithm) from the net
 * Since population will be sorted and also we will copy the good genes into next population we will use 
 * Comparable (ref: http://www.onjava.com/pub/a/onjava/2003/03/12/java_comp.html)
 * and
 * Cloneable (ref: http://mrbool.com/how-to-implement-cloning-in-java-using-cloneable-interface/28410)
 * @author Andrea
 *
 */
public class Chromosome implements Cloneable, Comparable<Object>{

	/**
	 * Membervariables
	 */
	//Stupid java needs object to be in the list... need to use Float which is a wrapper of float
	protected List<Float> genes;
	protected double fit;
	/**
	 * Constructors
	 */	
	public Chromosome() 
	{
		this.genes = new ArrayList<Float>();
	}
	
	//The following one are equivalent but not sure what I will use.
	public Chromosome(List<Float> gene)
	{
		this.genes = gene;
		this.fit = 0;
	}
	
	public Chromosome(float[] genes)
	{
		this.genes = new ArrayList<Float>();		
		SetGene(genes);
	}
	/**
	 * Cloning This Chromosome into the returned Chromosome
	 */
	public Object clone()
	{
		List<Float> tmpGenes = new ArrayList<Float>();
		for(float f : this.genes)
		{
			tmpGenes.add(f);
		}
		Chromosome retChromo =  new Chromosome(tmpGenes);
		//Copying also the current fitness function
		retChromo.fit = this.fit;
		
		return retChromo;
	}
	
	/**
	 * Comparing function to implement the comparable interface
	 */
	public int compareTo(Object o)
	{
		//Dynamic casting
		double result = ((Chromosome)o).GetFitness() - this.fit;
		if(result > 0)
			return 1;
		else if(result < 0)
			return -1;
		else return 0;
	}

	//This function has been copied: check behavior
	public void MutateGaussian(float mean, float deviation)
	{
		Random r = new Random();
		Random increment = new Random((new Random()).nextLong());
		int randomGene = increment.nextInt(11); //cambio geni a caso fino a raggiungere la dimensione dei geni, a passi di 10 max
		while(randomGene < genes.size())
		{
			Double d = r.nextGaussian() * deviation + mean;
			//Java casting di merda
			float offset = d.floatValue();
			genes.set(randomGene,genes.get(randomGene) + offset);
			
			randomGene += increment.nextInt(10) + 1;
		}
	}
	
	/**
	 * Getter/Setter Functions
	 */
	public void SetGene(float[] genes)
	{
		this.genes = new ArrayList<Float>();
		for(float f : genes)
			this.genes.add(f);	
	}
	public void SetGene(List<Float> genes){this.genes = genes;}
	public double GetFitness(){return fit;}
	public void SetFitness(double fitness){this.fit = fitness;}
	public List<Float> getGenes(){return this.genes;}
}
