public class Items {
	private boolean isConsumable;
	private int delta;
	
	public Items (boolean consumable, int delta) {
		isConsumable = consumable;
		this.delta = delta;
	}
	
	public void use(main.Dice d) {
		if (isConsumable){
            int selection = 1;  //todo replace value with player selected die face index
            d.changeFace(selection, delta);
			return;
		}
	}
}
