package fr.main.model;

import java.util.Random;

public enum Weather implements java.io.Serializable{

    SUNNY(0,"Ensoleillé",false,1,0),
    FOGGY(1,"Brumeux",true,1,0),
    RAINY(2,"Pluvieux",true,1,1),
    SNOWY(3,"Neigeux",true,2,2);

    public final int num,malusFuel,malusVision;
    public final String name;
    public final boolean fog;

    private Weather(int num, String name, boolean fog, int malusFuel, int malusVision){
        this.num         = num;
        this.name        = name;
        this.fog         = fog;
        this.malusFuel   = malusFuel;
        this.malusVision = malusVision;
    }

    public static Weather random(boolean fog){
        if (fog) return random(0,90,5,5);
        return random(90,0,5,5);
    }

    static Weather random(int sun, int fog, int rain, int snow){
        int sum     = sun+fog+rain+snow;
        Random rand = new Random();

        int res = rand.nextInt(sum);
        if (res < sun) return SUNNY;
        res -= sun;

        if (res < fog) return FOGGY;
        res -= fog;
        
        if (res < rain) return RAINY;
        
        return SNOWY;
    }

}
