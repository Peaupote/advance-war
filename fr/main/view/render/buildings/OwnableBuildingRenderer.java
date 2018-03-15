package fr.main.view.render.buildings;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;
import java.awt.Color;

public abstract class OwnableBuildingRenderer extends BuildingRenderer.BuildingRender {

    public OwnableBuildingRenderer (AbstractBuilding building) {
        super(building);
    }

    public void updateState(){
        String s;
        Color c = ((OwnableBuilding)building).getOwner() == null ? null : ((OwnableBuilding)building).getOwner().getColor();
        if (c.equals(Color.RED)) s = "red";
        else if (c.equals(Color.BLUE)) s = "blue";
        else if (c.equals(Color.GREEN)) s = "green";
        else if (c.equals(Color.YELLOW)) s = "yellow";
        else s = "white";
        anim.setState(s);
    }

    public void updateState(String s){
        updateState();
    }
}