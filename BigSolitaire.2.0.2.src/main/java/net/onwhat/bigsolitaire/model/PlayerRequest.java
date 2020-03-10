/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.model;

/**
 * A play request by a player.
 */
public class PlayerRequest {
    private TableLocation tableLocation;
    private int index;
    private int xOffSet;
    private int yOffSet;
    private int yEdgeOffSet;

    public PlayerRequest() {
    }

    public PlayerRequest(TableLocation tableLocation, int xOffSet, int yOffSet) {
        this.tableLocation = tableLocation;
        this.xOffSet = xOffSet;
        this.yOffSet = yOffSet;
    }

    public PlayerRequest(TableLocation tableLocation, int index) {
        this.tableLocation = tableLocation;
        this.index = index;
    }

    public PlayerRequest(TableLocation tableLocation) {
        this.tableLocation = tableLocation;
    }

    public PlayerRequest(TableLocation tableLocation, int index, int xOffSet, int yOffSet) {
        this.tableLocation = tableLocation;
        this.index = index;
        this.xOffSet = xOffSet;
        this.yOffSet = yOffSet;
    }


    public TableLocation getTableLocation() {
        return tableLocation;
    }

    public void setTableLocation(TableLocation tableLocation) {
        this.tableLocation = tableLocation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String toString() {
        return "Player Request: " + tableLocation + " " + index;
    }

    public int getxOffSet() {
        return xOffSet;
    }

    public void setxOffSet(int xOffSet) {
        this.xOffSet = xOffSet;
    }

    public int getyOffSet() {
        return yOffSet;
    }

    public void setyOffSet(int yOffSet) {
        this.yOffSet = yOffSet;
    }

    public int getyEdgeOffSet() {
        return yEdgeOffSet;
    }

    public void setyEdgeOffSet(int yEdgeOffSet) {
        this.yEdgeOffSet = yEdgeOffSet;
    }
}
