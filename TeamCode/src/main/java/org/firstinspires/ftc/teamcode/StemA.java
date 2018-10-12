package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.actuators.LSGamepad;
import org.firstinspires.ftc.teamcode.actuators.LSDrive;
import org.firstinspires.ftc.teamcode.actuators.LSMotor;
import org.firstinspires.ftc.teamcode.actuators.LSServo;

@TeleOp(name = "Stem A", group = "Linear opmode")

public class StemA extends LinearOpMode {

    @Override
    public void runOpMode() {

        ElapsedTime runtime = new ElapsedTime();

        LSDrive chassis = new LSDrive(new LSMotor(hardwareMap, "front_left_motor"),
                new LSMotor(hardwareMap, "front_right_motor"));
        LSGamepad gp1 = new LSGamepad(gamepad1);
        LSMotor arm = new LSMotor(hardwareMap, "arm");
        chassis.setWheelMode(LSDrive.WheelMode.NORMAL_WHEEL);

        int armMin = 3000, armMax = 15500;

        boolean isArmFree = false;
        boolean isArmChecked = true;


        telemetry.addLine("Sucessfully Initialized.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            gp1.updateStatus();

            //Basic Driving
            if (gp1.isKeysChanged(LSGamepad.LT, LSGamepad.RT, LSGamepad.jLeftX)) {
                chassis.drive(0, -gp1.getValue(LSGamepad.RT) + gp1.getValue(LSGamepad.LT), gp1.getValue(LSGamepad.jLeftX));
            }

            if (gp1.isKeyToggled(LSGamepad.Y)) {
                isArmFree = !isArmFree;
            }

            if (gp1.isKeyToggled(LSGamepad.LB)) {
                armMin = arm.getPosition();
                isArmChecked = false;
            }
            if (gp1.isKeyToggled(LSGamepad.RB)) {
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

            double speed = -gp1.getValue(LSGamepad.jRightY);
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
            telemetry.addLine("Arm encoder reading: " + arm.getPosition());
            telemetry.addLine("Arm lower bound: " + armMin);
            telemetry.addLine("Arm higher bound: " + armMax);
            telemetry.addLine("Left Wheel: " + chassis.getSpeed(LSDrive.Wheels.REAR_LEFT));
            telemetry.addLine("Right Wheel: " + chassis.getSpeed(LSDrive.Wheels.REAR_RIGHT));
            telemetry.update();
        }

        telemetry.addLine("Robot Stopped");
        telemetry.update();

    }
}
