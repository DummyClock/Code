import java.util.Random;
import java.util.concurrent.TimeUnit;

/*Elias Saied
 * 5/10/2022
 * 
 * An abstarct class that has the essential methods that ever move should do. 
 * useMove() must be defined in child classes.
 */

public abstract class Move implements MoveEntry {

	protected String moveName;
	protected String moveType;
	protected int max_pp, pp, damage, accuracy;
	protected int chanceOfStatus;			// This value of 100 determines the chance a status condition will be set
	protected int statusChanceThreshold;	// The minimum random number that will result in a status condition being set 
	protected int statusType;		// An int value that should be between 1 and 6. This will determine the type of status set. 1 = Confused; 2 = Paralyzed; 3 = Poison; 4 = Burn; 5 = Sleep; 6 = Freeze;
	protected int attackShellRaise;
	protected int defenseShellRaise;
	protected int speedShellRaise;
	protected int evasiveShellRaise;
	protected int accuracyShellRaise;
	protected Random generator = new Random();
	
	Move(String name, String type, int damage, int pp, int accuracy, int chanceOfStatus, int statusType)
	{
		moveName = name;
		moveType = type;
		this.damage = damage;
		max_pp = pp;
		this.pp = pp;
		this.accuracy = accuracy;
		this.chanceOfStatus = chanceOfStatus;
		this.statusType = statusType;
		statusChanceThreshold = 100 - chanceOfStatus;
		attackShellRaise = 0;
		defenseShellRaise = 0;
		evasiveShellRaise = 0;
		accuracyShellRaise = 0;
	}
	
	Move(String name, String type, int damage, int pp, int accuracy, int chanceOfStatus, int statusType, 
			int attackShell, int defenseShell, int speedShell, int evasiveShell, int accuracyShell)
	{
		moveName = name;
		moveType = type;
		this.damage = damage;
		max_pp = pp;
		this.pp = pp;
		this.accuracy = accuracy;
		this.chanceOfStatus = chanceOfStatus;
		this.statusType = statusType;
		statusChanceThreshold = 100 - chanceOfStatus;
		
		attackShellRaise = attackShell;
		defenseShellRaise = defenseShell;
		speedShellRaise = speedShell;
		evasiveShellRaise = evasiveShell;
		accuracyShellRaise = accuracyShell;
	}
	
	
	
	/*
	 *  The damage and possible effects from a Move will be inflicted onto the Opponent
	 *  @param foe	The opposing Pokemon who will recieve these effects
	 *  
	 */
	public abstract void useMove(Pokemon foe);

	/*
	 * @returns the current PP of the move being used
	 */
	@Override
	public int getPP() {
		// TODO Auto-generated method stub
		return pp;
	}

	/*
	 * @param PP_Values		Sets the maximum PP of a move to this value
	 */
	@Override
	public void setMaxPP(int PP_Value) {
		max_pp = PP_Value;
	}
	
	/*
	 * @return max_pp/The maximum PP of this move
	 */
	public int getMaxPP()
	{
		return max_pp;
	}
	
	/*
	 * Restores the current pp of a move to the max PP
	 */
	public void restoreMovePP()
	{
		pp = max_pp;
	}
	
	/*
	 *  @return the name of the Move
	 */
	@Override
	public String getMoveName() {
		// TODO Auto-generated method stub
		return moveName;
	}
	
	/*
	 * @return the type for a move
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return moveType;
	}

	/*
	 * Displays the Stat values of a Move
	 */
	@Override
	public void getMoveStats() {
		System.out.println("Move Name: " + moveName);
		System.out.println("Type: " + moveType);
		System.out.println("Damage:" + damage);
		System.out.println("Accuracy :" + accuracy);
		System.out.println("PP :" + pp + "/" + max_pp);

	}
	
	/*
	 * @return accuracy		The accuracy stat of the move
	 */
	public int getAccuracy()
	{
		return accuracy;
	}
	
	/*
	 * Changes the value levels a Pokemon. The shells effect attacks, defense, speed, evasiveness, & accuracy
	 * 
	 * @param foe 	The Pokemon that'll have their shell values altered.
	 */
	protected void changeShellValues(Pokemon foe)
	{
		//Get the current shell levels for each shell
		int att = foe.getAttackShell();
		int def = foe.getDefenseShell();
		int eva = foe.getEvasivenessShell();
		int acc = foe.getAccuracyShell();
		
		//apply shell changes  if they aren't set to 0
		if(attackShellRaise != 0)
		{
			foe.setAttackShell(att + attackShellRaise);
			alterShellMessages(foe.getName(), "attack", attackShellRaise, att);
		}
		if(defenseShellRaise != 0)
		{
			foe.setDefenseShell(def + defenseShellRaise);
			alterShellMessages(foe.getName(), "defense", defenseShellRaise, def);
		}
		if(evasiveShellRaise != 0)	
		{
			foe.setEvasivenessShell(eva + evasiveShellRaise);
			alterShellMessages(foe.getName(), "evasiveness", evasiveShellRaise, eva);
		}
		if(accuracyShellRaise != 0)
		{
			foe.setAccuracyShell(acc + accuracyShellRaise);
			alterShellMessages(foe.getName(), "accuracy", accuracyShellRaise, acc);
		}
	}
	
	/*
	 * Displays a message indicating how much the severity of the stat change.
	 * 
	 * @param name	The name of the Pokemon
	 * @param statName	The name of the stat being changed
	 * @param shellRaise	The amount a shell level is being changed
	 * @param currLevel		The current shell level of a stat
	 */
	private void alterShellMessages(String name, String statName, int shellRaise, int currLevel)
	{
		
		switch(shellRaise + currLevel)
		{
		case 6:
			System.out.println(name + "'s " + statName + " won't go any higher");
			break;
		case -6:
			System.out.println(name + "'s " + statName + " won't go any lower");
			break;
		default:
			{
				switch(shellRaise)
				{
				case 1:
					System.out.println(name + "'s " + statName + " rose!");
					break;
				case 2:
					System.out.println(name + "'s " + statName + " rose sharply!");
					break;
				case 3:
					System.out.println(name + "'s " + statName + " rose immensely!");
					break;
				case -1:
					System.out.println(name + "'s " + statName + " fell!");
					break;
				case -2:
					System.out.println(name + "'s " + statName + " fell sharply!");
					break;
				case -3:
					System.out.println(name + "'s " + statName + " immensely dropped!");
					break;
				default:
					System.out.println("Illegal shell change for " + statName);		//Debug messages
				}
			}
		}
		
	}
}
