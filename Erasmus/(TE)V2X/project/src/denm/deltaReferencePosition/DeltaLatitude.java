package denm.deltaReferencePosition;

public class DeltaLatitude {

    private int deltaLatitude;

    public DeltaLatitude(){
        deltaLatitude = 131072;
    }

    public DeltaLatitude(int deltaLatitude){
        this.deltaLatitude = deltaLatitude;
    }

    public int getDeltaLatitude() {
        return deltaLatitude;
    }

    public void setDeltaLatitude(int deltaLatitude) {
        this.deltaLatitude = deltaLatitude;
    }

    @Override
    public String toString(){
        return "Delta Latitude{" +
                "delta latitude=" + deltaLatitude +
                '}';
    }

}
