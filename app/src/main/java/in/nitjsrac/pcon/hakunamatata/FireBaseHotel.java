package in.nitjsrac.pcon.hakunamatata;

public class FireBaseHotel{
    double lat, longi;
    String name;

    public FireBaseHotel(double lat, double longi, String name){
        this.lat = lat;
        this.longi = longi;
        this.name = name;
    }

    public FireBaseHotel(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

}