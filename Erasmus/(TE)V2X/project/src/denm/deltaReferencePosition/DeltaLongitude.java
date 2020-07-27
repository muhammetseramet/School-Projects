package denm.deltaReferencePosition;

public class DeltaLongitude {

    private int deltaLongitude;

    public DeltaLongitude(){
        deltaLongitude = 131072;
    }

    public DeltaLongitude(int deltaLongitude){
        this.deltaLongitude = deltaLongitude;
    }

    public int getDeltaLongitude() {
        return deltaLongitude;
    }

    public void setDeltaLongitude(int deltaLongitude) {
        this.deltaLongitude = deltaLongitude;
    }

    @Override
    public String toString(){
        return "Delta Longitude{" +
                "delta longitude=" + deltaLongitude +
                '}';
    }
}
