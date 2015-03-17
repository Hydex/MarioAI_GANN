package competition.cig.andreacastegnaro.ga_an.ann;

//For neural network initialization
import java.security.InvalidParameterException;
import java.util.*;
import java.io.Serializable;

public class NeuralNetwork implements Serializable
{

	protected float inputs[];
	protected float outputs[];
	protected int outCount; //this track the total # outputs from net
	protected NeuronLayer layers[]; //this track the internal structure (at least two layers)
	protected int neuronsEachLayer[]; //this track for each layer the #number of neurons it contains	
	
	//Used to train the net and fast debugging
	protected float errors[];
	protected float expectedOutputs[][];
	
	protected float learningRate;

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
				layers[i] = new NeuronLayer(neuronsEachLayer[i],neuronsEachLayer[i-1],true);
			}
		}		
	}
	
	public NeuralNetwork(int neuronslayer[], float learningRate)
	{
		this(neuronslayer);
		this.learningRate = learningRate;
	}

	/**
	 * This function is meant to be used for the genetic algorithm implementation. 
	 * It represents in that key lecture a gene
	 * @return all weights from this topology
	 */
	public float[] GetTotalWeights()
	{
		int weightsCount = GetNumberTotalWeights();
		float weigths[] = new float[weightsCount];
		
		int count  = 0;
		
		for (NeuronLayer layer : this.layers)
		{
			for (Neuron neuron : layer.GetNeurons())
			{
				for(Connection link : neuron.GetConnections())
				{
					weigths[count] = link.getWeight();
					count++;
				}
			}
		}
		
		return weigths;
		
	}
	
	public int GetNumberTotalWeights()
	{
		int nofweigths = 0;
		
		for(int i = 0; i < neuronsEachLayer.length - 1; i++)
		{
			//dato n output e m input al livello successivo il numero di pesi e' n * m 
			nofweigths += neuronsEachLayer[i+1] * (neuronsEachLayer[i] + 1);
		}
		
		//for(NeuronLayer layer : layers)
		//{
		//	for(Neuron neuron : layer.GetNeurons())
		//	{
		//		nofweigths += neuron.GetNumberInputConnections();
		//	}
		//}
		
		return nofweigths;
	}

	public void ComputeNet()
	{
		float tempouts[] = null;
		//We need to go from the the first hidden layer!
		for (int i = 1; i < layers.length; i++)
		{
			tempouts = new float[neuronsEachLayer[i]];
			tempouts = layers[i-1].GetOutputs();
			//Get output from previous layer and setting as inputs of the current one
			layers[i].SetLayerInput(tempouts);
			layers[i].CalculateLayerOutput();
			
			//The last layer is the net output
			if(i == layers.length - 1)
			{
				outputs = layers[i].GetOutputs();
			}
		}
	}
	
	public void SetAllWeights(List<Float> weights)
	{
		int counter = 0;
		int stop = GetNumberTotalWeights();
		for(NeuronLayer layer : layers)
		{
			for(Neuron neuron : layer.GetNeurons())
			{
				for(Connection link : neuron.GetConnections())
				{
					if(counter == weights.size())
						throw new InvalidParameterException("Counter can be greater of weights size");
					link.setWeight(weights.get(counter));
					counter++;
				}
			}
		}
	}
	
	public void SetInputs (float inputs[])
	{
		this.inputs = inputs;
		try
		{
			//first layer
			layers[0].SetLayerOutput(inputs);
		}
		catch(java.lang.Exception e) {System.out.println("Error: " + e);}
	}
	
	public float[] GetOutputs()
	{
		return this.outputs;
	}
	
	public int[] GetNeuronsPerLayer()
	{
		return neuronsEachLayer;
	}
	
	public void SetLearningRate(float learningRate) {
		this.learningRate = learningRate;
	}
	/**
	 * Providing an entry point for debugging
	 * @param args
	 */
	public static void main(String[] args)
	{
		
	}
	
}
