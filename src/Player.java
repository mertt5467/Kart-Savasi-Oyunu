public class Player {
    int diff;
    String name;
    int hp;
    int mana;
    public Player(String name, int hp, int mana){
        this.name = name;
        this.hp = hp;
        this.mana = mana;
    }
    public Player(String name, int diff, int hp, int mana){
        this.diff = diff;
        this.name = name;
        this.hp = hp;
        this.mana = mana;
    }
}
