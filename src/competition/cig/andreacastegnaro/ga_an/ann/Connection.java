package competition.cig.andreacastegnaro.ga_an.ann;

import java.util.Random;

public class Connection {

	//TODO adding correction logic
	/**
	 * Member variables
	 */	
	private float weight;
	private float input;
	
	private float weightCorrection;
	/**
	 * Constructors
	 */
	/**
	 * Default set a Random weight
	 */
	public Connection()
	{
		Random r = new Random(new Random().nextLong());
		weight = r.nextFloat() * 1000000 % 2.0f + - 1.0f;
		weightCorrection = 0.0f;
		input = 0.0f;
	}
	
	public Connection(float inputValue)
	{
		Random r = new Random(new Random().nextLong());
		weight = r.nextFloat() * 1000000 % 2.0f + - 1.0f;
		weightCorrection = 0.0f;
		input = inputValue;
	}
	
	public Connection(float inputValue, float weightValue)
	{
		weight = weightValue;
		weightCorrection = 0.0f;
		input = inputValue;
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
	public float getWeight(){return weight;}
	public void setWeightCorrection(float value) {this.weightCorrection = value;}
    public float getWeightCorrection(){return weightCorrection;}
    public void setInputValue(float value){this.input = value;}
    public float getInputValue(){return input;}
}
