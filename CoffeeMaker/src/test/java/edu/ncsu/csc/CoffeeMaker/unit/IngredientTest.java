package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.ConstraintViolationException;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    @Autowired
    private IngredientService service;

    /**
     * Set up the tests
     */
    @Before
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddIngredient () {
        service.deleteAll();
        // Test creating a blank ingredient
        Ingredient i = new Ingredient();

        // Expecting null ingredient and 0 amount
        assertNull( i.getName() );
        assertEquals( 0, i.getAmount() );

        // Test creating an ingredient with Coffee and 5 amount
        i = new Ingredient( "Coffee", 5 );

        // Expecting IngredientType Coffee and 5 amount
        assertEquals( "Coffee", i.getName() );
        assertEquals( 5, i.getAmount() );

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        service.save( i );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

        i = service.findByName( "Coffee" );
        assertEquals( "Coffee", i.getName() );
        assertEquals( 5, i.getAmount() );

    }

    @Test
    @Transactional
    public void testAddIngredient1 () {
        // Test creating an Ingredient with a negative amount
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient i = new Ingredient( name, -5 );

        try {
            service.save( i );

            Assert.assertNull( "An Ingredient was able to be created with a negative amount",
                    service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddIngredient2 () {
        // Test adding more than one Ingredient to the database
        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient i = new Ingredient( name, 5 );

        service.save( i );

        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

        final Ingredient i2 = new Ingredient( "Chocolate", 3 );

        service.save( i2 );

        Assert.assertEquals( "There should be 2 Ingredients in the CoffeeMaker", 2, service.findAll().size() );

        // Verify the contents
        final Ingredient i3 = service.findByName( "Coffee" );
        final Ingredient i4 = service.findByName( "Chocolate" );

        assertEquals( "Coffee", i3.getName() );
        assertEquals( 5, i3.getAmount() );

        assertEquals( "Chocolate", i4.getName() );
        assertEquals( 3, i4.getAmount() );

    }

    @Test
    @Transactional
    public void testDeleteIngredients1 () {
        // Test deleting an ingredient
        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient i = new Ingredient( name, 5 );

        // Add 2 Ingredients
        service.save( i );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );
        final Ingredient i2 = new Ingredient( "Chocolate", 3 );
        service.save( i2 );
        Assert.assertEquals( "There should be 2 Ingredients in the CoffeeMaker", 2, service.findAll().size() );
        final Ingredient i3 = service.findByName( "Coffee" );
        final Ingredient i4 = service.findByName( "Chocolate" );

        // Verify contents
        assertEquals( "Coffee", i3.getName() );
        assertEquals( 5, i3.getAmount() );

        assertEquals( "Chocolate", i4.getName() );
        assertEquals( 3, i4.getAmount() );

        // Try deleting, there should be 1 ingredient left
        service.delete( i );

        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

        assertNull( service.findByName( "Coffee" ) );

    }

    @Test
    @Transactional
    public void testDeleteIngredients2 () {
        // Test deleteAll
        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient i = new Ingredient( name, 5 );

        // Add 2 Ingredients
        service.save( i );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );
        final Ingredient i2 = new Ingredient( "Chocolate", 3 );
        service.save( i2 );
        Assert.assertEquals( "There should be 2 Ingredients in the CoffeeMaker", 2, service.findAll().size() );
        final Ingredient i3 = service.findByName( "Coffee" );
        final Ingredient i4 = service.findByName( "Chocolate" );

        // Verify contents
        assertEquals( "Coffee", i3.getName() );
        assertEquals( 5, i3.getAmount() );

        assertEquals( "Chocolate", i4.getName() );
        assertEquals( 3, i4.getAmount() );

        // Delete all, there should be no ingredients left
        service.deleteAll();

        Assert.assertEquals( "There should be 0 Ingredients in the CoffeeMaker", 0, service.findAll().size() );

        assertNull( service.findByName( "Coffee" ) );
        assertNull( service.findByName( "Chocolate" ) );

    }

    @Test
    @Transactional
    public void testToString () {
        service.deleteAll();
        // Test creating a blank ingredient
        Ingredient i = new Ingredient();

        // Expecting null ingredient and 0 amount
        assertNull( i.getName() );
        assertEquals( 0, i.getAmount() );

        // Test creating an ingredient with Coffee and 5 amount
        i = new Ingredient( "Coffee", 5 );

        // Expecting IngredientType Coffee and 5 amount
        assertEquals( "Coffee", i.getName() );
        assertEquals( 5, i.getAmount() );

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        service.save( i );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

        // Make sure toString matches
        final Ingredient serviceI = service.findByName( "Coffee" );
        assertEquals( i.toString(), serviceI.toString() );

    }

}
