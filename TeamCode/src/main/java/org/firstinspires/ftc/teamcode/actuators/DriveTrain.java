package org.firstinspires.ftc.teamcode.actuators;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by LBYPatrick on 2017/11/3.
 */

final public class DriveTrain {

    /**
     * The positions of the wheels.
     */
    public enum Wheels{
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT,
    }

    private Motor FL                 = null;
    private Motor FR                 = null;
    private Motor BL = null;
    private Motor BR = null;
    private              double  maxSpeed           = 1.0;
    private              double  speedLevel         = 1.0;
    private              double  frontLeftPower     = 0;
    private              double  frontRightPower    = 0;
    private              double  rearLeftPower      = 0;
    private              double  rearRightPower     = 0;
    private              boolean is4WD              = false;
    private              WheelMode wheelMode = WheelMode.OMNI; //Omni as default

    /**
     * Drive Mode, can be Tank, Omni or Mecanum.
     */
    public enum WheelMode {
        NORMAL,
        OMNI,
        MECANUM,
        HYBRID_OMNI_TANK,
    }


    /**
     * Constructor for 4WD Mode using Motor class.
     * @param frontLeftMotor front left motor.
     * @param frontRightMotor front right motor.
     * @param rearLeftMotor back left motor.
     * @param rearRightMotor back right motor.
     */
    public DriveTrain(Motor frontLeftMotor, Motor frontRightMotor, Motor rearLeftMotor, Motor rearRightMotor) {

        FL = frontLeftMotor;
        FR = frontRightMotor;
        BL = rearLeftMotor;
        BR = rearRightMotor;

        FR.setReverse(true);
        BR.setReverse(true);

        is4WD = true;
    }

    /**
     * Constructor for 4WD Mode using the built-in DcMotor Class.
     * @param frontLeftMotor front left motor.
     * @param frontRightMotor front right motor.
     * @param rearLeftMotor back left motor.
     * @param rearRightMotor back right motor.
     */
    public DriveTrain(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor rearLeftMotor, DcMotor rearRightMotor) {
        this(new Motor(frontLeftMotor), new Motor(frontRightMotor), new Motor(rearLeftMotor),new Motor(rearRightMotor));
    }

    /**
     * Constructor for 2WD using DcMotor
     * @param leftMotor
     * @param rightMotor
     */
    public DriveTrain(DcMotor leftMotor, DcMotor rightMotor) {
        this(new Motor(leftMotor), new Motor(rightMotor));
    }


    /**
     * Constructor for 2WD using Motor
     * @param leftMotor
     * @param rightMotor
     */
    //Constructor for 2WD
    public DriveTrain(Motor leftMotor, Motor rightMotor) {

        BL = leftMotor;
        BR = rightMotor;

        BR.setReverse(true);

        is4WD = false;
        setWheelMode(WheelMode.NORMAL);
    }

    /**
     * Set wheel mode.
     * @param wheelMode To set which mode it is. There's DriveTrain.WheelMode for you to pass in.
     */
    public void setWheelMode(WheelMode wheelMode) {
        this.wheelMode = wheelMode;
    }

    /**
     * Drives the robot.
     * @param sideMove
     * @param forwardBack
     * @param rotation
     */
    public void drive(double sideMove, double forwardBack, double rotation) {
        switch(wheelMode) {
            case NORMAL:
                tankDrive(forwardBack,rotation);
                break;

            case OMNI:
                omniDrive(sideMove,forwardBack,rotation);
                break;
            case MECANUM:
                mecanumDrive(sideMove,forwardBack,rotation);

        }
    }

    /**
     * Tank Drive
     * @param forwardBack
     * @param rotation
     */
    public void tankDrive(double forwardBack, double rotation) {

        rotation = -rotation;

        //Calculate Adequate Power Level for motors
        rearLeftPower = Range.clip(forwardBack + rotation, -1.0, 1.0);
        rearRightPower = Range.clip(forwardBack - rotation, -1.0, 1.0);

        if (is4WD) {
            frontLeftPower = rearLeftPower;
            frontRightPower = rearRightPower;
        }
        //Pass calculated power level to motors
        BL.move(rearLeftPower);
        BR.move(rearRightPower);
        if(is4WD) {
            FL.move(frontLeftPower);
            FR.move(frontRightPower);
        }

    }

    /**
     * Omni Drive
     * @param sideMove
     * @param forwardBack
     * @param rotation
     */
    //From http://ftckey.com/programming/advanced-programming/
    public void omniDrive(double sideMove, double forwardBack, double rotation) {

        sideMove = -sideMove;

        FR.move(getLimited(forwardBack+sideMove-rotation));
        FL.move(getLimited(forwardBack-sideMove+rotation));
        BR.move(getLimited(forwardBack-sideMove-rotation));
        BL.move(getLimited(forwardBack+sideMove+rotation));
    }

    private double getLimited(double value) {
        if(value < -1) return -1;
        else if(value > 1) return 1;
        else return value;
    }

    /**
     * Mecanum Drive
     * @param sideMove
     * @param forwardBack
     * @param rotation
     */
    public void mecanumDrive(double sideMove, double forwardBack, double rotation) {

        //A little Math from https://ftcforum.usfirst.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example
        final double r = Math.hypot(sideMove, forwardBack);
        final double robotAngle = Math.atan2(forwardBack, sideMove) - Math.PI / 4;

        frontLeftPower = r * Math.cos(robotAngle) + rotation;
        frontRightPower = r * Math.sin(robotAngle) - rotation;
        rearLeftPower = r * Math.sin(robotAngle) + rotation;
        rearRightPower = r * Math.cos(robotAngle) - rotation;

        // Send calculated power to motors
        FL.move(frontLeftPower);
        FR.move(frontRightPower);
        BL.move(rearLeftPower);
        BR.move(rearRightPower);
    }

    /**
     * Updates speed limit.
     * @param speed needs to be greater than 0, otherwise the robot would move backwards or stop moving if it is 0
     */
    public void updateSpeedLimit(double speed) {

        if(is4WD) {
            FL.updateSpeedLimit(speed);
            FR.updateSpeedLimit(speed);
        }
        BL.updateSpeedLimit(speed);
        BR.updateSpeedLimit(speed);
    }

    /**
     * Gets motor encoders' readings.
     * @param position the position of the Wheel
     * @return the motor encoder reading of the specified wheel.
     */
    public double getEncoderInfo(Wheels position) {
        switch (position) {
            case FRONT_LEFT  : return FR.getCurrentPosition();
            case FRONT_RIGHT : return FL.getCurrentPosition();
            case REAR_LEFT   : return BL.getCurrentPosition();
            case REAR_RIGHT  : return BR.getCurrentPosition();
        }
        return 256;
    }

    /**
     * Gets motors' speeds.
     * @param position the position of the Wheel
     * @return the motor power reading of the specified wheel.
     */
    public double getSpeed(Wheels position) {
        switch (position) {
            case FRONT_LEFT  : return FR.getPower();
            case FRONT_RIGHT : return FL.getPower();
            case REAR_LEFT   : return BL.getPower();
            case REAR_RIGHT  : return BR.getPower();
            }
        return 256;
    }

    /**
     *
     * @return whether the DriveTrain is 4WD.
     */
    public boolean get4WDStat() {return is4WD;}

    /**
     * Omni + Tank hybrid drive (front omni, back tank)
     * @param forwardBack
     * @param rotation
     */

    public void omniTankDrive(double forwardBack, double rotation) {

        rearLeftPower = Range.clip(forwardBack + rotation, -1.0, 1.0);
        rearRightPower = Range.clip(forwardBack - rotation, -1.0, 1.0);

        frontLeftPower = forwardBack;
        frontRightPower = forwardBack;

        FL.move(frontLeftPower);
        FR.move(frontRightPower);
        BL.move(rearLeftPower);
        BR.move(rearRightPower);
    }

    /**
     * Pretty self-explanatory...
     * @return wheel mode.
     */

    public WheelMode getWheelMode() {
        return wheelMode;
    }
}