/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.control;

import junit.framework.TestCase;
import net.onwhat.bigsolitaire.control.Dealer;
import net.onwhat.bigsolitaire.model.Card;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Dealer unit testing.
 */
public class DealerTest extends TestCase {


    public void testDeal() throws Exception {
        Dealer dealer = new Dealer();
        List<Card> deckList = dealer.getDeck();
        Set<Card> deckSet = new HashSet<Card>();
        deckSet.addAll(deckList);
        assertEquals("deck list", 52,deckList.size());
        assertEquals("deck set", 52,deckSet.size());
    }

    public void testShuffle() throws Exception {
        Dealer dealer = new Dealer();
        List<Card> deckList = dealer.getDeck();
        dealer.shuffleDeck(deckList);
        Set<Card> deckSet = new HashSet<Card>();
        deckSet.addAll(deckList);
        assertEquals("deck list", 52,deckList.size());
        assertEquals("deck set", 52,deckSet.size());
    }

}
