public abstract class Property { 
	String nameOfProperty; 
	
	public Property(String nameOfProperty) {
		this.nameOfProperty = nameOfProperty;
	}
	
	public String getName() {
		return nameOfProperty;
	}
	
	public abstract void buyProperty(Player player, Board board);
        public abstract void payRent(Player player, Board board);
}
