package main;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ShopTest {

    private Shop shop;
    private Inventory inventory;
    private Items dieItem;
    private Items otherItem;

    @Before
    public void setUp() {
        shop = new Shop();
        inventory = new Inventory();

        // You may need to adapt these constructors to your real classes
        dieItem = new Items("die");
        otherItem = new Items("other");
    }

    @Test
    public void testInitialBalanceIsZero() {
        assertEquals(0.0, shop.getBalance(), 0.0001);
    }

    @Test
    public void testStoreReturnsZeroWhenBalanceIsNotPositive() {
        double result = shop.Store(0.0, inventory, dieItem);
        assertEquals(0.0, result, 0.0001);

        result = shop.Store(-10.0, inventory, dieItem);
        assertEquals(0.0, result, 0.0001);
    }
    @Test
    public void testStoreBuysDieAndReducesBalance() {
        double startingBalance = 20.0;

        double result = shop.Store(startingBalance, inventory, dieItem);

        // method should subtract 5
        assertEquals(15.0, result, 0.0001);

        // Check that inventory had addInv called once with dieItem.
        // This assumes Inventory has some method to inspect contents;
        // adjust for your real implementation.
        assertTrue(inventory.contains(dieItem));
    }

    @Test
    public void testStoreDoesNotChangeBalanceForNonDieItem() {
        double startingBalance = 20.0;

        double result = shop.Store(startingBalance, inventory, otherItem);

        // No change expected since item != "die"
        assertEquals(20.0, result, 0.0001);

        // Inventory should not have otherItem added (depends on your Inventory class)
        assertFalse(inventory.contains(otherItem));
    }

}
