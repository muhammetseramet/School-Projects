package denm.deltaReferencePosition;

public class DeltaAltitude {

    private int deltaAltitude;

    public DeltaAltitude(){
        deltaAltitude = 12800;
    }

    public DeltaAltitude(int deltaAltitude){
        this.deltaAltitude = deltaAltitude;
    }

    public int getDeltaAltitude() {
        return deltaAltitude;
    }

    public void setDeltaAltitude(int deltaAltitude) {
        this.deltaAltitude = deltaAltitude;
    }

    @Override
    public String toString(){
        return "Delta Altitude{" +
                "delta altitude=" + deltaAltitude +
                '}';
    }
}
