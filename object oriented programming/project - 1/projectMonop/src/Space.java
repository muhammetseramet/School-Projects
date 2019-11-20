public interface Space {
    void setSpaceName(Player player);
    void getMoneyFromStartPoint(Player player);
    void gotoJail(Player player);
    void getLottery(Player player);
    void payTax(Player player);
    void getOutOfPrison(Player player);
    void getCommunityChest(Player player);
}
