package competition.cig.andreacastegnaro.ga_an;

import competition.cig.andreacastegnaro.ga_an.ann.*;
import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;
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
		double score = 0;
		int indexOfNet = 0;
		this.seed = 2;
		
		for(int g = 0; g <= epochs; g++)
		{
			if(g > 0)
				ga.GetPopulationFromNetSet();
			
			System.out.println("Epochs: " + g);
			
			//Play mario with Neural Net's from the population
			for(int p = 0; p < ga.GetPopulation().size(); p++)
			{
				Agent controller = new MarioAgent_GA_NN(ga.GetNeuralNetworks().get(indexOfNet));
				
				for (int levelseed = 1; levelseed <= 15; levelseed++)
				{
					score += PlaySingleNet(controller, seed);
				}
				indexOfNet++;
				if((p%10)==0)
					System.out.print(".");
			}
		}
	}
	
	public double PlaySingleNet(Agent controller, int seed)
	{
		MarioAIOptions marioAIOptions = new MarioAIOptions(new String[0]);
		
		marioAIOptions.setAgent(controller);        
    	Task task = new ProgressTask(marioAIOptions);
    	
    	marioAIOptions.setVisualization(true);
    	marioAIOptions.setLevelRandSeed((int) seed);//(Math.random () * Integer.MAX_VALUE));
    	marioAIOptions.setLevelDifficulty(this.lvlDifficulty);
        	    	        
        if(lvlLength != -1)
        	marioAIOptions.setLevelLength(lvlLength);
        	
    	//marioAIOptions.setTimeLimit(marioAIOptions.getTimeLimit()/2);
        
        double fitness = Test(controller, marioAIOptions, seed); 
        return fitness;
		
	}
	
	public double Test(Agent controller, MarioAIOptions options, int seed)
	{
		double distancePassed = 0;
		int kills = 0;
		int killsSum = 0;
		int timeLeftSum = 0;
		int timeLeft = 0;
		int marioModeSum = 0;
		int marioMode = 0;
		double dp = 0;
       
       return 0;
       
	}
	
	public static void main(String[] args)
	{
		int lvlDifficulty = 2;
		int lvlLength = 1000;
		int epochs = 0;
		int geneSize = 100;
		int[] netLayers = new int[]{5,10,5};		
	}
}
