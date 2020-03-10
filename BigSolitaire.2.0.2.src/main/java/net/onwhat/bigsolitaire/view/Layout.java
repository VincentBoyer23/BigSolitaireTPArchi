/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.view;

import java.util.*;

import net.onwhat.bigsolitaire.model.PlayerRequest;
import net.onwhat.bigsolitaire.model.TableLocation;

import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;

/**
 * Details of the game layout and sizes.
 *
 *   Table layout details
 *       across top
 *           -C-ccC+C-C-C-C-
 *       across bottem
 *           +C+C+C+C+C+C+C+
 *       down
 *           -
 *           C
 *           +
 *           c13
 *           C
 *           -
 *   where
 *       - is a gap
 *       + is a big gap
 *       C is a full card
 *       c is the edge of a card
 *   and differ in values based on the axis.
 */
public class Layout {

    // accessable layout points
    private int height;
    private int width;
    private int deckX;
    private int faceX;
    private int done1X;
    private int done2X;
    private int done3X;
    private int done4X;
    private int working1X;
    private int working2X;
    private int working3X;
    private int working4X;
    private int working5X;
    private int working6X;
    private int working7X;
    private int topY;
    private int bottomY;

    private int cardHeight;
    private int cardEdge;
    private int cardWidth;

	private float factor;
    private int edgeGapWidth;
    private int edgeGapHeight;

    private static final float WIDTH_FULL_CARD = 5f;
    private static final float WIDTH_GAP = 0.3f;
    private static final float WIDTH_CENTRE = WIDTH_GAP * 6 + WIDTH_FULL_CARD * 7;
    private static final float WIDTH = WIDTH_GAP * 2 + WIDTH_CENTRE;

    private static final float HEIGHT_FULL_CARD = 8f;
    private static final float EDGE_CARD = 1.5f;
    private static final float HEIGHT_GAP = WIDTH_GAP;
    private static final float HEIGHT_BIG_GAP = 0.3f;
    private static final float HEIGHT_CENTRE = HEIGHT_BIG_GAP + HEIGHT_FULL_CARD + (EDGE_CARD * 17);
    private static final float HEIGHT = HEIGHT_GAP * 2 + HEIGHT_CENTRE;

	private Set<Rectangle2D.Float> intersections;
	private Map<Rectangle2D.Float,TableLocation> directIntersectionMap;
	private Map<Rectangle2D.Float, TableLocation> workingIntersectionMap;

    public Layout(int width, int height) {
		// create the intersection map
		createIntersections();

        // check resize assert
        if ( width < WIDTH || height < HEIGHT) {
            throw new Error("resize to small: " + width + "x" + height);
        }
        // set the w and h
        this.width = width;
        this.height = height;

        // calculate the new factor
        float hFactor = height / HEIGHT;
        float wFactor = width / WIDTH;
        if ( hFactor < wFactor ) {
            factor = hFactor;
        }
        else {
            factor = wFactor;
        }

        // calculate the gaps and spacing
        final int widthSpacer = (int)(factor * (WIDTH_FULL_CARD + WIDTH_GAP));
        edgeGapWidth = (width - (int)(factor * WIDTH_CENTRE))/2;
        edgeGapHeight = (height - (int)(factor * HEIGHT_CENTRE))/2;
        final int bigGapHeight = (int)(factor * HEIGHT_BIG_GAP);

        // calculate the points
        cardWidth = (int)(factor * WIDTH_FULL_CARD);
        deckX = edgeGapWidth;
        faceX = deckX + widthSpacer;

        working1X = edgeGapWidth;
        working2X = working1X + widthSpacer;
        working3X = working2X + widthSpacer;
        done1X = working4X = working3X + widthSpacer;
        done2X = working5X = working4X + widthSpacer;
        done3X = working6X = working5X + widthSpacer;
        done4X = working7X = working6X + widthSpacer;

        cardHeight = (int)(factor * HEIGHT_FULL_CARD);
        cardEdge = (int)(factor * EDGE_CARD);
        topY = edgeGapHeight;
        bottomY = edgeGapHeight + bigGapHeight + cardHeight;
    }


    public PlayerRequest request(int x, int y) {
		PlayerRequest result = null;

		// remove the edgegaps
		Rectangle notEdge = new Rectangle(edgeGapWidth,edgeGapHeight,width-edgeGapWidth*2,height-edgeGapHeight*2);
		if ( notEdge.contains(x,y) ) {
			// convert with factor
			float xFactored = (float)(x - edgeGapWidth)/factor;
			float yFactored = (float)(y - edgeGapHeight)/factor;

			// find intersections
			Rectangle2D.Float found = null;
			for (Rectangle2D.Float area : intersections ) {
				if ( area.contains(xFactored,yFactored) ) {
                    if ( found == null ) {
                        found = area;
                    }
                    else {
                        return null; // if more than one its is a miss
                    }
                }
			}

            if ( found == null ) {
                return null; // if none found
            }

            int xOffset = x - edgeGapWidth - (int)(found.getX()*factor);
            int yOffset = y - edgeGapHeight - (int)(found.getY()*factor);

            if ( directIntersectionMap.containsKey(found) ) {
                TableLocation location = directIntersectionMap.get(found);
                result = new PlayerRequest(location,xOffset,yOffset);
            }
            else {
                TableLocation location = workingIntersectionMap.get(found);
        		int index = workingIndex(found, yFactored);
                result = new PlayerRequest(location,index,xOffset,yOffset);
            }
		}

        return result;
    }

    public PlayerRequest request(Rectangle rectangle) {
		PlayerRequest result = null;

		// remove the edgegaps
		Rectangle notEdge = new Rectangle(edgeGapWidth,edgeGapHeight,width-edgeGapWidth*2,height-edgeGapHeight*2);
		if ( notEdge.intersects(rectangle) ) {
			// convert with factor
			float xFactored = (float)(rectangle.getX() - edgeGapWidth)/factor;
			float yFactored = (float)(rectangle.getY() - edgeGapHeight)/factor;
            float wFactored = (float)rectangle.getWidth()/factor;
            float hFactored = (float)rectangle.getHeight()/factor;

            // find intersections
			Rectangle2D.Float found = null;
			for (Rectangle2D.Float area : intersections ) {
				if ( area.intersects(xFactored, yFactored,wFactored,hFactored) ) {
                    if ( found == null ) {
                        found = area;
                    }
                    else {
                        return null; // if more than one its is a miss
                    }
                }
			}

            if ( found == null ) {
                return null; // if none found
            }

            if ( directIntersectionMap.containsKey(found) ) {
                TableLocation location = directIntersectionMap.get(found);
                result = new PlayerRequest(location);
            }
            else {
                TableLocation location = workingIntersectionMap.get(found);
        		int index = workingIndex(found, yFactored);
                result = new PlayerRequest(location,index);
            }
		}

        return result;
    }

    private int workingIndex(Rectangle2D.Float area, float y) {
        if ( y < area.getY() || y > area.getY() + area.getHeight() ) {
            throw new Error("working min is out of area");
        }
        else {
            float offset = y - (float)area.getY();
            return (int)(offset/EDGE_CARD) - 1;
        }
    }

    private void createIntersections() {
		intersections = new HashSet<Rectangle2D.Float>();
		directIntersectionMap = new HashMap<Rectangle2D.Float,TableLocation>();
		workingIntersectionMap = new HashMap<Rectangle2D.Float,TableLocation>();

        // calculate the gaps and spacing
        final float widthSpacer = WIDTH_FULL_CARD + WIDTH_GAP;
        final float tmpDeckX = 0;
        final float tmpFaceX = tmpDeckX + widthSpacer;
        final float tmpWorking1X = 0;
        final float tmpWorking2X = tmpWorking1X + widthSpacer;
        final float tmpWorking3X = tmpWorking2X + widthSpacer;
        final float tmpWorking4X = tmpWorking3X + widthSpacer;
        final float tmpWorking5X = tmpWorking4X + widthSpacer;
        final float tmpWorking6X = tmpWorking5X + widthSpacer;
        final float tmpWorking7X = tmpWorking6X + widthSpacer;
        final float tmpDone1X = tmpWorking4X;
        final float tmpDone2X = tmpWorking5X;
        final float tmpDone3X = tmpWorking6X;
        final float tmpDone4X = tmpWorking7X;
        final float tmpTopY = 0;
        final float tmpBottomY = HEIGHT_BIG_GAP + HEIGHT_FULL_CARD;

		Rectangle2D.Float working;

		// deck
		working = new Rectangle2D.Float(tmpDeckX,tmpTopY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD);
		intersections.add(working);
		directIntersectionMap.put(working,TableLocation.DECK);

		// face
		working = new Rectangle2D.Float(tmpFaceX,tmpTopY,WIDTH_FULL_CARD+EDGE_CARD *3,HEIGHT_FULL_CARD);
		intersections.add(working);
		directIntersectionMap.put(working,TableLocation.FACE);

		// done 1
		working = new Rectangle2D.Float(tmpDone1X,tmpTopY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD);
		intersections.add(working);
		directIntersectionMap.put(working,TableLocation.DONE_1);
		// done 2
		working = new Rectangle2D.Float(tmpDone2X,tmpTopY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD);
		intersections.add(working);
		directIntersectionMap.put(working,TableLocation.DONE_2);
		// done 3
		working = new Rectangle2D.Float(tmpDone3X,tmpTopY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD);
		intersections.add(working);
		directIntersectionMap.put(working,TableLocation.DONE_3);
		// done 3
		working = new Rectangle2D.Float(tmpDone4X,tmpTopY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD);
		intersections.add(working);
		directIntersectionMap.put(working,TableLocation.DONE_4);

		// working 1
		working = new Rectangle2D.Float(tmpWorking1X,tmpBottomY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD + EDGE_CARD * 13);
		intersections.add(working);
		workingIntersectionMap.put(working,TableLocation.UP_1);
		// working 2
		working = new Rectangle2D.Float(tmpWorking2X,tmpBottomY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD + EDGE_CARD * 13);
		intersections.add(working);
		workingIntersectionMap.put(working,TableLocation.UP_2);
		// working 3
		working = new Rectangle2D.Float(tmpWorking3X,tmpBottomY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD + EDGE_CARD * 13);
		intersections.add(working);
		workingIntersectionMap.put(working,TableLocation.UP_3);
		// working 4
		working = new Rectangle2D.Float(tmpWorking4X,tmpBottomY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD + EDGE_CARD * 13);
		intersections.add(working);
		workingIntersectionMap.put(working,TableLocation.UP_4);
		// working 5
		working = new Rectangle2D.Float(tmpWorking5X,tmpBottomY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD + EDGE_CARD * 13);
		intersections.add(working);
		workingIntersectionMap.put(working,TableLocation.UP_5);
		// working 6
		working = new Rectangle2D.Float(tmpWorking6X,tmpBottomY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD + EDGE_CARD * 13);
		intersections.add(working);
		workingIntersectionMap.put(working,TableLocation.UP_6);
		// working 7
		working = new Rectangle2D.Float(tmpWorking7X,tmpBottomY,WIDTH_FULL_CARD,HEIGHT_FULL_CARD + EDGE_CARD * 13);
		intersections.add(working);
		workingIntersectionMap.put(working,TableLocation.UP_7);
	}

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public int getDeckX() {
        return deckX;
    }

    public int getFaceX() {
        return faceX;
    }

    public int getDone1X() {
        return done1X;
    }

    public int getDone2X() {
        return done2X;
    }

    public int getDone3X() {
        return done3X;
    }

    public int getDone4X() {
        return done4X;
    }

    public int getWorking1X() {
        return working1X;
    }

    public int getWorking2X() {
        return working2X;
    }

    public int getWorking3X() {
        return working3X;
    }

    public int getWorking4X() {
        return working4X;
    }

    public int getWorking5X() {
        return working5X;
    }

    public int getWorking6X() {
        return working6X;
    }

    public int getWorking7X() {
        return working7X;
    }

    public int getCardHeight() {
        return cardHeight;
    }

    public int getCardEdge() {
        return cardEdge;
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }
}
