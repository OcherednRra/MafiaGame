package io.mafialike.baseclasses;

import java.util.ArrayList;

public class Player
{
    String name;
    String role;
    String id;
    ArrayList<Card> clueHand = new ArrayList<>();
    ArrayList<Card> weaponHand = new ArrayList<>();

    Player(String name, String role, String id)
    {
        this.name = name;
        this.role = role;
        this.id = id;
        for (int i = 0; i < 4; i++)
        {
            this.clueHand.add(Card.getRandomClueCard());
            this.weaponHand.add(Card.getRandomWeaponCard());
        }
    }

    public Player createPlayer(String name, String role, String id)
    {
        return new Player(name, role, id);
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

    public String toString()
    {
        return "\nИгрок " + this.name + " - " + this.role + ".\n" +
                "Clue: " + this.clueHand.toString() + "\n" +
                "Weapon: " + this.weaponHand.toString() + "\n";
    }
}