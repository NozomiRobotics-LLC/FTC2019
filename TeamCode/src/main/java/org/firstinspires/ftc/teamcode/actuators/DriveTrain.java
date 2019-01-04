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
    public enum Wheels {
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT,
    }

    private Motor FL = null;
    private Motor FR = null;
    private Motor BL = null;
    private Motor BR = null;
    private double maxSpeed = 1.0;
    private double speedLevel = 1.0;
    private double frontLeftPower = 0;
    private double frontRightPower = 0;
    private double rearLeftPower = 0;
    private double rearRightPower = 0;
    private boolean is4WD = false;
    private WheelMode wheelMode = WheelMode.OMNI; //Omni as default


    public enum WheelMode {
        NORMAL,
        OMNI,
        MECANUM,
        HYBRID_OMNI_TANK,
    }

    public DriveTrain(Motor frontLeftMotor, Motor frontRightMotor, Motor rearLeftMotor, Motor rearRightMotor) {

        FL = frontLeftMotor;
        FR = frontRightMotor;
        BL = rearLeftMotor;
        BR = rearRightMotor;
        FR.setReverse(true);
        BR.setReverse(true);

        is4WD = true;
    }

    public DriveTrain(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor rearLeftMotor, DcMotor rearRightMotor) {
        this(new Motor(frontLeftMotor), new Motor(frontRightMotor), new Motor(rearLeftMotor), new Motor(rearRightMotor));
    }

    public DriveTrain(DcMotor leftMotor, DcMotor rightMotor) {
        this(new Motor(leftMotor), new Motor(rightMotor));
    }

    public DriveTrain(Motor leftMotor, Motor rightMotor) {

        BL = leftMotor;
        BR = rightMotor;

        BR.setReverse(true);

        is4WD = false;
        setWheelMode(WheelMode.NORMAL);
    }

    public WheelMode getWheelMode() {
        return wheelMode;
    }

    private double getLimitedSpeed(double value) {
        if (value < -1) return -1;
        else if (value > 1) return 1;
        else return value;
    }


    public double getEncoderInfo(Wheels position) {
        switch (position) {
            case FRONT_LEFT:
                return FR.getCurrentPosition();
            case FRONT_RIGHT:
                return FL.getCurrentPosition();
            case REAR_LEFT:
                return BL.getCurrentPosition();
            case REAR_RIGHT:
                return BR.getCurrentPosition();
        }
        return 256;
    }

    public Motor getWheel(Wheels position) {
        switch(position) {
            case FRONT_LEFT:
                return FL;
            case FRONT_RIGHT:
                return FR;
            case REAR_LEFT:
                return BL;
            case REAR_RIGHT:
                return BR;
        }
        return null;
    }

    public double getSpeed(Wheels position) {
        switch (position) {
            case FRONT_LEFT:
                return FR.getPower();
            case FRONT_RIGHT:
                return FL.getPower();
            case REAR_LEFT:
                return BL.getPower();
            case REAR_RIGHT:
                return BR.getPower();
        }
        return 256;
    }

    public boolean is4WD() {
        return is4WD;
    }

    public void setSpeedLimit(double speed) {

        if (is4WD) {
            FL.setSpeedLimit(speed);
            FR.setSpeedLimit(speed);
        }
        BL.setSpeedLimit(speed);
        BR.setSpeedLimit(speed);
    }

    public void setWheelMode(WheelMode wheelMode) {
        this.wheelMode = wheelMode;
    }


    public void drive(double sideMove, double forwardBack, double rotation) {
        switch (wheelMode) {
            case NORMAL:
                tankDrive(forwardBack, rotation);
                break;
            case OMNI:
                omniDrive(sideMove, forwardBack, rotation);
                break;
            case MECANUM:
                mecanumDrive(sideMove, forwardBack, rotation);
                break;
            case HYBRID_OMNI_TANK:
                omniTankDrive(forwardBack,rotation);
                break;
        }
    }

    public void flipHead() {

        BL.setReverse(!BL.isReverse());
        BR.setReverse(!BR.isReverse());

        if(is4WD) {
            FL.setReverse(!FL.isReverse());
            FR.setReverse(!FR.isReverse());
        }
    }

    public void tankDrive(double forwardBack, double rotation) {

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
        if (is4WD) {
            FL.move(frontLeftPower);
            FR.move(frontRightPower);
        }

    }

    //From http://ftckey.com/programming/advanced-programming/
    public void omniDrive(double sideMove, double forwardBack, double rotation) {

        sideMove = -sideMove;

        FR.move(getLimitedSpeed(forwardBack + sideMove - rotation));
        FL.move(getLimitedSpeed(forwardBack - sideMove + rotation));
        BR.move(getLimitedSpeed(forwardBack - sideMove - rotation));
        BL.move(getLimitedSpeed(forwardBack + sideMove + rotation));
    }

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

    public void omniTankDrive(double forwardBack, double rotation) {

        rearLeftPower = Range.clip(forwardBack + rotation, -1.0, 1.0);
        rearRightPower = Range.clip(forwardBack - rotation, -1.0, 1.0);

        frontLeftPower = Range.clip(forwardBack - rotation, -1.0, 1.0);;
        frontRightPower = Range.clip(forwardBack + rotation, -1.0, 1.0);;

        FL.move(frontLeftPower);
        FR.move(frontRightPower);
        BL.move(rearLeftPower);
        BR.move(rearRightPower);
    }
}