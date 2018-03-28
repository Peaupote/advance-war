package fr.main.view.render.units;

import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;
import fr.main.model.units.HideableUnit;
import fr.main.model.units.weapons.*;
import fr.main.model.units.land.*;
import fr.main.model.units.air.*;
import fr.main.model.units.naval.*;

import fr.main.view.MainFrame;
import fr.main.view.interfaces.TerrainPanel;
import fr.main.view.render.Renderer;
import fr.main.view.render.units.air.*;
import fr.main.view.render.units.land.*;
import fr.main.view.render.units.naval.*;
import fr.main.view.render.animations.*;

public class UnitRenderer {

  protected static HashMap<AbstractUnit, Render> renderers = new HashMap<>();
  protected static final HashMap<Class<? extends AbstractUnit>, Function<AbstractUnit,? extends Render>> constructors;

  static{
    constructors = new HashMap<Class<? extends AbstractUnit>, Function<AbstractUnit, ? extends Render>>();
    constructors.put(BCopter.class    , BCopterRenderer::new);
    constructors.put(Bomber.class     , BomberRenderer::new);
    constructors.put(Fighter.class    , FighterRenderer::new);
    constructors.put(Stealth.class    , StealthRenderer::new);
    constructors.put(TCopter.class    , TCopterRenderer::new);
    constructors.put(AntiAir.class    , AntiAirRenderer::new);
    constructors.put(APC.class        , APCRenderer::new);
    constructors.put(Artillery.class  , ArtilleryRenderer::new);
    constructors.put(Infantry.class   , InfantryRenderer::new);
    constructors.put(Mech.class       , MechRenderer::new);
    constructors.put(Megatank.class   , MegatankRenderer::new);
    constructors.put(Missiles.class   , MissilesRenderer::new);
    constructors.put(MTank.class      , MTankRenderer::new);
    constructors.put(Neotank.class    , NeotankRenderer::new);
    constructors.put(Recon.class      , ReconRenderer::new);
    constructors.put(Rockets.class    , RocketsRenderer::new);
    constructors.put(Tank.class       , TankRenderer::new);
    constructors.put(Battleship.class , BattleshipRenderer::new);
    constructors.put(BlackBoat.class  , BlackBoatRenderer::new);
    constructors.put(Carrier.class    , CarrierRenderer::new);
    constructors.put(Cruiser.class    , CruiserRenderer::new);
    constructors.put(Lander.class     , LanderRenderer::new);
    constructors.put(Sub.class        , SubRenderer::new);
  }

  public static abstract class Render extends Renderer {

    protected Point offset;
    protected String state;
    protected Direction orientation;
    protected AbstractUnit unit;
    protected Animation anim;

    public Render (AbstractUnit unit) {
      this.unit        = unit;
      this.offset      = new Point(0, 0);
      this.orientation = Direction.RIGHT;
      this.state       = "idleRIGHT";
      this.anim        = new Animation();
    }

    public boolean moveOffset (Direction d){
      return moveOffset(d, true);
    }

    public void cancelOffset (){
      offset.move(0,0);
    }

    public boolean moveOffset (Direction d, boolean change) {
      d.move(offset);
      if (change && (Math.abs(offset.x) >= MainFrame.UNIT || Math.abs(offset.y) >= MainFrame.UNIT)){
        cancelOffset();
        return unit.move(d);
      }
      return true;
    }

    protected final String getDir () {
      String color;
      Color c = unit.getPlayer().getColor();
      if (c.equals(Color.red))         color = "red";
      else if (c.equals(Color.blue))   color = "blue";
      else if (c.equals(Color.green))  color = "green";
      else if (c.equals(Color.yellow)) color = "yellow";
      else throw new RuntimeException("Invalid color");
      return "./assets/" + color+ "/";
    }

    public void draw (Graphics g, int x, int y) {
      anim.draw(g, x + offset.x, y + offset.y);
      if (MainFrame.getTimer() % 50 <= 15)
        return;

      if (unit.getLife() < 34)
        g.drawImage(TerrainPanel.lifeImage, x + offset.x, y + offset.y, null);

      if (unit.getFuel().getQuantity() <= unit.getFuel().maximumQuantity / 2)
        g.drawImage(TerrainPanel.fuelImage, x + offset.x, y + offset.y + MainFrame.UNIT / 2, null);

      PrimaryWeapon pr = unit.getPrimaryWeapon();
      if (pr != null && pr.getAmmunition() <= pr.maximumAmmo / 2)
        g.drawImage(TerrainPanel.munitionsImage, x + offset.x + MainFrame.UNIT / 2, y + offset.y, null);

      if (unit instanceof HideableUnit && ((HideableUnit)unit).hidden())
        g.drawImage(TerrainPanel.moveImage, x + offset.x + MainFrame.UNIT / 2, y + offset.y + MainFrame.UNIT / 2, null);
    }

    public void setState (String state) {
      this.state = state;
      updateAnim();
    }

    public void setOrientation (Direction d) {
      this.orientation = d;
      updateAnim();
    }

    private void updateAnim () {
      anim.setState(state + orientation.toString());
    }
  }

  public static Render getRender (AbstractUnit unit) {
    if (renderers.containsKey(unit)) return renderers.get(unit);
    
    for (Map.Entry<Class<? extends AbstractUnit>, Function<AbstractUnit,? extends Render>> entry : constructors.entrySet())
      if (entry.getKey().isInstance(unit)){
        renderers.put(unit,entry.getValue().apply(unit));
        break;
      }

    return renderers.get(unit);
  }

  public static void render(Graphics g, Point coords, AbstractUnit unit) {
    getRender(unit).draw (g, coords.x, coords.y);
  }

}

