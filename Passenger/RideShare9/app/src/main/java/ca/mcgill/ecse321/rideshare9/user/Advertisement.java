package ca.mcgill.ecse321.rideshare9.user;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.HttpUtils;

public class Advertisement {

    private long id;
    private int availableSeats;
    private int vehicleId;
    private int driverId;
    private String title;
    private String startTime;
    private String startLocation;
    private String status;
    private List<Stop> stops;

    public Advertisement() {
    }

    public Advertisement(int id, int availableSeats, int vehicleId, int driverId, String title,
                         String startTime, String startLocation, String status, List<Stop> stops) {
        this.id = id;
        this.availableSeats = availableSeats;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.title = title;
        this.startTime = startTime;
        this.startLocation = startLocation;
        this.status = status;
        this.stops = stops;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }

    public static List<Advertisement> AdvertisementsFromJSONArray(JSONArray jsonAdArray) {
        int adCount = jsonAdArray.length();
        List<Advertisement> advertisements = new ArrayList<>();

        for (int i = 0; i < adCount; i++) {
            JSONObject advertisement = jsonAdArray.optJSONObject(i);
            advertisements.add(advertisementFromJSONObject(advertisement));
        }

        return advertisements;
    }

    public static Stop StopFromJSONArray(JSONObject jsonStopObject) {
        long stopId = jsonStopObject.optLong("id");
        String stopName = jsonStopObject.optString("stopName");
        float stopPrice = (float) jsonStopObject.optDouble("price");
        return new Stop(stopId, stopName, stopPrice);
    }

    private static Advertisement advertisementFromJSONObject(JSONObject jsonAdObject) {
        int adId = jsonAdObject.optInt("id");
        int adSeatsAvailable = jsonAdObject.optInt("seatAvailable");
        int adVehicleId = jsonAdObject.optInt("vehicle");
        int adDriverId = jsonAdObject.optInt("driver");
        String adTitle = jsonAdObject.optString("title");
        String adStartTime = jsonAdObject.optString("startTime");
        String adStartLocation = jsonAdObject.optString("startLocation");
        String adStatus = jsonAdObject.optString("status");
        List<Stop> adStops = new ArrayList<>();

        JSONArray stops = jsonAdObject.optJSONArray("stops");
        List<Long> adStopIds = new ArrayList<>();

        //TODO Get stops and create Stop objects
        int stopCount = stops.length();

        for (int j = 0; j < stopCount; j++) {
            adStopIds.add(stops.optLong(j));
        }

        for (adStopId : adStopIds) {
            HttpUtils.get();
        }

        Advertisement newAdvertisement = new Advertisement(adId, adSeatsAvailable, adVehicleId,
                adDriverId, adTitle, adStartTime, adStartLocation, adStatus, adStopIds);
    }
}
