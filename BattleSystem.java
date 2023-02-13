import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
/*	Elias Saied
	5/9/2022

	A short game where players can catch and use creatures called Pokemon to battle.
*/


public class BattleSystem {
	static Random generator = new Random();
	public static void main(String[] args) {
		//Start Up
		Scanner user = new Scanner(System.in);
		System.out.println("Hi there! Welcome to the world of Pokemon! What's your name?: ");
		String name = user.nextLine();
		Player myTrainer = new Player(name);
		
		//Select a starter and assign it moves
		Pokemon starter = new Pokemon("Squirtle", "Water", 220);
		Move Tackle = new AttackMove("Tackle", "Normal", 40, 35, 95, 0, 0);
		Move WaterPulse = new AttackMove("Water Pulse", "Water", 60, 20, 100, 20, 1);
		Move Withdraw = new StatusMove("Tail Whip", "Normal", 0, 40, 100, 0, 0, 
				0, -1, 0, 0, 0);
		starter.addMove(Tackle);
		starter.addMove(WaterPulse);
		starter.addMove(Withdraw);
		myTrainer.getTeam().add(starter);
		//System.out.println("A " + starter.getName() + " was added to your Team");
		//Add items into the player's bag
		myTrainer.getBackpack().addItem(new HealItem(20, "Potion", null, 3));
		myTrainer.getBackpack().addItem(new PokeBall((float)1.25, "Pokeball", null, 5));
		System.out.println("5 Pokeballs were added & 3 Potions added.");
		
		//Menu
		menu(myTrainer);

	}

	/*Display the amount of an item a Trainer has
	 * @param aTrainer	The Trainer whose bag is going to be accessed
	 * @return	item	The item quantity that'll be displayed
	 */
	static void displayItemQuantity(Trainer aTrainer, ItemEntry item)
	{
		System.out.println(aTrainer.getName()
				+ " has " + aTrainer.getBackpack().getQuantityOf(item) + " " + item.getItemName() + "s");
	}
	
	/* 
	 * Prompts the user to make a selection in order to do something.
	 * @param	myTrainer	The player that will be assigned to a selection
	 */
	static void menu(Player myTrainer)
	{
		//Menu
		Scanner user = new Scanner(System.in);
		int selection;
		System.out.println("Type in the Number of the Action you'd Like to Do");
		System.out.println("1) To the Wild Area");
		System.out.println("2) Trainer Battle");
		//System.out.println("3) Backpack");
		System.out.println("4) Team");
		System.out.println("5) PC");
		//System.out.println("6) Shop");
		System.out.println("7) Pokemon Center");
		System.out.println("8) Exit");
		
		//Store input
		try{
			selection = user.nextInt();
		}
		catch(InputMismatchException e){
			selection = 0;
		}
				
		//User's selection will cause an event to happen
		switch(selection)
		{
			case 1:		//Wild Battle
				wildArea(myTrainer);
				break;
			case 2:		//Trainer Battle
				trainerBattle(myTrainer, randomOpponent());
				break;
			case 4:		//Reorder your Team
				myTrainer.getTeam().rearrangeTeam();
				menu(myTrainer);
				break;
			case 5:		//Access your PC
				accessPC(myTrainer);
				menu(myTrainer);
				break;
			case 7:		//Pokemon Center: Heal your Pokemon for free
				System.out.println("Welcome to the Pokemon Center. Please wait a moment.");
				try {
					TimeUnit.SECONDS.sleep(1);
					System.out.print(".");
					TimeUnit.SECONDS.sleep(1);
					System.out.print(".");
					TimeUnit.SECONDS.sleep(1);
					System.out.print(".");
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				restoreHealth(myTrainer); //Heal Pokemon
				System.out.println("");		//Formatting
				System.out.println("Your Pokemon have fully been restored! Please visit us again.");
				menu(myTrainer);
				break;
			case 8:		//Exit Method & End Program
				System.out.println("Thank you for playing. Have a great day!");
				break;
			default:	//Debug message
				System.out.println("Invalid Selection");
				menu(myTrainer);
		}
	}
	
	/*
	 * Encounter a wild pokemon that can be battled and captured!
	 * @param myTrainer		The player that will battle this wild Pokemon
	 */
	static void wildArea(Player myTrainer)
	{
		//Set-up
		Team myTeam = myTrainer.getTeam();
		Backpack myBag = myTrainer.getBackpack();
		Scanner scan = new Scanner(System.in);
		boolean runAway = false;
		restoreShells(myTrainer);		//Restore shells to default level at the start of a battle
		System.out.println("");			//Formatting
		System.out.println("-----" + myTrainer.getName() + " walked into the tall grass.---");		//Formatting
		waitMiliSeconds(2000);
		
		//Wild Encounter
		Pokemon foe = spawnRandomWildPokemon();
		Pokemon lead = myTeam.sendPKMN();
		System.out.println("A wild " + foe.getName() + " has appeared!");
		waitMiliSeconds(1000);
		System.out.println("Go " + lead.getName() + "!");
		waitMiliSeconds(500);
		
		//Core of the Battle System
		while(!myTrainer.isTeamFainted() && !runAway && !foe.isCaught() && foe.getHP() > 0)		//While your team hasn't fainted or haven't ran away
		{
			//System.out.println("inner loop");
			while(lead.getHP() > 0 && foe.getHP() > 0 && !foe.isCaught() && runAway != true)	//Loop while neither PKMN has fainted nor was the wild PKMN caught
			{
				//Pokemon Battle
				displayBasicInfo(lead);
				displayBasicInfo(foe);
				System.out.println("What do you want to do?");
				System.out.println("1) Battle		2) Bag	 		3) Team			4) Run Away");
				String choice = scan.nextLine();
	
				//Use the user's choice to decide what to do
				switch(choice)
				{
					case "1":
						battle(lead, foe);
						break;
					case "2":
						if(checkBag(myTrainer, foe) && !foe.isCaught())	{	//Bag 	|| If an item is used and the foe isn't captured after the item is used, then foe attacks
							foe.useMove(wildPokemonAttack(foe), lead);
							myTeam.display();
						}
						break;
					case "3":
						if(myTeam.swapLeadPokemon())	//Team || If a swap was successful, foe attacks
						{
							System.out.println(lead.getName() + " has been sent back.");
							lead = myTeam.getPokemon(0);
							System.out.println("Go " + lead.getName() + "!");
							foe.useMove(wildPokemonAttack(foe), lead);
						}
						break;
					case "4":
						System.out.println("Succesfully managed to run");
						runAway = true;
						break;
					default:
						System.out.println("Invalid Selection");
				}
			}//end of while loop  --- A PKMN has fainted
			if(foe.getHP() <= 0)	//If the wild PKMN has fainted
			{
				System.out.println(foe.getName() + " has fainted.");
				waitMiliSeconds(500);
			}
			else if(!runAway && !foe.isCaught() && !myTrainer.isTeamFainted() && lead.getHP() <= 0)		//If player didn't run away, their team hasn't fainted and the Wild Pokemon hasn't been caught
			{
				System.out.println(lead.getName() + " has fainted.");
				waitMiliSeconds(500);
				myTeam.swapLeadPokemon();	//Select a PKMN to send out & Set lead PKMN: Will re-enter the loop
				lead = myTeam.getPokemon(0);
				System.out.println("Go " + lead.getName());
				waitMiliSeconds(500);
			}
			else if(myTrainer.isTeamFainted())
			{
					System.out.println(myTrainer.getName() + " ran out of usable Pokemon! You white-out...");
					waitMiliSeconds(1000);
					restoreHealth(myTrainer);
					restoreShells(myTrainer);
			}
		
		}//end of while loop --- Either the team has fainted, the opponent has fainted, opponent was caught, or player ran
			menu(myTrainer);
	}
	
	
	
	/*
	 * Trainer Battle System betwwen a Player and a CPU Trainer
	 * @param myTrainer		The player that will participate in this battle
	 * @param opponent		The CPU_Trainer that will participate in this battle
	 */	
	static void trainerBattle(Player myTrainer, CPUTrainer opponent)
	{
		//Set-up
		Team myTeam = myTrainer.getTeam();
		Backpack myBag = myTrainer.getBackpack();
		Scanner scan = new Scanner(System.in);
		restoreShells(myTrainer);		//Restore shells to default level at the start of a battle
		System.out.println("");			//Formatting
		System.out.println("-----" + opponent.getFullName() + " challenged you to a Pokemon battle!---");		//Formatting
		waitMiliSeconds(2000);
		
		//Trainer Battle
		restoreHealth(opponent);
		Team oppTeam = opponent.getTeam();
		Pokemon foe = oppTeam.sendPKMN();
		Pokemon lead = myTeam.sendPKMN();
		System.out.println(opponent.getFullName() + " sent out a " + foe.getName() + "!");
		waitMiliSeconds(1000);
		System.out.println("Go " + lead.getName() + "!");
		waitMiliSeconds(500);
		
		//Core of the Battle System
		while(!myTrainer.isTeamFainted() && !opponent.isTeamFainted())	//While neither team has fainted
		{
			//System.out.println("inner loop");
			while(lead.getHP() > 0 && foe.getHP() > 0)	//Loop while neither PKMN has fainted
			{
				//Pokemon Battle
				displayBasicInfo(lead);
				displayBasicInfo(foe);
				System.out.println("What do you want to do?");
				System.out.println("1) Battle		2) Bag	 		3) Team			4) Run Away");
				String choice = scan.nextLine();	//Store the input
	
				//Use the user's choice to decide what to do
				switch(choice)
				{
				case "1":
					battle(lead, foe);
					break;
				case "2":
					if(checkBag(myTrainer, foe) && !foe.isCaught())		//Bag 	|| If an item is used and the foe isn't captured after the item is used, then foe attacks
						foe.useMove(wildPokemonAttack(foe), lead);
					break;
				case "3":
					if(myTeam.swapLeadPokemon())	//Team || If a swap was successful, foe attacks
					{
						System.out.println(lead.getName() + " has been sent back.");
						lead = myTeam.getPokemon(0);
						System.out.println("Go " + lead.getName() + "!");
						foe.useMove(wildPokemonAttack(foe), lead);
					}
					break;
				case "4":
					System.out.println("You can't run away from a battle");
					break;
				default:
					System.out.println("Invalid Selection");
				}
			}//end of while loop  --- A PKMN has fainted
			
			//If the lead PKMN fainted and the player has useable PKMN
			if(lead.getHP() <= 0 && !myTrainer.isTeamFainted())		
			{
				System.out.println(lead.getName() + " has fainted.");
				waitMiliSeconds(500);
				myTeam.swapLeadPokemon();	//Select a PKMN to send out & Set lead PKMN: Will re-enter the loop
				lead = myTeam.getPokemon(0);
				System.out.println("Go " + lead.getName());
			}
			else if (myTrainer.isTeamFainted())		//if player doesnt have useable Pokemon
				{
					System.out.println(lead.getName() + " has fainted.");
					waitMiliSeconds(500);
					System.out.println(myTrainer.getName() + " ran out of usable Pokemon! You white-out...");
					restoreHealth(myTrainer);
					restoreShells(myTrainer);
				}
			
			if(foe.getHP() <= 0 && !opponent.isTeamFainted())		//if the foe has fainted but the opponent's team hasn't fainted
			{
				System.out.println(foe.getName() + " has fainted.");
				waitMiliSeconds(500);
				oppTeam.CPU_nextPKM();
				foe = oppTeam.getPokemon(0);
				System.out.println(opponent.getFullName() + " sent out " + foe.getName() + ".");
			}
			else if(opponent.isTeamFainted())						//else if their team has fainted
			{
				System.out.println(foe.getName() + " has fainted.");
				System.out.println("You defeat " + opponent.getName() + "!");
				waitMiliSeconds(1000);
			}
		
		}//end of while loop --- One team has fainted
			menu(myTrainer);
	}
	
	/*
	 * Allows the player to pick a move for their Pokemon to do and allows the CPU to attack
	 * @param mine	The player's Pokemon that is currently battling
	 * @param foe	The opposing Pokemon that will automatically have a move decided
	 */
	static void battle(Pokemon mine, Pokemon foe)
	{
		mine.displayMoveList();		//display your moves
		System.out.println("Type in a move number: ");		
		int player_choice;
		Scanner user = new Scanner(System.in);
		try							//Try to get an integer
		{
			player_choice = user.nextInt();	//select your move or go back to the menu
		}
		catch(InputMismatchException e)		//If not default to 1
		{
			player_choice = 1;
		}
		
		int wild_choice = wildPokemonAttack(foe);	//CPU randomly picks a move
		
		//Determine which PKMN is faster
		int option;
		if(mine.getSpeed() > foe.getSpeed())		//If your faster, you go first
			option = 0;
		else if (mine.getSpeed() < foe.getSpeed())	//Otherwise the foe goes first
			option = 1;
		else
			option = generator.nextInt(1);		//if equal then pick a random value
		
		System.out.println("");		//Exist for format
		if(option == 0)
		{
			if(mine.getHP() > 0)
				mine.useMove(player_choice - 1, foe); 	//You go first
			if(foe.getHP() > 0)
				foe.useMove(wild_choice, mine); 	//Foe attacks next
		}
		else
		{
			if(foe.getHP() > 0)
				foe.useMove(wild_choice, mine);		//Foe goes first
			if(mine.getHP() > 0)
				mine.useMove(player_choice - 1, foe); 	//You go next
		}
		System.out.println("");		//Exist for format
	}
	
	//Displays the Name, Level, & Health for a PKMN
	//@param P The Pokemon whose info will be displayed
	static void displayBasicInfo(Pokemon p)
	{
		System.out.println("-------------------------------");
		System.out.println(p.getName());
		System.out.println("	Lvl: " + p.getLevel());
		System.out.println("	HP: " + p.getHP() + "/" + p.getMaxHP());
		System.out.println("-------------------------------");
	}
	
	/*
	 * Creates a random Pokemon
	 * @return A randomly generated Pokemon
	 */
	static Pokemon spawnRandomWildPokemon()
	{
		Pokemon p;								//Create a Pokemon object that will be initialized later
		int random = generator.nextInt(26);		//0-25
		
		//Move Pool
		//Move name = new Move("Name", "Type", damage, pp, accuracy, chanceOfStatus, statusType)
		/*Move name = new Move("Name", "Type", damage, pp, accuracy, chanceOfStatus, statusType, 
								attackShell, defenseShell, speedShell, evasiveShell, accuracyShell)*/
		Move Tackle = new AttackMove("Tackle", "Normal", 40, 35, 95, 0, 0);
		Move Confusion = new AttackMove("Confusion", "Psychic", 50, 25, 100, 10, 1);
		Move Hypnosis = new StatusMove("Hypnosis", "Psychic", 0, 10, 80, 100, 5, 0, 0, 0, 0, 0);
		Move CloseCombat = new AttackMove("Close Combat", "Fighting", 120, 10, 100, 0, 0);
		Move Infinity = new AttackMove("Infinite Void", "Dark", 90, 30, 90, 0, 0);
		Move Flamethrower = new AttackMove("Flamethrower", "Fire", 90, 24, 90, 0, 0);
		Move Ember = new AttackMove("Ember", "Fire", 40, 40, 100, 10, 4);
		
		Move Growl = new StatusMove("Growl", "Nomral", 0, 40, 100, 0, 0,
									-1, 0, 0, 0, 0);
		Move TailWhip = new StatusMove("Tail Whip", "Normal", 0, 40, 100, 0, 0, 
									0, -1, 0, 0, 0);
		Move ScaryFace = new StatusMove("Scary Face", "Ghost", 0, 10, 100, 0, 0, 
				0, 0, -2, 0, 0);
		
		//Create a random pokemon
		switch (random)
		{
			case 0, 1:
				p = new Pokemon("Eevee", "Normal", 215);
				p.addMove(Tackle);
				p.addMove(TailWhip);
				break;
			case 2, 3, 4, 5, 6:
				p = new Pokemon("Onix", "Rock", "Ground", 220);
				p.addMove(Tackle);
				p.addMove(Growl);
				break;
			case 7:
				p = new Pokemon("Charizard", "Fire", "Flying", 160);
				p.addMove(Flamethrower);
				break;
			case 8, 9, 10:
				p = new Pokemon("Bulbasaur", "Grass", "Poison", 220);
				p.addMove(Tackle);
				p.addMove(Growl);
				break;
			case 11, 22:
				p = new Pokemon("Charmander", "Fire", 220);
				p.addMove(Tackle);
				p.addMove(Ember);
				p.addMove(TailWhip);
				break;
			case 12:
				p = new Pokemon("Kadabra", "Psychic", 200);
				p.addMove(Confusion);
				p.addMove(Hypnosis);
				break;
			case 13, 14:
				p = new Pokemon("Gengar", "Ghost", 165);
				p.addMove(Infinity);
				p.addMove(ScaryFace);
				break;
			case 15, 16:
				p = new Pokemon("Riachu", "Electric", 180);
				p.addMove(Tackle);
				p.addMove(TailWhip);
				break;
			case 17, 18, 19:
				p = new Pokemon("Garchomp", "Dragon", "Ground", 140);
				p.addMove(Growl);	
				p.addMove(CloseCombat);
				p.addMove(ScaryFace);
				break;
			case 20:
				p = new Pokemon("Dragonite", "Dragon", "Flying", 150);
				p.addMove(Confusion);
				p.addMove(Flamethrower);
				break;
			case 21:
				p = new Pokemon("Mewtwo", "Psychic", 100);
				p.addMove(Infinity);
				break;
			case 23:
				p = new Pokemon("Saturo", "Dark", "Fighting", 120);		//joke
				p.addMove(Infinity);
				p.addMove(Hypnosis);
				p.addMove(CloseCombat);
				break;
			default: //24, 25
				p = new Pokemon("Magikarp", "Water", 250);
				p.addMove(new AttackMove("Splash", "Water", 0, 0, 0, 0, 0));
				
				
		}
		
		return p;
	}
	
	//Used for wild PKMN | Determines which move they will perform
	//@param wild		The Pokemon that will have a move decided for them automatically
	static int wildPokemonAttack(Pokemon wild)
	{
		return generator.nextInt(4);		//Return a random value between 0 and 3
	}
	
	/*
	 * For the Player to swap out their current lead PKMN
	 */
	
	//Used to see what's inside bag and to select an item
	//@param me		The trainer whose bag is being checked
	//@param foe	The Pokemon that will have an item used on them (if it's a pokeball,
												//if it's a heal item, it will be applied to the trainer's team)
	static boolean checkBag(Trainer me, Pokemon foe)
	{
		Scanner user = new Scanner(System.in);
		Backpack bag = me.getBackpack();
		bag.display(); 		//display contents
		//user.nextLine();   //Clear out Scanner
		System.out.print("Type in the item you'd like to use or go back by typing -1: ");
		String choice = user.nextLine();	//select an item or return 
		
		if(choice.equals("-1")) 	//if you return, the foe doesn't attack yet
			return false; 
		else				//else search bag and use item
		{
			//System.out.println("HELP");
			ItemEntry desiredItem = bag.getItem(choice);
			//System.out.println("HELP");
	
			try
			{
				if(desiredItem.getItemType() == "Poke Ball")		//if item is a pokeball type
				{
					desiredItem.useItem(foe, me);
					return true;
				}
				else									//if item is a heal item
				{
					//Select a Pokemon from your team to heal
					System.out.print("Which Pokemon Number do you want to heal: ");
					me.getTeam().display(); 
					int pkmnPos	= user.nextInt() - 1;	//Subtract offset for acuracy
					Pokemon p = me.getTeam().getPokemon(pkmnPos);
					desiredItem.useItem(p, me);
					return true;
				}
			}
			catch(NullPointerException e)	//If there are less than 4 moves, then the missing slots won't be displayed
			{
				return false;
			}
		}
	}
	
	/*
	 * Access the contents of the player's PC
	 * @param Player	Theplayer that'll have their PC accessed
	 */
	static void accessPC(Player player)
	{
		System.out.println("Booted up the PC. What would you like to do?");
		Scanner user = new Scanner(System.in);
		PC_Storage myBox = player.getPC_Storage();
		Team myTeam = player.getTeam();
		int choice = 0;
		
		while(choice != -1)
		{
			System.out.println("Type in a number to perform a function or type -1 to exit: ");
			System.out.println("1) Display");
			System.out.println("2) Deposit");
			System.out.println("3) Withdraw");
			//System.out.println("4) Move");
			
			//Store the selected option
			try
			{
				choice = user.nextInt();
			}
			catch(InputMismatchException e)
			{
				choice = -1;
			}
			
			switch(choice)
			{
			case 1:
				System.out.println("---Display---");
				myBox.display();
				break;
			case 2:
				System.out.println("---Deposit---");
				System.out.println("Type in the Pokemon number you want to deposit or type -1 to exit: ");
				myTeam.display();
				try
				{
					choice = user.nextInt();
				}
				catch(InputMismatchException e)
				{
					choice = -1;
				}
				
				if(choice != -1 && myTeam.getNumOfPKMN() > 1)
				{
					Pokemon p = myTeam.getPokemon(choice-1);	//Get the actual Pokemon
					myBox.deposit(p);	//Add to Box
					myTeam.remove(choice-1); 
					//System.out.println("Deposit done");
				}
				else
				{
					System.out.println("You must hav atleast 1 PKMN");
				}
				break;
			case 3:
				System.out.println("---Withdraw---");
					Pokemon p = myBox.withdrawl();
					myTeam.add(p);
				break;
			case -1:
				System.out.println("Logged off PC.");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				break;
			default:
				System.out.println("Invalid selection");
			}
		}
		
	}
	
	//Restores the health of the entire team
	//@trainer	The trainer whose team will be healed and have all shell levels reset
	static void restoreHealth(Trainer trainer)
	{
		Team team = trainer.getTeam();
		for(int i = 0; i < team.getNumOfPKMN(); i++)
		{
			Pokemon p = team.getPokemon(i);
			p.setHP(p.getMaxHP()); 		//Set health to be full
			p.restorePP(); 				//Restore PP
		}
	}
	
	
	//Restores all the Team's shell levels after a battle
	static void restoreShells(Trainer trainer)
	{
		Team team = trainer.getTeam();
		for(int i = 0; i < team.getNumOfPKMN(); i++)
		{
			Pokemon p = team.getPokemon(i);
			p.setAttackShell(0);
			p.setDefenseShell(0);
			p.setEvasivenessShell(0);
			p.setAccuracyShell(0);
		}
	}
	
	/*
	 * Causes the program to pause for x amount of miliseconds
	 * @miliseconds		The amount of miliseconds to pause
	 */
	static void waitMiliSeconds(int miliseconds)
	{
		try {
			TimeUnit.MILLISECONDS.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * @return A random CPU_Trainer for the player to face off against in Trainer battles
	 */
	static CPUTrainer randomOpponent()
	{
		String name = "Willie";
		String trainerClass = "Ace Trainer";
		CPUTrainer cpu = new CPUTrainer(name, trainerClass);
		cpu.addToTeam(spawnRandomWildPokemon());
		cpu.addToTeam(spawnRandomWildPokemon());
		
		
		return cpu;
	}
}

/*
Hi there! Welcome to the world of Pokemon! What's your name?: 
Elias
Squirtle has been added to your team.
5 Pokeballs were added & 3 Potions added.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Garchomp has appeared!
Go Squirtle!
-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Garchomp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Magikarp has appeared!
Go Squirtle!
-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
3
Pokemon Team
1) Squirtle	Lvl: 50		HP: 120/120

Which Pokemon do you want to swap in? Type in it's number or type -1 to exit:
-1
-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		20/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
3

Squirtle used Tail Whip
Magikarp's defense fell!
Magikarp used Splash
Magikarp's attack missed.

-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		20/20 PP
3) Tail Whip		39/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Magikarp lost 35 HP
Magikarp is confused.
Magikarp used Splash
Magikarp's attack missed.

-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 85/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		19/20 PP
3) Tail Whip		39/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Magikarp lost 36 HP
Magikarp is confused.
Magikarp used Splash
Magikarp's attack missed.

-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 49/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
2
 - Potion		3
 - Pokeball		5
Type in the item you'd like to use or go back by typing -1: Pokeball
Elias threw a Pokeball.
*shake*
*shake*
*shake*
Gotcha!
Magikarp was caught!
Magikarp has been added to your team.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Kadabra has appeared!
Go Squirtle!
-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		18/20 PP
3) Tail Whip		39/40 PP
Type in a move number: 
3

Squirtle used Tail Whip
Kadabra's defense fell!
Kadabra used Confusion
Foe's Squirtle lost 21 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 99/120
-------------------------------
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		18/20 PP
3) Tail Whip		38/40 PP
Type in a move number: 
3

Squirtle used Tail Whip
Kadabra's defense fell!
Kadabra used Confusion
Foe's Squirtle lost 21 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 78/120
-------------------------------
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		18/20 PP
3) Tail Whip		37/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Kadabra lost 52 HP
Kadabra used Hypnosis

-------------------------------
Squirtle
	Lvl: 50
	HP: 78/120
-------------------------------
-------------------------------
Kadabra
	Lvl: 50
	HP: 68/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		17/20 PP
3) Tail Whip		37/40 PP
Type in a move number: 
2

Squirtle is fast asleep...
Kadabra used Confusion
Foe's Squirtle lost 22 HP
Squirtle has no Status Effects

-------------------------------
Squirtle
	Lvl: 50
	HP: 56/120
-------------------------------
-------------------------------
Kadabra
	Lvl: 50
	HP: 68/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		17/20 PP
3) Tail Whip		37/40 PP
Type in a move number: 
2

Squirtle is confused.
Squirtle used Water Pulse
Foe's Kadabra lost 47 HP
Kadabra used Hypnosis
Squirtle has no Status Effects

-------------------------------
Squirtle
	Lvl: 50
	HP: 56/120
-------------------------------
-------------------------------
Kadabra
	Lvl: 50
	HP: 21/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
2
 - Potion		3
 - Pokeball		4
Type in the item you'd like to use or go back by typing -1: Pokeball
Elias threw a Pokeball.
*shake*
*shake*
*shake*
Gotcha!
Kadabra was caught!
Kadabra has been added to your team.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
7
Welcome to the Pokemon Center. Please wait a moment.
...
Your Pokemon have fully been restored! Please visit us again.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
4
Pokemon Team
1) Squirtle	Lvl: 50		HP: 120/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Kadabra	Lvl: 50		HP: 120/120

To rearrange, select a PKMN number or enter -1 to exit: 
3
Select a second PKMN number: 
1
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
2
Charmander has been added to your team.
Magikarp has been added to your team.

-----Ace Trainer Willie challenged you to a Pokemon battle!---
Ace Trainer Willie sent out a Charmander!
Go Kadabra!
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
3
Pokemon Team
1) Kadabra	Lvl: 50		HP: 120/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Squirtle	Lvl: 50		HP: 120/120

Which Pokemon do you want to swap in? Type in it's number or type -1 to exit:
3
Kadabra has been sent back.
Go Squirtle!
Charmander used Ember
Foe's Squirtle lost 18 HP
-------------------------------
Squirtle
	Lvl: 50
	HP: 102/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		20/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Charmander lost 24 HP
Charmander used Tackle
Foe's Squirtle lost 17 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 85/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
2
 - Potion		3
 - Pokeball		3
Type in the item you'd like to use or go back by typing -1: Potion
Which Pokemon Number do you want to heal: Pokemon Team
1) Squirtle	Lvl: 50		HP: 85/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Kadabra	Lvl: 50		HP: 120/120

1
The Potion restored 20 HP to Squirtle.
Charmander used Tackle
Foe's Squirtle lost 17 HP
-------------------------------
Squirtle
	Lvl: 50
	HP: 88/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		19/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
3

Squirtle used Tail Whip
Charmander's defense fell!
Charmander used Ember
Foe's Squirtle lost 17 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 71/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		19/20 PP
3) Tail Whip		39/40 PP
Type in a move number: 
2

Charmander used Tackle
Foe's Squirtle lost 19 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 52/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		19/20 PP
3) Tail Whip		39/40 PP
Type in a move number: 
2

Charmander used Ember
Foe's Squirtle lost 17 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 35/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		19/20 PP
3) Tail Whip		39/40 PP
Type in a move number: 
2

Charmander used Ember
Foe's Squirtle lost 18 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 17/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		19/20 PP
3) Tail Whip		39/40 PP
Type in a move number: 
2

Charmander used Tackle
Foe's Squirtle lost 17 HP

Squirtle has fainted.
Pokemon Team
1) Squirtle	Lvl: 50		HP: 0/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Kadabra	Lvl: 50		HP: 120/120

Which Pokemon do you want to swap in? Type in it's number or type -1 to exit:
3
Go Kadabra
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		25/25 PP
2) Hypnosis		10/10 PP
Type in a move number: 
2

Kadabra used Hypnosis
Charmander is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		25/25 PP
2) Hypnosis		9/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Charmander lost 33 HP
Charmander is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 63/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		24/25 PP
2) Hypnosis		9/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Charmander lost 30 HP
Charmander is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 33/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		23/25 PP
2) Hypnosis		9/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Charmander lost 32 HP
Charmander is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Charmander
	Lvl: 50
	HP: 1/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		22/25 PP
2) Hypnosis		9/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Charmander lost 1 HP

Charmander has fainted.
Ace Trainer Willie sent out Magikarp.
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		21/25 PP
2) Hypnosis		9/10 PP
Type in a move number: 
2

Kadabra used Hypnosis
Kadabra's attack missed.
Magikarp used Splash
Magikarp's attack missed.

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		21/25 PP
2) Hypnosis		9/10 PP
Type in a move number: 
2

Kadabra used Hypnosis
Kadabra's attack missed.
Magikarp used Splash
Magikarp's attack missed.

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		21/25 PP
2) Hypnosis		9/10 PP
Type in a move number: 
2

Kadabra used Hypnosis
Magikarp is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1\
Invalid Selection
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		21/25 PP
2) Hypnosis		8/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Magikarp lost 23 HP
Magikarp is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 97/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		20/25 PP
2) Hypnosis		8/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Magikarp lost 24 HP
Magikarp is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 73/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away

Invalid Selection
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 73/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		19/25 PP
2) Hypnosis		8/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Magikarp lost 22 HP
Magikarp is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 51/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		18/25 PP
2) Hypnosis		8/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Magikarp lost 21 HP
Magikarp is fast asleep...

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 30/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		17/25 PP
2) Hypnosis		8/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Magikarp lost 20 HP
Magikarp has no Status Effects
Magikarp used Splash
Magikarp's attack missed.

-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 10/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Confusion		16/25 PP
2) Hypnosis		8/10 PP
Type in a move number: 
1

Kadabra used Confusion
Foe's Magikarp lost 10 HP

Magikarp has fainted.
You defeat Willie!
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
7
Welcome to the Pokemon Center. Please wait a moment.
...
Your Pokemon have fully been restored! Please visit us again.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Onix has appeared!
Go Kadabra!
-------------------------------
Kadabra
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Onix
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
3
Pokemon Team
1) Kadabra	Lvl: 50		HP: 120/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Squirtle	Lvl: 50		HP: 120/120

Which Pokemon do you want to swap in? Type in it's number or type -1 to exit:
3
Kadabra has been sent back.
Go Squirtle!
Onix used Growl
Squirtle's attack fell!
-------------------------------
Squirtle
	Lvl: 50
	HP: 120/120
-------------------------------
-------------------------------
Onix
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		20/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
2

Onix used Tackle
Foe's Squirtle lost 13 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 107/120
-------------------------------
-------------------------------
Onix
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		20/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Onix lost 24 HP
Onix used Growl
Squirtle's attack fell!

-------------------------------
Squirtle
	Lvl: 50
	HP: 107/120
-------------------------------
-------------------------------
Onix
	Lvl: 50
	HP: 96/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
2
 - Potion		2
 - Pokeball		3
Type in the item you'd like to use or go back by typing -1: Pokeball
Elias threw a Pokeball.
*shake*
*shake*
*shake*
Gotcha!
Onix was caught!
Onix has been added to your team.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Onix has appeared!
Go Squirtle!
-------------------------------
Squirtle
	Lvl: 50
	HP: 107/120
-------------------------------
-------------------------------
Onix
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
3
Invalid Selection
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
4
Pokemon Team
1) Squirtle	Lvl: 50		HP: 107/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Kadabra	Lvl: 50		HP: 120/120
4) Onix	Lvl: 50		HP: 96/120

To rearrange, select a PKMN number or enter -1 to exit: 
4
Select a second PKMN number: 
1
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Magikarp has appeared!
Go Onix!
-------------------------------
Onix
	Lvl: 50
	HP: 96/120
-------------------------------
-------------------------------
Magikarp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Garchomp has appeared!
Go Onix!
-------------------------------
Onix
	Lvl: 50
	HP: 96/120
-------------------------------
-------------------------------
Garchomp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Eevee has appeared!
Go Onix!
-------------------------------
Onix
	Lvl: 50
	HP: 96/120
-------------------------------
-------------------------------
Eevee
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Bulbasaur has appeared!
Go Onix!
-------------------------------
Onix
	Lvl: 50
	HP: 96/120
-------------------------------
-------------------------------
Bulbasaur
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		34/35 PP
2) Growl		38/40 PP
Type in a move number: 
2

Onix used Growl
Bulbasaur's attack fell!
Bulbasaur used Tackle
Foe's Onix lost 17 HP

-------------------------------
Onix
	Lvl: 50
	HP: 79/120
-------------------------------
-------------------------------
Bulbasaur
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		34/35 PP
2) Growl		37/40 PP
Type in a move number: 
2

Onix used Growl
Bulbasaur's attack fell!
Bulbasaur used Tackle
Foe's Onix lost 18 HP

-------------------------------
Onix
	Lvl: 50
	HP: 61/120
-------------------------------
-------------------------------
Bulbasaur
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		34/35 PP
2) Growl		36/40 PP
Type in a move number: 

1

Onix used Tackle
Foe's Bulbasaur lost 10 HP
Bulbasaur used Tackle
Foe's Onix lost 19 HP

-------------------------------
Onix
	Lvl: 50
	HP: 42/120
-------------------------------
-------------------------------
Bulbasaur
	Lvl: 50
	HP: 110/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		33/35 PP
2) Growl		36/40 PP
Type in a move number: 
1

Onix used Tackle
Foe's Bulbasaur lost 9 HP
Bulbasaur used Tackle
Foe's Onix lost 17 HP

-------------------------------
Onix
	Lvl: 50
	HP: 25/120
-------------------------------
-------------------------------
Bulbasaur
	Lvl: 50
	HP: 101/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Riachu has appeared!
Go Onix!
-------------------------------
Onix
	Lvl: 50
	HP: 25/120
-------------------------------
-------------------------------
Riachu
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
2
 - Potion		2
 - Pokeball		2
Type in the item you'd like to use or go back by typing -1: Pokeball
Elias threw a Pokeball.
*shake*
*shake*
Aghhhh! You almost had it!
Riachu used Tackle
Foe's Onix lost 18 HP
Pokemon Team
1) Onix	Lvl: 50		HP: 7/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Kadabra	Lvl: 50		HP: 120/120
4) Squirtle	Lvl: 50		HP: 107/120

-------------------------------
Onix
	Lvl: 50
	HP: 7/120
-------------------------------
-------------------------------
Riachu
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		32/35 PP
2) Growl		36/40 PP
Type in a move number: 
1

Onix used Tackle
Foe's Riachu lost 18 HP
Riachu used Tail Whip
Onix's defense fell!

-------------------------------
Onix
	Lvl: 50
	HP: 7/120
-------------------------------
-------------------------------
Riachu
	Lvl: 50
	HP: 102/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		31/35 PP
2) Growl		36/40 PP
Type in a move number: 
1

Onix used Tackle
Foe's Riachu lost 18 HP
Riachu used Tackle
Foe's Onix lost 7 HP

Onix has fainted.
Pokemon Team
1) Onix	Lvl: 50		HP: 0/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Kadabra	Lvl: 50		HP: 120/120
4) Squirtle	Lvl: 50		HP: 107/120

Which Pokemon do you want to swap in? Type in it's number or type -1 to exit:
4
Go Squirtle
-------------------------------
Squirtle
	Lvl: 50
	HP: 107/120
-------------------------------
-------------------------------
Riachu
	Lvl: 50
	HP: 84/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		19/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Riachu lost 27 HP
Riachu used Tail Whip
Squirtle's defense fell!

-------------------------------
Squirtle
	Lvl: 50
	HP: 107/120
-------------------------------
-------------------------------
Riachu
	Lvl: 50
	HP: 57/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		18/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Riachu lost 27 HP
Riachu used Tackle
Foe's Squirtle lost 26 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 81/120
-------------------------------
-------------------------------
Riachu
	Lvl: 50
	HP: 30/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
1
1) Tackle		35/35 PP
2) Water Pulse		17/20 PP
3) Tail Whip		40/40 PP
Type in a move number: 
2

Squirtle used Water Pulse
Foe's Riachu lost 26 HP
Riachu used Tackle
Foe's Squirtle lost 27 HP

-------------------------------
Squirtle
	Lvl: 50
	HP: 54/120
-------------------------------
-------------------------------
Riachu
	Lvl: 50
	HP: 4/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
2
 - Potion		2
 - Pokeball		1
Type in the item you'd like to use or go back by typing -1: Pokeball
Elias threw a Pokeball.
*shake*
*shake*
*shake*
Gotcha!
Riachu was caught!
Riachu has been added to your team.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Garchomp has appeared!
Go Squirtle!
-------------------------------
Squirtle
	Lvl: 50
	HP: 54/120
-------------------------------
-------------------------------
Garchomp
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
1

-----Elias walked into the tall grass.---
A wild Bulbasaur has appeared!
Go Squirtle!
-------------------------------
Squirtle
	Lvl: 50
	HP: 54/120
-------------------------------
-------------------------------
Bulbasaur
	Lvl: 50
	HP: 120/120
-------------------------------
What do you want to do?
1) Battle		2) Bag	 		3) Team			4) Run Away
4
Succesfully managed to run
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
5
Booted up the PC. What would you like to do?
Type in a number to perform a function or type -1 to exit: 
1) Display
2) Deposit
3) Withdraw
1
---Display---

Type in a number to perform a function or type -1 to exit: 
1) Display
2) Deposit
3) Withdraw
2
---Deposit---
Type in the Pokemon number you want to deposit or type -1 to exit: 
Pokemon Team
1) Squirtle	Lvl: 50		HP: 54/120
2) Magikarp	Lvl: 50		HP: 120/120
3) Kadabra	Lvl: 50		HP: 120/120
4) Onix	Lvl: 50		HP: 0/120
5) Riachu	Lvl: 50		HP: 4/120

2
Type in a number to perform a function or type -1 to exit: 
1) Display
2) Deposit
3) Withdraw
1
---Display---
1) Magikarp

Type in a number to perform a function or type -1 to exit: 
1) Display
2) Deposit
3) Withdraw
2
---Deposit---
Type in the Pokemon number you want to deposit or type -1 to exit: 
Pokemon Team
1) Squirtle	Lvl: 50		HP: 54/120
2) Kadabra	Lvl: 50		HP: 120/120
3) Onix	Lvl: 50		HP: 0/120
4) Riachu	Lvl: 50		HP: 4/120

3
Type in a number to perform a function or type -1 to exit: 
1) Display
2) Deposit
3) Withdraw
3
---Withdraw---
1) Magikarp
2) Onix

Select a PKMN number: 
-1
Invalid input. Try again: 
2
Onix has been added to your team.
Type in a number to perform a function or type -1 to exit: 
1) Display
2) Deposit
3) Withdraw
-1
Logged off PC.
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
7
Welcome to the Pokemon Center. Please wait a moment.
...
Your Pokemon have fully been restored! Please visit us again.
Type in the Number of the Action you'd Like to Do
1) To the Wild Area
2) Trainer Battle
4) Team
5) PC
7) Pokemon Center
8) Exit
8
Thank you for playing. Have a great day!
*/