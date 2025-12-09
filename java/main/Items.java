package main;

public class Items {
	private boolean isConsumable;
	private int delta;
	public String name;
	
	public Items (boolean consumable, int delta, String name) {
		isConsumable = consumable;
		this.delta = delta;
		this.name = name;
	}
	
	public Items(String string) {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return this.name;
	}

	public void use(main.Dice d) {
		if (isConsumable){
            int selection = 1;  //todo replace value with player selected die face index
            d.changeFace(selection, delta);
			return;
		}
	}
	
	
	
	
}
