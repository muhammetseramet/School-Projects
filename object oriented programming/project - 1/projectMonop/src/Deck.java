public class Deck {    
    private int randomLottery = 0;
    
    public Deck(){
        randomLottery = (int) (Math.random() * 300) + 1; //random money 1 to 300M
    }
    
    public int getRandomMoney() {
    	 return randomLottery;   	
    }
}
