package bms.building;

import bms.exceptions.FileFormatException;
import bms.floor.MaintenanceSchedule;
import bms.room.Room;
import bms.room.RoomType;
import org.junit.Before;
import org.junit.Test;
import bms.building.BuildingInitialiser;
import bms.building.Building;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class BuildingInitialiserTest {
    @Before
    public void setup() {
    }

    @Test
    public void TestGood1() throws IOException, FileFormatException {
        assertEquals("[Building: name=\"General Purpose South\", floors=5, Building: name=\"Forgan Smith Building\", floors=1, Building: name=\"Andrew N. Liveris Building\", floors=1]", BuildingInitialiser.loadBuildings("saves/TestGood.txt").toString());
    }

    @Test
    public void TestGood2() throws IOException, FileFormatException {
        assertEquals("[Building: name=\"General Purpose South\", floors=0]", BuildingInitialiser.loadBuildings("saves/TestGoodBuilding.txt").toString());
    }

    @Test
    public void TestGood3() throws IOException, FileFormatException {
        assertEquals("[Building: name=\"General Purpose South\", floors=2, Building: name=\"Forgan Smith Building\", floors=1, Building: name=\"Andrew N. Liveris Building\", floors=1]",BuildingInitialiser.loadBuildings("saves/TestGoodFloor.txt").toString());
    }

    @Test
    public void TestGood4() throws IOException, FileFormatException {
        assertEquals("[Building: name=\"Forgan Smith Building\", floors=1]",BuildingInitialiser.loadBuildings("saves/TestGoodRooms.txt").toString());
    }

    @Test
    public void TestGood5() throws IOException, FileFormatException {
        assertEquals("[Building: name=\"General Purpose South\", floors=2]", BuildingInitialiser.loadBuildings("saves/TestGoodSensors.txt").toString());
    }

    @Test(expected = FileNotFoundException.class)
    public void Test1() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/Random.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test2() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestDoubleFail.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test3() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestDupeFloor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test4() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestDupeRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test5() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestDupeSensor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test6() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestEmptyFile.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test7() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestEmptyLine.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test8() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestFloorLenMin.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test9() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestFloorWidMin.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test10() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestFloorTooLarge.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test11() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInsuffSpace.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test12() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestIntFail.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test13() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvaldFreqLess.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test14() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvaldFreqMore.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test15() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvaldFreqNull.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test16() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvaldSensorType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test17() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvaldSensorTypeCap.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test18() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvalEval.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test19() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvalEvalCap.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test20() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvalRoomType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test21() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvalRoomTypeCap.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test22() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvalScd.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test23() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestInvalWeight.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test24() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestLessColon.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test25() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestLessColonRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test26() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestLessColonSensor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test27() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestLessFloors.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test28() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestLessRooms.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test29() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestLessSensors.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test30() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestMoreColon.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test31() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestMoreColonRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test32() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestMoreColonSensor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test33() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestMoreFloors.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test34() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestMoreRooms.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test35() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestMoreSensors.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test36() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNegCap.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test37() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNegFloor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test38() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNegIdeal.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test39() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNegReadings.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test40() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNegRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test41() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNegSensor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test42() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNegVar.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test43() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestNoBelow.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test44() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestRoomMin.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test45() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestScdRoomNum.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test46() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestVarGreaterIdeal.txt");
    }

    @Test(expected = FileFormatException.class)
    public void Test47() throws IOException, FileFormatException {
        BuildingInitialiser.loadBuildings("saves/TestZeroFloorNum.txt");
    }

}