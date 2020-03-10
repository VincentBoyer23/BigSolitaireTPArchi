/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */

package net.onwhat.bigsolitaire;

import net.onwhat.bigsolitaire.view.GameFrame;
import net.onwhat.bigsolitaire.view.Splash;

/**
 * The root of the game.
 */
public class Main {
    public static void main(String args[]) {
        Splash splash = new Splash();
        splash.setVisible(true);
        new GameFrame(splash.getIconImage());
        splash.setVisible(false);
        splash.dispose();
    }
}
