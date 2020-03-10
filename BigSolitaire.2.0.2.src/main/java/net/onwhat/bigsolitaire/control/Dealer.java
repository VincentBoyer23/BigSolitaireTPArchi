/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.control;

import net.onwhat.bigsolitaire.model.Card;
import net.onwhat.bigsolitaire.model.Suit;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * Logic for handling decks of cards (52 cards without a joker).
 */
public class Dealer
{
    public List<Card> getDeck() {
        List <Card> deck = new ArrayList<Card>();

        // build full deck
        for ( Suit suit: Suit.values() ) {
            for ( int rank = 1; rank <= 13; rank++ ) {
                deck.add( new Card(suit,rank) );
            }
        }

        return deck;
    }

    public void shuffleDeck(List<Card> deck) {
        List<Card> shuffled = new ArrayList<Card>(deck.size());
        Random random = new Random();
        // pick the cards out at random
        while( ! deck.isEmpty() ) {
            int index = random.nextInt(deck.size());
            shuffled.add(deck.remove(index));
        }
        // put them all back
        deck.addAll(shuffled);
    }
}
