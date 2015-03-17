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
    	Agent controller = new MarioAgent_GA_NN("BestAgentNN.data",false);
    	int lvlLength = 1000;
    	double score = 0; 
		for (int levelseed = 0; levelseed < 10; levelseed++)
		{
			MarioAIOptions marioAIOptions = new MarioAIOptions(new String[0]);
			marioAIOptions.setVisualization(true);
			marioAIOptions.setTimeLimit(20);
			marioAIOptions.setFPS(99);
			marioAIOptions.setLevelLength(lvlLength);
			marioAIOptions.setLevelRandSeed(levelseed);
	    	marioAIOptions.setLevelDifficulty(0);
			marioAIOptions.setAgent(controller);
	    	BasicTask task = new BasicTask(marioAIOptions);
	    	task.doEpisodes(1, false, 1);
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

