package fr.main.view.render.units;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import fr.main.model.Direction;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.HideableUnit;
import fr.main.model.units.air.BCopter;
import fr.main.model.units.air.Bomber;
import fr.main.model.units.air.Fighter;
import fr.main.model.units.air.Stealth;
import fr.main.model.units.air.TCopter;
import fr.main.model.units.land.APC;
import fr.main.model.units.land.AntiAir;
import fr.main.model.units.land.Artillery;
import fr.main.model.units.land.Infantry;
import fr.main.model.units.land.MTank;
import fr.main.model.units.land.Mech;
import fr.main.model.units.land.Megatank;
import fr.main.model.units.land.Missiles;
import fr.main.model.units.land.Neotank;
import fr.main.model.units.land.Recon;
import fr.main.model.units.land.Rockets;
import fr.main.model.units.land.Tank;
import fr.main.model.units.naval.Battleship;
import fr.main.model.units.naval.BlackBoat;
import fr.main.model.units.naval.Carrier;
import fr.main.model.units.naval.Cruiser;
import fr.main.model.units.naval.Lander;
import fr.main.model.units.naval.Sub;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.view.MainFrame;
import fr.main.view.interfaces.TerrainPanel;
import fr.main.view.render.Renderer;
import fr.main.view.render.animations.Animation;
import fr.main.view.render.sprites.Sprite;
import fr.main.view.render.units.air.BCopterRenderer;
import fr.main.view.render.units.air.BomberRenderer;
import fr.main.view.render.units.air.FighterRenderer;
import fr.main.view.render.units.air.StealthRenderer;
import fr.main.view.render.units.air.TCopterRenderer;
import fr.main.view.render.units.land.APCRenderer;
import fr.main.view.render.units.land.AntiAirRenderer;
import fr.main.view.render.units.land.ArtilleryRenderer;
import fr.main.view.render.units.land.InfantryRenderer;
import fr.main.view.render.units.land.MTankRenderer;
import fr.main.view.render.units.land.MechRenderer;
import fr.main.view.render.units.land.MegatankRenderer;
import fr.main.view.render.units.land.MissilesRenderer;
import fr.main.view.render.units.land.NeotankRenderer;
import fr.main.view.render.units.land.ReconRenderer;
import fr.main.view.render.units.land.RocketsRenderer;
import fr.main.view.render.units.land.TankRenderer;
import fr.main.view.render.units.naval.BattleshipRenderer;
import fr.main.view.render.units.naval.BlackBoatRenderer;
import fr.main.view.render.units.naval.CarrierRenderer;
import fr.main.view.render.units.naval.CruiserRenderer;
import fr.main.view.render.units.naval.LanderRenderer;
import fr.main.view.render.units.naval.SubRenderer;
import fr.main.view.sound.MusicEngine;

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

        public static final Image lifeImage, ammoImage, lockImage, fuelImage,
                                  inftry, mech, tires, tread, ships, trans, air;

        static{
            Sprite sp = Sprite.get("./assets/ingame/things.png");

            lifeImage = sp.getImage(1, 44, 7, 6, 2);
            ammoImage = sp.getImage(1, 64, 7, 5, 2);
            lockImage = sp.getImage(0, 69, 8, 8, 2);
            fuelImage = sp.getImage(1, 53, 7, 8, 2);

            inftry = sp.getImage(10, 48, 32, 15);
            mech   = sp.getImage(44, 55, 32, 15);
            tires  = sp.getImage(10, 64, 31, 15);
            tread  = sp.getImage(49, 71, 31, 15);
            ships  = sp.getImage(10, 80, 31, 15);
            trans  = sp.getImage(49, 89, 32, 15);
            air    = sp.getImage( 9, 95, 31, 15);
        }

        /**
         * @param u the unit considered
         * @return the image associated to the unit's move type
         */
        public static Image getMoveImage(AbstractUnit u){
            switch (u.getMoveType()){
                case AIRY     : return air;
                case NAVAL    : return ships;
                case LANDER   : return trans;
                case WHEEL    : return tires;
                case TREAD    : return tread;
                case INFANTRY : return inftry;
                case MECH     : return mech;
            }
            return null;
        }

        protected Point offset;
        protected String state;
        protected Direction orientation;
        protected final AbstractUnit unit;
        protected Animation anim;
        protected MusicEngine death, selected, attack;

        public Render (AbstractUnit unit) {
            this.unit        = unit;
            this.offset      = new Point(0, 0);
            this.orientation = Direction.RIGHT;
            this.state       = "idleRIGHT";
            this.anim        = new Animation();
            this.death        = new MusicEngine("./assets/sound/screaming.wav");
            this.selected    = new MusicEngine("./assets/sound/song069.wav");
            this.attack      = new MusicEngine("./assets/sound/song025.wav");
        }

        public final void deathSound (){
            death.playOneTime();
        }

        public final void selectedSound (){
            selected.playOneTime();
        }

        public final void attackSound (){
            attack.playOneTime();
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
            
            // draw a heart to show that the unit's life is low
            if (unit.getLife() < 34)
                g.drawImage(TerrainPanel.lifeImage, x + offset.x, y + offset.y, null);

            // draw a jerrycan to show that the unit's fuel is low
            if (unit.getFuel().getQuantity() <= unit.getFuel().maximumQuantity / 2)
                g.drawImage(TerrainPanel.fuelImage, x + offset.x, y + offset.y + MainFrame.UNIT / 2, null);

            // draw a bullet to show that the unit's ammo is low
            PrimaryWeapon pr = unit.getPrimaryWeapon();
            if (pr != null && pr.getAmmunition() <= pr.maximumAmmo / 2)
                g.drawImage(TerrainPanel.ammoImage, x + offset.x + MainFrame.UNIT / 2, y + offset.y, null);

            // draw a lock to show that the unit is hidden
            if (unit instanceof HideableUnit && ((HideableUnit)unit).hidden())
                g.drawImage(lockImage, x + offset.x + MainFrame.UNIT / 2, y + offset.y + MainFrame.UNIT / 2, null);
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
        if (constructors.containsKey(unit.getClass())){
            renderers.put(unit, constructors.get(unit.getClass()).apply(unit));
            return renderers.get(unit);
        }

        for (Map.Entry<Class<? extends AbstractUnit>, Function<AbstractUnit,? extends Render>> entry : constructors.entrySet())
            if (entry.getKey().isInstance(unit)){
                renderers.put(unit,entry.getValue().apply(unit));
                break;
            }

        return renderers.get(unit);
    }

    public static void remove (AbstractUnit u){
        remove(renderers.get(u));
    }

    @SuppressWarnings("unlikely-arg-type")
	public static void remove(Render r){
        renderers.remove(r);
    }

    public static void render(Graphics g, Point coords, AbstractUnit unit) {
        getRender(unit).draw (g, coords.x, coords.y);
    }

}

