/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The container of the games.
 */
public class GameFrame extends JFrame {
	
	private static final long serialVersionUID = 518201887823791884L;

	private GamePanel gamePanel;

    public GameFrame(Image icon) {
        super("Big Solitaire");
        setIconImage(icon);
        Dimension screenSize = super.getToolkit().getScreenSize();

        // handle window actions
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

        // create controllers
        // create empty game

        // create menu bar and menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
		menu.getAccessibleContext().setAccessibleDescription("Game controls and options");
		menuBar.add(menu);
        setJMenuBar(menuBar);

		// new game item
		JMenuItem menuItem = new JMenuItem("New",KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Deal a new game");
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                deal();
			}
		});

		// exit item
		menuItem = new JMenuItem("Exit",KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Exit the application");
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});

		// create the content panel
		JPanel contentPanel = new JPanel(new BorderLayout());
		setContentPane(contentPanel);

		// create the status panel
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.setBorder(BorderFactory.createEtchedBorder());
		JLabel statusLabel = new JLabel("status");
		statusLabel.setBorder(BorderFactory.createEmptyBorder(2,2,3,3));
		statusPanel.add(statusLabel, BorderLayout.WEST);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);

        // create game panel
        gamePanel = new GamePanel(screenSize, statusLabel);
        contentPanel.add(gamePanel, BorderLayout.CENTER);

		// set the size to maximum
        setSize(300,300);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setResizable(false);
    }


    public void deal() {
        gamePanel.deal();
    }

    public void exit() {
		System.exit(0);
	}
}
