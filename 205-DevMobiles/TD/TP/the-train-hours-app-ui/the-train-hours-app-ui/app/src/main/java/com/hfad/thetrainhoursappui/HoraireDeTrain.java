package com.hfad.thetrainhoursappui;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoraireDeTrain {
    private String villeDepart;
    private String villeArrive;
    private String horaire;

    public static final HoraireDeTrain[] horaireDeTrain =  {
            new HoraireDeTrain("Montpellier", "Marseille", "8:05" ),
            new HoraireDeTrain("Montpellier", "Lyon", "8:19" ),
            new HoraireDeTrain("Montpellier", "Marseille", "9:45" ),
            new HoraireDeTrain("Montpellier", "Marseille", "11:09" ),
            new HoraireDeTrain("Montpellier", "Marseille", "13:08" ),
            new HoraireDeTrain("Montpellier", "Marseille", "17:05" ),
    };

    public HoraireDeTrain(String villeDepart, String villeArrive, String horaire) {
        this.villeDepart = villeDepart;
        this.villeArrive = villeArrive;
        this.horaire = horaire;
    }

    public String getVilleDepart(){
        return villeDepart;
    }

    public String getVilleArrive() {
        return villeArrive;
    }

    public String getHoraire(){
        return horaire;
    }

    public String toString(){
        return this.villeDepart + ":" + this.villeArrive + ":" + this.horaire;
    }
}