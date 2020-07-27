package sample;

import denm.DENM;
import denm.containers.LocationContainer;
import denm.deltaReferencePosition.*;
import denm.elements.event.EventPoint;
import denm.elements.event.InformationQuality;
import denm.header.ItsPduHeader;
import denm.messagebody.DecentralizedEnvironmentalNotificationMessage;

public class Main {

    private static String neptunCode = "E3W1OW";
    private static String taskId = "DENM - B.23";

    public static void main(String[] args) {
        DENM denm = new DENM();

        ItsPduHeader itsPduHeader = new ItsPduHeader();
        DecentralizedEnvironmentalNotificationMessage decentralizedEnvironmentalNotificationMessage
                = new DecentralizedEnvironmentalNotificationMessage();

        LocationContainer locationContainer = new LocationContainer();
        EventPoint eventPoint = new EventPoint();
        InformationQuality informationQuality = new InformationQuality();
        DeltaReferencePosition deltaReferencePosition =
                new DeltaReferencePosition();

        DeltaAltitude deltaAltitude = new DeltaAltitude();
        DeltaLatitude deltaLatitude = new DeltaLatitude();
        DeltaLongitude deltaLongitude = new DeltaLongitude();
        PathDeltaTime pathDeltaTime = new PathDeltaTime();

        deltaReferencePosition.setDeltaAltitude(deltaAltitude);
        deltaReferencePosition.setDeltaLatitude(deltaLatitude);
        deltaReferencePosition.setDeltaLongitude(deltaLongitude);
        deltaReferencePosition.setPathDeltaTime(pathDeltaTime);

        eventPoint.setDeltaReferencePosition(deltaReferencePosition);
        eventPoint.setInformationQuality(informationQuality);

        locationContainer.setEventpoint(eventPoint);
        decentralizedEnvironmentalNotificationMessage.setLocationContainer(locationContainer);

        denm.setDecentralizedEnvironmentalNotificationMessage(decentralizedEnvironmentalNotificationMessage);

        System.out.println("Neptun code: " + neptunCode);
        System.out.println("TaskID: " + taskId);
        System.out.println("DENM message structure: ");

        System.out.println(denm.toString());
    }
}
