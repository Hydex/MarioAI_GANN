package competition.cig.andreacastegnaro.ga_an;

import competition.cig.andreacastegnaro.ga_an.ann.*;
import ch.idsia.agents.controllers.*;
import ch.idsia.agents.*;
import ch.idsia.benchmark.mario.environments.Environment;

public class MarioAgent_GA_NN extends BasicMarioAIAgent implements Agent
{

	private NeuralNetwork ann;
	private int layers[];
	private final int numberOfHiddenNeurons = 10;
	private final int numberOfInputs = 5;
	private final int numberOfOutputs = 6;//Environment.numberOfKeys;
	static private final String name = "GANN Agent";
	private float inputs[];
	private boolean isMarioInAGap = false;
	private boolean verbose = false;
	private FileSaver annFile;
	
	public MarioAgent_GA_NN()
	{
		super(name);
		annFile = new FileSaver("BestAgentNN.data");
		zLevelScene = 1;
		zLevelEnemies = 1;
		inputs = new float[numberOfInputs];
		
		try
		{
			if(annFile.load() != null)
			{
				this.ann = annFile.load();
				this.ann.SetLearningRate(0.1f);
				this.layers = ann.GetNeuronsPerLayer();
			}
			else
			{
				int nlayers = 3;
				layers = new int[nlayers];
				layers[0] = this.numberOfInputs;
				layers[1] = this.numberOfHiddenNeurons;
				layers[2] = this.numberOfOutputs;
				ann = new NeuralNetwork(layers, 0.1f);
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
			
	public MarioAgent_GA_NN(String filename, boolean verbose)
	{
		super(name);
		this.verbose = verbose;
		annFile = new FileSaver(filename);
		zLevelScene = 1;
		zLevelEnemies = 1;
		inputs = new float[numberOfInputs];
		
		try
		{
			if(annFile.load() != null)
			{
				this.ann = annFile.load();
				this.ann.SetLearningRate(0.1f);
				this.layers = ann.GetNeuronsPerLayer();
			}
			else
			{
				int nlayers = 3;
				layers = new int[nlayers];
				layers[0] = this.numberOfInputs;
				layers[1] = this.numberOfHiddenNeurons;
				layers[2] = this.numberOfOutputs;
				ann = new NeuralNetwork(layers, 0.1f);
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public MarioAgent_GA_NN(NeuralNetwork ann)
	{
		super(name);
		zLevelScene = 1;
		zLevelEnemies = 1;
		layers = ann.GetNeuronsPerLayer();
		int nofinputs = layers[0];
		inputs = new float[numberOfInputs];
		
		this.ann = ann;
		
	}
	
	public boolean[] getAction()
	{
        CreateInputs();
        ann.SetInputs(inputs);
        ann.ComputeNet();
        float[] outputs = ann.GetOutputs();
        boolean[] action = new boolean[this.numberOfOutputs];
        for (int i = 0; i < action.length; i++) 
        { 
        	float f = outputs[i];
      	  	Math.round(f);
      	  	if(f > 0.5f)
      	  	{
      	  		action[i] = true;
      	  	}
      	  	else
      	  	{
      	  		action[i] = false;
      	  	}
        }
      
        return action;
	}
	
	public void CreateInputs()
    {
		
        //ENEMY OBSERVATION
          inputs[0] = enemyInFrontDistance();
          inputs[1] = enemyAboveDistance();
          inputs[2] = obstacleDistance();
          inputs[3] = obstacleHeight(inputs[2]);
        //MARIO STATE OBSERVATION
          inputs[4] = canMarioShoot();
          canMarioJump();
          
          PrintInputs();
    }
	
	private float enemyAboveDistance()
	{
		int distance = 3;
		float fraction = 1.0f/distance;
		float retWeightedDistance;
		for(int i = 1; i < 4; i++ )
	   	{
	   		int value = getEnemiesCellValue(marioEgoRow + i, marioEgoCol);
	   		if(value == 80) //Goomba/Troopa
	   		{
	   			retWeightedDistance = distance * fraction;
	   			return retWeightedDistance;
	   		}
	   		else
	   		{
	   			distance--;
	   		}
	   	}
	   	return 0;
	}
	
	private float enemyInFrontDistance()
	{
		int distance = 3;
		float fraction = 1.0f/distance;
		float retWeightedDistance;
		for(int i = 1; i < 4; i++ )
	   	{
	   		int value = getEnemiesCellValue(marioEgoRow, marioEgoCol + i);
	   		if(value == 80)
	   		{ //Goomba/Troopa //93 is spike koopa
	   			retWeightedDistance = distance * fraction;
	   			return retWeightedDistance;
	   		}
	   		distance--;
	   	}
	   	return 0;
	}
	
	private float obstacleDistance()
	{
		int distance = 3;
		float fraction = 1.0f/distance;
		float retWeightedDistance;
		for(int i = 1; i < 4; i++ )
	   	{
	   		int value = getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + i);
	   		//System.out.println(value);
	   		if(value == -60 || value == -85 || value == -24)
	   		{
	   			retWeightedDistance = distance * fraction;
	   			return retWeightedDistance;
	   		}  			
	   			
	   		distance--;
	   	}
	   	
	   	return 0; 	
	}
	
	private float obstacleHeight(float obdistance)
	{
		//return retWeightedDistance;
	   	int precision = 3;
	   	//obdistance = obstacleDistance();
	   	if(obdistance != 0) 
		{
	   		int distanceX = 0;
	   		int distance =  (int) Math.round(obdistance * precision);
	   		if(distance == 3)
   				distanceX = 1;			   
	   		else if(distance == 2)
	   			distanceX = 2;
	   		else if(distance == 1)
	   			distanceX = 3;
			
		   	float fraction = 1.0f/precision;
	   		float retWeightedHeigthDistance = 0;
		   	
		   	for(int i = 0; i < 3; i++ )
		   	{
		   		int value = getReceptiveFieldCellValue(marioEgoRow - i, marioEgoCol + distanceX);
		   		if(value < 0)
		   		{
		   			retWeightedHeigthDistance += fraction;		   			
		   		}		   			
		   	}
		   	return retWeightedHeigthDistance; 	
	   	}
	   	else
   			return 0;
	}
	
	private float canMarioShoot()
	{
		switch((marioMode))
		{
			case 0: return 0.33f;
			case 1: return 0.66f;
			case 2: return 0.99f;
			default: return -1.0f;
		}
	}
	
	private float canMarioJump()
	{
		float ret = 0;
		if(this.isMarioAbleToJump && this.isMarioOnGround)
			ret = 1;
		return ret;
	}
	
	private void PrintInputs()
	{
		if(verbose)
		{
			System.out.println("Enemy in front: " + inputs[0]);
			System.out.println("Enemy abov: " + inputs[1]);
			System.out.println("Obstacle front: " + inputs[2]);
			System.out.println("Obstacle height: " + inputs[3]);
			System.out.println("Mario state: " + inputs[4]);
			System.out.println("Mario jump: " + inputs[5]);
		}
	}
}
