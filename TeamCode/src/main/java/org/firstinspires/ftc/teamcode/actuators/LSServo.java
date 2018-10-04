package org.firstinspires.ftc.teamcode.actuators;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by LBYPatrick on 10/27/2017.
 */

final public class LSServo {


    //private static Servo servoObj = null;
    private double  minPos;
    private double  maxPos;
    private double  speedLimit  = 0.09;
    private boolean isContinuous = false;
    private Servo servo;


    //Constructors
    public LSServo(HardwareMap hwMap, String deviceName, boolean isForward, double min, double max) {
        this(hwMap.servo.get(deviceName), isForward,min,max);
    }

    public LSServo(Servo servoObject, boolean isForward, double min, double max) {
        minPos = min;
        maxPos = max;
        servo = servoObject;
        //servo.setDirection(Servo.Direction.FORWARD);
        servo.setDirection((isForward?Servo.Direction.FORWARD : Servo.Direction.REVERSE));

        //Run parameter check
        if(minPos > maxPos) {

            //Swap the values if min > max
            double temp = maxPos;
            maxPos = minPos;
            minPos = temp;
        }

        if(maxPos > 1) {
            maxPos = 1;
        }
        if(minPos < 0) {
            minPos = 0;
        }

    }

    //Move methods
    public void move(double position) {
        servo.setPosition(getLimitedPosition(position));
    }

    public void moveWithButton(boolean clockwise) {

        if(isContinuous) {
            move(clockwise? maxPos : minPos);
        }
        else {
            move(servo.getPosition() + (clockwise?speedLimit : -speedLimit));
        }
    }

    //Note that this method only works with continuous mode, and it's scaled from -1 to 1
    public void moveRotational(double speed) {
        if(speed < -1) {
            speed = -1;
        }
        else if(speed > 1) {
            speed = 1;
        }

        move(scale(-1,1,speed,minPos,maxPos));
    }

    //Other assisting methods
    public double getPos() {
        return servo.getPosition();
    }

    public double getMaxPosition() {
        return maxPos;
    }

    public void updateSpeedLimit(double speed) {
        speedLimit = speed;
    }

    public void setContinuous(boolean isContinuous) {
        this.isContinuous = isContinuous;
    }

    private double getLimitedSpeed(double speed) {
        return speed*speedLimit;
    }

    private double scale(double iMin, double iMax, double oMin, double oMax, double val) {

        double slope = (oMax - oMin) / (iMax - iMin);

        double distanceFromMin = val - iMin;

        return slope * distanceFromMin;

    }

    private double getLimitedPosition(double position) {return (position > maxPos ? maxPos : (position < minPos? minPos : position));}


}