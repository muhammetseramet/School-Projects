public class House extends Property {	
	int price;
	//we don't need an ownerId for house , just beacause it is already defining with land
        //a player cannot built a house on the land before she/he buy the land
        
	public House(String name, int price) {
	    super(name);
	    this.price = price;
	}
	
	public int getPrice() {
	    return price;
	}
	
	@Override
	public void buyProperty(Player player, Board board) {
	  if(board.getCurrentTurn() == board.ownerId){	//if current player is owner of land then player can build a house	
            System.out.println("Player " + board.getCurrentTurn() + " Build a House in " + getName() + " to " + price + "M");
	    player.setMoney(player.getMoney() - price);	//buy property
	  } 
	}
        
        @Override
	public void payRent(Player player, Board board) {
          if(board.getCurrentTurn() != board.ownerId){ //avoid that player pays tax to ownself
            int rent = price * 7 / 10;
	    System.out.println("Player " + board.getCurrentTurn() + " paid " + rent + "M as House Rent to " + board.getPlayer(board.ownerId).getName());
	    player.setMoney(player.getMoney() - rent); //tenant pays rent
	    System.out.println("Player " + board.ownerId + " collected " + rent + "M as House Rent from " + " Player " + board.getCurrentTurn());
            board.getPlayer(board.ownerId).setMoney(board.getPlayer(board.ownerId).getMoney() + rent); //property owner collect rent
          }
        }
        
}
