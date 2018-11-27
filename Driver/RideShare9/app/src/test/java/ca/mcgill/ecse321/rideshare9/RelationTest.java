package ca.mcgill.ecse321.rideshare9;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.model.Advertisement;
import ca.mcgill.ecse321.rideshare9.model.Stop;
import ca.mcgill.ecse321.rideshare9.model.Vehicle;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RelationTest {
    Advertisement ad;
    Vehicle v;
    VehicleItem vi;
    @Before
    public void preLudeAdv() {

        this.v = new Vehicle();
        v.setModel("T80-BVM");
        v.setId(12);
        v.setColor("Green");
        v.setDriver(210);
        v.setLicencePlate("XUXUE666");
        v.setMaxSeat(3);
        this.vi = new VehicleItem();
        vi.setId(v.getId());
        vi.setMaxSeat(v.getMaxSeat());
        vi.setColor(v.getColor());
        vi.setLicencePlate(v.getLicencePlate());
        vi.setModel(v.getModel());
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop(1, "s1", 3.2F));
        stops.add(new Stop(2, "s2", 3.9F));
        stops.add(new Stop(3, "s3", 4.0F));
        stops.add(new Stop(4, "s4", 1.3F));
        this.ad = new Advertisement(2233, 3, v, 210, "montreal",  "2018-11-02 06:48:33", "montreal", "REGISTERING", stops);
    }
    @Test
    public void assertStops() {
        assertEquals(4.0F, ad.getStops().get(2).getPrice(), 0.01);
        assertEquals(2, ad.getStops().get(1).getId());
        assertEquals("s4", ad.getStops().get(3).getName());
    }
    @Test
    public void assertAdv() {
        assertEquals(210, ad.getDriverId());
        assertEquals("montreal", ad.getTitle());
        assertEquals(2233, ad.getId());
    }
    @Test
    public void assertVehicle() {
        assertEquals(12, ad.getVehicle().getId());
        assertEquals("Green", ad.getVehicle().getColor());
        assertEquals(3, ad.getVehicle().getMaxSeat());
        assertEquals(210, ad.getVehicle().getDriver());
        assertEquals("XUXUE666", ad.getVehicle().getLicencePlate());
        assertEquals("T80-BVM", ad.getVehicle().getModel());
    }
    @Test
    public void assertVehicleItem() {
        assertEquals(vi.getId().longValue(), ad.getVehicle().getId());
        assertEquals(vi.getColor(), ad.getVehicle().getColor());
        assertEquals(vi.getMaxSeat().intValue(), ad.getVehicle().getMaxSeat());
        assertEquals(vi.getLicencePlate(), ad.getVehicle().getLicencePlate());
        assertEquals(vi.getModel(), ad.getVehicle().getModel());
    }
}