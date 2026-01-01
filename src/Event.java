import java.util.ArrayList;
import java.util.Random;

public class Event {
    int id;
    String name;

    public Event(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ArrayList<Event> eventList() {
        ArrayList<Event> event = new ArrayList<>();
        event.add(new Event(1, "Normal"));
        event.add(new Event(2, "Can Calma"));
        event.add(new Event(3, "Bitkinlik"));
        event.add(new Event(4, "Sans"));
        event.add(new Event(5, "Degis-Tokus"));
        event.add(new Event(6, "Mana bollugu"));
        event.add(new Event(7, "Zehirlenme"));
        event.add(new Event(8, "Dinclik"));
        return event;
    }

    public static void healthSteal(Player player, int result) {
        int steal = result / 2;
        player.hp += steal;
        if (player.hp > 100) {
            player.hp = 100;
        }
        System.out.println(player.name + ", " + steal + " can caldi!");
    }

    public static void exhaustion(ArrayList<Card> playerDeck, boolean player) {
        if (playerDeck.size() == 0)
            return;

        Random r = new Random();
        int chooseCard = r.nextInt(playerDeck.size());
        playerDeck.get(chooseCard).attack -= 3;
        playerDeck.get(chooseCard).defense -= 3;
        if (playerDeck.get(chooseCard).attack < 0) {
            playerDeck.get(chooseCard).attack = 0;
        }
        if (playerDeck.get(chooseCard).defense < 0) {
            playerDeck.get(chooseCard).defense = 0;
        }
        if (player) {
            System.out.println(
                    playerDeck.get(chooseCard).name + " adli kartiniz bitkinlige maruz kaldi. Statlari -3 dustu.");
        } else {
            System.out.println("Bilgisayarin bir karti bitkinlige maruz kaldi!");
        }
    }

    public static void luck(ArrayList<Card> playerDeck, ArrayList<Card> computerDeck) {
        boolean buffPlayer = App.rollDice();
        if (buffPlayer) {
            System.out.println("Eventi siz kazandiniz. Butun kartlariniz +1 bonus aldi, bilgisayarin ise -1 dustu.");
            for (int i = 0; i < playerDeck.size(); i++) {
                playerDeck.get(i).attack += 1;
                playerDeck.get(i).defense += 1;
            }
            for (int i = 0; i < computerDeck.size(); i++) {
                computerDeck.get(i).attack -= 1;
                computerDeck.get(i).defense -= 1;
                if (computerDeck.get(i).attack < 0) {
                    computerDeck.get(i).attack = 0;
                }
                if (computerDeck.get(i).defense < 0) {
                    computerDeck.get(i).defense = 0;
                }
            }

        } else {
            System.out.println(
                    "Eventi bilgisayar kazandi. Bilgisayarin butun kartlari +1 bonus aldi, sizin ise -1 dustu.");
            for (int i = 0; i < computerDeck.size(); i++) {
                computerDeck.get(i).attack += 1;
                computerDeck.get(i).defense += 1;
            }
            for (int i = 0; i < playerDeck.size(); i++) {
                playerDeck.get(i).attack -= 1;
                playerDeck.get(i).defense -= 1;
                if (playerDeck.get(i).attack < 0) {
                    playerDeck.get(i).attack = 0;
                }
                if (playerDeck.get(i).defense < 0) {
                    playerDeck.get(i).defense = 0;
                }
            }
        }
    }

    public static void swapCard(ArrayList<Card> playerDeck, ArrayList<Card> computerDeck) {
        if (playerDeck.size() == 0 || computerDeck.size() == 0)
            return;

        Random r = new Random();
        int choose = r.nextInt(playerDeck.size());

        System.out.println("Bilgisayara giden kartiniz: " + playerDeck.get(choose).name + " S: "
                + playerDeck.get(choose).attack + " / D: " + playerDeck.get(choose).defense);

        App.delay(500);
        int choose2 = r.nextInt(computerDeck.size());

        System.out.println("Bilgisayardan gelen kart: " + computerDeck.get(choose2).name + " S: "
                + computerDeck.get(choose2).attack + " / D: " + computerDeck.get(choose2).defense);
        computerDeck.add(playerDeck.get(choose));
        playerDeck.add(computerDeck.get(choose2));
        playerDeck.remove(choose);
        computerDeck.remove(choose2);
        App.delay(500);
        System.out.println("Onaylamak icin Enter'a tiklayiniz.");
        App.input.nextLine();
    }

    public static void vigor(ArrayList<Card> playerDeck, boolean player) {
        if (playerDeck.size() == 0)
            return;

        Random r = new Random();
        int chooseCard = r.nextInt(playerDeck.size());
        playerDeck.get(chooseCard).attack += 2;
        playerDeck.get(chooseCard).defense += 2;
        if (player) {
            System.out.println(
                    playerDeck.get(chooseCard).name + " adli kartiniz dinclesti! Statlari +2 yukseldi.");
        } else {
            System.out.println("Bilgisayarin bir karti dinclesti!");
        }
    }
}
