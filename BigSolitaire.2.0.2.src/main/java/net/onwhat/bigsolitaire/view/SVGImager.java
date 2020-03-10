/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.view;

import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

import net.onwhat.bigsolitaire.batik.ReplacementParsedURLJarProtocolHandler;
import net.onwhat.bigsolitaire.model.Card;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.net.URL;


/**
 * SVG imager producter.
 */
public class SVGImager
{
    private Map<String, BufferedImage> images;
    public static final String SPACE_NAME = "space";
    public static final String TABLE_NAME = "table";
    public static final String BACK_NAME = "back";

    private SVGImager() {
        ParsedURL.registerHandler(new ReplacementParsedURLJarProtocolHandler());
    }


    public static SVGImager create(Set<Card> deck, Layout layout, String filename) {
        SVGImager imager = new SVGImager();

        imager.load(deck, layout,filename);
        return imager;
    }


    public void paint(Graphics2D graphics2D, String name, int x, int y, int width, int height) {
        BufferedImage image = images.get(name);
        graphics2D.drawImage(image,x,y,x+width,y+height,0,0,width,height,null);
    }

    private void load(Set<Card> deck, Layout layout, String filename) {
        URL url = this.getClass().getClassLoader().getResource(filename);
        try {
            UserAgentAdapter userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            // create the environment and load the document
            Document document = loader.loadDocument(url.toString());

            BridgeContext context = new BridgeContext (userAgent, loader);
            Element svgRoot = document.getDocumentElement();
            GVTBuilder builder = new GVTBuilder();
            builder.build(context, document);   // force the load

            images = new HashMap<String,BufferedImage>(64);
            for( Card card : deck ) {
                images.put(card.getName(),createImage(svgRoot, context, builder,card.getName(),layout.getCardWidth(),layout.getCardHeight()));
            }
            images.put(SVGImager.SPACE_NAME,createImage(svgRoot, context, builder,SVGImager.SPACE_NAME,layout.getCardWidth(),layout.getCardHeight()));
            images.put(SVGImager.BACK_NAME,createImage(svgRoot, context, builder,SVGImager.BACK_NAME,layout.getCardWidth(),layout.getCardHeight()));
            images.put(SVGImager.TABLE_NAME,createImage(svgRoot, context, builder,SVGImager.TABLE_NAME,layout.getWidth(),layout.getHeight()));

            context.dispose();
        }
        catch (Exception e) {
            throw new Error("SVG load of " + filename + " url: " + url.toString(), e);
        }
    }

    private BufferedImage createImage(Element svgRoot, BridgeContext context, GVTBuilder builder, String name, int width, int height) {
        Element namedElement = find(svgRoot,name);
        if (namedElement == null) {
            throw new Error("SVG load could not find element " + name);
        }
        else {
            GraphicsNode node = builder.build(context, namedElement);
            if ( node == null ) {
                throw new Error("could not build node for " + name);
            }
            else {
                BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D graphics2D = image.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                node.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Rectangle2D bound = node.getBounds();
                AffineTransform transform = new AffineTransform();
                transform.scale((float)(width-1)/bound.getWidth(),(float)(height-1)/bound.getHeight());
                node.setTransform(transform);
                node.paint(graphics2D);
                return image;
            }
        }
    }

    private Element find(Element base, String name) {
        Element found = null;
        if ( name != null ) {
            Attr attr = base.getAttributeNode("id");
            if ( attr != null && name.equals(attr.getNodeValue()) ) {
                found = base;
            }
            else {
                NodeList nodes = base.getChildNodes();
                if ( nodes != null ) {
                    for( int i = 0; i < nodes.getLength(); i++ ) {
                        Node node = nodes.item(i);
                        if ( node instanceof Element ) {
                            found = find((Element)node, name);
                            if ( found != null ) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return found;
    }


}
