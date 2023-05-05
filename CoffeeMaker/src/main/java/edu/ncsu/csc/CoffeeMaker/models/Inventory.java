package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    // /** amount of coffee */
    // @Min ( 0 )
    // private Integer coffee;
    // /** amount of milk */
    // @Min ( 0 )
    // private Integer milk;
    // /** amount of sugar */
    // @Min ( 0 )
    // private Integer sugar;
    // /** amount of chocolate */
    // @Min ( 0 )
    // private Integer chocolate;

    /** list of ingredients in the inventory */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingInventory;

    /**
     * Constructor for Hibernate, initializes the inventory.
     */
    public Inventory () {
        ingInventory = new ArrayList<Ingredient>();

    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param ingredients
     *            the list of ingredients to set the inventory to
     */
    public Inventory ( final List<Ingredient> ingredients ) {
        ingInventory = ingredients;
    }

    /**
     * Returns the list of Ingredients in the inventory.
     *
     * @return the list of Ingredients in the inventory.
     */
    public List<Ingredient> getIngInventory () {
        return ingInventory;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Finds the ingredient in the inventory by a name
     *
     * @param ingName
     *            name to search for
     * @return ingredient with the given name, null if none
     */
    public Ingredient findIngredientByName ( final String ingName ) {
        for ( int i = 0; i < ingInventory.size(); i++ ) {
            if ( ingInventory.get( i ).getName().equals( ingName ) ) {
                return ingInventory.get( i );
            }
        }
        return null;
    }

    /**
     * Returns the amount of the ingredient in the inventory
     *
     * @param ingName
     *            name to search for
     * @return number of that ingredient
     */
    public Integer getAmountInInventory ( final String ingName ) {
        return findIngredientByName( ingName ).getAmount();
    }

    /**
     * Sets the amount of the ingredient in the inventory
     *
     * @param ingName
     *            name of ingredient to set
     * @param ingAmount
     *            amount to set ingredient to
     */
    public void setAmountInInventory ( final String ingName, final int ingAmount ) {
        findIngredientByName( ingName ).setAmount( ingAmount );
    }

    /**
     * Add the number of ingredient units in the inventory to the current amount
     * of ingredient units.
     *
     * @param ingName
     *            name of ingredient to check
     * @param ingAmount
     *            amount of ingredient to check
     * @return checked amount of ingredient
     * @throws IllegalArgumentException
     *             if the parameter isn't a positive integer
     */
    public int checkAmountInInventory ( final String ingName, final String ingAmount ) throws IllegalArgumentException {
        int amtIngredient = 0;
        try {
            amtIngredient = Integer.parseInt( ingAmount );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of " + ingName + " must be a positive integer" );
        }
        if ( amtIngredient < 0 ) {
            throw new IllegalArgumentException( "Units of " + ingName + " must be a positive integer" );
        }
        return amtIngredient;

    }

    // /**
    // * Returns the current number of chocolate units in the inventory.
    // *
    // * @return amount of chocolate
    // */
    // public Integer getChocolate () {
    // return chocolate;
    // }
    //
    // /**
    // * Sets the number of chocolate units in the inventory to the specified
    // * amount.
    // *
    // * @param amtChocolate
    // * amount of chocolate to set
    // */
    // public void setChocolate ( final Integer amtChocolate ) {
    // if ( amtChocolate >= 0 ) {
    // chocolate = amtChocolate;
    // }
    // }
    //
    // /**
    // * Add the number of chocolate units in the inventory to the current
    // amount
    // * of chocolate units.
    // *
    // * @param chocolate
    // * amount of chocolate
    // * @return checked amount of chocolate
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkChocolate ( final String chocolate ) throws
    // IllegalArgumentException {
    // Integer amtChocolate = 0;
    // try {
    // amtChocolate = Integer.parseInt( chocolate );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of chocolate must be a
    // positive integer" );
    // }
    // if ( amtChocolate < 0 ) {
    // throw new IllegalArgumentException( "Units of chocolate must be a
    // positive integer" );
    // }
    //
    // return amtChocolate;
    // }
    //
    // /**
    // * Returns the current number of coffee units in the inventory.
    // *
    // * @return amount of coffee
    // */
    // public Integer getCoffee () {
    // return coffee;
    // }
    //
    // /**
    // * Sets the number of coffee units in the inventory to the specified
    // amount.
    // *
    // * @param amtCoffee
    // * amount of coffee to set
    // */
    // public void setCoffee ( final Integer amtCoffee ) {
    // if ( amtCoffee >= 0 ) {
    // coffee = amtCoffee;
    // }
    // }
    //
    // /**
    // * Add the number of coffee units in the inventory to the current amount
    // of
    // * coffee units.
    // *
    // * @param coffee
    // * amount of coffee
    // * @return checked amount of coffee
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkCoffee ( final String coffee ) throws
    // IllegalArgumentException {
    // Integer amtCoffee = 0;
    // try {
    // amtCoffee = Integer.parseInt( coffee );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of coffee must be a positive
    // integer" );
    // }
    // if ( amtCoffee < 0 ) {
    // throw new IllegalArgumentException( "Units of coffee must be a positive
    // integer" );
    // }
    //
    // return amtCoffee;
    // }
    //
    // /**
    // * Returns the current number of milk units in the inventory.
    // *
    // * @return int
    // */
    // public Integer getMilk () {
    // return milk;
    // }
    //
    // /**
    // * Sets the number of milk units in the inventory to the specified amount.
    // *
    // * @param amtMilk
    // * amount of milk to set
    // */
    // public void setMilk ( final Integer amtMilk ) {
    // if ( amtMilk >= 0 ) {
    // milk = amtMilk;
    // }
    // }
    //
    // /**
    // * Add the number of milk units in the inventory to the current amount of
    // * milk units.
    // *
    // * @param milk
    // * amount of milk
    // * @return checked amount of milk
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkMilk ( final String milk ) throws
    // IllegalArgumentException {
    // Integer amtMilk = 0;
    // try {
    // amtMilk = Integer.parseInt( milk );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of milk must be a positive
    // integer" );
    // }
    // if ( amtMilk < 0 ) {
    // throw new IllegalArgumentException( "Units of milk must be a positive
    // integer" );
    // }
    //
    // return amtMilk;
    // }
    //
    // /**
    // * Returns the current number of sugar units in the inventory.
    // *
    // * @return int
    // */
    // public Integer getSugar () {
    // return sugar;
    // }
    //
    // /**
    // * Sets the number of sugar units in the inventory to the specified
    // amount.
    // *
    // * @param amtSugar
    // * amount of sugar to set
    // */
    // public void setSugar ( final Integer amtSugar ) {
    // if ( amtSugar >= 0 ) {
    // sugar = amtSugar;
    // }
    // }
    //
    // /**
    // * Add the number of sugar units in the inventory to the current amount of
    // * sugar units.
    // *
    // * @param sugar
    // * amount of sugar
    // * @return checked amount of sugar
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkSugar ( final String sugar ) throws
    // IllegalArgumentException {
    // Integer amtSugar = 0;
    // try {
    // amtSugar = Integer.parseInt( sugar );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of sugar must be a positive
    // integer" );
    // }
    // if ( amtSugar < 0 ) {
    // throw new IllegalArgumentException( "Units of sugar must be a positive
    // integer" );
    // }
    //
    // return amtSugar;
    // }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;
        for ( int i = 0; i < r.getIngredients().size(); i++ ) {
            final Ingredient ing = r.getIngredients().get( i );
            final Ingredient invIng = findIngredientByName( ing.getName() );
            if ( invIng != null && ( invIng.getAmount() < ing.getAmount() ) ) {
                isEnough = false;
                break;
            }
        }
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( int i = 0; i < r.getIngredients().size(); i++ ) {
                final Ingredient ing = r.getIngredients().get( i );
                final Ingredient invIng = findIngredientByName( ing.getName() );
                invIng.setAmount( invIng.getAmount() - ing.getAmount() );
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredients
     *            takes in a list of ingredients to add to inventory
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final List<Ingredient> ingredients ) {

        for ( final Ingredient i : ingredients ) {
            if ( i.getAmount() < 0 ) {
                throw new IllegalArgumentException( "Amount cannot be negative" );
            }

        }
        for ( int i = 0; i < ingredients.size(); i++ ) {
            final Ingredient add = ingredients.get( i );
            final Ingredient ing = findIngredientByName( add.getName() );
            if ( ing == null ) {
                ingInventory.add( add );
            }
            else {
                ing.setAmount( add.getAmount() );
            }

        }
        return true;
    }

    public void addIngredient ( final Ingredient i ) {
        this.ingInventory.add( i );
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient i : ingInventory ) {
            buf.append( i.getName() + ": " );
            buf.append( i.getAmount() + "\n" );
        }
        return buf.toString();
    }

}
