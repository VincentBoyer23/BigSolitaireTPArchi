/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.control;

import net.onwhat.bigsolitaire.model.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Play action controller. Updates a game given player requests.
 */
public class Player
{
    private Dealer dealer;

    public Player() {
        dealer = new Dealer();
    }

    /**
     * Attempt to pick up cards.
     * @param request
     * @return True if a change occured.
     */
    public boolean grab(Game game, PlayerRequest request) {
        PlayerRequest refined = refine(request, game);
        if (refined != null && game.getGrab() == null) {
            switch(refined.getTableLocation()) {
            case FACE:
            case DONE_1:
            case DONE_2:
            case DONE_3:
            case DONE_4:
                return grabOne(game,refined);
            case UP_1:
            case UP_2:
            case UP_3:
            case UP_4:
            case UP_5:
            case UP_6:
            case UP_7:
                return grabMany(game, refined);
            }
        }
        return false;
    }

    private boolean grabOne(Game game, PlayerRequest request) {
        boolean result = false;
        List<Card> pile = game.getCards().get(request.getTableLocation());
        List<Card> grab = new ArrayList<Card>();
        if ( ! pile.isEmpty() ) {
            grab.add(pile.remove(pile.size()-1));
            game.setGrab(grab);
            game.setGrabSource(request.getTableLocation());
            game.setLastChange(request.getTableLocation());
            result = true;
        }
        return result;
    }

    private boolean grabMany(Game game, PlayerRequest request) {
        boolean result = false;
        List<Card> pile = game.getCards().get(request.getTableLocation());
        List<Card> grab = new ArrayList<Card>();
        if ( ! pile.isEmpty() ) {
            while(pile.size() > request.getIndex()) {
                grab.add(pile.remove(request.getIndex()));
            }
            game.setGrab(grab);
            game.setGrabSource(request.getTableLocation());
            game.setLastChange(request.getTableLocation());
            result = true;
        }
        return result;
    }

    /**
     * Attempt to drop cards.
     * @param request
     * @return True if a change occured.
     */
    public boolean drop(Game game, PlayerRequest request) {
        PlayerRequest refined = refine(request, game);
        boolean changed = false;
        if (game.getGrab() != null) {
            if (refined != null) {
                List<Card> target = game.getCards().get(refined.getTableLocation());
                switch(refined.getTableLocation()) {
                case DONE_1:
                case DONE_2:
                case DONE_3:
                case DONE_4:
                    changed = dropDone(game, refined.getTableLocation(), target); break;
                case UP_1:
                case UP_2:
                case UP_3:
                case UP_4:
                case UP_5:
                case UP_6:
                case UP_7:
                    changed = dropWorking(game, refined.getTableLocation(), target); break;
                case DOWN_1: changed = dropOnEmpty(game, refined.getTableLocation(), target, game.getCards().get(TableLocation.UP_1)); break;
                case DOWN_2: changed = dropOnEmpty(game, refined.getTableLocation(), target, game.getCards().get(TableLocation.UP_2)); break;
                case DOWN_3: changed = dropOnEmpty(game, refined.getTableLocation(), target, game.getCards().get(TableLocation.UP_3)); break;
                case DOWN_4: changed = dropOnEmpty(game, refined.getTableLocation(), target, game.getCards().get(TableLocation.UP_4)); break;
                case DOWN_5: changed = dropOnEmpty(game, refined.getTableLocation(), target, game.getCards().get(TableLocation.UP_5)); break;
                case DOWN_6: changed = dropOnEmpty(game, refined.getTableLocation(), target, game.getCards().get(TableLocation.UP_6)); break;
                case DOWN_7: changed = dropOnEmpty(game, refined.getTableLocation(), target, game.getCards().get(TableLocation.UP_7)); break;
                }
            }

            if ( ! changed ) {
                returnGrab(game);
                changed = true;
            }
        }
        return changed;
    }

    private boolean dropDone(Game game, TableLocation location, List<Card> target) {
        boolean changed = false;
        List<Card> grab = game.getGrab();
        if ( grab != null && grab.size() == 1) {
            Card card = grab.get(0);
            if ( doneMoveOk( card, target ) ) {
                target.add(card);
                game.setGrab(null);
                game.setGrabSource(null);
                game.incMoves();
                game.setLastChange(location);
                changed = true;
            }
        }
        return changed;
    }

    private boolean dropWorking(Game game, TableLocation location, List<Card> target) {
        boolean changed = false;
        List<Card> grab = game.getGrab();
        if ( grab != null ) {
            Card card = grab.get(0);
            if ( workingMoveOk( card, target ) ) {
                target.addAll(grab);
                game.setGrab(null);
                game.setGrabSource(null);
                game.incMoves();
                game.setLastChange(location);
                changed = true;
            }
        }
        return changed;
    }

    private boolean dropOnEmpty(Game game, TableLocation location, List<Card> empty, List<Card> target) {
        boolean changed = false;
        if ( empty.isEmpty() ) {
            changed = dropWorking(game, location, target);
        }
        return changed;
    }

    private boolean returnGrab(Game game) {
        boolean changed = false;
        List<Card> grab = game.getGrab();
        if ( grab != null ) {
            List<Card> pile = game.getCards().get(game.getGrabSource());
            game.setLastChange(game.getGrabSource());
            pile.addAll(grab);
            game.setGrab(null);
            game.setGrabSource(null);
            changed = true;
        }
        return changed;
    }

    /**
     * Deal a new game.
     */
    public void deal(Game game) {
        game.clear();

        // get the deck ready
        List<Card> deck = dealer.getDeck();
        dealer.shuffleDeck(deck);

        // deal the hidden
        for(int i = 0; i < 1; i++) {
            game.getCards().get(TableLocation.DOWN_2).add(deck.remove(0));
        }
        for(int i = 0; i < 2; i++) {
            game.getCards().get(TableLocation.DOWN_3).add(deck.remove(0));
        }
        for(int i = 0; i < 3; i++) {
            game.getCards().get(TableLocation.DOWN_4).add(deck.remove(0));
        }
        for(int i = 0; i < 4; i++) {
            game.getCards().get(TableLocation.DOWN_5).add(deck.remove(0));
        }
        for(int i = 0; i < 5; i++) {
            game.getCards().get(TableLocation.DOWN_6).add(deck.remove(0));
        }
        for(int i = 0; i < 6; i++) {
            game.getCards().get(TableLocation.DOWN_7).add(deck.remove(0));
        }

        // deal the shown
        game.getCards().get(TableLocation.UP_1).add(deck.remove(0));
        game.getCards().get(TableLocation.UP_2).add(deck.remove(0));
        game.getCards().get(TableLocation.UP_3).add(deck.remove(0));
        game.getCards().get(TableLocation.UP_4).add(deck.remove(0));
        game.getCards().get(TableLocation.UP_5).add(deck.remove(0));
        game.getCards().get(TableLocation.UP_6).add(deck.remove(0));
        game.getCards().get(TableLocation.UP_7).add(deck.remove(0));

        // deal the rest to the deck and leave the face empty
        game.getCards().get(TableLocation.DECK).addAll(deck);

        game.setLastChange(null);
    }

    /**
     * Attempt an automatic move.
     * @param request
     * @return True if a change occured.
     */
    public boolean quick(Game game, PlayerRequest request) {
        boolean alreadyChanged = returnGrab(game);
        PlayerRequest refined = refine(request, game);
        boolean changed = false;
        if (refined != null) {
            switch(refined.getTableLocation()) {
            case DECK: changed = deckRequest(game); break;
            case FACE:
            case UP_1:
            case UP_2:
            case UP_3:
            case UP_4:
            case UP_5:
            case UP_6:
            case UP_7:
                changed = autoMove(game,game.getCards().get(refined.getTableLocation())); break;
            case DOWN_1: changed = downRequest(game,game.getCards().get(refined.getTableLocation()),game.getCards().get(TableLocation.UP_1),refined.getTableLocation()); break;
            case DOWN_2: changed = downRequest(game,game.getCards().get(refined.getTableLocation()),game.getCards().get(TableLocation.UP_2),refined.getTableLocation()); break;
            case DOWN_3: changed = downRequest(game,game.getCards().get(refined.getTableLocation()),game.getCards().get(TableLocation.UP_3),refined.getTableLocation()); break;
            case DOWN_4: changed = downRequest(game,game.getCards().get(refined.getTableLocation()),game.getCards().get(TableLocation.UP_4),refined.getTableLocation()); break;
            case DOWN_5: changed = downRequest(game,game.getCards().get(refined.getTableLocation()),game.getCards().get(TableLocation.UP_5),refined.getTableLocation()); break;
            case DOWN_6: changed = downRequest(game,game.getCards().get(refined.getTableLocation()),game.getCards().get(TableLocation.UP_6),refined.getTableLocation()); break;
            case DOWN_7: changed = downRequest(game,game.getCards().get(refined.getTableLocation()),game.getCards().get(TableLocation.UP_7),refined.getTableLocation()); break;
            }
        }
        return changed || alreadyChanged;
    }

    /**
     * Flip a down pile card over onto the up pile if the up is empty and a down card remains.
     * @param game The game cards.model.
     * @param down The down pile.
     * @param up The up pile that corresponds to the down pile.
     * @return True if changed.
     */
    private boolean downRequest(Game game, List<Card> down, List<Card> up, TableLocation location) {
        boolean changed = false;
        if ( up.isEmpty() && ! down.isEmpty() ) {
            game.setLastChange(location);
            changed = true;
            up.add(down.remove(0));
            game.incMoves();
        }
        return changed;
    }

    /**
     * Try to automatically move a card from a pile onto one of the done piles.
     * @param game The game cards.model.
     * @param from The pile to try and move from.
     * @return True if a change was made.
     */
    private boolean autoMove(Game game, List<Card> from) {
        boolean changed = false;
        List<Card> pile1 = game.getCards().get(TableLocation.DONE_1);
        List<Card> pile2 = game.getCards().get(TableLocation.DONE_2);
        List<Card> pile3 = game.getCards().get(TableLocation.DONE_3);
        List<Card> pile4 = game.getCards().get(TableLocation.DONE_4);
        if ( ! from.isEmpty() ) {
            changed = true;
            Card card = from.get(from.size()-1);
            if ( doneMoveOk(card, pile1) ) {
                game.setLastChange(TableLocation.DONE_1);
                pile1.add(card);
            }
            else if ( doneMoveOk(card, pile2) ) {
                game.setLastChange(TableLocation.DONE_2);
                pile2.add(card);
            }
            else if ( doneMoveOk(card, pile3) ) {
                game.setLastChange(TableLocation.DONE_3);
                pile3.add(card);
            }
            else if ( doneMoveOk(card, pile4) ) {
                game.setLastChange(TableLocation.DONE_4);
                pile4.add(card);
            }
            else {
                changed = false;
            }

            if ( changed ) {
                from.remove(from.size()-1);
                game.incMoves();
            }
        }
        return changed;
    }

    /**
     * @return True is the card can be put onto the done pile.
     */
    private boolean doneMoveOk(Card card, List<Card> pile) {
        if ( pile.isEmpty() ) {
            return card.getRank() == 1;
        }
        else {
            Card pileLast = pile.get(pile.size()-1);
            return card.getSuit() == pileLast.getSuit() && card.getRank() == pileLast.getRank()+1;
        }
    }

    /**
     * @return True is the card can be put on the working pile.
     */
    private boolean workingMoveOk(Card card, List<Card> pile) {
        if ( pile.isEmpty() ) {
            return card.getRank() == 13;
        }
        else {
            Card pileLast = pile.get(pile.size()-1);
            return otherColor(card, pileLast) && card.getRank() == pileLast.getRank()-1;
        }
    }

    /**
     * @return True if the two cards suits are not the same color.
     */
    private boolean otherColor(Card a, Card b) {
        if ( a.getSuit() == Suit.DIAMONDS || a.getSuit() == Suit.HEARTS ) {
            return b.getSuit() == Suit.CLUBS || b.getSuit() == Suit.SPADES;
        }
        else {
            return b.getSuit() == Suit.DIAMONDS || b.getSuit() == Suit.HEARTS;
        }
    }

    /**
     * Handle a deck pile request.
     * If deck is empty try and flip the face pile otherwise try and deal to the face pile.
     * @param game The game cards.model.
     * @return True if a change has been made.
     */
    private boolean deckRequest(Game game) {
        boolean changed = false;
        List<Card> pile = game.getCards().get(TableLocation.DECK);
        List<Card> otherPile = game.getCards().get(TableLocation.FACE);
        if ( pile.isEmpty() ) {
            if ( ! otherPile.isEmpty() ) {
                // flip the face pile over
                game.incMoves();
                pile.addAll(otherPile);
                otherPile.clear();
                game.setLastChange(TableLocation.DECK);
                changed = true;
            }
        }
        else {
            game.incMoves();
            for( int i = 0; i < game.getDeal() && ! pile.isEmpty(); i++ ) {
                game.incCardsDealt();
                otherPile.add(pile.remove(0));
            }
            game.setLastChange(TableLocation.DECK);
            changed = true;
        }
        return changed;
    }

    /**
     * Where possible refine the request.
     * Specifically determine UP locations to UP or DOWN and filter for empty locations.
     * @return The refined request.
     */
    private PlayerRequest refine(PlayerRequest request, Game game) {
        PlayerRequest result = request;
        if ( request != null) {
            switch(request.getTableLocation()) {
            case UP_1: result = refine(request, game.getCards().get(TableLocation.UP_1), game.getCards().get(TableLocation.DOWN_1), TableLocation.DOWN_1 ); break;
            case UP_2: result = refine(request, game.getCards().get(TableLocation.UP_2), game.getCards().get(TableLocation.DOWN_2), TableLocation.DOWN_2 ); break;
            case UP_3: result = refine(request, game.getCards().get(TableLocation.UP_3), game.getCards().get(TableLocation.DOWN_3), TableLocation.DOWN_3 ); break;
            case UP_4: result = refine(request, game.getCards().get(TableLocation.UP_4), game.getCards().get(TableLocation.DOWN_4), TableLocation.DOWN_4 ); break;
            case UP_5: result = refine(request, game.getCards().get(TableLocation.UP_5), game.getCards().get(TableLocation.DOWN_5), TableLocation.DOWN_5 ); break;
            case UP_6: result = refine(request, game.getCards().get(TableLocation.UP_6), game.getCards().get(TableLocation.DOWN_6), TableLocation.DOWN_6 ); break;
            case UP_7: result = refine(request, game.getCards().get(TableLocation.UP_7), game.getCards().get(TableLocation.DOWN_7), TableLocation.DOWN_7 ); break;
            }
        }

        return result;
    }

    /**
     * Where possible refine a request on a given pair of up/down piles.
     * @return The refined request.
     */
    private PlayerRequest refine(PlayerRequest request, List<Card> up, List<Card> down, TableLocation downLocation) {
        PlayerRequest result = null;
        if ( up.isEmpty() ) {
            if ( ! down.isEmpty() ) {
                result = new PlayerRequest(downLocation,request.getxOffSet(),request.getyOffSet());
            }
            else {
                result = new PlayerRequest(request.getTableLocation(),request.getxOffSet(),request.getyOffSet());
            }
        }
        else {
            if ( request.getIndex() >= 0 ) {
                if ( request.getIndex() >= up.size()-1 ) {
                    request.setyEdgeOffSet(up.size());
                    result = new PlayerRequest(request.getTableLocation(), up.size()-1, request.getxOffSet(),request.getyOffSet());
                }
                else {
                    request.setyEdgeOffSet(1);
                    result = request;
                }
            }
        }
        return result;
    }


    public Dealer getDealer() {
        return dealer;
    }

}
