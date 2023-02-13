import java.util.concurrent.TimeUnit;

/*Elias Saied
 * June 10, 2022
 * 
 * This type of move will alter the stat levels of a Pokemon.
 */

public class StatusMove extends Move {

	StatusMove(String name, String type, int damage, int pp, int accuracy, int chanceOfStatus, int statusType,
	int attackShell, int defenseShell, int speedShell, int evasiveShell, int accuracyShell) 
	{
		super(name, type, damage, pp, accuracy, chanceOfStatus, statusType, attackShell, defenseShell, speedShell, evasiveShell,
				accuracyShell);	
	}

	
	/*
	 *  Status Effects from a Move will be inflicted onto the Opponent
	 *  @param Target	The Pokemon who will recieve these effects
	 *  
	 *  
	 */
	public void useMove(Pokemon target) {
		
		if(pp <= 0)		//Check to see if this move can be done	
		{
			System.out.println("This move is out of PP");
			
			try {
				TimeUnit.MILLISECONDS.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			//Decrement PP
			pp--;
		
			//Apply Shell changes
			changeShellValues(target);
			
			//Apply Status Conditions
			int chanceToEffect = generator.nextInt(101);		// Create a random number between 0 and 100	
			if(chanceToEffect >= statusChanceThreshold )		// If the random number passes the threshold, apply statusCondtion
				target.setStatusCondition(statusType);  
		}
	}
	
	
}
