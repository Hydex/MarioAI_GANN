package competition.cig.andreacastegnaro.ga_an.ann;

import java.util.Random;
import java.io.Serializable;

public class Connection implements Serializable
{

	 /**
	 * Member variables
	 */	
	private float weight;
	private float input;
	
	private float weightCorrection;
	
	private String name;
	/**
	 * Constructors
	 */
	/**
	 * Default set a Random weight
	 */
	public Connection()
	{
		Random r = new Random(new Random().nextLong());
		this.weight = r.nextFloat() * 1000000 % 2.0f + - 1.0f;
		this.weightCorrection = 0;
		this.input = 0.0F;
		this.name = "NONAME";
	}
	
	public Connection(float inputValue)
	{
		Random r = new Random(new Random().nextLong());
		weight = r.nextFloat() * 1000000 % 2.0f + - 1.0f;
		weightCorrection = 0.0f;
		input = inputValue;
		name = "NONAME";
	}
	
	public Connection(float inputValue, float weightValue)
	{
		weight = weightValue;
		weightCorrection = 0.0f;
		input = inputValue;
	}
	
	public Connection(String name, float inputValue)
	{
		this(inputValue);
		this.name = name;
	}
	/**
	 * Functions
	 */
	public void updateWeight(float upWeightValue)
	{
		weight += upWeightValue;
	}
	/**
	 * Setter/Getter functions
	 */
	public void setWeight(float value) { this.weight = value;}
	public float getWeight(){return this.weight;}
	public void setWeightCorrection(float value) {this.weightCorrection = value;}
    public float getWeightCorrection(){return weightCorrection;}
    public void setInputValue(float value) throws Exception
    {
    	if(name != "BIAS")
    		this.input = value;
    	else throw new Exception("Cannot change Bias input");
	}
    public float getInputValue(){return input;}
    public String GetName(){return this.name;}
}
