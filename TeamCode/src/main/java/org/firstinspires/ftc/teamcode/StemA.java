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
        LSMotor intake = new LSMotor(hardwareMap, "intake");
        LSMotor arm   = new LSMotor(hardwareMap, "arm");
        LSServo thrower = new LSServo(hardwareMap, "thrower",true,0, 1);

        thrower.setContinuous(true);

        chassis.setWheelMode(LSDrive.WheelMode.NORMAL_WHEEL);


        telemetry.addLine("Sucessfully Initialized.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            gp1.updateStatus();

            //Basic Driving
            if (gp1.isKeysChanged(LSGamepad.LT, LSGamepad.RT, LSGamepad.jLeftX)) {
                chassis.drive(0, gp1.getValue(LSGamepad.RT) - gp1.getValue(LSGamepad.LT), gp1.getValue(LSGamepad.jLeftX));
            }

            //Intake
            if (gp1.isKeyChanged(LSGamepad.A) || gp1.isKeyChanged(LSGamepad.B)) {

                if (gp1.isKeyHeld(LSGamepad.A) && gp1.isKeyHeld(LSGamepad.B)) {
                    intake.move(0);
                }
                else if (gp1.isKeyHeld(LSGamepad.A)) {
                    intake.move(1);
                }
                else if (gp1.isKeyHeld(LSGamepad.B)) {
                    intake.move(-1);
                }
            }

            //Arm motor
            if(gp1.isKeyChanged(LSGamepad.dPadUp) || gp1.isKeyChanged(LSGamepad.dPadDown)) {

                arm.move(gp1.getValue(LSGamepad.dPadUp) - gp1.getValue(LSGamepad.dPadDown));
                //You need to move to another direction slowly for keeping the cube face up
                thrower.moveRotational((gp1.getValue(LSGamepad.dPadUp) - gp1.getValue(LSGamepad.dPadDown)) * -0.1);

            }

            //Now the hardest part, the "thrower"

            if(gp1.isKeyChanged(LSGamepad.jRightY)) {
                thrower.moveRotational(gp1.getValue(LSGamepad.jRightY) * 0.5);
           }
        }

        telemetry.addLine("Robot Stopped");
        telemetry.update();

    }
}
