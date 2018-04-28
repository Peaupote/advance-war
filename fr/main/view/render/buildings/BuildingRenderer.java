package fr.main.view.render.buildings;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.Airport;
import fr.main.model.buildings.Barrack;
import fr.main.model.buildings.City;
import fr.main.model.buildings.Dock;
import fr.main.model.buildings.Headquarter;
import fr.main.model.buildings.MissileLauncher;
import fr.main.view.render.Renderer;
import fr.main.view.render.animations.Animation;

public class BuildingRenderer{

    protected static HashMap<AbstractBuilding, BuildingRender> renderers = new HashMap<>();

    public static abstract class BuildingRender extends Renderer {
        protected String state;
        protected AbstractBuilding building;
        protected Animation anim;

        public BuildingRender (AbstractBuilding building) {
            this.building = building;
            state         = null;
            anim          = new Animation();
        }

        public void draw (Graphics g, int x, int y) {
            anim.draw(g, x, y);
        }

        public void updateState (String state) {
            this.state = state;
            anim.setState(state);
        }
    }

    public static BuildingRender getRender (AbstractBuilding building) {
        if (renderers.containsKey(building)) return renderers.get(building);

        // TODO: make dynamic
        if      (building instanceof Dock)            renderers.put(building, new DockRenderer(building));
        else if (building instanceof Airport)         renderers.put(building, new AirportRenderer(building));
        else if (building instanceof Barrack)         renderers.put(building, new BarrackRenderer(building));
        else if (building instanceof City)            renderers.put(building, new CityRenderer(building));
        else if (building instanceof Headquarter)     renderers.put(building, new HeadquarterRenderer(building));
        else if (building instanceof MissileLauncher) renderers.put(building, new MissileLauncherRenderer(building));
        return renderers.get(building);
    }

    public static void render(Graphics g, Point coords, AbstractBuilding building) {
    	if(building == null) return;
		if(coords == null) {
			System.err.println("No coords");
			return;
		}
		if (getRender(building) == null) {
			System.err.println("No renderer");
			return;
		}
        getRender(building).draw (g, coords.x, coords.y);
    }

    public static void updateAll(){
        for (BuildingRender b : renderers.values())
            if (! (b instanceof HeadquarterRenderer))
                b.updateState("");
    }
}
