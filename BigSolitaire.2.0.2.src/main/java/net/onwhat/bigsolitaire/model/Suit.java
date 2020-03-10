/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.model;

/**
 * The suits
 */
public enum Suit {
    CLUBS("club"), DIAMONDS("diamond"), HEARTS("heart"), SPADES("spade");

    private String name;

    Suit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
