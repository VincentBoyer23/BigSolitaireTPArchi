/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.view;

import net.onwhat.bigsolitaire.model.*;

import java.util.List;

import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Graphics class used for drawing cards, empty spots and the table.
 */
public class Drawer
{

    private Layout layout;

    private BufferedImage tableBuffer;
    private BufferedImage grabBuffer;
    private SVGImager svgImager;

    public Drawer(Layout layout, Set<Card> deck, String filename) {
        this.layout = layout;
        svgImager = SVGImager.create(deck, layout, filename);
        createBuffers();
        System.gc();
    }

    /**
     * Draw the table.
     *
     * @param graphics2D The drawing target.
     */
    public void drawLocation(Graphics2D graphics2D, TableLocation location, Game game) {
        Map<TableLocation, List<Card>> cards = game.getCards();

        switch (location) {
        case DECK:
        case FACE:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getDeckX(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getFaceX(), layout.getTopY(), layout.getCardWidth()+(layout.getCardEdge()*2), layout.getCardHeight());
            // draw the deck
            if (cards.get(TableLocation.DECK).isEmpty()) {
                drawImage(graphics2D, SVGImager.SPACE_NAME, layout.getDeckX(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
            }
            else {
                drawImage(graphics2D, SVGImager.BACK_NAME, layout.getDeckX(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
            }
            // draw the face
            drawThreePile(graphics2D, cards.get(TableLocation.FACE), layout.getFaceX(), layout.getTopY());
            break;
        case UP_1:
        case DOWN_1:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getWorking1X(), layout.getBottomY(), layout.getCardWidth(), layout.getCardHeight()+(layout.getCardEdge()*15));
            // draw the ups
            drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_1), cards.get(TableLocation.UP_1), layout.getWorking1X(), layout.getBottomY());
            break;
        case UP_2:
        case DOWN_2:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getWorking2X(), layout.getBottomY(), layout.getCardWidth(), layout.getCardHeight()+(layout.getCardEdge()*15));
            // draw the ups
            drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_2), cards.get(TableLocation.UP_2), layout.getWorking2X(), layout.getBottomY());
            break;
        case UP_3:
        case DOWN_3:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getWorking3X(), layout.getBottomY(), layout.getCardWidth(), layout.getCardHeight()+(layout.getCardEdge()*15));
            // draw the ups
            drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_3), cards.get(TableLocation.UP_3), layout.getWorking3X(), layout.getBottomY());
            break;
        case UP_4:
        case DOWN_4:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getWorking4X(), layout.getBottomY(), layout.getCardWidth(), layout.getCardHeight()+(layout.getCardEdge()*15));
            // draw the ups
            drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_4), cards.get(TableLocation.UP_4), layout.getWorking4X(), layout.getBottomY());
            break;
        case UP_5:
        case DOWN_5:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getWorking5X(), layout.getBottomY(), layout.getCardWidth(), layout.getCardHeight()+(layout.getCardEdge()*15));
            // draw the ups
            drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_5), cards.get(TableLocation.UP_5), layout.getWorking5X(), layout.getBottomY());
            break;
        case UP_6:
        case DOWN_6:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getWorking6X(), layout.getBottomY(), layout.getCardWidth(), layout.getCardHeight()+(layout.getCardEdge()*15));
            // draw the ups
            drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_6), cards.get(TableLocation.UP_6), layout.getWorking6X(), layout.getBottomY());
            break;
        case UP_7:
        case DOWN_7:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getWorking7X(), layout.getBottomY(), layout.getCardWidth(), layout.getCardHeight()+(layout.getCardEdge()*15));
            // draw the ups
            drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_7), cards.get(TableLocation.UP_7), layout.getWorking7X(), layout.getBottomY());
            break;
        case DONE_1:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getDone1X(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
            // draw the done
            drawSimplePile(graphics2D, cards.get(TableLocation.DONE_1), layout.getDone1X(), layout.getTopY());
            break;
        case DONE_2:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getDone2X(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
            // draw the done
            drawSimplePile(graphics2D, cards.get(TableLocation.DONE_2), layout.getDone2X(), layout.getTopY());
            break;
        case DONE_3:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getDone3X(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
            // draw the done
            drawSimplePile(graphics2D, cards.get(TableLocation.DONE_3), layout.getDone3X(), layout.getTopY());
            break;
        case DONE_4:
            // draw the table
            drawImage(graphics2D, SVGImager.TABLE_NAME, layout.getDone4X(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
            // draw the done
            drawSimplePile(graphics2D, cards.get(TableLocation.DONE_4), layout.getDone4X(), layout.getTopY());
            break;
        }
    }

    /**
     * Draw the one location on the table.
     */
    public void drawTable(Graphics2D graphics2D, Game game) {
        Map<TableLocation, List<Card>> cards = game.getCards();

        // draw the table
        drawImage(graphics2D, SVGImager.TABLE_NAME, 0, 0, layout.getWidth(), layout.getHeight());

        // draw the deck
        if (cards.get(TableLocation.DECK).isEmpty()) {
            drawImage(graphics2D, SVGImager.SPACE_NAME, layout.getDeckX(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
        }
        else {
            drawImage(graphics2D, SVGImager.BACK_NAME, layout.getDeckX(), layout.getTopY(), layout.getCardWidth(), layout.getCardHeight());
        }

        // draw the face
        drawThreePile(graphics2D, cards.get(TableLocation.FACE), layout.getFaceX(), layout.getTopY());

        // draw the done
        drawSimplePile(graphics2D, cards.get(TableLocation.DONE_1), layout.getDone1X(), layout.getTopY());
        drawSimplePile(graphics2D, cards.get(TableLocation.DONE_2), layout.getDone2X(), layout.getTopY());
        drawSimplePile(graphics2D, cards.get(TableLocation.DONE_3), layout.getDone3X(), layout.getTopY());
        drawSimplePile(graphics2D, cards.get(TableLocation.DONE_4), layout.getDone4X(), layout.getTopY());

        // draw the ups
        drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_1), cards.get(TableLocation.UP_1), layout.getWorking1X(), layout.getBottomY());
        drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_2), cards.get(TableLocation.UP_2), layout.getWorking2X(), layout.getBottomY());
        drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_3), cards.get(TableLocation.UP_3), layout.getWorking3X(), layout.getBottomY());
        drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_4), cards.get(TableLocation.UP_4), layout.getWorking4X(), layout.getBottomY());
        drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_5), cards.get(TableLocation.UP_5), layout.getWorking5X(), layout.getBottomY());
        drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_6), cards.get(TableLocation.UP_6), layout.getWorking6X(), layout.getBottomY());
        drawWorkingPile(graphics2D, cards.get(TableLocation.DOWN_7), cards.get(TableLocation.UP_7), layout.getWorking7X(), layout.getBottomY());
    }


    /**
     * Draw a simple pile of cards.
     */
    private void drawSimplePile(Graphics2D graphics2D, List<Card> pile, int x, int y) {
        if (pile.isEmpty()) {
            drawImage(graphics2D, SVGImager.SPACE_NAME, x, y, layout.getCardWidth(), layout.getCardHeight());
        }
        else {
            Card card = pile.get(pile.size() - 1);
            drawImage(graphics2D, card.getName(), x, y, layout.getCardWidth(), layout.getCardHeight());
        }
    }

    /**
     * Draw a working pile of cards.
     */
    private void drawThreePile(Graphics2D graphics2D, List<Card> pile, int x, int y) {
        if (pile.isEmpty()) {
            drawImage(graphics2D, SVGImager.SPACE_NAME, x, y, layout.getCardWidth(), layout.getCardHeight());
        }
        else {
            int xUp = x;
            if (pile.size() - 3 >= 0) {
                Card card = pile.get(pile.size() - 3);
                drawImage(graphics2D, card.getName(), xUp, y, layout.getCardWidth(), layout.getCardHeight());
                xUp += layout.getCardEdge();
            }
            if (pile.size() - 2 >= 0) {
                Card card = pile.get(pile.size() - 2);
                drawImage(graphics2D, card.getName(), xUp, y, layout.getCardWidth(), layout.getCardHeight());
                xUp += layout.getCardEdge();
            }
            if (pile.size() - 1 >= 0) {
                Card card = pile.get(pile.size() - 1);
                drawImage(graphics2D, card.getName(), xUp, y, layout.getCardWidth(), layout.getCardHeight());
                xUp += layout.getCardEdge();
            }
        }
    }

    /**
     * Draw a working pile of cards.
     */
    private void drawWorkingPile(Graphics2D graphics2D, List<Card> down, List<Card> up, int x, int y) {
        if (down.isEmpty()) {
            drawImage(graphics2D, SVGImager.SPACE_NAME, x, y, layout.getCardWidth(), layout.getCardHeight());
        }
        else {
            int yUp = y;
            float yInt = (float) layout.getCardEdge() / (float) down.size();
            for (Iterator<Card> itr = down.iterator(); itr.hasNext();) {
                itr.next();
                if (itr.hasNext()) {
                    drawImage(graphics2D, SVGImager.BACK_NAME, x, yUp, layout.getCardWidth(), layout.getCardEdge() * 2);
                    yUp += yInt;
                }
                else {
                    drawImage(graphics2D, SVGImager.BACK_NAME, x, yUp, layout.getCardWidth(), layout.getCardHeight());
                }
            }
        }

        if (! up.isEmpty()) {
            int yUp = y + layout.getCardEdge();
            for (Iterator<Card> itr = up.iterator(); itr.hasNext();) {
                Card card = itr.next();
                if (itr.hasNext()) {
                    drawImage(graphics2D, card.getName(), x, yUp, layout.getCardWidth(), layout.getCardEdge() * 2);
                }
                else {
                    drawImage(graphics2D, card.getName(), x, yUp, layout.getCardWidth(), layout.getCardHeight());
                }
                yUp += layout.getCardEdge();
            }
        }

    }

    /**
     * Draw a working pile of cards.
     */
    public void drawWorkingPile(Graphics2D graphics2D, List<Card> pile, int x, int y) {
        if (! pile.isEmpty()) {
            int yUp = y;
            for (Iterator<Card> itr = pile.iterator(); itr.hasNext();) {
                Card card = itr.next();
                if (itr.hasNext()) {
                    drawImage(graphics2D, card.getName(), x, yUp, layout.getCardWidth(), layout.getCardEdge() * 2);
                }
                else {
                    drawImage(graphics2D, card.getName(), x, yUp, layout.getCardWidth(), layout.getCardHeight());
                }
                yUp += layout.getCardEdge();
            }
        }
    }

    public void createBuffers() {
        tableBuffer = new BufferedImage(layout.getWidth(), layout.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        grabBuffer = new BufferedImage(layout.getCardWidth(), layout.getCardHeight() + layout.getCardEdge() * 13, BufferedImage.TYPE_4BYTE_ABGR);
    }

    private void drawImage(Graphics2D graphics2D, String name, int x, int y, int width, int height) {
        svgImager.paint(graphics2D, name, x, y, width, height);
    }

    public void updateBuffers(Game game) {
        Graphics2D tableBufferGraphics2D = tableBuffer.createGraphics();
        if (game.getLastChange() == null) {
            drawTable(tableBufferGraphics2D, game);
        }
        else {
            drawLocation(tableBufferGraphics2D, game.getLastChange(), game);
            game.setLastChange(null);
        }
        if (game.getGrab() != null) {
            Graphics2D grabBufferGraphics2D = grabBuffer.createGraphics();
            grabBufferGraphics2D.setBackground(new Color(0, 0, 0, Color.TRANSLUCENT));
            grabBufferGraphics2D.clearRect(0, 0, grabBuffer.getWidth(), grabBuffer.getHeight());
            drawWorkingPile(grabBufferGraphics2D, game.getGrab(), 0, 0);
        }
    }

    public void drawBuffered(Game game, Graphics2D graphics2D, int x, int y) {
        graphics2D.drawImage(tableBuffer, 0, 0, null);
        if (game.getGrab() != null) {
            graphics2D.drawImage(grabBuffer, x, y, null);
        }
    }
}
