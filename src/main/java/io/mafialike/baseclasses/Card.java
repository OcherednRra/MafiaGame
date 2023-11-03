package io.mafialike.baseclasses;

import java.util.Objects;
import java.util.Random;

public class Card
{
    String title;
    String type;
    static Random random = new Random();

    Card(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public static Card getRandomClueCard()
    {
        Card card;
        do
        {
            card = new Card(random.nextInt(DeceptionGame.getClueCardsNumber()) + 1 + ".png", "clue");
        }
        while (DeceptionGame.getInGameCardsList().contains(card));
        DeceptionGame.getInGameCardsList().add(card);
        return card;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getType()
    {
        return this.type;
    }

    public static Card getRandomWeaponCard()
    {
        Card card;
        do
        {
            card = new Card(random.nextInt(DeceptionGame.getWeaponCardsNumber()) + 1 + ".png", "weapon");
        }
        while (DeceptionGame.getInGameCardsList().contains(card));
        DeceptionGame.getInGameCardsList().add(card);
        return card;
    }

    public String toString() {
        return this.type + " card " + this.title;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Card card = (Card) obj;
        return Objects.equals(title, card.title) &&
                Objects.equals(type, card.type);
    }
}
