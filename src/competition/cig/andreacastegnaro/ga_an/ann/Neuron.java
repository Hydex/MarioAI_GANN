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
	private Connection connectionLinks[];
	private float outputValue; //must be [0,1]
	/**
	 * Constructors
	 */
	public Neuron()
	{
		this.connectionLinks = new Connection[0];
		this.numInputLinks = 0;
		this.outputValue = 0.0f;
	}
	
	public Neuron(int connectionsCount)
	{
		this.numInputLinks = connectionsCount;
		this.connectionLinks = new Connection[connectionsCount];
		for(int i = 0; i<numInputLinks; ++i)
		{
			connectionLinks[i] = new Connection();
		}
		this.outputValue = 0.0f;
	}
	
	public Neuron(float input[])
	{
		this.numInputLinks = input.length;
		connectionLinks = new Connection[numInputLinks];
		for(int i = 0; i < numInputLinks; i++)
		{
			connectionLinks[i] = new Connection(input[i]);
		}
		this.outputValue = 0.0f;
	}
	
	public Neuron(Connection connectionlinks[])
	{
		this.connectionLinks = connectionlinks;
	}
	
	/**
	 * Functions
	 */
	public void SetInputValues(float input[]) throws Exception
	{
		if(input.length == connectionLinks.length)
		{
			for(int i = 0; i < connectionLinks.length; i++)
			{
				connectionLinks[i].setInputValue(input[i]);
			}
		}
		else throw new Exception("Number of connections does not match");
	}
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
    	float sum = 0.0f;
    	for(Connection conn : connectionLinks)
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
	/**
	 * Getter functions
	 */
	public float GetNumeberInputConnections(){return numInputLinks;}
	public Connection[] GetConnections() {return connectionLinks;}
	public float GetOutputValue(){return outputValue;}
	
}
