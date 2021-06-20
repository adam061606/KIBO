papackage jp.jaxa.iss.kibo.rpc.sampleapk;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        // astrobee is undocked and the mission starts
        api.startMission();

        // astrobee is undocked and the mission starts (position move from move from dock to point A)
        moveToWrapper(11.21, -9.8, 4.79, 0, 0, -0.707, 0.707);

        // astrobee scans the qr code and retrieves information of point A'
        Bitmap image = api.getBitmapNavCam();
        String content = readQR(image);
        api.sendDiscoveredQR(content);
        Scanner content = new Scanner(content);
        Scanner line;
        int p;
        float x;
        float y;
        float z;
        while(content.hasNext()){
            line = new Scanner(content.nextLine());
            p = line.nextInt();
            x = line.nextFloat();
            y = line.nextFloat();
            z = line.nextFloat();
            System.out.println("p: "+p+", x: "+x+", y: "+y+", z: "+z)
        }

        // move towards point A' based on pattern retrieved

        // callibrate to locate ar tags

        // callibrate to face the target centre

        // irradiate the laser
        api.laserControl(true);

        // take snapshots
        api.takeSnapshot();

        // turn off the laser
        api.laserControl(false);

        // move to the rear of Bay7
        moveToWrapper(10.6, -8.0, 4.5, 0, -0, -0.707, 0.707);

        // Send mission completion
        api.reportMissionCompletion();
    }

    @Override
    protected void runPlan2(){
        // write here your plan 2
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    // You can add your method
    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){

        final int LOOP_MAX = 3;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while(!result.hasSucceeded() || loopCounter < LOOP_MAX){
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
    }

}

