package denm.elements.event;

// Quality level of provided information
public class InformationQuality {

    // TODO range is between 0 and 7  {unavailable(0), lowest(1), highest(7)}
    private int informationQualityValue;

    public int getInformationQualityValue() {
        return informationQualityValue;
    }

    public void setInformationQualityValue(int informationQualityValue) {
        this.informationQualityValue = informationQualityValue;
    }

    @Override
    public String toString(){
        return "Information Quality Value{" +
                "information quality value=" + informationQualityValue +
                '}';
    }
}
