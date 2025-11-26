public class Items {
	private boolean isConsumable;
	private int delta;
	
	public Item (boolean consumable, int delta) {
		isConsumable = consumable;
		this.delta = delta;
	}
	
	public void use(Dice d) {
		if isConsumable{
			selection = 1;  //todo replace value with player selected die face index
			d.changeFace(selection, delta);
			return;
		}
	}
}
