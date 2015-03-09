package competition.cig.andreacastegnaro.ga_an.ann;

/**
 * 
 * @author Andrea
 * This class is meant to be used as an helper for the neural network for handling operations
 * A layer has n neurons and n outputs.
 * Layers can be connected to each other to allow depth inside the structure
 * Use neuron class for activating and set all the output values.
 */

public class NeuronLayer
{
	/**
	 * Member functions
	 */
	private int neuronsCount;
	private Neuron neurons[];
	private float outputs[];
	/**
	 * Constructors
	 */
	/**
	 * Standard Layer constructor
	 * @param numNeurons
	 * @param numInputLinksPerNeuron
	 */
    public NeuronLayer(int numNeurons, int numInputLinksPerNeuron)
    {
        this.neuronsCount = numNeurons;
        outputs = new float[numNeurons];
        neurons = new Neuron[numNeurons];
        for(int i = 0; i<numNeurons; i++)
            neurons[i] = new Neuron(numInputLinksPerNeuron);
    }
    
	/**
	 * This is needed for input layer
	 * @param numNeurons
	 */
    public NeuronLayer(int numNeurons)
    {
        this.neuronsCount = numNeurons;
        outputs = new float[numNeurons];
        neurons = new Neuron[numNeurons];
        for(int i = 0; i<numNeurons; i++)
            neurons[i] = new Neuron();
    }
	
	public NeuronLayer(Neuron neurons[])
	{
		this.neurons  = neurons;
	}
	/**
	 * Functions
	 */
	public void CalculateLayerOutput()
	{
		for(int i = 0; i < neuronsCount; i++)
		{
			neurons[i].Activate();
			outputs[i] = neurons[i].GetOutputValue();
		}
	}
	
	/**
	 * Setter functions
	 */
	public void SetLayerInput(float input[])
	{
		for(Neuron neuron : neurons)
		{
			try
			{
				neuron.SetInputValues(input);
			}			
			catch (Exception e){e.printStackTrace();}
		}
	}
	
	
	/**
	 * Getter functions
	 */
	public int GetNeuronsPerLayer(){return this.neuronsCount;}
	public Neuron[] GetNeurons(){return this.neurons;}
	public float[] GetOutputs(){return this.outputs;}
}
