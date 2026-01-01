import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Collections;

public class App {
    static Scanner input = new Scanner(System.in);
    static Random r = new Random();

    public static Player human;
    public static Player computer;

    static ArrayList<Event> gameEvent = Event.eventList();
    static ArrayList<Card> gameDeck = DeckManager.createDeck();
    public static ArrayList<Card> playerDeck;
    public static ArrayList<Card> computerDeck;

    static BattleMove playerMove;
    static BattleMove computerMove;

    static boolean buffPlayer;

    static Event currentEvent;

    static int computerDamage = 0;
    static int playerDamage = 0;

    static Card pas = new Card(0, "Pas", 0, 0);

    public static void main(String[] args) throws Exception {
        System.out.println("---------- Fantasy Card Fighter 2 -----------");
        System.out.println("Baslamak icin Enter'a tiklayiniz.");
        input.nextLine();
        Thread.sleep(1000);
        gameMenu();
    }

    static void gameMenu() {
        System.out.println("---------- Oyun Menusu -----------");
        while (true) {
            System.out.println("1 - Oyuna Basla");
            System.out.println("2 - Nasil Oynanir?");
            System.out.println("3 - Desteyi incele");
            System.out.println("0 - Cikis yap");
            int choose = playerInput();
            delay(1000);
            switch (choose) {
                case 1 -> chooseDiff();
                case 2 -> howToPlay();
                case 3 -> viewDeck();
                case 0 -> System.exit(0);
                default -> System.out.println("Gecersiz secim.");
            }
        }
    }

    static void game() {
        System.out.println("Zar atma asamasi");
        boolean playerDiceWin = rollDice();
        System.out.println("Kart dagitma asamasi");
        delay(1000);
        dealDeck();
        System.out.println("Oyun baslatiliyor...");
        delay(2000);
        boolean forfeit;
        for (int i = 1;; i++) {
            delay(500);
            System.out.println("-------- " + i + ". TUR --------");
            delay(500);
            currentEvent = chooseEvent();
            eventPlayer(currentEvent);
            if (playerDiceWin) {
                forfeit = playerTour();
                if (!forfeit) {
                    computerTour();
                }
            } else {
                computerTour();
                forfeit = playerTour();
            }
            warTour();
            boolean finish = gameResult(forfeit);
            if (finish) {
                break;
            }
        }
    }

    static void eventPlayer(Event currentEvent) {
        if (currentEvent.id == 3) {
            int choose = r.nextInt(2);
            if (choose == 0) {
                Event.exhaustion(playerDeck, true);
            } else {
                Event.exhaustion(computerDeck, false);
            }
            delay(1000);
        } else if (currentEvent.id == 4) {
            Event.luck(playerDeck, computerDeck);
            delay(1000);
        } else if (currentEvent.id == 5) {
            Event.swapCard(playerDeck, computerDeck);
        } else if (currentEvent.id == 6) {
            delay(1000);
            System.out.println("Her oyuncuya +5 mana verildi");
            computer.mana += 5;
            human.hp += 5;
        } else if (currentEvent.id == 7) {
            delay(1000);
            System.out.println("Oyuncular zehirlendi! her oyuncunun HP'si -3 dusuruldu.");
            computer.hp -= 3;
            human.hp -= 3;
        } else if (currentEvent.id == 8) {
            int choose = r.nextInt(2);
            if (choose == 0) {
                Event.vigor(playerDeck, true);
            } else {
                Event.vigor(computerDeck, false);
            }
        }
        gameResult(false);
    }

    static boolean gameResult(boolean forfeit) {
        if (human.hp <= 0 || computer.hp <= 0 || playerDeck.size() == 0 || computerDeck.size() == 0
                || forfeit == true) {
            System.out.println("Oyun bitti!");
            delay(1000);
            if (human.hp <= 0 && computer.hp <= 0) {
                System.out.println("Berabere, iki kullanici da öldü.");
            } else if (human.hp <= 0) {
                System.out.println("Bilgisayar kazandi, Oyuncu öldü.");
            } else if (computer.hp <= 0) {
                System.out.println("Oyuncu kazandi, Bilgisayar öldü.");
            } else if (computer.hp > human.hp) {
                System.out.println("Kart bitti. Bilgisayar cani yuksek oldugundan dolayi kazandi.");
            } else if (human.hp > computer.hp) {
                System.out.println("Kart bitti, Oyuncu kazandi. Bilgisayarin cani dusuk oldugundan dolayi kaybetti.");
            } else if (forfeit) {
                System.out.println("Bilgisayar kazandi, oyuncu teslim oldu.");
            } else if (human.hp == computer.hp) {
                System.out.println("Oyun berabere bitti. Kazanan yok.");
            }
            return true;
        } else {
            return false;
        }
    }

    static void computerTour() {
        delay(500);
        System.out.println("Bilgisayarin sirasi");
        Card maxAttack = pas;
        Card maxDefense = pas;
        for (int i = 0; i < computerDeck.size(); i++) {
            if (maxAttack.attack < computerDeck.get(i).attack && computer.mana > computerDeck.get(i).attack) {
                maxAttack = computerDeck.get(i);
                maxAttack.id = i;

            }
            if (maxDefense.defense < computerDeck.get(i).defense && computer.mana > computerDeck.get(i).defense) {
                maxDefense = computerDeck.get(i);
                maxDefense.id = i;
            }
        }
        int computerChoose;

        delay(500);
        if (computer.diff == 1) {
            computerChoose = r.nextInt(3);
        } else if (computer.diff == 2) {
            computerChoose = r.nextInt(2);
        } else {
            computerChoose = 0;
        }
        if (computerChoose == 0) {
            computerChooseCard(maxAttack, maxDefense);
            return;
        } else {
            System.out.println("Bilgisayar Hamle Yapiyor...");
            delay(3000);
            Collections.shuffle(computerDeck);
            for (int i = 0; i < computerDeck.size(); i++) {
                if (computer.mana >= computerDeck.get(i).attack) {
                    computerMove = new BattleMove(computerDeck.get(i), true);
                    computerDeck.remove(i);
                    return;
                } else if (computer.mana >= computerDeck.get(i).defense) {
                    computerMove = new BattleMove(computerDeck.get(i), false);
                    computerDeck.remove(i);
                    return;
                }
            }
            computerMove = new BattleMove(pas, false);
            System.out.println("Bilgisayar hamle yapti.");
            delay(1000);
            return;
        }
    }

    static void computerChooseCard(Card maxAttack, Card maxDefense) {
        System.out.println("Bilgisayar Hamle Yapiyor...");
        delay(3500);
        int totalAttack = 0;
        int playerTotalAttack = 0;
        for (Card nextCard : playerDeck) {
            playerTotalAttack += nextCard.attack;
        }
        for (Card nextCard : computerDeck) {
            totalAttack += nextCard.attack;
        }
        if (maxAttack.attack > human.hp) {
            computerMove = new BattleMove(maxAttack, true);
            computerDeck.remove(maxAttack.id);
        } else if (computer.mana < 10 && computer.hp > human.hp + 25 && maxAttack.attack < human.hp) {
            computerMove = new BattleMove(pas, false);
        } else if (maxDefense != pas && (human.hp - 25 > computer.hp || computer.hp < 25)) {
            computerMove = new BattleMove(maxDefense, false);
            computerDeck.remove(maxDefense.id);
        } else if (maxAttack != pas
                && (computer.hp - 25 > human.hp || maxAttack.attack > human.hp || currentEvent.id == 2)) {
            computerMove = new BattleMove(maxAttack, true);
            computerDeck.remove(maxAttack.id);
        } else if (maxDefense != pas && playerTotalAttack > totalAttack
                && (human.mana > computer.mana + 10 && computer.mana < 15)) {
            computerMove = new BattleMove(maxDefense, false);
            computerDeck.remove(maxDefense.id);
        } else if (maxAttack != pas && totalAttack > playerTotalAttack && (human.mana < 15)) {
            computerMove = new BattleMove(maxAttack, true);
            computerDeck.remove(maxAttack.id);
        } else if (maxDefense == pas && maxAttack == pas) {
            computerMove = new BattleMove(pas, false);
        } else {
            if (maxAttack != pas) {
                computerMove = new BattleMove(maxAttack, true);
                computerDeck.remove(maxAttack.id);
            } else if (maxDefense != pas) {
                computerMove = new BattleMove(maxDefense, false);
                computerDeck.remove(maxDefense.id);
            } else {
                computerMove = new BattleMove(pas, false);
            }
        }
        System.out.println("Bilgisayar hamle yapti.");
        delay(1000);
        return;
    }

    static boolean playerTour() {
        System.out.println("Sizin siraniz");
        delay(500);
        while (true) {
            delay(500);
            System.out.println("1 - Hamle yap");
            System.out.println("2 - Kartlarini Kontrol Et");
            System.out.println("3 - Bilgisayarin statlarini incele");
            System.out.println("4 - Teslim ol");
            System.out.println("HP: " + human.hp + " Mana: " + human.mana);
            int choose = playerInput();
            switch (choose) {
                case 1 -> {
                    playerChooseCard();
                    return false;
                }
                case 2 -> {
                    playerDeckViewer(0);
                }
                case 3 -> {
                    System.out.println("Bilgisayar Mana: " + computer.mana);
                    System.out.println("Bilgisayar HP'si: " + computer.hp);
                    System.out.println("Onaylamak icin Enter'a tiklayiniz.");
                    input.nextLine();
                }
                case 4 -> {
                    return true;
                }
            }
        }
    }

    static void playerChooseCard() {
        boolean manaCheck;
        playerDeckViewer(1);
        while (true) {
            System.out.println("HP: " + human.hp + " Mana: " + human.mana);
            System.out.println("Kartinizi seciniz. Pas Gecmek icin 0'i tuslayiniz.");
            int chooseCard = playerInput();
            delay(1000);
            if (chooseCard == 0) {
                playerMove = new BattleMove(pas, false);
                break;
            } else if (chooseCard <= playerDeck.size()) {
                manaCheck = playerChooseMove(playerDeck.get(chooseCard - 1));
                if (manaCheck) {
                    playerDeck.remove(playerDeck.get(chooseCard - 1));
                    break;
                }
            } else {
                System.out.println("Yanlis secim.");
            }
        }
    }

    static boolean playerChooseMove(Card x) {
        System.out.println("Yapacaginiz eylemi seciniz.");
        while (true) {
            System.out.println("1 - Atak");
            System.out.println("2 - Defans");
            int chooseMove = playerInput();
            if (chooseMove == 1) {
                if (x.attack > human.mana) {
                    System.out.println("Mana yetersiz.");
                    delay(500);
                    return false;
                } else {
                    playerMove = new BattleMove(x, true);
                    return true;
                }
            } else if (chooseMove == 2) {
                if (x.defense > human.mana) {
                    System.out.println("Mana yetersiz.");
                    delay(500);
                    return false;
                } else {
                    playerMove = new BattleMove(x, false);
                    return true;
                }
            } else {
                System.out.println("Gecersiz Secim.");
            }
        }
    }

    static Event chooseEvent() {
        int choose = r.nextInt(gameEvent.size());
        delay(500);
        System.out.println("TUR EVENTI: " + gameEvent.get(choose).name);
        delay(1000);
        return gameEvent.get(choose);
    }

    static void warTour() {
        int playerPower = playerMove.getPower();
        int computerPower = computerMove.getPower();
        delay(1000);
        System.out.println("Sizin kartiniz: " + playerMove.card.name + " Saldiri: " + playerMove.card.attack
                + " Defans: " + playerMove.card.defense);
        System.out.println("Bilgisayarin karti: " + computerMove.card.name + " Saldiri: " + computerMove.card.attack
                + " Defans: " + computerMove.card.defense);
        delay(2000);
        if (Math.random() <= 0.15 && playerMove.isAttack == true) {
            System.out.println("Oyuncu kritik vurus gerceklestirdi!");
            playerPower += (playerPower * 0.50);
            delay(1000);
        }
        if (Math.random() <= 0.15 && computerMove.isAttack == true) {
            System.out.println("Bilgisayar kritik vurus gerceklestirdi!");
            computerPower += (computerPower * 0.50);
            delay(1000);
        }
        delay(1000);
        if (computerMove.isAttack == true && playerMove.isAttack == true) {
            warResults(playerPower, computerPower, 0);
        } else if (computerMove.isAttack == true && playerMove.isAttack == false) {
            warResults(playerPower, computerPower, 1);
        } else if (computerMove.isAttack == false && playerMove.isAttack == true) {
            warResults(playerPower, computerPower, 2);
        } else {
            warResults(playerPower, computerPower, 3);
        }
        endTour(playerPower, computerPower);
    }

    static void endTour(int playerPower, int computerPower) {
        human.mana -= playerPower;
        computer.mana -= computerPower;
        if (computerDamage >= 2) {
            computer.hp += 3;
        }
        if (playerDamage >= 2) {
            human.hp += 3;
        }
        if (computer.hp > 100) {
            computer.hp = 100;
        }
        if (human.hp > 100) {
            human.hp = 100;
        }
        human.mana += 8;
        computer.mana += 8;
        delay(1000);
        System.out.println("Oyuncunun cani: " + human.hp);
        System.out.println("Bilgisayarin cani: " + computer.hp);
        delay(1500);
        System.out.println("Tur bitti.");
        delay(2000);
    }

    static void warResults(int playerPower, int computerPower, int x) {
        int result = playerPower - computerPower;
        switch (x) {
            case 0 -> {
                System.out.println("Iki oyuncu da saldirmayi secti!");
                delay(1000);
                System.out.println("Sizin gucunuz " + playerPower + " --- " + computerPower + " Bilgisayar gucu");
                delay(1000);
                human.hp -= computerPower;
                computer.hp -= playerPower;
                computerDamage = 0;
                playerDamage = 0;
                if (currentEvent.id == 2) {
                    Event.healthSteal(human, playerPower);
                    Event.healthSteal(computer, computerPower);
                }
            }
            case 1 -> {
                System.out.println("Bilgisayar saldirmayi, oyuncu savunmayi secti!");
                delay(1000);
                System.out.println("Sizin gucunuz " + playerPower + " --- " + computerPower + " Bilgisayar gucu");
                delay(1000);
                if (result >= 0) {
                    System.out.println("Saldiri basariyla savusturuldu!");
                    delay(500);
                    System.out.println("Mana kazanci: " + (result));
                    human.mana += result;
                    computerDamage++;
                    playerDamage++;
                } else {
                    System.out.println("Savunma basarisiz!");
                    delay(500);
                    human.hp -= (-1 * result);
                    computerDamage++;
                    playerDamage = 0;
                    if (currentEvent.id == 2) {
                        Event.healthSteal(computer, -1 * result);
                    }
                }
            }
            case 2 -> {
                System.out.println("Bilgisayar savunmayi, oyuncu saldirmayi secti!");
                delay(1000);
                System.out.println("Sizin gucunuz " + playerPower + " --- " + computerPower + " Bilgisayar gucu");
                delay(1000);
                if (result >= 0) {
                    System.out.println("Saldiri basarili!");
                    delay(500);
                    computer.hp -= result;
                    computerDamage = 0;
                    playerDamage++;
                    if (currentEvent.id == 2) {
                        Event.healthSteal(human, result);
                    }
                } else {
                    System.out.println("Bilgisayar saldiriyi savusturdu!");
                    delay(500);
                    computer.mana += (-1 * result);
                    computerDamage++;
                    playerDamage++;
                }
            }
            case 3 -> {
                System.out.println("Iki oyuncu da savunmayi secti.");
                delay(1000);
                computerDamage++;
                playerDamage++;
            }
        }
    }

    static void dealDeck() {
        playerDeck = new ArrayList<>();
        computerDeck = new ArrayList<>();
        Collections.shuffle(gameDeck);
        for (int i = 0; i < 8; i++) {
            playerDeck.add(gameDeck.get(0));
            gameDeck.remove(0);
            computerDeck.add(gameDeck.get(0));
            gameDeck.remove(0);
        }
        gameDeck = DeckManager.createDeck();
        playerDeckViewer(0);
        delay(500);
    }

    static void playerDeckViewer(int x) {
        delay(500);
        System.out.println("Kartlariniz: ");
        for (int i = 0; i < playerDeck.size(); i++) {
            delay(250);
            System.out.printf("%-1d. %-10s \tAtak: %-5d \tDefans: %d%n",
                    i + 1,
                    playerDeck.get(i).name,
                    playerDeck.get(i).attack,
                    playerDeck.get(i).defense);
        }
        delay(500);
        if (x == 0) {
            System.out.println("Onaylamak icin Enter'a tiklayin.");
            input.nextLine();
        }
    }

    static void chooseDiff() {
        delay(500);
        System.out.println("Zorluk");
        while (true) {
            System.out.println("1 - Kolay");
            System.out.println("2 - Orta");
            System.out.println("3 - Zor");
            System.out.println("0 - Geri Don");
            int choose = playerInput();
            if (choose < 0 || choose > 3) {
                System.out.println("Yanlis secim.");
                delay(500);
                continue;
            } else if (choose == 0) {
                return;
            } else {
                human = new Player("Oyuncu", 80, 50);
                computer = new Player("Bilgisayar", choose, 80, 50);
                delay(500);
                game();
            }
        }
    }

    public static boolean rollDice() {
        boolean diceWinPlayer;
        while (true) {
            delay(1000);
            System.out.println("Zar atmak icin Enter'a tiklayiniz.");
            input.nextLine();
            delay(1500);
            int playerDice = r.nextInt(6) + 1;
            System.out.println("Zar sonucunuz: " + playerDice);
            delay(500);
            System.out.println("Bilgisayar zar atiyor...");
            delay(1500);
            int computerDice = r.nextInt(6) + 1;
            System.out.println("Bilgisayarin zar sonucu: " + computerDice);
            delay(500);
            if (playerDice > computerDice) {
                diceWinPlayer = true;
                System.out.println("Zar atisini kazandiniz.");
                break;
            } else if (computerDice > playerDice) {
                diceWinPlayer = false;
                System.out.println("Bilgisayar zar atisini kazandi.");
                break;
            } else {
                System.out.println("Berabere. Tekrar atiliyor.");
            }
        }
        return diceWinPlayer;
    }

    static void howToPlay() {
        delay(1000);
        System.out.println("Bolumler");
        while (true) {
            delay(1000);
            System.out.println("1 - Oyun nasil isliyor?");
            System.out.println("2 - Mana nedir?");
            System.out.println("3 - HP nedir?");
            System.out.println("4 - Event nedir?");
            System.out.println("0 - Geri Don");
            int choose = playerInput();
            if (choose == 0) {
                break;
            }
            guide(choose);
        }
    }

    static void guide(int x) {
        delay(500);
        switch (x) {
            case 1 -> {
                System.out.println(
                        "\nHer iki oyuncuya da her karttan tek olmak sartiyla 8'er kart dagitilir. Oyuncular bu kartlarin degerlerine bakarak bir sonraki stratejik hamlesini secerler.");
                System.out.println("Kartlarda 2 adet deger bulunur. 1 - Saldiri 2 - Defans.");
                System.out.println(
                        "Oyuncu ilkonce kart secer. Sonra bu kartla yapacagi hamleyi secer ve turunu bitirir.");
                System.out.println("Saldiri, karsi oyuncunun hp degerini dusurur.");
                System.out.println("Defans ise karsi oyuncunun saldirisini engeller.");
                System.out.println("Eger iki oyuncu da saldirirsa ikisininde hp'si azalir.");
                System.out.println(
                        "Eger iki oyuncu da defansi secerse o tur pas gecer ve iki oyuncu da kartlarini kaybederler.");
                System.out.println("Sansa bagli olarak bilgisayarin da oyuncununda saldirdigi zaman %15 sans ile kritik vurma sansi vardir. Kritik hasar sabit %50 dir.");
                System.out.println("Ayrica her tur rastgele bir event secilir.");
                System.out.println("Bu eventlere gore oyunun kurallari tur basina degisebilir.");
                System.out.println(
                        "Oyuncularin elindeki kartlar bitince veya herhangi bir oyuncunun HP'si 0'a dusunce oyun biter.");
                System.out.println("\nGeri donmek icin Enter'a tiklayin.");
                input.nextLine();
            }
            case 2 -> {
                System.out.println("\nMana kartlari kullanmak icin gereken bedeldir.");
                System.out.println("Her oyuncu 50 Mana ile baslar.");
                System.out.println("Mana bedeli, Kartlarin sectiginiz ozelligine gore belirlenir.");
                System.out.println("Ornek olarak: ");
                System.out.println(gameDeck.get(0).name + " ( S: " + gameDeck.get(0).attack + " / D: "
                        + gameDeck.get(0).defense + " ) adli kartin saldiri ozelligini kullanirsaniz "
                        + gameDeck.get(0).attack + " mana harcarsiniz.");
                System.out.println("Q / A: ");
                System.out.println("Q: Peki Manam biterse ne olur?");
                System.out.println("A: Mananiz biterse o zaman kart oynayamaz ve pas gecmek durumunda kalirsiniz.");
                System.out.println("Q: Mana nasil yenilenir?");
                System.out.println(
                        "A: Her tur otomatik olarak 8 mana yenilersiniz. Ayrica basarili yaptiginiz her savunmaya karsilik 5 + (saldiridan arti kalan defans puani) kazanirsiniz.");
                System.out.println("\nGeri donmek icin Enter'a tiklayin.");
                input.nextLine();
            }
            case 3 -> {
                System.out.println("\nHP, \"Health Point\" (yani saglik puani) demektir.");
                System.out.println("Her oyuncu 80 HP ile baslar.");
                System.out.println("Oyuncular yaptiklari saldiri hamleleriyle birbirlerinin HP puanlarini dusurur.");
                System.out.println("Saldiriya karsi yapilan basarili savunma ise HP'yi korur.");
                System.out.println("Q / A: ");
                System.out.println("Q: HP yenilenebilir mi?");
                System.out.println(
                        "A: Eger oyuncu 2 tur ustuste hasar almaz ise tur basina +3 hp yeniler.");
                System.out.println("Q: HP puanim biterse ne olur?");
                System.out.println("A: Oyun biter ve kaybedersiniz.");
                System.out.println("\nGeri donmek icin Enter'a tiklayin.");
                input.nextLine();
            }
            case 4 -> {
                System.out.println(
                        "Event, her tur rastgele secilen ve oyunun gidisatini degistirmeye yarayan olaylardir.");
                System.out.println("Eventler sizin lehinize de olabilir alehinize de.");
                System.out.println("Q / A");
                System.out.println("Q: Hangi eventler var?");
                System.out.println("A: 1 - Normal = Oyunda herhangi bir kural degismez.");
                System.out.println("A: 2 - Can Calma = Rakibe verilen hasarin /2 miktarinda can yenilersiniz.");
                System.out.println(
                        "A: 3 - Bitkinlik = Rastgele bir oyuncunun rastgele bir kartinin statlarina -3 dusus uygular.");
                System.out.println(
                        "A: 4 - Sans = Event geldiginde zar atilir. Kazanan oyuncunun butun kartlarina +1, kaybedenin ise -1 uygulanir.");
                System.out.println("A: 5 - Degis - Tokus = Oyuncularin arasinda rastgele secilen 1 kart degistirilir.");
                System.out.println("A: 6 - Mana Bollugu = Oyunculara +5 mana ekler.");
                System.out.println("A: 7 - Zehirlenme = Oyuncularin canini -3 azaltir.");
                System.out.println(
                        "A: 8 - Dinclik: = Rastgele bir oyuncunun rastgele bir kartinin statlarina +2 artis uygular.");
                System.out.println("\nGeri donmek icin Enter'a tiklayin.");
                input.nextLine();
            }
            default -> System.out.println("Gecersiz secim.");
        }
    }

    static void viewDeck() {
        System.out.println();
        for (Card nextCard : gameDeck) {
            System.out.println("Kart Ismi: " + nextCard.name);
            System.out.println("Saldiri: " + nextCard.attack);
            System.out.println("Defans: " + nextCard.defense);
            System.out.println("--------------------");
        }
        System.out.println("Onaylamak icin Enter'a basiniz.");
        input.nextLine();
        return;
    }

    static int playerInput() {
        while (true) {
            try {
                System.out.print("Secim: ");
                int x = input.nextInt();
                input.nextLine();
                return x;
            } catch (InputMismatchException e) {
                System.out.println("Gecersiz parametre! lutfen tekrar deneyin.");
                input.nextLine();
                delay(500);
            }
        }
    }

    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            System.out.println("Beklenmedik bir sorunla karsilasildi.");
        }
    }
}
