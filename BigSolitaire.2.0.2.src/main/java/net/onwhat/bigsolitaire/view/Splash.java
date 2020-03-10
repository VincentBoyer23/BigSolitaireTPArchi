/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.view;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Splash Screen.
 * Place holder while loading.
 */
public class Splash extends JFrame
{
	private static final long serialVersionUID = -6435980577216324110L;
	public final static String DEFAULT_ICON_IMAGE_FILE = "icon.jpg";
    public final static String DEFAULT_SPLASH_IMAGE_FILE = "splash.jpg";

    private BufferedImage splashImage = null;
    private BufferedImage iconImage = null;

    public void paint(Graphics g)
    {
        g.drawImage(splashImage,0,0,null);
    }

    public Splash()
    {
        super("Big Solitaire");

        // load the images
        loadImages();

        // setup the window
        int w = splashImage.getWidth(null);
        int h = splashImage.getHeight(null);
        Dimension screenSize = super.getToolkit().getScreenSize();
        int x = (screenSize.width - w) / 2;
        int y = (screenSize.height - h) /2;

        super.setResizable(false);
        super.setUndecorated(true);
        super.setBounds(x,y,w,h);
        super.setIconImage(iconImage);
    }

    private void loadImages()
    {
        splashImage = getImage(DEFAULT_SPLASH_IMAGE_FILE);
        iconImage = getImage(DEFAULT_ICON_IMAGE_FILE);
    }

    public BufferedImage getImage(String name)
    {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(name);
            if ( is == null ) {
                is = new FileInputStream(new File("src/main/resources/" + name));
            }

            BufferedImage result = ImageIO.read(is);
            is.close();
            return result;
        }
        catch (IOException e) {
            throw new Error("failure reading image: " + name, e);
        }
    }

    public Image getIconImage() {
        return iconImage;
    }
}
