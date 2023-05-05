package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests. Need delete, put, get
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void ensureIngredient () throws Exception {
        service.deleteAll();

        /* Test saving an Ingredient */

        final Ingredient i = new Ingredient( "Coffee", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {

        service.deleteAll();

        /* Test that the service is counting Ingredients */

        final Ingredient i = new Ingredient( "Coffee", 5 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        Assert.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddIngredient1 () throws Exception {

        service.deleteAll();

        /*
         * Tests a ingredient with a duplicate name to make sure it's rejected
         */

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient r1 = new Ingredient( name, 5 );

        service.save( r1 );

        final Ingredient r2 = new Ingredient( name, 5 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assert.assertEquals( "There should only one ingredient in the CoffeeMaker", 1, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testGetIngredients1 () throws Exception {
        service.deleteAll();

        /* Test the get all API call */

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient r1 = new Ingredient( name, 5 );

        service.save( r1 );

        final String ing = mvc.perform( get( "/api/v1/ingredients" ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( ing.contains( "\"name\":\"Coffee\"" ) );
        assertTrue( ing.contains( "\"amount\":5" ) );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

    }

    @Test
    @Transactional
    public void testGetIngredients2 () throws Exception {
        service.deleteAll();

        /* Test the get {name} API call */

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient r1 = new Ingredient( name, 5 );

        service.save( r1 );

        final String ing = mvc.perform( get( "/api/v1/ingredients/" + name ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( ing.contains( "\"name\":\"Coffee\"" ) );
        assertTrue( ing.contains( "\"amount\":5" ) );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

    }

    @Test
    @Transactional
    public void testUpdateIngredient1 () throws Exception {
        service.deleteAll();

        /* Test updating an ingredient */

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient r1 = new Ingredient( name, 5 );

        service.save( r1 );

        /* Verify the added ingredient */
        String ing = mvc.perform( get( "/api/v1/ingredients/" + name ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( ing.contains( "\"name\":\"Coffee\"" ) );
        assertTrue( ing.contains( "\"amount\":5" ) );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

        /* Edit the ingredient */
        r1.setAmount( 3 );

        /* Verify the updated ingredient */
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) );

        ing = mvc.perform( get( "/api/v1/ingredients/" + name ) ).andExpect( status().isOk() ).andReturn().getResponse()
                .getContentAsString();

        assertTrue( ing.contains( "\"name\":\"Coffee\"" ) );
        assertTrue( ing.contains( "\"amount\":3" ) );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );

    }

    @Test
    @Transactional
    public void testGetIngredients3 () throws Exception {
        service.deleteAll();

        /* Test getting an ingredient that doesn't exist */

        mvc.perform( get( "/api/v1/ingredients/Coffee" ) ).andExpect( status().is4xxClientError() );

        /* Make sure everything else works */
        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient r1 = new Ingredient( name, 5 );

        service.save( r1 );

        final String ing = mvc.perform( get( "/api/v1/ingredients/" + name ) ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( ing.contains( "\"name\":\"Coffee\"" ) );
        assertTrue( ing.contains( "\"amount\":5" ) );

    }

    @Test
    @Transactional
    public void testDeleteIngredient () throws Exception {
        service.deleteAll();

        /* Test deleting the ingredients */

        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Ingredient r1 = new Ingredient( name, 5 );

        /* Nothing to delete */
        mvc.perform( delete( "/api/v1/ingredients/" + name ) ).andExpect( status().is4xxClientError() );

        /* Add and delete the added ingredient */
        service.save( r1 );
        Assert.assertEquals( "There should be 1 Ingredient in the CoffeeMaker", 1, service.findAll().size() );
        mvc.perform( delete( "/api/v1/ingredients/" + name ) ).andExpect( status().isOk() );

        /* Assert no ingredients */
        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );

        /* Try deleting nothing again */
        mvc.perform( get( "/api/v1/ingredients/" + name ) ).andExpect( status().is4xxClientError() );
        Assert.assertEquals( "There should be no Ingredients in the CoffeeMaker", 0, service.findAll().size() );

    }

}
