package competition.cig.andreacastegnaro.ga_an;
import java.io.*;
import competition.cig.andreacastegnaro.ga_an.ann.NeuralNetwork;


/**
 * This class has been copied from the internet. I ll put thee reference if I remember
 *
 */
public class FileSaver
{
	File sourceFile;
	public FileSaver(String fileName)
	{
		sourceFile = new File(fileName);
	}
	
	
	public void save(NeuralNetwork ann) throws IOException {
	    FileOutputStream f_out = new FileOutputStream(sourceFile);//"myobject.data");
	
	// Write object with ObjectOutputStream
	    ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
	
	// Write object out to disk
	    //obj_out.writeObject(this);
	    obj_out.writeObject(ann);
	    obj_out.close();
	}
	
	public NeuralNetwork load() throws IOException {
	    // Read from disk using FileInputStream
		if(sourceFile.exists())
		{
			FileInputStream f_in = new FileInputStream(sourceFile);//"myobject.data");
		
		    // Read object using ObjectInputStream
		    ObjectInputStream obj_in = new ObjectInputStream (f_in);
		
		    // Read an object
		    Object obj;
		    try 
		    {
		    	obj = obj_in.readObject();
		        NeuralNetwork ann = (NeuralNetwork) obj;
		        obj_in.close();
		        return ann;
		    } 
		    catch (IOException e) {e.printStackTrace();} 
		    catch (ClassNotFoundException e) {e.printStackTrace();}
		}		            
	    return null;
	}
}
