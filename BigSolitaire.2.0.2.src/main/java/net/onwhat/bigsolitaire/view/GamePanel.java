/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.view;

import javax.swing.*;

import net.onwhat.bigsolitaire.control.*;
import net.onwhat.bigsolitaire.model.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;


/**
 * Panel that displays the cards.view of the games.
 */
public class GamePanel extends JPanel {

	private static final long serialVersionUID = -3936491834458987994L;

	private static final String FILENAME = "cards.svg";

	private Game game;
	private Player player;
	private Layout layout;
	private Drawer drawer;
    private JLabel statusLabel;
    private int grabX;
    private int grabY;

    public GamePanel(Dimension size, JLabel statusLabel) {
        this.statusLabel = statusLabel;
        this.player = new Player();
		this.game = new Game();
        this.layout = new Layout((int)size.getWidth(),(int)size.getHeight());
        this.drawer = new Drawer(layout, new HashSet<Card>(player.getDealer().getDeck()), FILENAME);
        deal();

        addMouseListener(new GameMouseAdapter());
        addMouseMotionListener(new GameMouseMotionAdapter());
        setOpaque(true);
        setDoubleBuffered(false);
    }

	public void deal() {
		player.deal(game);
        updateDrawer();
        repaint();
        updateStatus();
    }

    public void updateStatus() {
        statusLabel.setText("Dealt: " + game.getCardsDealt() + " Moves: " + game.getMoves());
        if (gameOver()) {
            if ( JOptionPane.showConfirmDialog(this, "Deal a new game?", "Game Over", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) {
                deal();
            }
        }
    }

    private boolean gameOver() {
        for(TableLocation location : TableLocation.values() ) {
            if ( location != TableLocation.DONE_1 && location != TableLocation.DONE_2 && location != TableLocation.DONE_3 && location != TableLocation.DONE_4 ) {
                if (! game.getCards().get(location).isEmpty() ) {
                    return false;
                }
            }
        }
        return game.getGrab() == null;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D)g;
        drawer.drawBuffered(game,graphics2D,grabX-layout.getCardWidth()/2,grabY-layout.getCardHeight()/2);
    }

    private class GameMouseAdapter extends MouseAdapter {
        private PlayerRequest last;

        public void mousePressed(MouseEvent e) {
            boolean changed;
            grabX = e.getX();
            grabY = e.getY();
            PlayerRequest request = createRequest(e);
            last = request;
            changed = player.grab(game,request);
            if ( changed ) {
                updateDrawer();
                repaint();
            }
            updateStatus();
        }

        public void mouseReleased(MouseEvent e) {
            PlayerRequest request = createRequest(e);
            if ( request != null && last != null && request.getTableLocation() == last.getTableLocation()) {
                player.quick(game,request);
            }
            else {
                player.drop(game,request);
            }
            last = null;
            updateDrawer();
            repaint();
            updateStatus();
        }

        private PlayerRequest createRequest(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            return layout.request(x,y);
        }
    }

    private class GameMouseMotionAdapter extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if ( game.getGrab() != null && game.getGrab().size() > 0) {
                grabX = e.getX();
                grabY = e.getY();
                repaint();
            }
        }
    }

    private void updateDrawer() {
        drawer.updateBuffers(game);
    }

}
