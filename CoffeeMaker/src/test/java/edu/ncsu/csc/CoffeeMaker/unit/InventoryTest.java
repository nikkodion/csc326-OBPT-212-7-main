package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    /**
     * Set up the tests by creating an inventory
     */
    @Before
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        final Ingredient chocolate = new Ingredient( "Chocolate", 500 );
        final Ingredient coffee = new Ingredient( "Coffee", 500 );
        final Ingredient milk = new Ingredient( "Milk", 500 );
        final Ingredient sugar = new Ingredient( "Sugar", 500 );
        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( coffee );
        ingredients.add( chocolate );
        ingredients.add( sugar );
        ingredients.add( milk );
        ivt.addIngredients( ingredients );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        // Test consuming inventory
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        /**
         * recipe.setChocolate( 10 ); recipe.setMilk( 20 ); recipe.setSugar( 5
         * ); recipe.setCoffee( 1 );
         */
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        recipe.setPrice( 5 );
        Assert.assertTrue( i.enoughIngredients( recipe ) );
        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assert.assertEquals( 490, (int) i.getAmountInInventory( "Chocolate" ) );
        Assert.assertEquals( 480, (int) i.getAmountInInventory( "Milk" ) );
        Assert.assertEquals( 495, (int) i.getAmountInInventory( "Sugar" ) );
        Assert.assertEquals( 499, (int) i.getAmountInInventory( "Coffee" ) );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        // Test adding to the inventory
        Inventory ivt = inventoryService.getInventory();

        final Ingredient chocolate = new Ingredient( "Coffee", 5 );
        final Ingredient coffee = new Ingredient( "Milk", 3 );
        final Ingredient milk = new Ingredient( "Sugar", 7 );
        final Ingredient sugar = new Ingredient( "Chocolate", 2 );
        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( coffee );
        ingredients.add( chocolate );
        ingredients.add( sugar );
        ingredients.add( milk );

        ivt.addIngredients( ingredients );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for coffee", 5,
                (int) ivt.getAmountInInventory( "Coffee" ) );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for milk", 3,
                (int) ivt.getAmountInInventory( "Milk" ) );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values sugar", 7,
                (int) ivt.getAmountInInventory( "Sugar" ) );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values chocolate", 2,
                (int) ivt.getAmountInInventory( "Chocolate" ) );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        // Test that a negative field does not add to inventory
        final Inventory ivt = inventoryService.getInventory();

        final Ingredient chocolate = new Ingredient( "Coffee", -5 );
        final Ingredient coffee = new Ingredient( "Milk", 3 );
        final Ingredient milk = new Ingredient( "Sugar", 7 );
        final Ingredient sugar = new Ingredient( "Chocolate", 2 );
        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( coffee );
        ingredients.add( chocolate );
        ingredients.add( sugar );
        ingredients.add( milk );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee",
                    500, (int) ivt.getAmountInInventory( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk",
                    500, (int) ivt.getAmountInInventory( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar",
                    500, (int) ivt.getAmountInInventory( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate",
                    500, (int) ivt.getAmountInInventory( "Chocolate" ) );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        // Test that a negative field does not add to inventory
        final Inventory ivt = inventoryService.getInventory();

        final Ingredient chocolate = new Ingredient( "Chocolate", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", -3 );
        final Ingredient milk = new Ingredient( "Milk", 7 );
        final Ingredient sugar = new Ingredient( "Sugar", 2 );
        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( coffee );
        ingredients.add( chocolate );
        ingredients.add( sugar );
        ingredients.add( milk );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee",
                    500, (int) ivt.getAmountInInventory( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk",
                    500, (int) ivt.getAmountInInventory( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar",
                    500, (int) ivt.getAmountInInventory( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate",
                    500, (int) ivt.getAmountInInventory( "Chocolate" ) );
        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        // Test that a negative field does not add to inventory
        final Inventory ivt = inventoryService.getInventory();

        final Ingredient chocolate = new Ingredient( "Chocolate", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient milk = new Ingredient( "Milk", -7 );
        final Ingredient sugar = new Ingredient( "Sugar", 2 );
        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( coffee );
        ingredients.add( chocolate );
        ingredients.add( sugar );
        ingredients.add( milk );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee",
                    500, (int) ivt.getAmountInInventory( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk",
                    500, (int) ivt.getAmountInInventory( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar",
                    500, (int) ivt.getAmountInInventory( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate",
                    500, (int) ivt.getAmountInInventory( "Chocolate" ) );
        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        // Test that a negative field does not add to inventory
        final Inventory ivt = inventoryService.getInventory();

        final Ingredient chocolate = new Ingredient( "Chocolate", 5 );
        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient milk = new Ingredient( "Milk", 7 );
        final Ingredient sugar = new Ingredient( "Sugar", -2 );
        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add( coffee );
        ingredients.add( chocolate );
        ingredients.add( sugar );
        ingredients.add( milk );

        try {
            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee",
                    500, (int) ivt.getAmountInInventory( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk",
                    500, (int) ivt.getAmountInInventory( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar",
                    500, (int) ivt.getAmountInInventory( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate",
                    500, (int) ivt.getAmountInInventory( "Chocolate" ) );
        }

    }

    // Author: jlbeasl2
    @Test
    @Transactional
    public void testCreateNewInventory () {
        inventoryService.deleteAll();
        final Inventory inv = inventoryService.getInventory();
        // Creating inventory from nothing
        assertEquals( "Making sure inventory starts out empty", inv.getIngInventory().size(), 0 );
    }

    // Author: jlbeasl2
    @Test
    @Transactional
    public void testCheckInventoryUsingCheck () {
        final Inventory inv = inventoryService.getInventory();
        int coffeeAmt = 0, milkAmt = 0, sugarAmt = 0, chocoAmt = 0;

        // Test all possible branches of coffee
        try {
            coffeeAmt = inv.checkAmountInInventory( "Coffee", "-1" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( coffeeAmt, 0 );
            assertEquals( "Units of Coffee must be a positive integer", e.getMessage() );
        }
        try {
            coffeeAmt = inv.checkAmountInInventory( "Coffee", "one" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( coffeeAmt, 0 );
            assertEquals( "Units of Coffee must be a positive integer", e.getMessage() );
        }
        try {
            coffeeAmt = inv.checkAmountInInventory( "Coffee", "1" );
            assertEquals( coffeeAmt, 1 );
        }
        catch ( final IllegalArgumentException e ) {
            fail();
        }

        // Test all possible branches of milk
        try {
            milkAmt = inv.checkAmountInInventory( "Milk", "-1" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( milkAmt, 0 );
            assertEquals( "Units of Milk must be a positive integer", e.getMessage() );
        }
        try {
            milkAmt = inv.checkAmountInInventory( "Milk", "one" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( milkAmt, 0 );
            assertEquals( "Units of Milk must be a positive integer", e.getMessage() );
        }
        try {
            milkAmt = inv.checkAmountInInventory( "Milk", "1" );
            assertEquals( milkAmt, 1 );
        }
        catch ( final IllegalArgumentException e ) {
            fail();
        }

        // Check all possible branches of sugar
        try {
            sugarAmt = inv.checkAmountInInventory( "Sugar", "-1" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( sugarAmt, 0 );
            assertEquals( "Units of Sugar must be a positive integer", e.getMessage() );
        }
        try {
            sugarAmt = inv.checkAmountInInventory( "Sugar", "one" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( sugarAmt, 0 );
            assertEquals( "Units of Sugar must be a positive integer", e.getMessage() );
        }
        try {
            sugarAmt = inv.checkAmountInInventory( "Sugar", "1" );
            assertEquals( sugarAmt, 1 );
        }
        catch ( final IllegalArgumentException e ) {
            fail();
        }

        // Check all possible branches of chocolate
        try {
            chocoAmt = inv.checkAmountInInventory( "Chocolate", "-1" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( chocoAmt, 0 );
            assertEquals( "Units of Chocolate must be a positive integer", e.getMessage() );
        }
        try {
            chocoAmt = inv.checkAmountInInventory( "Chocolate", "one" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( chocoAmt, 0 );
            assertEquals( "Units of Chocolate must be a positive integer", e.getMessage() );
        }
        try {
            chocoAmt = inv.checkAmountInInventory( "Chocolate", "1" );
            assertEquals( chocoAmt, 1 );
        }
        catch ( final IllegalArgumentException e ) {
            fail();
        }
    }

}
