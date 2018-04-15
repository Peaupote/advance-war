package fr.main.view.render.buildings;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;

import java.awt.Graphics;
import java.awt.Color;

public abstract class OwnableBuildingRenderer extends BuildingRenderer.BuildingRender {

    private final OwnableBuilding oBuilding;

    public OwnableBuildingRenderer (AbstractBuilding building) {
        super(building);
        oBuilding = (OwnableBuilding)building;
    }

    public void updateState(){
        String s;
        Color c = ((OwnableBuilding)building).isNeutral() ? Color.WHITE : ((OwnableBuilding)building).getOwner().getColor();
        if      (c.equals(Color.RED))    s = "red";
        else if (c.equals(Color.BLUE))   s = "blue";
        else if (c.equals(Color.GREEN))  s = "green";
        else if (c.equals(Color.YELLOW)) s = "yellow";
        else                             s = "white";
        anim.setState(s);
    }

    @Override
    public void draw(Graphics g, int x, int y){
        super.draw(g, x, y);
        g.setColor(Color.BLACK);
        g.drawString(oBuilding.getLife() + "", x, y);
    }

    public void updateState(String s){
        updateState();
    }
}