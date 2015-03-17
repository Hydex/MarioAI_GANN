package competition.cig.andreacastegnaro.ga_an;

import competition.cig.andreacastegnaro.ga_an.ann.*;
import ch.idsia.agents.Agent;
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
	
	private void trainPopulation()
	{
		double score = 0;
		int indexOfNet = 0;
		this.seed = 1;
		
		for(int g = 0; g < epochs; g++)
		{
			if(g > 0)
				ga.GetPopulationFromNetSet();
			
			System.out.println("Epoch: " + g);
			
			//Play mario with Neural Net's from the population
			for(int p = 0; p < ga.GetPopulation().size(); p++)
			{
				Agent controller = new MarioAgent_GA_NN(ga.GetNeuralNetworks().get(indexOfNet));
				
				for (int levelseed = 0; levelseed < 10; levelseed++)
				{
					score += PlaySingleNet(controller, levelseed);
				}
				
				ga.GetPopulation().get(indexOfNet).SetFitness(score);
				
				System.out.println("Fitness Score: " + score);
				
				indexOfNet++;
				score = 0;
				
				//if((p%10)==0)
				//	System.out.print(".");
			}
			indexOfNet = 0;
			System.out.println();
			System.out.println("Repopulating");
			ga.Repopulate();
			
			if(g!=0 && (g%10)==0) //Save the best calculated agent after each 10th repopulation.
			{
				System.out.print("...SAVING...");
				ga.SaveANN();
				System.out.println("...SAVING DONE...");
			}
		}
		
		ga.SortPopulationAfterFitness();
		ga.SaveANN();
		
		System.exit (0);
	}
	
	public double PlaySingleNet(Agent controller, int seed)
	{
		MarioAIOptions marioAIOptions = new MarioAIOptions(new String[0]);
		
		marioAIOptions.setAgent(controller);        
    	BasicTask task = new BasicTask(marioAIOptions);
    	
    	marioAIOptions.setVisualization(false);
    	marioAIOptions.setLevelRandSeed(seed);//(Math.random () * Integer.MAX_VALUE));
    	marioAIOptions.setLevelDifficulty(this.lvlDifficulty);
    	marioAIOptions.setTimeLimit(20);
    	marioAIOptions.setFPS(99);
    	
        if(lvlLength != -1)
        	marioAIOptions.setLevelLength(lvlLength);
        	
    	//marioAIOptions.setTimeLimit(marioAIOptions.getTimeLimit()/2);
        
        double fitness = Test(controller, task, seed); 
        return fitness;
		
	}
	
	public double Test(Agent controller, BasicTask task, int seed)
	{
		double distancePassed = 0;
		int kills = 0;
		int timeLeft = 0;
		int marioMode = 0;
		double dp = 0;
       
		task.doEpisodes(1, false, 1);
		
		EvaluationInfo info = task.getEnvironment().getEvaluationInfo();
		//evInfo = info;
		distancePassed = info.distancePassedPhys;
		kills = info.killsTotal;
		timeLeft = info.timeLeft;
		marioMode = info.marioMode;
		
		boolean falledInAGap = info.Memo.equals("Gap");
		
		double computedFit = ComputeFitness(distancePassed, timeLeft, kills, marioMode, falledInAGap);		
		
		//System.out.println("\nEvaluationInfo: \n" + task.getEnvironment().getEvaluationInfoAsString());
				
		return computedFit;
       
	}
	
	private double ComputeFitness(double dist, int timeLeft, int kills, int marioMode, boolean isInGap)
	{
		double fitness = (dist * 10);
		if(isInGap)
		{
			marioMode = 0;
			fitness = -5000;
		}
		
		int bonusDistance = this.lvlLength/2;
		
		if(dist > bonusDistance)
		{
			fitness *= (marioMode + 1);
			fitness += kills * 10 + timeLeft * 2;
			
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
			fitness *= (marioMode + 1);
		}
		
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
		int lvlLength = 1000;
		int epochs = 50;
		int geneSize = 50;
		int[] netLayers = new int[]{5,7,6};
		
		String trainedGANN = "BestAgentNN.data";
		
		new GeneticAlgorithmTrainer(lvlDifficulty, lvlLength, epochs, geneSize, netLayers, trainedGANN);
	}
}
