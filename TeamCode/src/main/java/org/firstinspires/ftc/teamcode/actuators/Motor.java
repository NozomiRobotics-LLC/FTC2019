package org.firstinspires.ftc.teamcode.actuators;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by LBYPatrick on 11/9/2017.
 */

public class Motor {

            private     double  speedLimit = 1.0;
            private     double  motorSpeed = 0;
            private     DcMotor motor;
            private     boolean isForward = true;

    public Motor(DcMotor dcMotorObj, boolean isForward) {

        motor = dcMotorObj;
        motor.setDirection(DcMotor.Direction.FORWARD);
        this.isForward = isForward;
    }

    public void setReverse(boolean isReverse) {
        this.isForward = !isReverse;
    }

    public Motor(DcMotor dcMotorObj) {
        this(dcMotorObj, true);
    }

    public Motor(HardwareMap hwMap, String deviceName) {
        this(hwMap.dcMotor.get(deviceName));
    }

    public Motor(HardwareMap hwMap, String deviceName, boolean isForward) {
        this(hwMap.dcMotor.get(deviceName),isForward);
    }

    private double getLimitedSpeed(double rawSpeed) {return rawSpeed * speedLimit;}

    public void moveWithButton(boolean up, boolean down) {

        if (up == down) motor.setPower(0);
        else { motor.setPower(getLimitedSpeed(up? 1 : -1)); }
    }

    public void move(double value) {
        motor.setPower(getLimitedSpeed(value)*(isForward? 1 : -1));
    }

    public double getPower() { return motor.getPower(); }

    public double getSpeed() {
        return getPower();
    }

    public int getPosition() {
        return motor.getCurrentPosition();
    }

    public int getCurrentPosition() {
        return getPosition();
    }

    public void updateSpeedLimit(double speed) { speedLimit = speed; }
}
