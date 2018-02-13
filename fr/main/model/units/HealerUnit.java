package fr.main.model.units;

public interface HealerUnit<T extends AbstractUnit> extends AbstractUnit {

    public boolean canHeal(AbstractUnit u);
    public void heal(T u);

}