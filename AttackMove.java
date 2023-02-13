import java.util.Random;
import java.util.concurrent.TimeUnit;

/*Elias Saied
 * June 10, 2022
 * 
 * This type of move will cause an opponent to lose health.
 */

public class AttackMove extends Move {

	//private Random generator = new Random();
	AttackMove(String name, String type, int damage, int pp, int accuracy, int chanceOfStatus, int statusType) {
		super(name, type, damage, pp, accuracy, chanceOfStatus, statusType);
	}
	
	/*
	 *  The damage and possible effects from a Move will be inflicted onto the Opponent
	 *  @param foe	The opposing Pokemon who will recieve these effects
	 *  
	 *  
	 */
	public void useMove(Pokemon foe) {
		
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
			pp--;		//lower pp of move
			//Retrieve attributes of the target pokemon
			int level = foe.getLevel();
			double attack = foe.getAttack() * foe.getAttackShellEffects();
			double defense = foe.getDefense() * foe.getDefenseShellEffects();
			int criticalHit = generator.nextInt(257);			// Random num between 0 and 256 :
			double random = (generator.nextInt(16) + 85)/100.0;		// Random percent value between 85 and 100
			double damageToTake;
			//System.out.println("HP: " + foe.getHP());
			//System.out.println("Attack: " +attack);
			//System.out.println("Defense: " + foe.getDefense() + " Level: " + foe.getDefenseShellEffects());
			
			//System.out.println(defense);
			//Calculate if a critical hit will land
			if(criticalHit == 256)		//Bonus damage if critical hit = 256
				damageToTake = (((((2 * level)/5) + 2) * damage * attack/defense)/50 + 2) * 2 * random;
			else
				damageToTake = (((((2 * level)/5) + 2) * damage * attack/defense)/50 + 2) * random;
			
			
			//Apply Damage to Target Pokemon
			int currentHP = foe.getHP();
			if(currentHP > damageToTake){
				foe.setHP(currentHP - (int)damageToTake);
				System.out.println("Foe's " + foe.getName() + " lost " + (int)damageToTake + " HP");
			}
			else{
				foe.setHP(0);	// Prevents a negative HP value from existing
				System.out.println("Foe's " + foe.getName() + " lost " + currentHP + " HP");
			}	
			//System.out.println(foe.getName() + "	HP: " + foe.getHP());
			
			//Apply Shell changes
			changeShellValues(foe);
			
			//Apply Status Conditions
			int chanceToEffect = generator.nextInt(101);		// Create a random number between 0 and 100	
			if(chanceToEffect >= statusChanceThreshold )		// If the random number passes the threshold, apply statusCondtion
				foe.setStatusCondition(statusType);  
		}
	}

}

