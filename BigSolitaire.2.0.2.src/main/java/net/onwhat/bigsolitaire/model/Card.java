/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.model;

/**
 * Card cards.model. Holds the type of card and the its state.
 */
public class Card {
    private Suit suit;
    private int rank;
    private String name;

    public Card(Suit suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        this.name = suit.getName() + rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Card card = (Card) o;

        if (rank != card.rank) return false;
        return suit == card.suit;
    }

    public int hashCode() {
        int result;
        result = (suit != null ? suit.hashCode() : 0);
        result = 29 * result + rank;
        return result;
    }

    public String getName() {
		return name;
	}
}
