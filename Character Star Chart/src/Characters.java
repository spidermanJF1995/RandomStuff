import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Characters
{
	public static void main(String[] args) throws FileNotFoundException
	{	
		Scanner scan = new Scanner(new File("Characters.txt"));
		String get_text = "", final_text = "";
		int[] char_count = new int[26]; // 0 represents 'A', 1 represents 'B' and the pattern continues until 25 represents 'Z'
		int text_length = 0;
		int char_as_number = 0, holder = 0;
		int lowest_count = 99999999, max_count = 0;

		while(scan.hasNext()) // gets all of the characters in the file and puts them into one long String
		{
			get_text = scan.nextLine().toUpperCase(); // sends all Characters of text toUpperCase()
			final_text += get_text;
		} // end while()
		
		text_length = final_text.length();
		
		for(int i = 0; text_length != 0; i++, text_length--) // counts the occurrences of each character and puts the count into char_count[]
		{
			char_as_number = final_text.charAt(i); // no need to cast char to int. it is done automatically
						
			if(char_as_number < 65 || char_as_number > 90) // checks to see if the character is not a letter
			{
				// do nothing
			}
			else
			{
				char_count[char_as_number - 65]++; // -65 since 'A' has a value of 65 and to make 'A' be at 0, -65 is needed
			} // end if/else
		} // end for
				
		for(int i = 0; i < char_count.length; i++) // finds the least count value and the highest count value
		{
			holder = char_count[i];
			
			if(holder != 0 && holder < lowest_count)
			{
				lowest_count = holder;
			} // end if
			
			if(holder != 0 && holder > max_count)
			{
				max_count = holder;
			} // end if
		} // end for

		System.out.println(); // buffer from top of Console output
		
		for(int i = 0; max_count > 0;) // prints out the appropriate '*' and " " as needed
		{
			if(char_count[i] == max_count) // ensures the '*' gets printed top to bottom
			{
				System.out.print("  * ");
				char_count[i] = char_count[i] - 1;
				i++;
			}
			else
			{
				System.out.print("    ");
				i++;
			} // end if/else
			
			if(i == 26) // checks to see if a new line needs to be created
			{
				i = 0;
				System.out.println();
				max_count--;
			} // end if
									
		} // end for

/*********************************************************************************************************************************************************
 * 	Below loops the printing of the bottom of the chart. It is a little easier than typing 2 long Strings. 		
		
		for(int i = 0; i < 26; i++)
		{
			System.out.print(" ___");
		} // end for
		
		System.out.println();

		for(int i = 65; i < 91; i++)
		{
			char character = (char) i;
			System.out.print("  " + character + " ");
		} // end for
*********************************************************************************************************************************************************/
		
/*********************************************************************************************************************************************************
 * 	Below is the bottom of the chart manually created. 	
*********************************************************************************************************************************************************/
 
  		System.out.println(" ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___");
		
		System.out.println("  A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P   Q   R   S   T   U   V   W   X   Y   Z ");  

		scan.close();
	} // end main() method
} // end Characters class
