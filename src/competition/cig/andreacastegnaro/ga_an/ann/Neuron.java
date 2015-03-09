package competition.cig.andreacastegnaro.ga_an.ann;

/**
 * The Neuron class will handle the output on the single neuron:
 * It will need to track all the input connections coming from previous layer and output to the next layer
 * It is based on the implementation found on the http://www.ai-junkie.com/ and https://kunuk.wordpress.com/2010/10/23/hybrid-ai-example-with-java-genetic-algo-and-nn/
 * Put as a reference also in the final document
 */

public class Neuron {

	/**
	 * Member variables
	 */	
	private int numInputLinks;
	private float outputValue; //must be [0,1]
	private Connection connectionLinks[];
	
	/**
	 * Constructors
	 */
	public Neuron()
	{
		this.outputValue = 0;
		this.connectionLinks = new Connection[0];
		this.numInputLinks = 0;
	}
	
	public Neuron(float input[])
	{
		this.numInputLinks = input.length;
		connectionLinks = new Connection[numInputLinks];
		for(int i = 0; i < numInputLinks; i++)
		{
			//TODO provide this kind of constructor in Connection
			connectionLinks[i] = new Connection[input[i]]);
		}
		this.outputValue = 0;
	}
	
	public Neuron(Connection connectionlinks[])
	{
		this.connectionLinks = connectionlinks;
	}
	
	/**
	 * Functions
	 */
    public void SetOutputValue(float outputValue)
    {
    	this.outputValue = outputValue;
    }
    
    public void Activate()
    {
    	outputValue = Sigmoid(EvaluateSum());
    }
    
    private float EvaluateSum()
    {
    	float sum = 0;
    	for(Connection conn : connectionLinks)
    		//TODO add the input value (coming from prev layer and the weight
    		sum += conn.getInputValue() * conn.getWeight();
    	return sum;
    }
    /**
	 * Activation function (can decide between several one but this is the common one)
	 */
	private float Sigmoid(float value)
	{
		return 1 / (1 + (float)Math.exp(-value));
	}
}
