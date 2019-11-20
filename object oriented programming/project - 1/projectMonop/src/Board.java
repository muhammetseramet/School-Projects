
public class Board implements Space {
    Player players[];  
    int totalPlayers;  //all players between 2 and 8
    int activePlayers; //player who don't bankrupt
    private int currentTurn; //player who has the turn of play
    Deck deck; //deck of cards for money prizes and punishments   
    boolean areLandsOwned[];  //hold if all lands are owned
    int ownerId = 0; // owner id of lands and houses (same owner id because house on same land)
    boolean areStationsOwned[];
    int stationOwnerId = 0; //owner id of stations
    boolean goToJail; // varible if any player goes to jail
    final int numberOfSpace = 40; //square number on the board   
    Property property; // we need to use polymorphism to call Land class
    String names[][] = { //name of lands
            {"Acıbadem" , "Çengelköy" , "Haydarpaşa"} , //üsküdar
            {"Sultanahmet" , "Sirkeci" , "Eminönü"} , //fatih
            {"Göztepe" , "Ayrılık Çeşmesi" , "Fenerbahçe"} , //kadıköy
            {"Bebek" , "Ortaköy" , "Arnavutköy"} , //beşiktaş
            {"Burgazadası" , "Heybeliada" , "Büyükada"} , //adalar
            {"Bağcılar" , "Esenler" , "Bayrampaşa"} , //bağcılar-esenler-bayrampaşa
        };
    String stationNames[] = {"Balat İskelesi" , "Karaköy İskelesi" , "Eminönü İskelesi" ,
                             "Haydarpaşa Tren Garı"};
      
    public Board(){
        
    }
    
    public Board(int totalPlayers) {
        deck = new Deck();
        // we assumed there are 18 lands (on the real game there are 22 lands)
        areLandsOwned = new boolean[18];  //control if any player is a owner of a land 
        areStationsOwned = new boolean[4]; //there are 4 stations , control if any player is a owner of the station
    	activePlayers = totalPlayers; //assign active players as total players
        players = new Player[totalPlayers];
	this.totalPlayers = totalPlayers;
	for(int i = 0;i < players.length;i++){
	    players[i] = new Player(i, "Player " + i); //name the players(Player 0 , Player 1 etc.)
	}           
	}

    public void movePlayer(Player player, int step) { 
        if (player.isBankrupt()) {
            System.out.println("Player " + currentTurn + " bankrupt");
        } 
        else {   
            // player continues to play
            player.setLocation(player.getLocation() + step);  //move players         
            //position indexes 0 to 39 (40 in total)
            if(player.getLocation() > 39) {   // put player movement in a loop on board
            	player.setLocation(player.getLocation() - numberOfSpace);
            	if(goToJail == false) {
            	//send money(200M) to player from bank if player pass from starting point 
            	player.setMoney(player.getMoney() + 200);
            	System.out.println("Player " + currentTurn + " takes 200M from bank.");
            	}
            }       
              
            //use these methods at every turn , every movement
            System.out.println("Player " + currentTurn + " in Square " + player.getLocation());
            setSpaceName(players[currentTurn]);
            getMoneyFromStartPoint(players[currentTurn]);
    	    getLottery(players[currentTurn]); 
            getCommunityChest(players[currentTurn]);
    	    gotoJail(players[currentTurn]);
            getOutOfPrison(players[currentTurn]);
    	    payTax(players[currentTurn]);        
            System.out.println("Player " + currentTurn + " Has Money : " + players[currentTurn].getMoney() + "M");	    
            buyProperties(players[currentTurn]);
            payRents(players[currentTurn]);
            HouseOperations(players[currentTurn]);
            buyStations(players[currentTurn]);
            payStationRents(players[currentTurn]);
            showAccountOfPlayer();         
            bankrupt(players[currentTurn]);        
        }
    }
    
    public void bankrupt(Player player) {
        if(player.getMoney() < 0){
            player.setBankrupt(true);
            activePlayers--; //decrease active players because of bankrupt
            System.out.println("Player " + currentTurn + " bankrupt");
        }
    }
    public void decideWinner() {
        for (Player player : players) {
            if (player.isBankrupt() == false) {             
                if(activePlayers == 1){ //if there is just 1 player
                System.out.println(player.getName() + " WON THE GAME !!!");
            }
            }
        }       
    }
    
    public Player[] getAllPlayers() {
	return players;  
    }
    
    public Player getPlayer(int id) {
	return players[id];
    }
    
    public int getCurrentTurn() { 
        return currentTurn;
    }
    
    public void showAccountOfPlayer(){
        System.out.println("Player " + currentTurn + "'s Account: " + players[currentTurn].getMoney() + "M");
    }
    
    public Player getCurrentPlayer() {
    	return players[currentTurn];
    }
    
    public void nextTurn() {  //limiting turn of play
    	if(++currentTurn >= players.length){
	   currentTurn = 0;
	}
    }

    private void buyProperties(Player player){ //buying different lands with Polymorphism
        switch (player.getLocation()) {
            //purple places Üsküdar
            case 11 :
                if(areLandsOwned[0] == false){
                property = new Land(names[0][0] , 140);
                property.buyProperty(player, this);
                ownerId = currentTurn;  //decide which player bought this property              
                areLandsOwned[0] = true;  //decide that is this property owned
                }
                break;
            case 13:
                if(areLandsOwned[1] == false){
                property = new Land(names[0][1] , 140);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[1] = true;
                }
                break;
            case 14:  
                if(areLandsOwned[2] == false){
                property = new Land(names[0][2] , 160);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[2] = true;
                }
                break;
            //orange places Fatih
            case 16:
                if(areLandsOwned[3] == false){
                property = new Land(names[1][0] , 180);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[3] = true;
                }
                break;
            case 18:
                if(areLandsOwned[4] == false){
                property = new Land(names[1][1] , 180);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[4] = true;
                }
                break;
            case 19:  
                if(areLandsOwned[5] == false){
                property = new Land(names[1][2] , 200);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[5] = true;
                }
                break;
            //red places Kadıköy
            case 21:
                if(areLandsOwned[6] == false){
                property = new Land(names[2][0] , 220);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[6] = true;
                }
                break;
            case 23:
                if(areLandsOwned[7] == false){
                property = new Land(names[2][1] , 220);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[7] = true;
                }
                break;
            case 24:
                if(areLandsOwned[8] == false){
                property = new Land(names[2][2] , 240);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[8] = true;
                }
                break;   
            //yellow places Beşiktaş
            case 26:
                if(areLandsOwned[9] == false){
                property = new Land(names[3][0] , 260);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[9] = true;
                }
                break;
            case 27:
                if(areLandsOwned[10] == false){
                property = new Land(names[3][1] , 260);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[10] = true;
                }
                break;
            case 29:
                if(areLandsOwned[11] == false){
                property = new Land(names[3][2] , 280);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[11] = true;
                }
                break;   
            //green places Adalar
            case 31:
                if(areLandsOwned[12] == false){
                property = new Land(names[4][0] , 300);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[12] = true;
                }
                break;
            case 32:
                if(areLandsOwned[13] == false){
                property = new Land(names[4][1] , 300);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[13] = true;
                }
                break;
            case 34:
                if(areLandsOwned[14] == false){
                property = new Land(names[4][2] , 320);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[14] = true;
                }
                break;   
            //blue places Bağcılar , Esenler , Bayrampaşa
            case 37:
                if(areLandsOwned[15] == false){
                property = new Land(names[5][0] , 100);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[15] = true;
                }
                break;
            case 39:
                if(areLandsOwned[16] == false){
                property = new Land(names[5][1] , 100);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[16] = true;
                }
                break;
            case 1:
                if(areLandsOwned[17] == false){
                property = new Land(names[5][2] , 120);
                property.buyProperty(player, this);
                ownerId = currentTurn;  
                areLandsOwned[17] = true;
                }
                break;        
        }
    }
    
    public void payRents(Player player){ // paying rents to players who has lands
        switch (player.getLocation()) {
            //purple places Üsküdar
            case 11 :
                if(areLandsOwned[0] == true){
                property = new Land(names[0][0] , 140);
                property.payRent(player, this);              
                }
                break;
            case 13:
                if(areLandsOwned[1] == true){
                property = new Land(names[0][1] , 140);
                property.payRent(player, this);
                }
                break;
            case 14:  
                if(areLandsOwned[2] == true){
                property = new Land(names[0][2] , 160);
                property.payRent(player, this);
                }
                break;
            //orange places Fatih
            case 16:
                if(areLandsOwned[3] == true){
                property = new Land(names[1][0] , 180);
                property.payRent(player, this);
                }
                break;
            case 18:
                if(areLandsOwned[4] == true){
                property = new Land(names[1][1] , 180);
                property.payRent(player, this);
                }
                break;
            case 19:  
                if(areLandsOwned[5] == true){
                property = new Land(names[1][2] , 200);
                property.payRent(player, this);
                }
                break;
            //red places Kadıköy
            case 21:
                if(areLandsOwned[6] == true){
                property = new Land(names[2][0] , 220);
                property.payRent(player, this);
                }
                break;
            case 23:
                if(areLandsOwned[7] == true){
                property = new Land(names[2][1] , 220);
                property.payRent(player, this);
                }
                break;
            case 24:
                if(areLandsOwned[8] == true){
                property = new Land(names[2][2] , 240);
                property.payRent(player, this);
                }
                break;   
            //yellow places Beşiktaş
            case 26:
                if(areLandsOwned[9] == true){
                property = new Land(names[3][0] , 260);
                property.payRent(player, this);
                }
                break;
            case 27:
                if(areLandsOwned[10] == true){
                property = new Land(names[3][1] , 260);
                property.payRent(player, this);
                }
                break;
            case 29:
                if(areLandsOwned[11] == true){
                property = new Land(names[3][2] , 280);
                property.payRent(player, this);
                }
                break;   
            //green places Adalar
            case 31:
                if(areLandsOwned[12] == true){
                property = new Land(names[4][0] , 300);
                property.payRent(player, this);
                }
                break;
            case 32:
                if(areLandsOwned[13] == true){
                property = new Land(names[4][1] , 300);
                property.payRent(player, this);
                }
                break;
            case 34:
                if(areLandsOwned[14] == true){
                property = new Land(names[4][2] , 320);
                property.payRent(player, this);
                }
                break;   
            //blue places Bağcılar , Esenler , Bayrampaşa
            case 37:
                if(areLandsOwned[15] == true){
                property = new Land(names[5][0] , 100);
                property.payRent(player, this);
                }
                break;
            case 39:
                if(areLandsOwned[16] == true){
                property = new Land(names[5][1] , 100);
                property.payRent(player, this);
                }
                break;
            case 1:
                if(areLandsOwned[17] == true){
                property = new Land(names[5][2] , 120);
                property.payRent(player, this);
                }
                break;           
        }
    }
    
    public void HouseOperations(Player player){ //building houses or paying rents for houses
        switch (player.getLocation()) {
            //purple places Üsküdar
            case 11 :
                if(areLandsOwned[0] == true && ownerId == currentTurn){
                property = new House(names[0][0] , 110);
                property.buyProperty(player, this); 
                property.payRent(player, this);              
                }
                break;
            case 13:
                if(areLandsOwned[1] == true && ownerId == currentTurn){
                property = new House(names[0][1] , 110);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 14:  
                if(areLandsOwned[2] == true && ownerId == currentTurn){
                property = new House(names[0][2] , 120);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            //orange places Fatih
            case 16:
                if(areLandsOwned[3] == true){
                property = new House(names[1][0] , 130);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 18:
                if(areLandsOwned[4] == true){
                property = new House(names[1][1] , 130);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 19:  
                if(areLandsOwned[5] == true){
                property = new House(names[1][2] , 140);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            //red places Kadıköy
            case 21:
                if(areLandsOwned[6] == true){
                property = new House(names[2][0] , 150);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 23:
                if(areLandsOwned[7] == true){
                property = new House(names[2][1] , 150);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 24:
                if(areLandsOwned[8] == true){
                property = new House(names[2][2] , 160);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;   
            //yellow places Beşiktaş
            case 26:
                if(areLandsOwned[9] == true){
                property = new House(names[3][0] , 170);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 27:
                if(areLandsOwned[10] == true){
                property = new House(names[3][1] , 170);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 29:
                if(areLandsOwned[11] == true){
                property = new House(names[3][2] , 180);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;   
            //green places Adalar
            case 31:
                if(areLandsOwned[12] == true){
                property = new House(names[4][0] , 190);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 32:
                if(areLandsOwned[13] == true){
                property = new House(names[4][1] , 190);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 34:
                if(areLandsOwned[14] == true){
                property = new House(names[4][2] , 200);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;   
            //blue places Bağcılar , Esenler , Bayrampaşa
            case 37:
                if(areLandsOwned[15] == true){
                property = new House(names[5][0] , 90);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 39:
                if(areLandsOwned[16] == true){
                property = new House(names[5][1] , 90);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;
            case 1:
                if(areLandsOwned[17] == true){
                property = new House(names[5][2] , 100);
                property.buyProperty(player, this);
                property.payRent(player, this);
                }
                break;        
        }
    }
    
    public void buyStations(Player player){  
        switch (player.getLocation()) {
            //Balat İskelesi
            case 5 :
                if(areStationsOwned[0] == false){
                property = new Station(stationNames[0] , 200);
                property.buyProperty(player, this);
                stationOwnerId = currentTurn;  //decide which player bought this property              
                areStationsOwned[0] = true;  //decide that is this property owned
                }
                break;
            //Karaköy İskelesi    
            case 15:
                if(areStationsOwned[1] == false){
                property = new Land(stationNames[1] , 200);
                property.buyProperty(player, this);
                stationOwnerId = currentTurn;  
                areStationsOwned[1] = true;
                }
                break;
            //Eminönü İskelesi    
            case 25:  
                if(areStationsOwned[2] == false){
                property = new Land(stationNames[2] , 200);
                property.buyProperty(player, this);
                stationOwnerId = currentTurn;  
                areStationsOwned[2] = true;
                }
                break;
            //Haydarpaşa Tren Garı
            case 35:
                if(areStationsOwned[3] == false){
                property = new Land(stationNames[3] , 200);
                property.buyProperty(player, this);
                stationOwnerId = currentTurn;  
                areStationsOwned[3] = true;
                }
                break;
        }
    }
    
    public void payStationRents(Player player){
        switch(player.getLocation()){
            /////////STATION RENTS
            case 5:
                if(areStationsOwned[0] == true){
                property = new Station(stationNames[0] , 200);
                property.payRent(player, this);
                }
                break;
            case 15:
                if(areStationsOwned[1] == true){
                property = new Station(stationNames[1] , 200);
                property.payRent(player, this);
                }
                break;
            case 25:
                if(areStationsOwned[2] == true){
                property = new Station(stationNames[2] , 200);
                property.payRent(player, this);
                }
                break;
            case 35:
                if(areStationsOwned[3] == true){
                property = new Station(stationNames[3] , 200);
                property.payRent(player, this);
                }
                break;
        }
    }
    
    @Override
    public void getMoneyFromStartPoint(Player player) {
         if(player.getLocation() == 0 && goToJail == false) {
        	 //take money(200M) from bank if you stop at start point 
        	 player.setMoney(player.getMoney() + 200); 
        	 System.out.println("Player " + currentTurn + " takes 200M from bank.");
         }
    }

    @Override
    public void getCommunityChest(Player player) {  //chance
        if(player.getLocation() == 2 || player.getLocation() == 17 || 
                player.getLocation() == 33) { //chance square indexes
        	//get random prize from deck class
        	player.setMoney(player.getMoney() + deck.getRandomMoney()); 
            System.out.println("Player " + currentTurn + " takes "+ deck.getRandomMoney() + "M " + 
            		"from Community Chest.");
        }
    }
    
    @Override
    public void getLottery(Player player) {  //chance
        if(player.getLocation() == 7 || player.getLocation() == 22 || 
                player.getLocation() == 36) { //chance square indexes
        	//get random prize from deck class
        	player.setMoney(player.getMoney() + deck.getRandomMoney()); 
            System.out.println("Player " + currentTurn + " won "+ deck.getRandomMoney() + "M " + 
            		"from Lottery.");
        }
    }
  
    @Override
    public void gotoJail(Player player) {
    	if(player.getLocation() == 30) {
    	   goToJail = true; //player goes to jail
    	   System.out.println("Player " + currentTurn + " goes to jail.");
    	}
    }
    
    @Override
    public void getOutOfPrison(Player player) {
    	if(goToJail){
        player.setMoney(player.getMoney() - 50);  
        System.out.println("Player " + currentTurn + " gets out from the jail to 50M.");
        goToJail = false;
        }
    }

    @Override
    public void payTax(Player player) { //here is all taxes including water electricity and luxury
    	if(player.getLocation() == 4  || player.getLocation() == 12 || 
    	   player.getLocation() == 28 || player.getLocation() == 38) {
    	   player.setMoney(player.getMoney() - 150); //pay 200M income tax, electric,water tax and luxury tax	
    	   System.out.println("Player " + currentTurn + " pays 150M to bank as tax.");
    	}
    }
     
    @Override
    public void setSpaceName(Player player) {
    switch (player.getLocation()) {
            case 10 :
            System.out.println("Player " + currentTurn + " just visited the jail");
            break;
            
            //purple places Üsküdar
            case 11 :
                System.out.println("Player " + currentTurn + " in " + names[0][0]); 
                break;
            case 13:
                System.out.println("Player " + currentTurn + " in " + names[0][1]);
                break;
            case 14:  
                System.out.println("Player " + currentTurn + " in " + names[0][2]);
                break;
            //orange places Fatih
            case 16:
                System.out.println("Player " + currentTurn + " in " + names[1][0]);
                break;
            case 18:
                System.out.println("Player " + currentTurn + " in " + names[1][1]);
                break;
            case 19:  
                System.out.println("Player " + currentTurn + " in " + names[1][2]);
                break;
            
            case 20 :
                System.out.println("Player " + currentTurn + " went on Vacation");
                break;
                
            //red places Kadıköy
            case 21:
                System.out.println("Player " + currentTurn + " in " + names[2][0]);
                break;
            case 23:
                System.out.println("Player " + currentTurn + " in " + names[2][1]);
                break;
            case 24:
                System.out.println("Player " + currentTurn + " in " + names[2][2]);
                break;   
            //yellow places Beşiktaş
            case 26:
                System.out.println("Player " + currentTurn + " in " + names[3][0]);
                break;
            case 27:
                System.out.println("Player " + currentTurn + " in " + names[3][1]);
                break;
            case 29:
                System.out.println("Player " + currentTurn + " in " + names[3][2]);
                break;   
            //green places Adalar
            case 31:
                System.out.println("Player " + currentTurn + " in " + names[4][0]);
                break;
            case 32:
                System.out.println("Player " + currentTurn + " in " + names[4][1]);
                break;
            case 34:
                System.out.println("Player " + currentTurn + " in " + names[4][2]);
                break;   
            //blue places Bağcılar , Esenler , Bayrampaşa
            case 37:
                System.out.println("Player " + currentTurn + " in " + names[5][0]);
                break;
            case 39:
                System.out.println("Player " + currentTurn + " in " + names[5][1]);
                break;
            case 1:
                System.out.println("Player " + currentTurn + " in " + names[5][2]);
                break;        
        }
    }
    
}