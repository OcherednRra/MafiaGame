package io.mafialike.baseclasses;

import io.mafialike.other.Config;

import java.util.ArrayList;

public class Player
{
    String name;
    String role;
    String id;

    ArrayList<Card> clueHand = new ArrayList<>();
    ArrayList<Card> weaponHand = new ArrayList<>();

    public Player(String name, String role, String id)
    {
        this.name = name;
        this.role = role;
        this.id = id;

        setClueHand();
        setWeaponHand();
    }

    public void setClueHand()
    {
        this.clueHand = Card.createClueHand();
    }

    public void setWeaponHand()
    {
        this.weaponHand = Card.createWeaponHand();
    }

    public ArrayList<Card> getClueHand()
    {
        return this.clueHand;
    }

    public ArrayList<Card> getWeaponHand()
    {
        return this.weaponHand;
    }

    public String getName()
    {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }

    public String getRoleDescription() {
        return Config.getDescriptionOf(this.getRole());
    }

    public String getID() {
        return this.id;
    }

    public String toString()
    {
        return "" + this.name + ":" + this.role + ":" + this.id;
    }
}