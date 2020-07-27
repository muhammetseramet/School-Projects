package denm.containers;

import denm.elements.event.EventPoint;

public class LocationContainer {
    // TODO In my opinion its should be event point. We don't actually need event history.

    private EventPoint eventpoint;

    public LocationContainer(){}

    public LocationContainer(EventPoint eventPoint){
        this.eventpoint = eventPoint;
    }

    public EventPoint getEventpoint() {
        return eventpoint;
    }

    public void setEventpoint(EventPoint eventpoint) {
        this.eventpoint = eventpoint;
    }

    @Override
    public String toString(){
        return "Location Container{" + "\n" +
                "\teventPoint=" + eventpoint +
                '}';
    }

}
