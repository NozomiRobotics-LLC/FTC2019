package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.actuators.Controller;
import org.firstinspires.ftc.teamcode.actuators.DriveTrain;
import org.firstinspires.ftc.teamcode.actuators.MotorControl;
import org.firstinspires.ftc.teamcode.actuators.ServoControl;

@TeleOp(name = "Stem A", group = "Linear opmode")

public class StemA extends LinearOpMode {

    @Override
    public void runOpMode() {

        ElapsedTime runtime = new ElapsedTime();

        DriveTrain chassis = new DriveTrain(new MotorControl(hardwareMap, "front_left_motor"),
                new MotorControl(hardwareMap, "front_right_motor"));
        Controller gp1 = new Controller(gamepad1);

        chassis.setWheelMode(DriveTrain.WheelMode.NORMAL_WHEEL);

        MotorControl intake = new MotorControl(hardwareMap, "intake");
        MotorControl arm   = new MotorControl (hardwareMap, "arm");
        ServoControl thrower = new ServoControl(hardwareMap, "thrower",true,-1, 1);

        telemetry.addLine("Sucessfully Initialized.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            gp1.updateStatus();

            //Basic Driving
            if(gp1.isKeysChanged(Controller.LT,Controller.RT, Controller.jLeftX)) {
                chassis.drive(0, gp1.getValue(Controller.RT) - gp1.getValue(Controller.LT), gp1.getValue(Controller.jLeftX));
            }

            //Intake
            if(gp1.isKeyChanged(Controller.A) || gp1.isKeyChanged(Controller.B)) {
                if(gp1.isKeyHeld(Controller.A) && gp1.isKeyHeld(Controller.B)) {
                    intake.move(0);
                }
                else if (gp1.isKeyHeld(Controller.A)) {
                    intake.move(1);
                }
                else if(gp1.isKeyHeld(Controller.B)) {
                    intake.move(-1);
                }
            }

            //Do stuff here
        }

        telemetry.addLine("Robot Stopped");
        telemetry.update();

    }
}
