package fr.main.model.units;

public enum UnitType {
    LAND("Terrestre"),
    TANK("Tank"),
    VEHICLE("Vehicule"),
    NAVAL("Navale"),
    INFANTRY("Infantrie"),
    AIR("Aérienne"),
    PLANE("Avion"),
    HELICOPTER("Hélicoptère"),
    BOAT("Bâteau"),
    SUBMARINE("Sous-marin"),
    TRANSPORT("Transport");

    public final String typeName;

    UnitType(String typeName) {
        this.typeName = typeName;
    }
}
