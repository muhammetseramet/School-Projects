package denm.elements.event;

import denm.deltaReferencePosition.DeltaReferencePosition;

public class EventPoint {

    private InformationQuality informationQuality;
    private DeltaReferencePosition deltaReferencePosition;

    public EventPoint(){}

    public EventPoint(InformationQuality informationQuality,
                      DeltaReferencePosition deltaReferencePosition){
        this.informationQuality = informationQuality;
        this.deltaReferencePosition = deltaReferencePosition;
    }

    public InformationQuality getInformationQuality() {
        return informationQuality;
    }

    public DeltaReferencePosition getDeltaReferencePosition() {
        return deltaReferencePosition;
    }

    public void setInformationQuality(InformationQuality informationQuality) {
        this.informationQuality = informationQuality;
    }

    public void setDeltaReferencePosition(DeltaReferencePosition deltaReferencePosition) {
        this.deltaReferencePosition = deltaReferencePosition;
    }

    @Override
    public String toString(){
        return "EventPoint{" + "\n" +
                "\t\tinformationQuality=" + informationQuality + "\n" +
                "\t\tdeltaReferencePosition=" + deltaReferencePosition + "\n" +
                '}';
    }

}
