/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Solitarie game cards.model.
 */
public class Game {

	public final static int DEAL_DEFAULT = 3;

    /**
     * Cards dealt in this game.
     */
    private int cardsDealt;
    /**
     * The number of cards to deal from the deck.
     */
    private int deal;
    /**
     * The number of moves made in this game.
     */
    private int moves;
    /**
     * Cards on the table.
     */
    private Map<TableLocation,List<Card>> cards;
    /**
     * Grabbed cards
     */
    private List<Card> grab;
    /**
     * Grabed from.
     */
    private TableLocation grabSource;

    /**
     * Last change..
     */
    private TableLocation lastChange;

    public Game() {
        deal = DEAL_DEFAULT;
        moves = 0;
        cardsDealt = 0;

        // create the list for all the card locations
        this.cards = new HashMap<TableLocation,List<Card>>();
        for( TableLocation location : TableLocation.values() ) {
            this.cards.put(location,new ArrayList<Card>());
        }
    }


    public Map<TableLocation, List<Card>> getCards() {
        return cards;
    }

    public int getCardsDealt() {
        return cardsDealt;
    }

    public void setCardsDealt(int cardsDealt) {
        this.cardsDealt = cardsDealt;
    }

    public int getDeal() {
        return deal;
    }

    public void setDeal(int deal) {
        this.deal = deal;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void clear() {
        cardsDealt = 0;
        moves = 0;

        for( TableLocation location : TableLocation.values() ) {
            cards.get(location).clear();
        }
    }

    public void incMoves() {
        moves++;
    }


    public void incCardsDealt() {
        cardsDealt++;
    }

    public List<Card> getGrab() {
        return grab;
    }

    public void setGrab(List<Card> grab) {
        this.grab = grab;
    }

    public TableLocation getGrabSource() {
        return grabSource;
    }

    public void setGrabSource(TableLocation grabSource) {
        this.grabSource = grabSource;
    }

    public TableLocation getLastChange() {
        return lastChange;
    }

    public void setLastChange(TableLocation lastChange) {
        this.lastChange = lastChange;
    }
}
