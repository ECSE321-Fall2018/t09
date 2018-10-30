package ca.mcgill.ecse321.rideshare9.user;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONObject;

public class Stop implements Parcelable {
    private long id;
    private String name;
    private float price;

    public static final Parcelable.Creator<Stop> CREATOR = new Creator<Stop>() {
        @Override
        public Stop createFromParcel(Parcel source) {
            return new Stop(source);
        }

        @Override
        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };

    public Stop() {
    }

    public Stop(Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.price = parcel.readFloat();
    }

    public Stop(long id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return "{id: " + getId() + ", name: " + getName() + ", price: " + getPrice() + "}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public static Stop stopFromJSONObject(JSONObject jsonStopObject) {
        long stopId = jsonStopObject.optLong("id");
        String stopName = jsonStopObject.optString("stopName");
        float stopPrice = (float) jsonStopObject.optDouble("price");

        return new Stop(stopId, stopName, stopPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeFloat(price);
    }
}
