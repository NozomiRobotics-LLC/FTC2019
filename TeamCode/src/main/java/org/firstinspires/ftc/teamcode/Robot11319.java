package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.actuators.DriveTrain;
import org.firstinspires.ftc.teamcode.actuators.Motor;
import org.firstinspires.ftc.teamcode.actuators.XboxGP;

public class Robot11319 {

    static LinearOpMode opMode;
    static HardwareMap hwMap;
    static Telemetry telemetry;
    static DriveTrain driveTrain;
    static XboxGP gp1,gp2;
    static Motor arm, sweeper;
    static boolean hasGP2 = false;

    public static void
    init(LinearOpMode linearOpMode) {

        opMode = linearOpMode;
        hwMap  = opMode.hardwareMap;
        telemetry = opMode.telemetry;

        driveTrain = new DriveTrain(new Motor(hwMap,"front_left"),
                                    new Motor(hwMap,"front_right"),
                                    new Motor(hwMap, "rear_left"),
                                    new Motor(hwMap,"rear_right"));
        gp1 = new XboxGP(opMode.gamepad1);

        arm = new Motor(hwMap, "arm");
        sweeper = new Motor(hwMap, "sweeper");

    }

    public static void
    setChassis(DriveTrain.WheelMode mode) {
        driveTrain.setWheelMode(DriveTrain.WheelMode.HYBRID_OMNI_TANK);
    }

    public static void
    useSecondController(Gamepad gamepad2) {
        hasGP2 = true;
        gp2 = new XboxGP(gamepad2);
    }

    public static boolean
    runTeleOp() {

        if(!hasGP2) {
            gp2 = gp1;
        }

        opMode.waitForStart();

        while(opMode.opModeIsActive()) {
            teleopPeriodic();
        }
        telemetry.addData("Status", "Match/Practice Ended");
        telemetry.update();
        return true;
    }

    public static void
    teleopPeriodic() {
        gp1.fetchData();
        drive();
        actuate();
    }

    static private void
    actuate() {

        boolean isSweeperMove = gp1.isKeyHeld(XboxGP.A);
        //Arm
        if(gp1.isKeyHeld(XboxGP.dPadUp) != gp1.isKeyHeld(XboxGP.dPadDown)) {
            boolean isUp = gp1.isKeyHeld(XboxGP.dPadUp);
            arm.moveWithButton(isUp, !isUp);
        }
        else {
            arm.moveWithButton(false,false);
        }

        //Sweeper
        if(gp1.isKeyHeld(XboxGP.A) != gp1.isKeyHeld(XboxGP.B)) {
            sweeper.moveWithButton(isSweeperMove, !isSweeperMove);
        }
        else if(!isSweeperMove) {
            sweeper.moveWithButton(false,false);
        }
    }

    static private void
    drive() {
        //Drive Control
        switch(driveTrain.getWheelMode()) {
            case OMNI:
            case MECANUM:
                if (gp1.isKeysChanged(XboxGP.jLeftY, XboxGP.jLeftX, XboxGP.RT, XboxGP.LT)) {
                    double xSpeed = gp1.getValue(XboxGP.jLeftX);
                    double ySpeed = -gp1.getValue(XboxGP.jLeftY);
                    double rotation = gp1.getValue(XboxGP.RT) - gp1.getValue(XboxGP.LT);
                    driveTrain.drive(xSpeed, ySpeed, rotation);
                }
                break;

            case NORMAL:
            case HYBRID_OMNI_TANK:
                if (gp1.isKeysChanged(XboxGP.RT, XboxGP.LT, XboxGP.jLeftX)) {
                    double rotation = gp1.getValue(XboxGP.jLeftX);
                    double ySpeed = gp1.getValue(XboxGP.RT) - gp1.getValue(XboxGP.LT);
                    driveTrain.drive(0, ySpeed, rotation);
                }
                break;
        }
    }

}
