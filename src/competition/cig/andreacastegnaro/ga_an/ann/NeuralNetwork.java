package competition.cig.andreacastegnaro.ga_an.ann;

//For neural network initialization
import java.security.InvalidParameterException;

public class NeuralNetwork
{

	/**
	 * Neural Net as a box: m input, n output, l layers
	 */
	
	protected float inputs[];
	protected float outputs[];
	protected int outCount; //this track the total # outputs from net
	protected NeuronLayer layers[]; //this track the internal structure (at least two layers)
	protected int neuronsEachLayer[]; //this track for each layer the #number of neurons it contains	
	
	//Used to train the net and fast debugging
	protected float errors[];
	protected float expectedOutputs[][];

	/**
	 * Constructors
	 */
	
    /**
     * @param neuronsPerLayer
     */
	public NeuralNetwork(int neuronslayer[])
	{		
		if(neuronslayer.length < 2)
		{
			throw new InvalidParameterException("Invalid number of layers");
		}

		this.layers = new NeuronLayer[neuronslayer.length];
		this.neuronsEachLayer = neuronslayer;
		//last layer is the output
		this.outCount = neuronslayer[neuronslayer.length - 1];
		this.outputs = new float[this.outCount];
		this.errors = new float[this.outCount];
		
		for(int i = 0; i < layers.length; i++)
		{
			if(i == 0)
			{
				layers[i] = new NeuronLayer(neuronsEachLayer[i]);
			}
			else
			{
				layers[i] = new NeuronLayer(neuronsEachLayer[i],neuronsEachLayer[-1]);
			}
		}

		
	}
	/**
	 * Providing an entry point for debugging
	 * @param args
	 */
	public static void main(String[] args)
	{
		
	}
	
}
