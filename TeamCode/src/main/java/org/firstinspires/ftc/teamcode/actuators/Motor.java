package org.firstinspires.ftc.teamcode.actuators;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by LBYPatrick on 11/9/2017.
 */

public class Motor {

    public enum OpMode {
        ENCODER,
        POWER
    }

    private double speedLimit = 1.0;
    private OpMode opMode = Motor.OpMode.POWER;
    private DcMotor motor;

    //Constructors

    public Motor(DcMotor dcMotorObj, boolean isForward) {

        motor = dcMotorObj;
        motor.setDirection(DcMotor.Direction.FORWARD);
    }

    public Motor(DcMotor dcMotorObj) {
        this(dcMotorObj, true);
    }

    public Motor(HardwareMap hwMap, String deviceName) {
        this(hwMap.dcMotor.get(deviceName));
    }

    public Motor(HardwareMap hwMap, String deviceName, boolean isForward) {
        this(hwMap.dcMotor.get(deviceName), isForward);
    }


    //Setters

    public void setOpMode(OpMode opMode) {
        this.opMode = opMode;
        switch (opMode) {
            case ENCODER:
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                break;
            case POWER:
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                break;
        }
    }

    public void setReverse(boolean isReverse) {
        motor.setDirection(isReverse ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
    }

    public void setSpeedLimit(double speed) {
        speedLimit = speed;
    }

    //Getters

    private double getLimitedSpeed(double rawSpeed) {
        return rawSpeed * speedLimit;
    }

    public boolean isMotorBusy() {
        return motor.isBusy();
    }

    public double getPower() {
        return motor.getPower();
    }

    public double getSpeed() {
        return getPower();
    }

    public int getPosition() {
        return motor.getCurrentPosition();
    }

    public int getCurrentPosition() {
        return getPosition();
    }


    //Others

    public void moveWithButton(boolean up, boolean down) {

        if (up == down) motor.setPower(0);
        else {
            motor.setPower(getLimitedSpeed(up ? 1 : -1));
        }
    }

    public void moveWithEncoder(double speed, int location) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor.setTargetPosition(motor.getCurrentPosition() + location);

        motor.setPower(Math.abs(speed));
    }

    public void move(double value) {

        if (opMode != OpMode.POWER) {
            setOpMode(OpMode.POWER);
        }

        motor.setPower(getLimitedSpeed(value));
    }
}
