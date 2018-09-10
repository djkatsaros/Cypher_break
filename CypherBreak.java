import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

//import java.util.Random;

/**
 * Attempts to break an encrypted message. Uses letter frequency as a basis, comparing the frequency
 * of a letter to average frequencies for letters in the English language. Note, this method works 
 * far better the longer the message is (which is...intuitive).
 * 
 * References
 * [1] An Introduction to Mathematical Cryptography 
 * 			by Hoffstein, Pipher, Silverman 
 * 
 * @author (Dean Katsaros -- katsaros@math.umass.edu) 
 * @version (3/1/17)
 */
public class CypherBreak {

	
	public static void main(String[] args)
	{
		System.out.println("Enter an encrypted sentence");
        Scanner inputScanned = new Scanner(System.in);
        String input = inputScanned.nextLine();
        input = input.toLowerCase();
        //input.replaceAll("\\s","");
        char[] inputArray = input.toCharArray();
        inputScanned.close();
        
        char[] alphabet = { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r',
	            's','t','u','v','w','x','y','z'};
      
        char[] alphabetOrdered = {'e','t','a','o','n','r','i','s','h','d','l','f','c','m','u','g',
        		'y','p','w','b','v','k','x','j','q','z'};
        
        int[] alphabetOrderedIndices = {4,19,0,14,13,17,8,18,7,3,11,5,2,12,20,6,24,15,22,1,21,
        		10,23,9,16,25};
        
        // Frequencies as given in [1] 
        double[] freqs = {0,0.0815, 0.0144, 0.0276, 0.0379, 0.1311, 0.0292, 0.0199, 0.0526, 0.0635, 0.0013, 0.0042,
        		0.0339, 0.0254, 0.0710, 0.0800, 0.0198, 0.0012, 0.0683, 0.0610, 0.1047, 0.0246, 0.0092, 0.0154, 
        		0.0017, 0.0198, 0.0008} ;
        
        double[] freqsOrdered= {0.1311, 0.1047,0.0815,0.0800, 0.0710,0.0683,0.0635,0.0610,0.0526,0.0379,0.0339,
        		0.0292,0.0276,0.0254,0.0246,0.0199,0.0198,0.0197,0.0154,0.0144,0.0092,0.0042,0.0017,0.0013,
        		0.0012, 0.0008};
        
        //Calculate the frequencies of each letter in the message. Create cypher. 
        //Decrypt the message
        double[] inputFreqs = getFreqs(inputArray, alphabet);
        char[] cypherUsingFreqs = createCypherDist(inputFreqs, inputArray,freqs,alphabet);
        char[] cypherUsingOrder = createCypherOrder(inputFreqs, alphabetOrderedIndices, alphabet);
        char[] decodedMessageFreqs = decrypt(inputArray, cypherUsingFreqs, alphabet);
        char[] decodedMessageOrder = decrypt(inputArray, cypherUsingOrder, alphabet);
        
        //Print decoded message using  freqs
        for(int index = 0; index<decodedMessageFreqs.length; index++)
        {
        	System.out.print(decodedMessageFreqs[index]);
        }
        System.out.print("\n");
        //Print decoded message using  Order
        for(int index = 0; index<decodedMessageOrder.length; index++)
        {
        	System.out.print(decodedMessageOrder[index]);
        }
        
        System.out.print("\n\n");
        for(int index2 = 0; index2<inputFreqs.length; index2++)
        {
        System.out.print(inputFreqs[index2]+" ");
        }
        System.out.print("\n\n");
        
        //print  cyphers for visual analysis
        for(int index3 = 0; index3<inputFreqs.length; index3++)
        {
        System.out.print(cypherUsingFreqs[index3]);
        }
        System.out.print("\n\n");
        for(int index3 = 0; index3<inputFreqs.length; index3++)
        {
        System.out.print(cypherUsingOrder[index3]);
        }
        System.out.print("\n\n");
        
        // sanity check
        System.out.println(inputArray.length);
        System.out.println(decodedMessageFreqs.length);
        System.out.println(decodedMessageOrder.length);
	}
	
	public static double[] getFreqs(char[] input, char[] alphabet)
	{
		double[] inputFreqs = new double[alphabet.length] ;
		for(int ind = 0; ind < alphabet.length; ind++)
		{
			for(int ind2 = 0; ind2 < input.length; ind2++)
			{
				if(input[ind2] == alphabet[ind])
				{
					inputFreqs[ind] = inputFreqs[ind] + 1;
				}
			}
		}
		
		for(int ind3 = 0; ind3 < inputFreqs.length; ind3++)
		{
			inputFreqs[ind3] = (inputFreqs[ind3] / input.length);
		}
		
		return inputFreqs;
	}
	
	/* Creates a decrypting cipher by minimizing differences between the encrypted messages 
	*freqs and  standard freqs 
	*/
	public static char[] createCypherDist(double[] inputFreqs, char[] input, double[] freqs, char[] alphabet)
	{
		char[] cypher = new char[alphabet.length];
		for (int index  = 0; index < inputFreqs.length; index++)
		{
			if ( inputFreqs[index]!= 0)
			{
				double minFreq = 1;
				int minIndex = 0;
				for (int index2 =0; index2 < freqs.length; index2++)
				{
					if( Math.abs(inputFreqs[index] - freqs[index2] ) <= minFreq)
							{
								minFreq = Math.abs(inputFreqs[index] - freqs[index2]);
								minIndex = index2;
							}
				}
				//creates the inverse of the cypher via finding the frequency match.
				cypher[index] = alphabet[minIndex];
			}
		}
		
		return cypher;
	}
	
	/* Creates a decrypting cipher by using the ordered frequencies. Finds the max. input freq, 
	 * assigns that character to e in the cypher, and then deletes the max. from input freqs.
	 * Assigns the next highest freq to t in the cypher. Etc. Calls the two Aux methods needed
	 * because java has no standard methods that easily satisfy these needs. 
	 */
	public static char[] createCypherOrder(double[] inputFreqs, int[] alphabetOrderedIndices, char[] alphabet)
	{
		int maxIndex = 100;		
		char[] cypher = new char[alphabetOrderedIndices.length];
		
		for(int i = 0; i < alphabetOrderedIndices.length; i++)
		{
			maxIndex = getIndexofMax(inputFreqs);
			cypher[maxIndex] = alphabet[alphabetOrderedIndices[i]];
			inputFreqs = removeMax(inputFreqs,maxIndex);
		}
		return cypher;
	}

	public static char[] decrypt(char[] input, char[] cypher, char[] alphabet)
	{
		char[] output = new char[input.length];
		for(int k = 0; k < cypher.length; k++)
		{
			for (int j = 0; j < input.length; j++)
			{
				if(input[j] == cypher[k]) 
				{
					output[j] = alphabet[k];
				}
			}
		}
		
		return output;
	}
	
	// Auxiliary methods 
	// get the index that achieves the max of an array
	public static int getIndexofMax(double[] Array)
	{
		int maxIndex = 0;
        double max = Array[0];

        for(int i = 0; i < Array.length; i++)
        {
            if(Array[i] > max)
            {
                max = Array[i]; 
                maxIndex = i;
            }	
        }
		return maxIndex;
	}
	
	// remove the max of the array
	public static double[] removeMax(double[] oldArray, int maxIndex)
	{
		double[] newArray = new double[oldArray.length - 1];
		
		for(int i = 0; i< maxIndex; i++) {newArray[i] = oldArray[i];}
		for(int j = maxIndex+1; j <oldArray.length; j++) {newArray[j-1] = oldArray[j];}
				
		return newArray;
	}
	
	
	
}
