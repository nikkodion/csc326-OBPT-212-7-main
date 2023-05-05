package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {
    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testRecipes () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.setPrice( 350 );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );

        recipeService.save( r );

        List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );
        Recipe dbRecipe = dbRecipes.get( 0 );
        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        assertEquals( r.findIngredientByName( "Coffee" ).getAmount(),
                dbRecipe.findIngredientByName( "Coffee" ).getAmount() );
        assertEquals( r.findIngredientByName( "Sugar" ).getAmount(),
                dbRecipe.findIngredientByName( "Sugar" ).getAmount() );
        assertEquals( r.findIngredientByName( "Milk" ).getAmount(),
                dbRecipe.findIngredientByName( "Milk" ).getAmount() );
        assertEquals( r.findIngredientByName( "Chocolate" ).getAmount(),
                dbRecipe.findIngredientByName( "Chocolate" ).getAmount() );

        dbRecipe = recipeService.findByName( "Mocha" );
        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        /**
         * assertEquals( r.getCoffee(), dbRecipe.getCoffee() ); assertEquals(
         * r.getSugar(), dbRecipe.getSugar() ); assertEquals( r.getMilk(),
         * dbRecipe.getMilk() ); assertEquals( r.getChocolate(),
         * dbRecipe.getChocolate() );
         */

        dbRecipe.setPrice( 15 );
        // dbRecipe.setSugar( 12 );
        recipeService.save( dbRecipe );

        dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );
        final Recipe dbRecipeEdited = dbRecipes.get( 0 );
        assertEquals( dbRecipe.getName(), dbRecipeEdited.getName() );
        assertEquals( dbRecipe.getPrice(), dbRecipeEdited.getPrice() );

        assertEquals( r.findIngredientByName( "Coffee" ).getAmount(),
                dbRecipe.findIngredientByName( "Coffee" ).getAmount() );
        assertEquals( r.findIngredientByName( "Sugar" ).getAmount(),
                dbRecipe.findIngredientByName( "Sugar" ).getAmount() );
        assertEquals( r.findIngredientByName( "Milk" ).getAmount(),
                dbRecipe.findIngredientByName( "Milk" ).getAmount() );
        assertEquals( r.findIngredientByName( "Chocolate" ).getAmount(),
                dbRecipe.findIngredientByName( "Chocolate" ).getAmount() );
    }

    @Test
    @Transactional
    public void testDeleteRecipes () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.setPrice( 100 );
        r.addIngredient( new Ingredient( "Coffee", 1 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );

        recipeService.save( r );

        List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );
        final Recipe dbRecipe = dbRecipes.get( 0 );
        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        /**
         * assertEquals( r.getCoffee(), dbRecipe.getCoffee() ); assertEquals(
         * r.getSugar(), dbRecipe.getSugar() ); assertEquals( r.getMilk(),
         * dbRecipe.getMilk() ); assertEquals( r.getChocolate(),
         * dbRecipe.getChocolate() );
         */

        assertEquals( 1, recipeService.count() );
        recipeService.delete( dbRecipe );
        dbRecipes = recipeService.findAll();
        assertEquals( 0, dbRecipes.size() );
        assertEquals( 0, recipeService.count() );
    }

    @Test
    @Transactional
    public void testRecipeIDs () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.setPrice( 100 );
        r.addIngredient( new Ingredient( "Coffee", 1 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );

        recipeService.save( r );

        recipeService.findByName( "Mocha" ).equals( recipeService.findById( r.getId() ) );
        assertTrue( recipeService.existsById( r.getId() ) );
        recipeService.delete( r );
        assertFalse( recipeService.existsById( r.getId() ) );
        assertNull( recipeService.findById( r.getId() ) );
        assertNull( recipeService.findByName( r.getName() ) );
    }

    @Test
    @Transactional
    public void testLists () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.setPrice( 100 );
        r.addIngredient( new Ingredient( "Coffee", 1 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final List<Recipe> recipeList = new ArrayList<Recipe>();
        recipeList.add( r );
        assertTrue( recipeList.contains( r ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha2" );
        r2.setPrice( 200 );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );

        recipeList.add( r2 );
        assertEquals( 2, recipeList.size() );
        assertTrue( recipeList.contains( r2 ) );
        recipeService.saveAll( recipeList );
        final List<Recipe> recipeServiceList = recipeService.findAll();
        assertEquals( recipeServiceList, recipeList );
    }
}
