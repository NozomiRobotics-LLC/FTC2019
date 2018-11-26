package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.actuators.DriveTrain;
import org.firstinspires.ftc.teamcode.actuators.Motor;
import org.firstinspires.ftc.teamcode.actuators.XboxGP;
import org.firstinspires.ftc.teamcode.actuators.IMU;

@TeleOp(name = "Stem A(Experimental)", group = "Linear opmode")

public class StemA_Exp extends LinearOpMode {

    @Override
    public void runOpMode() {

        ElapsedTime runtime = new ElapsedTime();

        DriveTrain chassis = new DriveTrain(new Motor(hardwareMap, "front_left_motor"),
                new Motor(hardwareMap, "front_right_motor"));
        XboxGP gp1 = new XboxGP(gamepad1);
        Motor arm = new Motor(hardwareMap, "arm");
        IMU revHub = new IMU(hardwareMap);
        chassis.setWheelMode(DriveTrain.WheelMode.NORMAL);

        int armMin = 3000, armMax = 15500;

        boolean isArmFree = false;
        boolean isArmChecked = true;


        telemetry.addLine("Sucessfully Initialized.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            gp1.fetchData();

            //Basic Driving
            if (gp1.isKeysChanged(XboxGP.LT, XboxGP.RT, XboxGP.jLeftX)) {
                chassis.drive(0, -gp1.getValue(XboxGP.RT) + gp1.getValue(XboxGP.LT), gp1.getValue(XboxGP.jLeftX));
            }

            if (gp1.isKeyToggled(XboxGP.Y)) {
                isArmFree = !isArmFree;
            }

            if (gp1.isKeyToggled(XboxGP.LB)) {
                armMin = arm.getPosition();
                isArmChecked = false;
            }
            if (gp1.isKeyToggled(XboxGP.RB)) {
                armMax = arm.getPosition();
                isArmChecked = false;
            }

            //Check Arm bounds
            if (!isArmChecked) {
                if (armMax < armMin) {
                    int temp = armMax;
                    armMax = armMin;
                    armMin = temp;
                }
                isArmChecked = true;
            }

            double speed = -gp1.getValue(XboxGP.jRightY);
            double reading = arm.getPosition();

            //Free Mode
            if (isArmFree) {
                arm.move(speed);
            } else if (reading >= armMax) {
                arm.move(speed > 0 ? 0 : speed);
            } else if (reading <= armMin) {
                arm.move(speed < 0 ? 0 : speed);
            } else {
                arm.move(speed);
            }

            IMU.IMUData dataBuffer = revHub.getAll();

            telemetry.addData("Arm encoder reading",arm.getPosition());
            telemetry.addData("Arm lower bound",armMin);
            telemetry.addData("Arm higher bound: ",armMax);
            telemetry.addData("Left Wheel: ",chassis.getSpeed(DriveTrain.Wheels.REAR_LEFT));
            telemetry.addData("Right Wheel: ",chassis.getSpeed(DriveTrain.Wheels.REAR_RIGHT));
            telemetry.addData("Left Joystick X",gp1.getValue(XboxGP.jLeftX));
            telemetry.addData("X acc", dataBuffer.xAccel);
            telemetry.addData("Y acc", dataBuffer.yAccel);
            telemetry.addData("Z acc",dataBuffer.zAccel);
            telemetry.update();
        }

        telemetry.addLine("Robot Stopped");
        telemetry.update();

    }
}
