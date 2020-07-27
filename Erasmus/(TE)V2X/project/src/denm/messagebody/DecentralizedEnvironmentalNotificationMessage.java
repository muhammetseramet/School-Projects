package denm.messagebody;


import denm.containers.AlacarteContainer;
import denm.containers.LocationContainer;
import denm.containers.ManagementContainer;
import denm.containers.SituationContainer;

public class DecentralizedEnvironmentalNotificationMessage {

    private AlacarteContainer alacarteContainer;
    private LocationContainer locationContainer;
    private ManagementContainer managementContainer;
    private SituationContainer situationContainer;

    public DecentralizedEnvironmentalNotificationMessage(){}

    public DecentralizedEnvironmentalNotificationMessage
            (LocationContainer locationContainer, AlacarteContainer alacarteContainer,
             ManagementContainer managementContainer, SituationContainer situationContainer){
        this.alacarteContainer = alacarteContainer;
        this.locationContainer = locationContainer;
        this.managementContainer = managementContainer;
        this.situationContainer = situationContainer;
    }

    public AlacarteContainer getAlacarteContainer() {
        return alacarteContainer;
    }

    public LocationContainer getLocationContainer() {
        return locationContainer;
    }

    public ManagementContainer getManagementContainer() {
        return managementContainer;
    }

    public SituationContainer getSituationContainer() {
        return situationContainer;
    }

    public void setAlacarteContainer(AlacarteContainer alacarteContainer) {
        this.alacarteContainer = alacarteContainer;
    }

    public void setLocationContainer(LocationContainer locationContainer) {
        this.locationContainer = locationContainer;
    }

    public void setManagementContainer(ManagementContainer managementContainer) {
        this.managementContainer = managementContainer;
    }

    public void setSituationContainer(SituationContainer situationContainer) {
        this.situationContainer = situationContainer;
    }

    @Override
    public String toString() {
        return "DecentralizedEnvironmentalNotificationMessage{" + "\n" +
                "managementContainer=" + managementContainer + "\n" +
                ", situationContainer=" + situationContainer + "\n" +
                ", locationContainer=" + locationContainer + "\n" +
                ", alacarteContainer=" + alacarteContainer + "\n" +
                '}';
    }
}
