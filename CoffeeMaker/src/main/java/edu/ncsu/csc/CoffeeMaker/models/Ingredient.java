package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredients used in CoffeeMaker. The Ingredients are tied to the database via
 * Hibernate. Check IngredientService and IngredientRepository for database
 * support.
 *
 * @author Christian Andersen
 *
 */
@Entity
public class Ingredient extends DomainObject {

    /** id for the Ingredient */
    @Id
    @GeneratedValue
    private long   id;

    /** name of the Ingredient */
    private String name;

    /** amount of the Ingredient */
    @Min ( 0 )
    private int    amount;

    /**
     * Empty constructor for Hibernate
     */
    public Ingredient () {
    }

    /**
     * Constructs an Ingredient with the given name and amount.
     *
     * @param name
     *            the name of the Ingredient
     * @param amount
     *            the amount of the Ingredient
     */
    public Ingredient ( final String name, final int amount ) {
        super();
        this.name = name;
        this.amount = amount;
    }

    /**
     * Returns the name of the Ingredient.
     *
     * @return the name of the Ingredient
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of the Ingredient.
     *
     * @param name
     *            the name to set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the amount of the Ingredient.
     *
     * @return the amount of the Ingredient.
     */
    public int getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the Ingredient.
     *
     * @param amount
     *            the amount to set
     */
    public void setAmount ( final int amount ) {
        this.amount = amount;
    }

    /**
     * Returns the id of an Ingredient.
     *
     * @return the id of the Ingredient
     */
    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Returns the Ingredient in a formatted String.
     *
     * @return the Ingredient in string format
     */
    @Override
    public String toString () {
        return "Ingredient [ingredient=" + name + ", amount=" + amount + "]";
    }

}
