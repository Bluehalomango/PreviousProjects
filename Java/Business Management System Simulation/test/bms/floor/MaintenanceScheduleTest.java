package bms.floor;

import bms.room.Room;
import bms.room.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MaintenanceScheduleTest {
    private MaintenanceSchedule mainScd;
    private MaintenanceSchedule nullScd;

    private Room roomBasic;
    private Room roomComp;
    private Room roomMore;

    private List<Room> roomList;


    @Before
    public void setup() {
        roomBasic = new Room(1, RoomType.STUDY, 5);
        roomComp = new Room(2, RoomType.LABORATORY, 17.25);
        roomMore = new Room(3, RoomType.OFFICE, 10);
        roomList = new ArrayList<>();
        roomList.add(roomBasic);
        roomList.add(roomComp);
        roomList.add(roomMore);

        mainScd = new MaintenanceSchedule(roomList);
    }

    @Test (expected = NullPointerException.class)
    public void maintenanceTestNull() {
        nullScd = new MaintenanceSchedule(null);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void maintenanceTestIndex() {
        roomList = new ArrayList<>();
        nullScd = new MaintenanceSchedule(roomList);
    }

    @Test (expected = NullPointerException.class)
    public void getMaintenanceTimeNull() {
        mainScd.getMaintenanceTime(null);
    }

    @Test
    public void getMaintenanceTimeTest1() {
        assertEquals(5, mainScd.getMaintenanceTime(roomBasic));
    }

    @Test
    public void getMaintenanceTimeTestStudyDiscrep() {
        roomBasic = new Room(1, RoomType.STUDY, 10);
        assertEquals(6, mainScd.getMaintenanceTime(roomBasic));
    }

    @Test
    public void getMaintenanceTimeTestOfficeDiscrep() {
        roomBasic = new Room(1, RoomType.OFFICE, 10);
        assertEquals(7, mainScd.getMaintenanceTime(roomBasic));
    }

    @Test
    public void getMaintenanceTimeTestOfficeDiscrep2() {
        roomBasic = new Room(1, RoomType.OFFICE, 9);
        assertEquals(6, mainScd.getMaintenanceTime(roomBasic));
    }

    @Test
    public void getMaintenanceTimeTestLabDiscrep() {
        roomBasic = new Room(1, RoomType.LABORATORY, 10);
        assertEquals(7, mainScd.getMaintenanceTime(roomBasic));
    }

    @Test
    public void getMaintenanceTimeTestOfficeRound() {
        roomBasic = new Room(1, RoomType.OFFICE, 6);
        assertEquals(5, mainScd.getMaintenanceTime(roomBasic));
    }

    @Test
    public void getMaintenanceTimeTestLabLarge() {
        roomBasic = new Room(1, RoomType.LABORATORY, 47);
        assertEquals(22, mainScd.getMaintenanceTime(roomBasic));
    }


    @Test
    public void getMaintenanceTimeTestLabRound() {
        roomBasic = new Room(1, RoomType.LABORATORY, 6.5);
        assertEquals(6, mainScd.getMaintenanceTime(roomBasic));
    }

    @Test
    public void getCurrentRoomTestBasic() {
        assertEquals(roomBasic, mainScd.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomTestComp() {
        for (int i = 0; i <= mainScd.getMaintenanceTime(mainScd.getCurrentRoom()); i++) {
            mainScd.elapseOneMinute();
        }
        assertEquals(roomComp, mainScd.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomTestMore() {
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        assertEquals(roomMore, mainScd.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomTestBasicRep() {
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        assertEquals(roomBasic, mainScd.getCurrentRoom());
    }

    @Test
    public void getCurrentRoomTestMoreRep() {
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        assertEquals(roomMore, mainScd.getCurrentRoom());
    }

    @Test (expected = NullPointerException.class)
    public void getCurrentRoomTestNull() {
        assertNull(nullScd.getCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomTest1() {
        mainScd.elapseOneMinute();
        assertEquals(1, mainScd.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomTest3() {
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals(3, mainScd.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomTest5() {
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals(5, mainScd.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomTestLap() {
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals(0, mainScd.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomTest2LapSecondRoom() {
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals(7, mainScd.getTimeElapsedCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoomTestEvac() {
        roomBasic.setFireDrill(true);
        roomBasic.evaluateRoomState();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals(0, mainScd.getTimeElapsedCurrentRoom());
    }

    @Test
    public void encodeTestBasic() {
        assertEquals("1,2,3", mainScd.encode());
    }

    @Test
    public void encodeTestSingle() {
        roomList = new ArrayList<>();
        roomList.add(roomBasic);
        mainScd = new MaintenanceSchedule(roomList);
        assertEquals("1", mainScd.encode());
    }

    @Test
    public void encodeTestRepeats() {
        roomList = new ArrayList<>();
        roomList.add(roomBasic);
        roomList.add(roomComp);
        roomList.add(roomBasic);
        roomList.add(roomComp);
        mainScd = new MaintenanceSchedule(roomList);
        assertEquals("1,2,1,2", mainScd.encode());
    }

    @Test
    public void encodeTestRepeatsMany() {
        roomList = new ArrayList<>();
        roomList.add(roomBasic);
        roomList.add(roomComp);
        roomList.add(roomBasic);
        roomList.add(roomComp);
        roomList.add(roomBasic);
        roomList.add(roomComp);
        roomList.add(roomMore);
        roomList.add(roomComp);
        roomList.add(roomMore);
        mainScd = new MaintenanceSchedule(roomList);
        assertEquals("1,2,1,2,1,2,3,2,3", mainScd.encode());
    }

    @Test
    public void toStringTest() {
        assertEquals("MaintenanceSchedule: currentRoom=#1, currentElapsed=0", mainScd.toString());
    }

    @Test
    public void toStringTestTime() {
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals("MaintenanceSchedule: currentRoom=#1, currentElapsed=3", mainScd.toString());
    }

    @Test
    public void toStringTestChange() {
        mainScd.skipCurrentMaintenance();
        assertEquals("MaintenanceSchedule: currentRoom=#2, currentElapsed=0", mainScd.toString());
    }

    @Test
    public void toStringTestTimeSkip() {
        mainScd.skipCurrentMaintenance();
        mainScd.skipCurrentMaintenance();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals("MaintenanceSchedule: currentRoom=#3, currentElapsed=3", mainScd.toString());
    }

    @Test
    public void toStringTestTimeFull() {
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        mainScd.elapseOneMinute();
        assertEquals("MaintenanceSchedule: currentRoom=#2, currentElapsed=0", mainScd.toString());
    }

    @Test (expected = NullPointerException.class)
    public void toStringTestNull() {
        nullScd.toString();
    }

}