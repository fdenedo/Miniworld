package org.miniworld.miniworld.gameassets;

import org.miniworld.miniworld.view.Polygon;

public class Building {
    Polygon base;

    public Building(Polygon base) {
        this.base = base;
    }

    public Polygon getBase() {
        return base;
    }
}
