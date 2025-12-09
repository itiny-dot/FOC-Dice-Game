package main;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
	
	private final List<Items> items = new ArrayList<>();
	public Inventory() {
		
	}
	

	   // ← make this NON-static so it works on each Inventory object
    public void addInv(Items item) {
        items.add(item);
    }

    public boolean contains(Items item) {
        return items.contains(item);
    }

    public int size() {
        return items.size();
    }
}