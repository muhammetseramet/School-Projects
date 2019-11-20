public class Land extends Property{		
	int price; 
	int ownerId = -1;
	
	public Land(String name, int price) {
		super(name);
		this.price = price;
	}
	
	public void setOwner(int ownerId) {
		this.ownerId = ownerId;
	}
	
	public int getPrice() {
		return price;
	}
	
	@Override
	public void buyProperty(Player player, Board board) {
            if(ownerId < 0){	 //if there is no owner of land	
            System.out.println("Player " + board.getCurrentTurn() + " bought " + getName() + " to " + price + "M");
	    ownerId = player.getId(); //owner is player who bought this land
	    player.setMoney(player.getMoney() - price);	//buy property
	    }             
	}
        
        @Override
	public void payRent(Player player, Board board) {
            if(board.getCurrentTurn() != board.ownerId){ //avoid that player pays tax to ownself
            int rent = price * 7 / 10; //rental fee is %70 of price
	    System.out.println("Player " + board.getCurrentTurn() + " paid " + rent + "M as rental fee to " + board.getPlayer(board.ownerId).getName());
	    player.setMoney(player.getMoney() - rent); //tenant pays rent
	    System.out.println("Player " + board.ownerId + " collected " + rent + "M as rental fee from " + " Player " + board.getCurrentTurn());
            board.getPlayer(board.ownerId).setMoney(board.getPlayer(board.ownerId).getMoney() + rent); //property owner collect rent
            }
        }
       
}
