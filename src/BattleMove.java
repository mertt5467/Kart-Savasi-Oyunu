public class BattleMove {
    Card card;
    boolean isAttack;
    public BattleMove(Card card, boolean isAttack){
        this.card = card;
        this.isAttack = isAttack;
    }
    public int getPower(){
        if(isAttack){
            return card.attack;
        }
        else{
            return card.defense;
        }
    }
}
