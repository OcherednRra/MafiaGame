package io.mafialike.baseclasses;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class Card
{
    String title;
    String type;

    Card(String title, String type)
    {
        this.title = title;
        this.type = type;
    }

    public static ArrayList<Card> createHand(Supplier<Card> cardSupplier)
    {
        ArrayList<Card> hand = new ArrayList<>();
        for (byte i = 0; i < 4; i++) { hand.add(cardSupplier.get()); }
        return hand;
    }

    public static ArrayList<Card> createClueHand()
    {
        return createHand(Card::getRandomClueCard);
    }

    public static ArrayList<Card> createWeaponHand()
    {
        return createHand(Card::getRandomWeaponCard);
    }

    private static Card getRandomCard(String cardType, int cardNumber)
    {
        Card card;
        do
        {
            card = new Card(new Random().nextInt(cardNumber) + 1 + ".png", cardType);
        }
        while (DeceptionGame.getInGameCardsList().contains(card));
        DeceptionGame.getInGameCardsList().add(card);
        return card;
    }

    private static Card getRandomClueCard()
    {
        return getRandomCard("clue", DeceptionGame.getClueCardsNumber());
    }

    private static Card getRandomWeaponCard()
    {
        return getRandomCard("weapon", DeceptionGame.getWeaponCardsNumber());
    }

    public String getTitle()
    {
        return this.title;
    }
    public String getType()
    {
        return this.type;
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
