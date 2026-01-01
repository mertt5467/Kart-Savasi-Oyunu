import java.util.ArrayList;

public class DeckManager {
    
    public static ArrayList<Card> createDeck(){
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(new Card(1, "Zeus", 26, 9));
        deck.add(new Card(2, "Kayle", 25, 10));
        deck.add(new Card(3, "Morgana", 16, 16));
        deck.add(new Card(4, "Hades", 20, 13));
        deck.add(new Card(5, "Rover", 18, 15));
        deck.add(new Card(6, "Quinn", 21, 6));
        deck.add(new Card(7, "Elysia", 14, 12));
        deck.add(new Card(8, "Rias", 18, 9));
        deck.add(new Card(9, "Mary", 11, 6));
        deck.add(new Card(10, "Lucian", 19, 7));
        deck.add(new Card(11, "Rogue", 11,22 ));
        deck.add(new Card(12, "Rock", 8,26 ));
        deck.add(new Card(13, "Angela", 12, 21));
        deck.add(new Card(14, "Lilith", 22, 11));
        deck.add(new Card(15, "Phoenix", 20, 13));
        deck.add(new Card(16, "Tigreal", 13, 18));
        return deck;
    }
}
