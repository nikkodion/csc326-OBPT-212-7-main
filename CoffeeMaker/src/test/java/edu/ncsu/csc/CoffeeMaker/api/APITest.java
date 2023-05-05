package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe r = createRecipe( "Mocha", 10, 5, 5, 5, 5 );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        Assert.assertTrue( recipe.contains( "Mocha" ) );

        final Inventory i = new Inventory();
        final List<Ingredient> ilist = createIngredients( 50, 50, 50, 50 );
        i.addIngredients( ilist );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() ).andDo( print() );

    }

    @Test
    @Transactional
    public void testRemoveRecipe1 () throws Exception {

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue( !recipe.contains( "Mocha" ) );

        final Recipe r = createRecipe( "Mocha", 10, 5, 3, 4, 8 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        Assert.assertTrue( recipe.contains( "Mocha" ) );

        /* Test removing a recipe */
        mvc.perform( delete( "/api/v1/recipes/Mocha" ) ).andExpect( status().isOk() );

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        Assert.assertFalse( recipe.contains( "Mocha" ) );

    }

    @Test
    @Transactional
    public void testRemoveRecipe2 () throws Exception {

        /* Test removing a nonexistent recipe */

        mvc.perform( delete( "/api/v1/recipes/Mocha" ) ).andExpect( status().is4xxClientError() );
    }

    @Test
    @Transactional
    public void testCreateRecipe1 () throws Exception {

        /* Create 5 recipes to test same name and limit bounds */
        final Recipe r1 = createRecipe( "Mocha", 10, 1, 1, 1, 1 );

        final Recipe r2 = createRecipe( "Mocha", 20, 2, 2, 2, 2 );

        final Recipe r3 = createRecipe( "Coffee", 30, 3, 3, 3, 3 );

        final Recipe r4 = createRecipe( "Latte", 40, 4, 4, 4, 4 );

        final Recipe r5 = createRecipe( "Chicken", 50, 5, 5, 5, 5 );

        /* Make sure the recipes list is completely blank */
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        assertTrue( !recipe.contains( "Mocha" ) );
        assertTrue( !recipe.contains( "Coffee" ) );
        assertTrue( !recipe.contains( "Latte" ) );
        assertTrue( !recipe.contains( "Chicken" ) );

        /* Add recipes, 2nd is dupe, 5th is above 3 recipe limit */
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r5 ) ) ).andExpect( status().isInsufficientStorage() );

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        /* Make sure Mocha and its -unique- fields are present */
        assertTrue( recipe.contains( "Mocha" ) );
        assertTrue( recipe.contains( "chocolate\",\"amount\":1" ) );
        assertTrue( recipe.contains( "coffee\",\"amount\":1" ) );
        assertTrue( recipe.contains( "milk\",\"amount\":1" ) );
        assertTrue( recipe.contains( "sugar\",\"amount\":1" ) );
        assertTrue( recipe.contains( "\"price\":10" ) );

        /* Make sure the duplicate Mocha values and is not present */
        assertFalse( recipe.contains( "chocolate\",\"amount\":2" ) );
        assertFalse( recipe.contains( "coffee\",\"amount\":2" ) );
        assertFalse( recipe.contains( "milk\",\"amount\":2" ) );
        assertFalse( recipe.contains( "sugar\",\"amount\":2" ) );
        assertFalse( recipe.contains( "\"price\":20" ) );

        /* Make sure Coffee and its -unique- fields are present */
        assertTrue( recipe.contains( "Coffee" ) );
        assertTrue( recipe.contains( "chocolate\",\"amount\":3" ) );
        assertTrue( recipe.contains( "coffee\",\"amount\":3" ) );
        assertTrue( recipe.contains( "milk\",\"amount\":3" ) );
        assertTrue( recipe.contains( "sugar\",\"amount\":3" ) );
        assertTrue( recipe.contains( "\"price\":30" ) );

        /* Make sure Latte and its -unique- fields are present */
        assertTrue( recipe.contains( "Latte" ) );
        assertTrue( recipe.contains( "chocolate\",\"amount\":4" ) );
        assertTrue( recipe.contains( "coffee\",\"amount\":4" ) );
        assertTrue( recipe.contains( "milk\",\"amount\":4" ) );
        assertTrue( recipe.contains( "sugar\",\"amount\":4" ) );
        assertTrue( recipe.contains( "\"price\":40" ) );

        /* Make sure Chicken and its -unique- fields are not present */
        assertTrue( !recipe.contains( "Chicken" ) );
        assertFalse( recipe.contains( "chocolate\",\"amount\":5" ) );
        assertFalse( recipe.contains( "coffee\",\"amount\":5" ) );
        assertFalse( recipe.contains( "milk\",\"amount\":5" ) );
        assertFalse( recipe.contains( "sugar\",\"amount\":5" ) );
        assertFalse( recipe.contains( "\"price\":50" ) );
    }

    @Test
    @Transactional
    public void testGetRecipe1 () throws Exception {
        /* Test getting a recipe that does not exist yet */
        mvc.perform( get( "/api/v1/recipes/Chicken" ) ).andExpect( status().is4xxClientError() );

        /* Create a new recipe, add to the list */
        final Recipe r5 = createRecipe( "Chicken", 50, 5, 5, 5, 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r5 ) ) ).andExpect( status().isOk() );

        /* Check to make sure the recipe is actually there */
        final String recipe = mvc.perform( get( "/api/v1/recipes/Chicken" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        /* Make sure Chicken and its -unique- fields are present */
        assertTrue( recipe.contains( "chocolate\",\"amount\":5" ) );
        assertTrue( recipe.contains( "coffee\",\"amount\":5" ) );
        assertTrue( recipe.contains( "milk\",\"amount\":5" ) );
        assertTrue( recipe.contains( "sugar\",\"amount\":5" ) );
        assertTrue( recipe.contains( "\"price\":50" ) );
    }

    @Test
    @Transactional
    public void testGetInventory1 () throws Exception {

        /* Create a recipe to test inventory usage */
        final Recipe r5 = createRecipe( "Chicken", 50, 5, 5, 5, 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r5 ) ) ).andExpect( status().isOk() );

        /* Create and set a new inventory */

        final List<Ingredient> ilist = createIngredients( 50, 50, 50, 50 );
        final Inventory i = new Inventory( ilist );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        /* Confirm inventory contents */
        String inv = mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        assertTrue( inv.contains( "\"chocolate\",\"amount\":50" ) );
        assertTrue( inv.contains( "\"coffee\",\"amount\":50" ) );
        assertTrue( inv.contains( "\"milk\",\"amount\":50" ) );
        assertTrue( inv.contains( "\"sugar\",\"amount\":50" ) );

        /* Make coffee, should use some inventory */
        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Chicken" ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( 100 ) ) )
                .andExpect( status().isOk() );

        /* Confirm inventory contents again */
        inv = mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        assertTrue( inv.contains( "\"chocolate\",\"amount\":45" ) );
        assertTrue( inv.contains( "\"coffee\",\"amount\":45" ) );
        assertTrue( inv.contains( "\"milk\",\"amount\":45" ) );
        assertTrue( inv.contains( "\"sugar\",\"amount\":45" ) );

        /* Make coffee, but with insufficient money */
        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Chicken" ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( 49 ) ) )
                .andExpect( status().is4xxClientError() );

        /* Confirm inventory does not change from errors */
        inv = mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        assertTrue( inv.contains( "\"chocolate\",\"amount\":45" ) );
        assertTrue( inv.contains( "\"coffee\",\"amount\":45" ) );
        assertTrue( inv.contains( "\"milk\",\"amount\":45" ) );
        assertTrue( inv.contains( "\"sugar\",\"amount\":45" ) );

        final Ingredient testIngredient = new Ingredient( "testIngredient", 500 );

        mvc.perform( post( String.format( "/api/v1/inventory" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( testIngredient ) ) ).andExpect( status().isOk() );

        mvc.perform( post( String.format( "/api/v1/inventory" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( testIngredient ) ) ).andExpect( status().is( 409 ) );
    }

    // Allows fast creation of a recipe for tests
    private Recipe createRecipe ( final String name, final int price, final int choc, final int coffee, final int milk,
            final int sugar ) {
        final Recipe r = new Recipe();

        r.setName( name );
        r.setPrice( price );

        final Ingredient i1 = new Ingredient( "chocolate", choc );
        final Ingredient i2 = new Ingredient( "coffee", coffee );
        final Ingredient i3 = new Ingredient( "milk", milk );
        final Ingredient i4 = new Ingredient( "sugar", sugar );

        r.addIngredient( i1 );
        r.addIngredient( i2 );
        r.addIngredient( i3 );
        r.addIngredient( i4 );

        return r;
    }

    // Allows fast population of ingredients for tests
    private List<Ingredient> createIngredients ( final int choc, final int coffee, final int milk, final int sugar ) {
        final List<Ingredient> list = new ArrayList<Ingredient>();
        list.add( new Ingredient( "chocolate", choc ) );
        list.add( new Ingredient( "coffee", coffee ) );
        list.add( new Ingredient( "milk", milk ) );
        list.add( new Ingredient( "sugar", sugar ) );

        return list;
    }

}
