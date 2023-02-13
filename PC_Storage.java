import java.util.InputMismatchException;
import java.util.Scanner;
/*	Elias Saied
5/9/2022

	The PC Storage is used as extra storage when the player has extra Pokemon they can't carry on their
	team, or if they want to offload Pokemon from their team.
*/

public class PC_Storage implements Storage {
	private final static int MAX_CAPACITY = 50;
	private int PKMN_in_storage;
	private Pokemon[] PC;
	private Scanner keyboard;
	
	public PC_Storage()
	{
		PKMN_in_storage = 0;
		PC = new Pokemon[MAX_CAPACITY];
	}
	
	//Add a PKMN to the PC
	//@param PKMN	 the Pokemon to be added
	public void deposit(Pokemon PKMN)
	{
		if(checkStorage())	//If the PC is not full
		{
			PC[PKMN_in_storage] = PKMN;	//Add into PC
			PKMN_in_storage++;
		}
			
	}
	
	/*
	 * Requests the user to choose a Pokemon from the PC that will be taken out
	 */
	public Pokemon withdrawl()
	{
		keyboard = new Scanner(System.in);
		int PKMN_Pos;
		display();
		System.out.println("Select a PKMN number: ");
		try {
			PKMN_Pos = keyboard.nextInt();
		}
		catch(InputMismatchException e)
		{
			PKMN_Pos = -10;
		}
		while(PKMN_Pos < 0 || PKMN_Pos > MAX_CAPACITY)	//Loop if the input is out of bounds
		{
			System.out.println("Invalid input. Try again: ");
			PKMN_Pos = keyboard.nextInt();
		}
		return remove(PKMN_Pos-1);		//Subtract pos - 1 
	}

	/*
	 * Displays the entries within the PC
	 */
	public void display() {
		for(int i = 0; i < PKMN_in_storage; i++) 
		{
			try 
			{
				System.out.println( (i + 1) + ") " + PC[i].getName());
			}
			catch(NullPointerException e)
			{
				//do nothing 
				System.out.println("Failure");
			}
		}
		System.out.println();
	}

	/*
	 * Check if the PC is full
	 * @return	true/false if the PC is full
	 */
	public boolean checkStorage()
	{
		if(PKMN_in_storage == MAX_CAPACITY)
		{
			System.out.println("PC Box is full. Unable to catch anymore PKMN.");
			return false;
		}
		else
			return true;
	}

	//private void swap()
	//{
		//Pokemon Team_PKMN;
		//Pokemon PC_PKMN = withdrawl();
	//}
	
	/*
	 * Removes a Pokemon from the PC and lowers the amount of PKMN inside
	 */
	private Pokemon remove(int PKMN_Pos)
	{
		Pokemon PKMN;
		PKMN = PC[PKMN_Pos];
		shiftElements(PKMN_Pos);	//Moves PKMN down 1 entry
		PKMN_in_storage--;			//Decrement pkmn count
		return PKMN;
	}
	
	/*
	 * Moves PKMN from the box down by 1 to prevent null entries from existing in between
	 * @param startingPos	The position where elements of the PC will be shifted down by 1
	 */
	private void shiftElements(int startingPos)
	{
		while(PC[startingPos] != null)		//While the current PKMN isn't NULL
		{
			PC[startingPos] = PC[startingPos + 1];	//Move the next PKMN down one element
			startingPos++;				//increment position
		}
	}

	//display stats
	public void ShowStats(Pokemon PKMN)
	{
		
	}

}
