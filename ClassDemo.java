
public class ClassDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Testing");
		Pokemon Gengar = new Pokemon("Gengar", "Ghost", 200);
		Pokemon Onix = new Pokemon("Onix", "Rock", "Ground", 220);
		Move Tackle = new AttackMove("Tackle", "Normal", 40, 35, 95, 100, 5);
		Gengar.addMove(Tackle);
		Gengar.useMove(0, Onix);
		Gengar.useMove(0, Onix);
		Gengar.useMove(0, Onix);
		Gengar.useMove(0, Onix);
		System.out.println(Onix.getName() + "	HP: " + Onix.getHP());
		
		System.out.println(Onix.getStatusCondition());
		Onix.addMove(Tackle);
		Onix.useMove(0, Gengar);
		Onix.useMove(0, Gengar);
		Onix.useMove(0, Gengar);

		Player me = new Player("Yukino");
		Backpack myBag = me.getBackpack();		
		
		myBag.addItem(new HealItem(20, "Potion", null, 3));
		HealItem potion = new HealItem(20, "Potion", null, 99);		// Create 3 potions
		System.out.println("Potion in bag? " + myBag.contains(potion));
		System.out.println();
		
		//myBag.addItem(potion);
		System.out.println("2nd: Potion in bag? " + myBag.contains(potion));
		System.out.println(me.getName() + " has " + potion.getQuantity());
		potion.useItem(Onix, me);
		potion.useItem(Onix, me);
		potion.useItem(Onix, me);
		potion.useItem(Onix, me);
		PokeBall pokeball = new PokeBall((float)1.25, "Poke Ball", null, 5);
		pokeball.useItem(Onix, me);
		myBag.addItem(new PokeBall((float)1.25, "Poke Ball", null, 5));
		pokeball.useItem(Onix, me);
		
		//System.out.println(me.getName() + " has an Onix " + me.getTeam().contains(Onix));
		displayItemQuantity(me, pokeball);
		
		myBag.display();


	}

	//Display the amount of an item a Trainer has
	static void displayItemQuantity(Trainer aTrainer, ItemEntry item)
	{
		System.out.println(aTrainer.getName()
				+ " has " + aTrainer.getBackpack().getQuantityOf(item) + " " + item.getItemName() + "s");
	}
	
}