package fr.main.view.interfaces;

import java.awt.*;

import fr.main.view.MainFrame;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.players.Player;
import fr.main.model.units.Unit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.terrains.AbstractTerrain;

import fr.main.view.Position;
import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.buildings.BuildingRenderer;
import fr.main.view.render.sprites.Sprite;
import fr.main.view.render.units.UnitRenderer;

/**
 * User interface showing informations about the terrain,
 * unit or building under the cursor.
 */
public class TerrainPanel extends InterfaceUI {

    static final Color BACKGROUNDCOLOR = new Color(255,146,0,190);
    static final Color FOREGROUNDCOLOR = Color.white;
    static final int WIDTH = 100, HEIGHT = 200, MARGIN = 10;

    /**
     * Unit icons.
     */
    public static final Image lifeImage, ammoImage, fuelImage, visionImage, buildingImage, starImage;

    static{
        Sprite sp = Sprite.get("./assets/ingame/things.png");

        lifeImage     = sp.getImage(75,  1, 11,  9, 1.5);
        fuelImage     = sp.getImage(62,  0, 11, 13, 1.5);
        visionImage   = sp.getImage(75, 16, 30, 14, 1);
        buildingImage = sp.getImage( 1, 29, 11, 12, 2);
        starImage     = sp.getImage( 1, 92,  8,  8, 2);
        ammoImage     = UnitRenderer.Render.ammoImage;
    }

    boolean leftSide;
    int x, y;

    protected final Position.Cursor cursor;
    protected final Position.Camera camera;
    protected final Universe world;

    public TerrainPanel (Position.Cursor cursor, Position.Camera camera) {
        this.cursor = cursor;
        this.camera = camera;
        world = Universe.get();
    }

    @Override
    protected void draw (Graphics g) {
        int halfw = MainFrame.width() / (2 * MainFrame.UNIT),
                halfh = MainFrame.height() / (2 * MainFrame.UNIT);
        leftSide = cursor.getX() - camera.getX() >= halfw && cursor.getY() - camera.getY() >= halfh; 
        x = leftSide ? MARGIN : MainFrame.width() - WIDTH - MARGIN;
        y = MainFrame.height() - HEIGHT - MARGIN;
        
        g.setColor (BACKGROUNDCOLOR);
        g.fillRect (x, y, WIDTH, HEIGHT);

        AbstractTerrain terrain   = world.getTerrain(cursor.getX(), cursor.getY());
        AbstractUnit unit         = world.getUnit(cursor.getX(), cursor.getY());
        AbstractBuilding building = world.getBuilding(cursor.getX(), cursor.getY());

        //image displayed : terrain or building
        Point img = new Point(x + 30, y + 20);
        if (building == null) TerrainRenderer.render (g, img, cursor.position()); // display the building
        else                  BuildingRenderer.render(g, img, building);          // display the terrain

        g.setColor (FOREGROUNDCOLOR);

        // terrain name
        String name = world.getTerrain(cursor.getX(), cursor.getY()).toString();
        g.drawString (name, x + 30 + MainFrame.UNIT / 2 - g.getFontMetrics().stringWidth(name) / 2, y + 70);

        // terrain defense
        int defense = terrain.getDefense(null) + (building == null ? 0 : building.getDefense(null));
        int beginX  = x + WIDTH / 2 - defense * 17 / 2;
        for (int i = 0; i < defense; i ++)
            g.drawImage(starImage, beginX + i * 17, y + 72, null);

        // unit
        if (unit != null &&
                (unit.getPlayer() == world.getCurrentPlayer() ||
                 world.isVisibleOpponentUnit(cursor.getX(), cursor.getY()))) {

            // unit's name
            g.drawString(unit.getName(), x + 15, y + 100);
            // unit's owner
            g.drawString(unit.getPlayer().name, x + 15, y + 120);

            // unit's life
            g.drawImage(lifeImage, x + 15, y + 130, null);
            g.drawString(unit.getLife() + "", x + 30, y + 140);

            // unit's vision
            g.drawImage(visionImage, x + 60, y + 130, null);
            g.drawString(unit.getVision() + "", x + 75, y + 135);

            // unit's move quantity
            g.drawImage(UnitRenderer.Render.getMoveImage(unit), x + 15, y + 153, null);
            g.drawString(unit.getMoveQuantity() + "/" + unit.getMaxMoveQuantity(), x + 38, y + 158);

            // unit's weapon's ammo
            PrimaryWeapon p = unit.getPrimaryWeapon();
            if (p != null){
                g.drawImage(ammoImage, x + 60, y + 153, null);
                g.drawString(p.getAmmunition() + "/" + p.maximumAmmo, x + 75, y + 158);
            }

            // unit's fuel
            Unit.Fuel fuel = unit.getFuel();
            g.drawImage(fuelImage, x + 15, y + 170, null);
            g.drawString(fuel.getQuantity() + "/" + fuel.maximumQuantity, x + 35, y + 185);

        } else if (building != null) { // building

            // building name
            g.drawString(building.getName(), x + 15, y + 100);
            if (building instanceof OwnableBuilding) {
                Player p = ((OwnableBuilding)building).getOwner();
                // building's owner
                g.drawString(p == null ? "Neutre" : p.name, x + 15, y + 120); // owner

                g.drawImage(buildingImage, x + 15, y + 130, null);
                // building's life
                g.drawString(((OwnableBuilding)building).getLife() + "", x + 45, y + 145);
            }

        } else { // there is nothing particular on the tile
            g.drawString ("No Unit", x + 15, y + 100);
            g.drawString ("No Building", x + 15, y + 120);
        }
    }
}

