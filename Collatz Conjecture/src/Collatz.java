import java.util.Scanner;

public class Collatz 
{
	public static void main(String[] args)
	{
		Scanner ask = new Scanner(System.in);
		int User_Number = 0;
		boolean Is_Collatz_Number = false;
		
		System.out.print("Please enter a number: ");
		User_Number = ask.nextInt();
		ask.close();
		
		if(User_Number == 1) // start big if/else
		{
			System.out.println("The number you entered is as low as it can go");
		}
		else
		{
			while(!Is_Collatz_Number)
			{
				if(User_Number == 1) // start inner if/else if/else
				{
					Is_Collatz_Number = true;
					System.out.println("The number was a Collatz number");
				}
				else if(User_Number % 2 == 0)
				{
					System.out.print(User_Number + " has been changed to --> ");
					User_Number = User_Number / 2;
					System.out.println(User_Number);
				}
				else
				{
					System.out.print(User_Number + " has been changed to --> ");
					User_Number = (User_Number * 3) + 1;
					System.out.println(User_Number);
				} // end inner if/else if/else
				
			} // end while
		} // end big if/else
	
	} // end main method
} // end Collatz class
