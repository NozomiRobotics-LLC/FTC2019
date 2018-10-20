package org.firstinspires.ftc.teamcode.actuators;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class IMU {

    public static class IMUData {
        public IMUData(String [] strs, double [] nums) {
            status = strs[0];
            calibration = strs[1];
            heading = nums[0];
            rolling = nums[1];
            pitch = nums[2];
            xAccel = nums[3];
            yAccel = nums[4];
            zAccel = nums[5];
        }
        public String status,
                      calibration;
        public double heading,
                      rolling,
                      pitch,
                      xAccel,
                      yAccel,
                      zAccel;

    }

    public enum IMUDataType {
        STATUS,
        CALIBRATION,
        HEADING,
        ROLLING,
        PITCH,
        XACCEL,
        YACCEL,
        ZACCEL
    }

    IMUData dataBuffer;

    //Stuff for getting data from IMU
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    public IMU(HardwareMap hwMap) {
        imu = hwMap.get(BNO055IMU.class, "imu"); //RevHub
        init();
    }
    public IMU(HardwareMap hwMap, String deviceName) {
        imu = hwMap.get(BNO055IMU.class, deviceName);
        init();
    }

    private void init() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);
    }

    public IMUData getAll () {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity  = imu.getGravity();
        return new IMUData(
                new String [] {
                    imu.getSystemStatus().toShortString(),
                    imu.getCalibrationStatus().toString()
                },
                new double [] {
                        AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES,angles.firstAngle)),
                        AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES,angles.secondAngle)),
                        AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(AngleUnit.DEGREES,angles.thirdAngle)),
                        gravity.xAccel,
                        gravity.yAccel,
                        gravity.zAccel
                });
    }
}
