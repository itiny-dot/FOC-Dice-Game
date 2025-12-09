package main;
import javax.swing.JFrame;

public class Shop extends JFrame{

    private double balance;

    public Shop(){
    	balance = 0;
    }
    
    public double getBalance() {
    	return this.balance;
    }
    
    public double Store(double balance, Inventory inventory, Items item) {

        if (balance <= 0) {
            return 0;
        }

        if ("die".equals(item.getName())) {
            inventory.addInv(item);
            balance -= 5;
        }

        return balance;
    }
    
}
