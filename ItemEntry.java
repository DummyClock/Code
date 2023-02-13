/*	Elias Saied
	4/12/2022
	
	An interface that'll be used to create Item objects.
*/

public interface ItemEntry {

	/*
	 * @return The name of the Item
	 */
	public String getItemName();
	
	/*
	 * @return The description of the Item
	 */
	public String getDescription();
	
	/*
	 * @param description	The new description to bet assigned to the object
	 */
	public void setDescription(String description);
	
	/*
	 *  @return The quantity of an Item
	 */
	public void setQuantity(int num_of_items);
	
	/*
	 *  Lower the quantity of an object by 1
	 */
	public void decrementQuantity();
	
	/*
	 * @return The quantity of an item object
	 */
	public int getQuantity();
	
	/*
	 *  Used to determine which itemType this item is
	 *  @return 	A string value indicating the type
	 */
	public String getItemType();
	
	/*
	 * @return true/false if both items have the same name
	 */
	public boolean compareTo(ItemEntry anItem);
	
	/*
	 * Uses the item to perform an action and will subtract 1 from the quantity
	 */
	public <T extends ItemEntry> void useItem(Pokemon target, Trainer trainer);
	
}
