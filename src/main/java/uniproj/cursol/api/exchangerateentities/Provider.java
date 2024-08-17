package uniproj.cursol.api.exchangerateentities;

import java.util.ArrayList;

public class Provider {
    public int id;
    public String alias;
    public String name;
    public Logos logos;
    public String type;
    public boolean partner;
    public ArrayList<Quote> quotes;
    public String logo;
}