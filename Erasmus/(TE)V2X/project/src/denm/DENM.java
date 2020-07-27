package denm;

import denm.header.ItsPduHeader;
import denm.messagebody.DecentralizedEnvironmentalNotificationMessage;

public class DENM {

    private ItsPduHeader itsPduHeader;
    private DecentralizedEnvironmentalNotificationMessage decentralizedEnvironmentalNotificationMessage;

    public DENM(){}

    public DENM(ItsPduHeader itsPduHeader,
                DecentralizedEnvironmentalNotificationMessage decentralizedEnvironmentalNotificationMessage){
        this.itsPduHeader = itsPduHeader;
        this.decentralizedEnvironmentalNotificationMessage = decentralizedEnvironmentalNotificationMessage;
    }

    public DecentralizedEnvironmentalNotificationMessage getDecentralizedEnvironmentalNotificationMessage() {
        return decentralizedEnvironmentalNotificationMessage;
    }

    public ItsPduHeader getItsPduHeader() {
        return itsPduHeader;
    }

    public void setDecentralizedEnvironmentalNotificationMessage
            (DecentralizedEnvironmentalNotificationMessage decentralizedEnvironmentalNotificationMessage) {
        this.decentralizedEnvironmentalNotificationMessage = decentralizedEnvironmentalNotificationMessage;
    }

    public void setItsPduHeader(ItsPduHeader itsPduHeader) {
        this.itsPduHeader = itsPduHeader;
    }

    @Override
    public String toString(){
        return "DENM{" +
                "itsPduHeader=" + itsPduHeader +
                ", decentralizedEnvironmentalNotificationMessage=" + decentralizedEnvironmentalNotificationMessage +
                '}';
    }
}
