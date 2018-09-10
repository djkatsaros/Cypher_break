import java.util.Scanner;
import java.util.Random;

/**
 * Creates a random cypher
 * 
 * @author (Dean Katsaros -- katsaros@math.umass.edu) 
 * @version (3/1/17)
 */
public class Cypher
{
    /* main. takes as input a sentence and outputs an encoded sentence. The Cypher is also outputed,
     *  visualized as a mapping from the alphabet to the cypher. The Cypher map is created via randomly
     *  assigning a letter of the alphabet to another letter in  a bijective fashion Using the random() 
     *  function.
     * */
    public static void main(String[] args)
    {
        System.out.println("Enter a sentence");
        Scanner inputScanned = new Scanner(System.in);
        String input = inputScanned.nextLine();
        input = input.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        input.replaceAll("\\s","");
        char[] inputArray = input.toCharArray();
        inputScanned.close();
        
        char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r',
            's','t','u','v','w','x','y','z'};
        char[] cypherArray = alphabet.clone();
        createCypher(cypherArray);
        encrypt(alphabet,inputArray, cypherArray);
        
        for(int index=0; index<alphabet.length; index++)
        {
            System.out.print(alphabet[index]);    
        }
        System.out.print("\n");
        for(int index2=0; index2<cypherArray.length; index2++)
        {
            System.out.print(cypherArray[index2]);
        }
        
        System.out.println("\n The encrypted message is:\n");
        for(int index = 0; index<inputArray.length; index++)
        {
            System.out.print(inputArray[index]);
        }
        System.out.print("\n");
        decrypt(alphabet, inputArray, cypherArray);
        for(int index4=0;index4<inputArray.length; index4++)
        {
            System.out.print(inputArray[index4]);
        }

    }
    /* Creates Cypher */
    public static void createCypher(char[] alphabet)
    {
        if (alphabet != null) 
        {
            Random generator = new Random();
            for (int index =0 ; index < alphabet.length; index ++)
            {
                int otherIndex = generator.nextInt(alphabet.length);
                char temp = alphabet[index];
                alphabet[index] = alphabet[otherIndex];
                alphabet[otherIndex] = temp;
            }
        }
    }
    
    /* Encodes via the created Cypher*/
    public static void encrypt(char[] alphabet, char[] input, char[] cypher)
    {
        if (input != null)
        {
            for(int index = 0;index < input.length; index++)
            {
                for(int index2 = 0; index2 < alphabet.length; index2 ++)
                {
                    if(input[index] == alphabet[index2])
                    {
                        input[index] = cypher[index2];
                        break;
                    }
                }
            }
        }
    }
    
    /* Decodes via the cypher's inverse map */
    public static void decrypt(char[] alphabet, char[] encryptedInput, char[] cypher)
    {
        if (encryptedInput != null)
        {
            for(int index = 0;index < encryptedInput.length; index++)
            {
                for(int index2 = 0; index2 < cypher.length; index2 ++)
                {
                    if(encryptedInput[index] == cypher[index2])
                    {
                        encryptedInput[index] = alphabet[index2];
                        break;
                    }
                }
            }
        }
    }

}

