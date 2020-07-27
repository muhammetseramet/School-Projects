package denm.deltaReferencePosition;

public class PathDeltaTime {

    private int pathdeltaTime;

    public int getPathdeltaTime() {
        return pathdeltaTime;
    }

    public void setPathdeltaTime(int pathdeltaTime) {
        this.pathdeltaTime = pathdeltaTime;
    }

    @Override
    public String toString(){
        return "Path Delta Time{" +
                "path delta time=" + pathdeltaTime +
                '}';
    }

}
