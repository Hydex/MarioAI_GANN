package ch.idsia.scenarios;

import competition.cig.andreacastegnaro.ga_an.MarioAgent_GA_NN;
import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.*;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;


public class PlayGANN
{
	public static double ComputeFitness(int lvlLength, double dist, int timeLeft, int kills, int marioMode, boolean isInGap)
	{
		double fitness = (dist * 10);
		if(isInGap)
		{
			marioMode = 0;
			fitness = -5000;
		}
		
		int bonusDistance = lvlLength/2;
		
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
    public static void main(String[] args) 
    {  	
    	Agent controller = new MarioAgent_GA_NN("BestAgentNNlvl2.data",false);
    	int lvlLength = 1000;
    	double score = 0; 
		for (int seed = 0; seed < 15; seed++)
		{
			MarioAIOptions marioAIOptions = new MarioAIOptions(new String[0]);
			marioAIOptions.setAgent(controller);        
	    	BasicTask task = new BasicTask(marioAIOptions);
	    	
	    	marioAIOptions.setVisualization(true);
	    	marioAIOptions.setLevelRandSeed(seed);
	    	marioAIOptions.setLevelDifficulty(0);
	    	
	    	marioAIOptions.setFPS(70);
	    	marioAIOptions.setFrozenCreatures(false);
	    	marioAIOptions.setLevelType(seed%3);
	    	task.doEpisodes(1, true, 1);
	    	EvaluationInfo info = task.getEnvironment().getEvaluationInfo();
	    	double distancePassed = info.distancePassedPhys;
			int kills = info.killsTotal;
			int timeLeft = info.timeLeft;
			int marioMode = info.marioMode;
			
			boolean falledInAGap = info.Memo.equals("Gap");
			score += PlayGANN.ComputeFitness(lvlLength, distancePassed, timeLeft, kills, marioMode, falledInAGap);
			//double currentscore = 
		}   	
    	System.out.println("Final score: " + score);
        System.exit(0);
    }
}

