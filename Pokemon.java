import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*	Elias Saied
4/12/2022

Contains all the basic information about a Pokemon and it's moves.
*/

public class Pokemon implements PokemonEntry {
	
	private String name;
	private String[] types = {null, null};
	private List<String> typeWeakness = new ArrayList<String>();			// Will automatically be set depending on the Pokemon's type1 & type2
	private List<String> superEffective = new ArrayList<String>();
	private List<String> noEffect = new ArrayList<String>();
	private Move[] moveList = new Move[4];
	private int pokedexNum, maxHP, HP, level, attackStat, defenseStat, speedStat, 
		statusType, statusEffectTurns, maxStatusEffectTurns;
	private int attackShell, defShell, speedShell, accuracyShell, evasiveShell;
	private float captureRate;
	private boolean isCaught, isEffected, isSleep, isBurned, isFrozen, isPoisoned, isConfused, isParalyzed;
	private Random generator = new Random();
	
	public Pokemon(String pokeName, String type, int catchRate)
	{
		name = pokeName;
		level = 50;
		types[0] = type;
		
		// Calls a private function to set the weaknessTypes for each Pokemon
		setWeakness(types[0]);
		
		maxHP = 120;
		HP = maxHP;
		attackStat = 10;
		defenseStat = 10;
		speedStat = 10;
		captureRate = catchRate;
		isCaught = false;
		
		accuracyShell = 0;
		evasiveShell = 0;
		attackShell = 0;
		defShell = 0;
	}
	
	public Pokemon(String pokeName, String type1, String type2, int catchRate)
	{
		name = pokeName;
		level = 50;
		types[0] = type1;
		types[1] = type2;
		isEffected = false;
		
		// Calls a private function to set the weaknessTypes for each Pokemon
		setWeakness(types[0]);
		setWeakness(types[1]);
		
		maxHP = 120;
		HP = maxHP;
		attackStat = 10;
		defenseStat = 10;
		speedStat = 10;
		captureRate = catchRate;
		isCaught = false;
		
		accuracyShell = 0;
		evasiveShell = 0;
		attackShell = 0;
		defShell = 0;
	}
	
	/*
	 * @param name	A String value assigned to the Pokemon
	 * Set's the name of the Pokemon
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	/*
	 * @return name		The name of the Pokemon
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/*
	 * Get the 1-2 types assigned to a Pokemon
	 * @return types	An array of Strings that specific the Pokemon's type(s)
	 */
	@Override
	public String[] getTypes() {
		// TODO Auto-generated method stub
		return types;
	}

	/*
	 * Returns the type weakness of a Pokemon
	 * @return	typeWeakness	A List of Strings that indicate which types are weak for this Pokemon
	 */
	@Override
	public List<String> getTypeWeakness() {
		// TODO Auto-generated method stub
		return typeWeakness;
	}

	/*
	 * Set the Pokemon's HP. HP can't exceed the maxHP
	 * @param healthPoints		The amount of health that will set for this Pokemon 
	 */
	@Override
	public void setHP(int healthPoints) {
		if(maxHP > healthPoints)		//Checks if the amount to heal won't go over maxHP
			HP = healthPoints;
		else							//Stops HP from going over maxHP
			HP = maxHP;
	}

	/*
	 * @return The HP of this Pokemon
	 */
	@Override
	public int getHP() {
		// TODO Auto-generated method stub
		return HP;
	}
	
	/*
	 * Set the max HP for this Pokemon
	 * @param maximum_HP	The maximum HP for a Pokemon  
	 */
	public void setMaxHP(int maximum_HP)
	{
		maxHP = maximum_HP;
	}
	
	/*
	 * @return	The max HP of the Pokemon
	 */
	public int getMaxHP()
	{
		return maxHP;
	}

	/*
	 * @return	The current level of this Pokemon
	 */
	public int getLevel()
	{
		return level;
	}
	
	/*
	 * Changes the base attack stat for this Pokemon
	 * @param attackStat	The new base attack to be set
	 */
	@Override
	public void setAttack(int attackStat) {
		this.attackStat = attackStat;

	}

	/*
	 * @return 	The current attack stat of this Pokemon
	 */
	@Override
	public int getAttack() {
		// TODO Auto-generated method stub
		return attackStat;
	}

	/*
	 * Changes the base defense stat for a Pokemon
	 * @defStat		The new defense stat to be set
	 */
	@Override
	public void setDefense(int defStat) {
		defenseStat = defStat;

	}

	/*
	 * @return 		The current defense stat
	 */
	@Override
	public int getDefense() {
		// TODO Auto-generated method stub
		return defenseStat;
	}

	/*
	 * Changes the current speed stat
	 * @param speedStat		The new speed stat to be set
	 */
	@Override
	public void setSpeed(int speedStat) {
		this.speedStat = speedStat;

	}

	/*
	 * @return 	The current speed stat
	 */
	@Override
	public int getSpeed() {
		// TODO Auto-generated method stub
		return speedStat;
	}
	
	/* Change the level of the attackShell
	 * @param level		The amount of levels to increase the attackShell
	 */
	public void setAttackShell(int level)
	{
		attackShell = checkShells(level);
	}
	
	/*
	 * @return the current shell level for Attack
	 */
	public int getAttackShell()
	{
		return attackShell;
	}
	
	/*
	 * Get's the decimal percentage of the current attack shell level
	 * @return 	A percentage that'll be multiplied to the attack stat
	 */
	public double getAttackShellEffects()
	{
		return shellMultipliers_for_Attack_and_Defense(attackShell);
	}
	
	/* Change the level of the defenseShell
	 * @param level		The amount of levels to increase the defense Shell
	 */
	public void setDefenseShell(int level)
	{
		defShell = checkShells(level);
	}
	
	/*
	 * @return the current shell level for Defense
	 */
	public int getDefenseShell()
	{
		return defShell;
	}
	
	/*
	 * Get's the decimal percentage of the current defense shell level
	 * @return 	A percentage that'll be multiplied to the defense stat
	 */
	public double getDefenseShellEffects()
	{
		return shellMultipliers_for_Attack_and_Defense(defShell);
	}
	
	/* Change the level of the speed Shell
	 * @param level		The amount of levels to increase the speed Shell
	 */
	public void setSpeedShell(int level)
	{
		speedShell = checkShells(level);
	}
	
	/*
	 * @return the current shell level for Speed
	 */
	public int getSpeedShell()
	{
		return speedShell;
	}
	
	/*
	 * Get's the decimal percentage of the current speed shell level
	 * @return 	A percentage that'll be multiplied to the speed stat
	 */
	public double getSpeedEffects()
	{
		return shellMultipliers_for_Attack_and_Defense(speedShell);
	}
	
	/* Change the level of the evasiveness Shell
	 * @param level		The amount of levels to increase the evasiveness shell
	 */
	public void setEvasivenessShell(int level)
	{
		evasiveShell = checkShells(level);
	}
	
	/*
	 * @return the current shell level for Evasiveness
	 */
	public int getEvasivenessShell()
	{
		return evasiveShell;
	}
	
	/* Change the level of the evasiveness Shell
	 * @param level		The amount of levels to increase the evasiveness shell
	 */
	public void setAccuracyShell(int level)
	{
		accuracyShell = checkShells(level);
	}
	
	/*
	 * @return the current shell level for accuracy
	 */
	public int getAccuracyShell()
	{
		return accuracyShell;
	}

	/*
	 * @return	The Pokemon's captureRate
	 */
	public float getCaptureRate()
	{
		return captureRate;
	}
	
	/*
	 * Changes the capture rate of the Pokemon
	 * @param captureRate	The new captureRate to set
	 */
	public void setCaptureRate(float captureRate)
	{
		this.captureRate = captureRate;
	}
	
	/* Assigns a Pokedex number to this Pokemon
	 * @param PKMN_Num	A unique number key that'll be used to identify the pokemon
	 */
	@Override
	public void setPokedexNum(int PKMN_Num) {
		pokedexNum = PKMN_Num;
	}

	/*
	 * @return	The pokedex number
	 */
	@Override
	public int getPokedexNum() {
		// TODO Auto-generated method stub
		return pokedexNum;
	}
	
	/*
	 * @return	An int that's indicative of the Pokemon's status condition (i.e. paralyzed, frozen, etc.)
	 */
	public int getStatusCondition()
	{
		return statusType;
	}
	
	/*
	 * @return true/false if the Pokemon has a status condition set (i.e. Paralyzed, Frozen, Sleep, Poison, Burn)
	 */
	public boolean isStatusEffect()
	{
		return isEffected;
	}

	/*
	 * Set's this Pokemon as a CAPTURED pokemon
	 */
	@Override
	public void caught()
	{
		isCaught = true;
	}
	
	/*
	 *@param 	true/false If the Pokemon has been captured and is owned by a trainer 
	 */
	@Override
	public boolean isCaught() {
		return isCaught;
	}
	


	/*
	 * Set the weakness and advantages of this Pokemon based on what type it is
	 * @param Type	A string value indicating the type of this Pokemon
	 */
	public void setWeakness(String type)
	{
		switch(type)
		{
		case "Normal": 	//Normal
			typeWeakness.add("Fight");
			noEffect.add("Ghost");
			break;
		case "Fire":		//Fire
			superEffective.add("Grass");	
			superEffective.add("Ice");
			superEffective.add("Bug");
			superEffective.add("Steel");
			typeWeakness.add("Water");
			typeWeakness.add("Ground");
			typeWeakness.add("Rock");
			break;
		case "Water":		//Water
			typeWeakness.add("Electric");
			typeWeakness.add("Grass");
			superEffective.add("Fire");
			superEffective.add("Ground");
			superEffective.add("Rock");
			break;
		case "Grass":		//Grass
			typeWeakness.add("Fire");
			typeWeakness.add("Ice");
			typeWeakness.add("Poison");
			typeWeakness.add("Flying");
			typeWeakness.add("Bug");
			superEffective.add("Water");
			superEffective.add("Ground");
			superEffective.add("Rock");
			break;
		case "Electric":		//Electric
			typeWeakness.add("Ground");
			superEffective.add("Water");
			superEffective.add("Flying");
			break;
		case "Ice":		//Ice
			typeWeakness.add("Fire");
			typeWeakness.add("Fight");
			typeWeakness.add("Rock");
			typeWeakness.add("Steel");
			superEffective.add("Grass");
			superEffective.add("Ground");
			superEffective.add("Flying");
			superEffective.add("Dragon");
			break;
		case "Fight":		//Fighting
			typeWeakness.add("Flying");
			typeWeakness.add("Psychic");
			typeWeakness.add("Fairy");
			superEffective.add("Normal");
			superEffective.add("Ice");
			superEffective.add("Rock");
			superEffective.add("Dark");
			superEffective.add("Steel");
			break;
		case "Poison":		//Poison
			typeWeakness.add("Ground");
			typeWeakness.add("Psychic");
			superEffective.add("Grass");
			superEffective.add("Fairy");
			break;
		case "Ground":		//Ground
			typeWeakness.add("Water");
			typeWeakness.add("Grass");
			typeWeakness.add("Ice");
			superEffective.add("Fire");
			superEffective.add("Electric");
			superEffective.add("Poison");
			superEffective.add("Rock");
			superEffective.add("Steel");
			noEffect.add("Electric");
			break;
		case "Flying":	//Flying
			typeWeakness.add("Electric");
			typeWeakness.add("Rock");
			typeWeakness.add("Ice");
			superEffective.add("Grass");
			superEffective.add("Fight");
			superEffective.add("Bug");
			noEffect.add("Ground");
			break;
		case "Psychic":	//Psychic
			typeWeakness.add("Bug");
			typeWeakness.add("Ghost");
			typeWeakness.add("Dark");
			superEffective.add("Fight");
			superEffective.add("Poison");
			break;
		case "Bug":	//Bug
			typeWeakness.add("Fire");
			typeWeakness.add("Flying");
			typeWeakness.add("Rock");
			superEffective.add("Grass");
			superEffective.add("Psychic");
			superEffective.add("Dark");
			break;
		case "Rock":	//Rock
			typeWeakness.add("Water");
			typeWeakness.add("Grass");
			typeWeakness.add("Fight");
			typeWeakness.add("Ground");
			typeWeakness.add("Steel");
			superEffective.add("Fire");
			superEffective.add("Ice");
			superEffective.add("Flying");
			superEffective.add("Bug");
			break;
		case "Ghost":	//Ghost
			typeWeakness.add("Ghost");
			typeWeakness.add("Dark");
			noEffect.add("Normal");
			noEffect.add("Fighting");
			break;
		case "Dark":	//Dark
			typeWeakness.add("Fight");
			typeWeakness.add("Bug");
			typeWeakness.add("Fairy");
			superEffective.add("Psychic");
			superEffective.add("Ghost");
			noEffect.add("Psychic");
			break;
		case "Dragon":	//Dragon
			typeWeakness.add("Dragon");
			typeWeakness.add("Ice");
			typeWeakness.add("Fairy");
			superEffective.add("Dragon");
			break;
		case "Steel":	//Steel
			typeWeakness.add("Fire");
			typeWeakness.add("Fight");
			typeWeakness.add("Ground");
			superEffective.add("Ice");
			superEffective.add("Rock");
			superEffective.add("Fairy");
			noEffect.add("Poison");
			break;
		case "Fairy":	//Fairy
			typeWeakness.add("Poison");
			typeWeakness.add("Steel");
			superEffective.add("Fight");
			superEffective.add("Dragon");
			superEffective.add("Dark");
			noEffect.add("Dragon");
			break;
		default:
			System.out.println("Not a legal type");	
		}
	}
	
	/*
	 * Checks if the requested move number is legal (0-3). If not, it defaults to suing the first move.
	 * @param moveNum	A number correlating to a move from the Pokemon's MoveList
	 * @return	A legal move number that can be used
	 */
	public int checkMove(int moveNum)
	{
		try
		{
			moveList[moveNum].getMoveName();
		}
		catch(NullPointerException e)
		{
			return 0;
		}
		
		return moveNum;
	}
	
	/*
	 * Use one of the moves from the Pokemon's MoveList
	 * @param moveNum	The move number that'll reference one of the Pokemon's moves (Must be 0, 1, 2, or 3)
	 * @param target	The pokemon that will be effected by this move
	 */
	@Override
	public void useMove(int moveNum, Pokemon target) {
		if(HP > 0) 	//If Pokemon didn't faint
		{
			if(isEffected == true && (statusEffectTurns < maxStatusEffectTurns)) 
			{
				moveNum = checkMove(moveNum);
				statusEffectTurns++;	//Increment status effect turn
				switch (statusType)
				{
				case 1:		//Confused
					//Chance to Hit self
					System.out.println(name + " is confused.");
					if(generator.nextInt(101) > 77)		//33% to Hurt Self
						{attackSelf(40);
						break;}
					else {
						attack(moveNum, target);
						break;
					}
				case 2:		//Paralyzed
					//Chance to Not Move
					if(generator.nextInt(101) < 77)	
						attack(moveNum, target);
					else
						System.out.println(name + " is paralyzed. It can't move!");
					break;
				case 5:		//Sleep
					//Don't move for 2-5 turns
					System.out.println(name + " is fast asleep...");
					break;
				case 6:		//Frozen
					//Don't move for 2-5 turns
					System.out.println(name + " is frozen solid!");
					break;
				default:
				}
			}
			else
				{
				//Reset status attributes & Attaack
				resetStatus();
				attack(moveNum, target);
				}
		}
		else
			System.out.println(name + " is unable to battle.");
	}
		
	/*
	 * Uses one of the Pokemon's moves to attack & Does an accuracy test to determine if the move wil hit.
	 * @param moveNum	A number between 0 and 3 that will be used to retrieve a move from the Pokemon's Move List
	 * @param target	The Pokemon that will recieve the effects of the selected Move
	 */
	private void attack(int moveNum, Pokemon target)
	{
		if(0 <= moveNum && moveNum < 4)
		{
			moveNum = checkMove(moveNum);
			System.out.println(name + " used " + moveList[moveNum].getMoveName());
			if(accuracyTest(moveList[moveNum].getAccuracy(), target.getEvasivenessShell()))	//Do an accuracy test
				moveList[moveNum].useMove(target);
			else
				System.out.println(name + "'s attack missed.");
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Does an accuracy test to determine if a move will hit
	 * @param moveAccuracy	The accuracy of the move that will be used to attack
	 * @param target_evasiveShell 	The evaiseShell level of the target
	 * @return A boolean indicating the success of this test
	 * Precondition: accuracyShell and target_evasiveShell are between -6 and 6
	 */
	private boolean accuracyTest(int moveAccuracy, int target_evasiveShell)
	{
		double adjustedStages = shellMultipliers_for_Accuracy(accuracyShell) * shellMultipliers_for_Evasive(target_evasiveShell);;
		int randomNum = generator.nextInt(99) + 1;		//Select a random num between 1 + 100
		
		return randomNum <= moveAccuracy * adjustedStages; 
	}
	
	/*
	 * Check a Shell level for a stat. It makes sure they don't surpass -6 or 6.
	 * @param a		The shell level to be checked
	 * @return		A legal shell level
	 */
	private int checkShells(int a)
	{
		if(a >= 6)
			return 6;
		else if(a <= -6)
			return -6;
		else 
			return a;
	}
	
	/*
	 * Used to determine the the correct shell level for the attack and defense
	 * @param a		A Shell level between -6 and 6
	 * @return 		A decimal value that'll alter the effectiveness of a move
	 * @precondition	a will be between -6 and 6
	 */
	private double shellMultipliers_for_Attack_and_Defense(int a)
	{		
		switch (a)
		{
		case -6:
			return (double)2/8;
		case -5:
			return (double)2/7;
		case -4:
			return (double)2/6;
		case -3:
			return (double)2/5;
		case -2:
			return (double)2/4;
		case -1:
			return (double)2/3;
		case 6:
			return (double)4;
		case 1:
			return (double)3/2;
		case 2:
			return (double)2;
		case 3:
			return (double)5/2;
		case 4:
			return (double)6;
		case 5:
			return (double)7/2;
		default:
			return 1;
		}
	}
	
	/*
	 * Used to determine the the correct shell level for the accuracy test
	 * @param a	The accuracyShell
	 * @return a decimal value that'll alter the likelihood of an attack landing
	 */
	private double shellMultipliers_for_Accuracy(int a)
	{		
		switch (a)
		{
		case -6:
			return (double)3/9;
		case -5:
			return (double)3/8;
		case -4:
			return (double)3/7;
		case -3:
			return (double)3/6;
		case -2:
			return (double)3/5;
		case -1:
			return (double)3/4;
		case 6:
			return (double)3;
		case 1:
			return (double)4/3;
		case 2:
			return (double)5/3;
		case 3:
			return (double)2;
		case 4:
			return (double)7/3;
		case 5:
			return (double)8/3;
		default:
			return 1;
		}
	}
	
	/*
	 * Used to determine the the correct shell level for the evasiveness test
	 * @param a	The target's evasiveShell
	 * @return a decimal value that'll alter the likelihood of an attack missing
	 */
	private double shellMultipliers_for_Evasive(int a)
	{		
		switch (a)
		{
		case 6:
			return 3/9;
		case 5:
			return 3/8;
		case 4:
			return 3/7;
		case 3:
			return 3/6;
		case 2:
			return 3/5;
		case 1:
			return 3/4;
		case -6:
			return 3;
		case -1:
			return 4/3;
		case -2:
			return 5/3;
		case -3:
			return 2;
		case -4:
			return 7/3;
		case -5:
			return 8/3;
		default:
			return 1;
		}
	}
	
	
	/*
	 * Used to make the a Pokemon lower it's own health
	 * @param damage	The amount of damage a Pokemon will do to itself
	 */
	private void attackSelf(int damage)
	{
		int damageToTake;
		double random = (generator.nextInt(16) + 85)/100.0;		// Random percent value between 85 and 100
		damageToTake = (int)((((((2 * level)/5) + 2) * 40 * (float)attackStat/(float)defenseStat)/50 + 2) * 2 * random);
		
		HP = HP - damageToTake;
		System.out.println(name + " hit itself in the confusion");
	}


	/*
	 *  Set's a status condition for the Pokemon based on the integer passed in
	 *  	1 = Confuse
	 *  	2 = Paralyze
	 *  	3 = Poison
	 *  	4 = Burn
	 *  	5 = Sleep
	 *  	6 = Freeze
	 *  @param statusType	An int that will determine the status set
	 */
	public void setStatusCondition(int statusType) {
		// TODO Auto-generated method stub
		this.statusType = statusType;
		if(!isEffected)		//if the pokemon is not already effected
		{
			maxStatusEffectTurns = generator.nextInt(4) + 2;	// Set's status for 2-5 turns
			switch (statusType)
			{
			case 1:
				isConfused = true;
				isEffected = true;
				statusEffectTurns = 0;
				break;
			case 2:
				isParalyzed = true;
				isEffected = true;
				statusEffectTurns = 0;
				break;
			case 3:
				isPoisoned = true;
				isEffected = true;
				statusEffectTurns = 0;
				break;
			case 4:
				isBurned = true;
				isEffected = true;
				statusEffectTurns = 0;
				break;
			case 5:
				isSleep = true;
				isEffected = true;
				statusEffectTurns = 0;
				break;
			case 6:
				isFrozen = true;
				isEffected = true;
				statusEffectTurns = 0;
				break;
			default:
				System.out.println("Illegal Status Effect");
			}
		}
		else
			System.out.println(name + " has no Status Effects");
	}

	/*
	 * Adds a new move into the Move list
	 * @param newMove	A move that will be added
	 */
	@Override
	public void addMove(Move newMove) {
		if(moveList[0] == null)
			moveList[0] = newMove;
		else if(moveList[1] == null)
			moveList[1] = newMove;
		else if(moveList[2] == null)
			moveList[2] = newMove;
		else if(moveList[3] == null)
			moveList[3] = newMove;
		else
			System.out.println(name + "tried to learn " + newMove.getMoveName() + " but " + name 
					+ "can only know 4 moves at a time.");
			
		}
		
	/*
	 * Displays all current moves of this Pokemon
	 */
	@Override
	public void displayMoveList() {
		for(int i = 0; i < 4; i++) 
		{
			try {
			System.out.println(i + 1 + ") " + moveList[i].getMoveName() 
					+ "\t\t" + moveList[i].getPP() + "/" + moveList[i].getMaxPP() + " PP");
			}
			catch(NullPointerException e)	//If there are less than 4 moves, then the missing slots won't be displayed
			{
				System.out.print("");
			}
		}
	}
	
	/*
	 * Restores each move's PP to max. 
	 */
	public void restorePP()
	{
			for(int i = 0; i < 4; i++) 
			{
				try {
					moveList[i].restoreMovePP();
				}
				catch(NullPointerException e)	//If there are less than 4 moves, then the missing slots won't be displayed
				{
					//do nothing if caught
				}
			}
	}

	/*
	 * Removes a move from the Pokemon's move list
	 * @param moveToRemove	A move to be removed
	 */
	/*public Move deleteMove(Move moveToRemove) {
		// TODO Auto-generated method stub
		return null;
	}*/

	/*
	 * Reset all the status conditions of this Pokemon
	 */
	private void resetStatus()
	{
		isEffected = false;
		isSleep = false;
		isParalyzed = false;
		isBurned =  false;
		isFrozen = false;
		isPoisoned = false;
		isConfused = false;
		statusEffectTurns = 0;
	}
	
}
