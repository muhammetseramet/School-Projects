public class Player {
    private int id;
    private String name; 
    private int location;
    private int money = 500;
    private boolean bankrupt;
    PairOfDice dice;     

    public Player() {
		
    }
    
    public Player(int playerId , String playerName) {
		this.id = playerId;
                this.name = playerName;
    }
    
    public int tossDie() {
        dice = new PairOfDice(); // call dice with constructor, by the way it's roll method.
        return dice.getTotal();  //return sum of 2 dice.
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
	return name;
    }
    
    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }
}