package competition.cig.andreacastegnaro.ga_an;

import competition.cig.andreacastegnaro.ga_an.ann.*;
import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.tasks.*;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;
/**
 * This class will train the neural network using the genetic algorithm principles
 * It will save the best trained agent in a file which can be imported separetely for future use
 * @author Andrea
 *
 */
public class GeneticAlgorithmTrainer {

	private int lvlDifficulty = 0;
	private int lvlLength = -1;
	
	final int trials = 1;
	private int epochs;
	
	private int crossover = -1;
	private int multicrossover = -1;
	private int bestsave = -1;
	
	Agent agent;
	private GeneticAlgorithm ga;
	private int[] layers;
	private int nLevelWins = 0;
	
	private int levelseed = 0;
	private boolean frozenEnemies = false;
	private boolean changeLvl = false;
	
	
	private EvaluationInfo evInfo;
	private String trainedFile = "";
	
	private GeneticAlgorithmTrainer(int difficulty, int lvlLenght, int repopulations, int geneSize, int[] layers, String trained)
	{
		this.lvlDifficulty = difficulty;
		this.lvlLength = lvlLenght;
		this.layers = layers;
		this.epochs = repopulations;
		this.trainedFile = trained;
		if(this.trainedFile.equals(""))
		{
			ga = new GeneticAlgorithm(geneSize, layers);
		}
		else
		{
			ga = new GeneticAlgorithm(geneSize, layers, this.trainedFile);
		}		
		trainPopulation();
	}
	
	private GeneticAlgorithmTrainer(int difficulty, int lvlLenght, int repopulations, int geneSize, int[] layers, String trained, int crossover, int multicrossover, int bestsave)
	{
		this.crossover = crossover;
		this.multicrossover = multicrossover;
		this.bestsave = bestsave;
		//this(difficulty, lvlLenght, repopulations, geneSize, layers, trained);
		this.lvlDifficulty = difficulty;
		this.lvlLength = lvlLenght;
		this.layers = layers;
		this.epochs = repopulations;
		this.trainedFile = trained;
		if(this.trainedFile.equals(""))
		{
			ga = new GeneticAlgorithm(geneSize, layers, this.crossover, this.multicrossover, this.bestsave);
		}
		else
		{
			ga = new GeneticAlgorithm(geneSize, layers, this.trainedFile, this.crossover, this.multicrossover, this.bestsave);
		}		
		trainPopulation();
	}
	
	private void trainPopulation()
	{
		double score = 0;
		int indexOfNet = 0;
		
		for(int g = 0; g < epochs; g++)
		{
			if(g > 0)
				ga.GetPopulationFromNetSet();
			
			System.out.println();
			System.out.println("Epoch: " + g);
			System.out.println("Current seed lvl: " + this.levelseed);
			System.out.println("Frozen enemies: " + (this.frozenEnemies == true ? "no" : "yes"));
			//Play mario with Neural Net's from the population
			boolean freezeEnemies = this.frozenEnemies;
			for(int p = 0; p < ga.GetPopulation().size(); p++)
			{
				Agent controller = new MarioAgent_GA_NN(ga.GetNeuralNetworks().get(indexOfNet));
				for(int i = 0; i <15; i++)			
					score += PlaySingleNet(controller, i, false);
				
				ga.GetPopulation().get(indexOfNet).SetFitness(score);
				
				//System.out.println("Fitness Score: " + score);
				
				indexOfNet++;
				score = 0;
			}
			indexOfNet = 0;
			System.out.println();
			System.out.println("Repopulating");
			ga.Repopulate();
			
			System.out.println();
			System.out.println("Adjusting difficulty");
	        if(this.frozenEnemies == true && this.changeLvl == true)
	        {
	        	System.out.println("Changing level seed");
	        	if(this.levelseed < 15 )
	        		this.levelseed++;
	        	this.frozenEnemies = false;
	        	this.changeLvl = false;
	        }
	        	
			if(g!=0 && (g%10)==0) //Save the best calculated agent after each 10th repopulation.
			{
				System.out.println();
				System.out.print("...SAVING...");
				ga.SaveANN();
				System.out.println("...SAVING DONE...");				
			}
		}
		
		ga.SortPopulationAfterFitness();
		ga.SaveANN();
		
		System.exit (0);
	}
	
	public double PlaySingleNet(Agent controller, int seed, boolean freezeEnemies)
	{
		MarioAIOptions marioAIOptions = new MarioAIOptions(new String[0]);
		
		marioAIOptions.setAgent(controller);        
    	BasicTask task = new BasicTask(marioAIOptions);
    	
    	marioAIOptions.setVisualization(false);
    	marioAIOptions.setLevelRandSeed(seed);
    	marioAIOptions.setLevelDifficulty(this.lvlDifficulty);
    	
    	marioAIOptions.setFPS(99);
    	marioAIOptions.setFrozenCreatures(freezeEnemies);
    	marioAIOptions.setLevelType(seed%3);
    	    	
        if(lvlLength != -1)
        	marioAIOptions.setLevelLength(lvlLength);
        	        
        double fitness = Test(controller, task, seed); 
        return fitness;		
	}
	
	public double Test(Agent controller, BasicTask task, int seed)
	{
		double distancePassed = 0;
		int killsByStomp = 0;
		int killsByFire = 0;
		int timeLeft = 0;
		int marioMode = 0;
		int marioStatus = 0;
		int coins = 0;
		       
		task.doEpisodes(1, false, 1);
		
		EvaluationInfo info = task.getEnvironment().getEvaluationInfo();
		//evInfo = info;
		distancePassed = info.distancePassedPhys;
		killsByStomp = info.killsByStomp;
		killsByFire = info.killsByFire;
		coins = info.coinsGained;
		timeLeft = info.timeLeft;
		marioMode = info.marioMode;
		marioStatus = info.marioStatus;
		
		boolean falledInAGap = info.Memo.equals("Gap");
		
		double computedFit = ComputeFitness(distancePassed, timeLeft, killsByStomp, killsByFire, coins, marioMode, falledInAGap, marioStatus);		
		
		//System.out.println("\nEvaluationInfo: \n" + task.getEnvironment().getEvaluationInfoAsString());
				
		return computedFit;
       
	}
	
	private double ComputeFitness(double dist, int timeLeft, int killsByStomp, int killsByFire, int coins, int marioMode, boolean isInGap, int mariostatus)
	{
		double fitness = (dist * 10);
		if(isInGap)
		{
			marioMode = 0;
			fitness = -5000;
		}
		
		//int bonusDistance1 = (int)(this.lvlLength * 0.3);
		int bonusDistance2 = (int)(this.lvlLength * 0.5);
		
		/*if(dist > bonusDistance1)
		{
			fitness += timeLeft * 5;
		}
		else*/ if(dist > bonusDistance2)
		{
			//fitness *= (marioMode + 1);
			fitness += killsByFire * 100 + killsByStomp * 50;
			
			if(marioMode==2)
			{
				fitness += 5000;
			}
			else if(marioMode == 1)
			{
				fitness += 2500;
			}			
		}
		else
		{
			//fitness *= (marioMode + 1);
		}
		
		if(mariostatus == Mario.STATUS_WIN)
		{
			fitness += 10000;
		}
		//Evaluating the current achieved to see if increase level difficulty;
		/*if(mariostatus == Mario.STATUS_WIN && this.frozenEnemies == false && this.changeLvl == false)
		{
			this.frozenEnemies = true;
			System.out.println();
			System.out.println("Completed Frozen Scenario...");
			System.out.println();
		}
		else if (mariostatus == Mario.STATUS_WIN && this.frozenEnemies == true && this.changeLvl == false)
		{
			System.out.println();
			System.out.println("Completed Scenario...");
			System.out.println();
			this.changeLvl = true;
		}*/
		
		return fitness;
	}
	
	public void PrintEvaluation()
	{
		if(evInfo != null)
		{
			//evInfo.P
		}
	}
	
	public static void main(String[] args)
	{
		int lvlDifficulty = 0;
		int lvlLength = -1;
		int epochs = 1000;
		int geneSize = 100;
		int[] netLayers = new int[]{13,15,10,5};
		int bestsave = 20;
		int crossover = 80;
		int multicrossover = 60;
		String trainedGANN = "BestAgentNNlvl3.data";
		
		//deprecated
		//new GeneticAlgorithmTrainer(lvlDifficulty, lvlLength, epochs, geneSize, netLayers, trainedGANN);
		new GeneticAlgorithmTrainer(lvlDifficulty, lvlLength, epochs, geneSize, netLayers, trainedGANN, crossover, multicrossover, bestsave);
		
	}
}
