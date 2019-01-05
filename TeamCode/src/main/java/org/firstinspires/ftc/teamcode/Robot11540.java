package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.actuators.DriveTrain;
import org.firstinspires.ftc.teamcode.actuators.Motor;
import org.firstinspires.ftc.teamcode.actuators.XboxGP;

public class Robot11540 {

    static LinearOpMode opMode;
    static HardwareMap hwMap;
    static Telemetry telemetry;
    static DriveTrain driveTrain;
    static XboxGP gp1,gp2;
    static Motor  arm;
    static Motor lift;
    static boolean hasGP2 = false;

    public static void
    init(LinearOpMode linearOpMode) {

        opMode = linearOpMode;
        hwMap  = opMode.hardwareMap;
        telemetry = opMode.telemetry;

        driveTrain = new DriveTrain(new Motor(hwMap,"front_left"),
                                    new Motor(hwMap,"front_right"),
                                    new Motor(hwMap,"rear_left"),
                                    new Motor(hwMap, "rear_right"));
        gp1 = new XboxGP(opMode.gamepad1);

        arm = new Motor(hwMap, "arm");

        lift = new Motor(hwMap, "lift");

        driveTrain.setWheelMode(DriveTrain.WheelMode.NORMAL);
    }

    public static void
    setChassis(DriveTrain.WheelMode mode) {
        driveTrain.setWheelMode(mode);
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

        telemetry.addData("Status","Running");
        telemetry.update();

        while(opMode.opModeIsActive()) {
            teleopPeriodic();
            postInfo();
        }
        telemetry.addData("Status", "Match/Practice Ended");
        telemetry.update();
        return true;
    }

    public static void
    teleopPeriodic() {
        gp1.fetchData();
        drive();
        configure();
        actuate();
    }

    static private void
    configure() {
        if(gp1.isKeyToggled(XboxGP.RB)) {
             driveTrain.flipHead();
        }

    }

    static private void
    actuate() {
        if(gp1.isKeysChanged(XboxGP.dPadUp,XboxGP.dPadDown)) {
            boolean isArmUp = gp1.isKeyHeld(XboxGP.dPadUp);

            if(gp1.getValue(XboxGP.dPadUp) == gp1.getValue(XboxGP.dPadDown)) {
                arm.moveWithButton(false,false);
            }
            else {
                arm.moveWithButton(isArmUp, !isArmUp);
            }
        }

        if(gp1.isKeysChanged(XboxGP.A,XboxGP.B)) {

            boolean isLiftUp = gp1.isKeyHeld(XboxGP.B);

            if(gp1.getValue(XboxGP.A) == gp1.getValue(XboxGP.B)) {
                lift.moveWithButton(false,false);
            }

            else {
                lift.moveWithButton(isLiftUp,!isLiftUp);
            }
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
                if (gp1.isKeysChanged(XboxGP.RT, XboxGP.LT, XboxGP.jLeftX)) {
                    double rotation = -gp1.getValue(XboxGP.jLeftX);
                    double ySpeed = gp1.getValue(XboxGP.RT) - gp1.getValue(XboxGP.LT);
                    telemetry.addData("Rotation",rotation);
                    driveTrain.drive(0, ySpeed, rotation);
                    telemetry.update();
                }
                break;
        }
    }

    static void postInfo() {
        telemetry.addData("Arm Encoder Reading",arm.getPosition());
        telemetry.addData("Lift Encoder Reading", lift.getPosition());
        telemetry.update();
    }
}
