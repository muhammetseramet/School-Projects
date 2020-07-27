package denm.elements.event;
import java.util.ArrayList;
import java.util.List;

public class EventHistory {

    private List<EventPoint> eventPoint;

    public EventHistory(){
        eventPoint = new ArrayList<EventPoint>(23);
    }

    public List<EventPoint> getEventPoint() {
        return eventPoint;
    }

    public void setEventPoint(List<EventPoint> eventPoint) {
        this.eventPoint = eventPoint;
    }

}
