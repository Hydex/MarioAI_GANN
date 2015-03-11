package competition.cig.andreacastegnaro.ga_an;

import competition.cig.andreacastegnaro.ga_an.ann.*;
import ch.idsia.agents.Agent;
/**
 * This class will train the neural network using the genetic algorithm principles
 * It will save the best trained agent in a file which can be imported separetely for future use
 * @author Andrea
 *
 */
public class GeneticAlgorithmTrainer {

	private int lvlDifficulty;
	private int lvlLength = -1;
	final int trials = 1;
	private int epochs;
	
	private int seed = -1;
	private boolean lvlCompleted =  false;
	Agent agent;
	private GeneticAlgorithm ga;
	private int[] layers;
	private int nLevelWins = 0;
	private boolean changeLvl = false;
	
	private GeneticAlgorithmTrainer(int difficulty, int lvlLenght, int repopulations, int geneSize, int[] layers)
	{
		this.lvlDifficulty = difficulty;
		this.lvlLength = lvlLenght;
		this.layers = layers;
		this.epochs = repopulations;
		ga = new GeneticAlgorithm(geneSize, layers);
		trainPopulation();
	}
	
	private void trainPopulation()
	{
		double score;
		
		this.seed = 2;
		
		for(int g = 0; g <= epochs; g++)
		{
			if(g > 0)
				ga.GetPopulationFromNetSet();
			
			System.out.println("Epochs: " + g);
			
			//Play mario with Neural Net's from the population
			for(int p = 0; p <)
		}
	}
	
	public static void main(String[] args)
	{
		//*
		/**
		 * 
		 * 
		 * 
		 * 
		 */
		int lvlDifficulty = 2;
		int lvlLength = 1000;
		int epochs = 0;
		int geneSize = 100;
		int[] netLayers = new int[]{5,10,5};
		
	}
}
