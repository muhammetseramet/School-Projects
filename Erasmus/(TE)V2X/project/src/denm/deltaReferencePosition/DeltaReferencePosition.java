package denm.deltaReferencePosition;

public class DeltaReferencePosition {

    private DeltaAltitude deltaAltitude;
    private DeltaLatitude deltaLatitude;
    private DeltaLongitude deltaLongitude;
    private PathDeltaTime pathDeltaTime;

    public DeltaReferencePosition(){}

    public DeltaReferencePosition (DeltaAltitude deltaAltitude, DeltaLatitude deltaLatitude,
                                   DeltaLongitude deltaLongitude, PathDeltaTime pathDeltaTime){
        this.deltaAltitude = deltaAltitude;
        this.deltaLatitude = deltaLatitude;
        this.deltaLongitude = deltaLongitude;
        this.pathDeltaTime = pathDeltaTime;
    }

    public DeltaAltitude getDeltaAltitude() {
        return deltaAltitude;
    }

    public DeltaLatitude getDeltaLatitude() {
        return deltaLatitude;
    }

    public DeltaLongitude getDeltaLongitude() {
        return deltaLongitude;
    }

    public PathDeltaTime getPathDeltaTime() {
        return pathDeltaTime;
    }

    public void setDeltaAltitude(DeltaAltitude deltaAltitude) {
        this.deltaAltitude = deltaAltitude;
    }

    public void setDeltaLatitude(DeltaLatitude deltaLatitude) {
        this.deltaLatitude = deltaLatitude;
    }

    public void setDeltaLongitude(DeltaLongitude deltaLongitude) {
        this.deltaLongitude = deltaLongitude;
    }

    public void setPathDeltaTime(PathDeltaTime pathDeltaTime) {
        this.pathDeltaTime = pathDeltaTime;
    }

    @Override
    public String toString(){
        return "DeltaReferencePosition{" + "\n" +
                "\t\tdeltaAltitude=" + deltaAltitude + "\n" +
                "\t\tdeltaLatitude=" + deltaLatitude + "\n" +
                "\t\tdeltaLongitude=" + deltaLongitude + "\n" +
                "\t\tpathDeltaTime=" + pathDeltaTime + "\n" +
                '}';
    }

}
