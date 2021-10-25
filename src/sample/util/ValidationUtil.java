package sample.util;

import sample.controller.appointmentControllers.AppointmentController;
import sample.controller.appointmentControllers.ModifyAppointmentController;
import sample.controller.customerControllers.CustomerController;
import sample.dao.AppointmentDao;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Appointment;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Contains public methods which take in a controller object and perform validation on user input.
 * Generates and error message string and calls AlertUtil to display an error message if any validation
 * fails.*/
public abstract class ValidationUtil {

    private static String errorMessageToDisplay;
    private static Duration diff;
    private static int startHour;
    private static int endHour;

    /**
     * Calls individual field validation methods. If all methods return true, this method returns true. If one
     * or more fail, AlertUtil.showErrorAlert is called.*/
    public static boolean validateCustomer(CustomerController controller) {
        errorMessageToDisplay = "Please correct the following errors: ";
        if (
                validateName(controller) &
                validateAddress(controller) &
                validatePostalCode(controller) &
                validatePhone(controller) &
                validateDivision(controller)
        ){
            return true;
        } else {
            AlertUtil.showErrorAlert(errorMessageToDisplay);
        }
        errorMessageToDisplay = "";
        return false;
    }

    /**
     * Calls individual field validation methods. If all methods return true, this method returns true. If one
     * or more fail, AlertUtil.showErrorAlert is called.*/
    public static boolean validateAppointment(AppointmentController controller) {
        errorMessageToDisplay = "Please correct the following errors: ";
        if (controller.hasEmptyDateTimeCombo()) {
            errorMessageToDisplay += "\n-All date/time values must be filled in.";
        }
        setDiff();
        setHours();
        if (
                validateTitle(controller) &
                validateDescription(controller) &
                validateLocation(controller) &
                validateType(controller) &
                validateStart(controller) &
                validateEnd(controller) &
                validateTimeLogic(controller) &
                validateCustomerCombo(controller) &
                validateCustomerConflicts(controller) &
                validateUserCombo(controller) &
                validateContactCombo(controller) &
                !controller.hasEmptyDateTimeCombo()
        ) {
            return true;
        } else {
            AlertUtil.showErrorAlert(errorMessageToDisplay);
        }
        errorMessageToDisplay = "";
        return false;
    }

    /**
     * Sets diff to the Duration.between the local timezone and easter timezone at start of day. Used for time
     * validation in other methods.*/
    private static void setDiff() {
        ZoneId localZone = ZoneId.systemDefault();
        ZoneId easternZone = ZoneId.of("America/New_York");
        LocalDate today = LocalDate.now();
        diff = Duration.between(today.atStartOfDay(localZone), today.atStartOfDay(easternZone));
    }

    /**
     * Business hours are 8:00am to 10:00pm or 22:00. startHour and endHour member variables represent the start
     * and end hours as integers. This method simply adjusts them to account for the difference between system
     * time zone and eastern time zone (by using diff).*/
    private static void setHours() {
        int hourOffset = (int)diff.toHours();
        startHour =  8 + hourOffset;
        endHour = 22 + hourOffset;
    }

    /**
     * Ensures title field is not empty, and that it does not exceed 50 characters.*/
    private static boolean validateTitle(AppointmentController controller) {
        if (controller.appTitleField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Title cannot be blank.";
            return false;
        } else if (controller.appTitleField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Title must be fewer than 50 characters.";
            return false;
        }
        return true;
    }

    /**
     * Ensures description field is not empty and that it does not exceed 50 characters.*/
    private static boolean validateDescription(AppointmentController controller) {
        if (controller.appDescriptionField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Description cannot be blank.";
            return false;
        } else if (controller.appDescriptionField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Description must be fewer than 50 characters.";
            return false;
        }
        return true;
    }

    /**
     * Ensures location field is not empty and that it does not exceed 50 characters.*/
    private static boolean validateLocation(AppointmentController controller) {
        if (controller.appLocationField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Location cannot be blank.";
            return false;
        } else if (controller.appLocationField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Location must be fewer than 50 characters.";
            return false;
        }
        return true;
    }

    /**
     * Ensures that a selection has been made in the type combo box.*/
    private static boolean validateType(AppointmentController controller) {
        if (controller.appTypeCombo.getSelectionModel().isEmpty()) {
            errorMessageToDisplay += "\n-Type must be selected.";
            return false;
        }
        return true;
    }

    /**
     * Checks whether the start time selected by the user is within business hours by converting system time to EST.
     * If any date/time parameters have been left blank, the method simply returns false and doesn't
     * do anything.*/
    private static boolean validateStart(AppointmentController controller) {
        if (controller.hasEmptyDateTimeCombo()) {return false;}
        int setStartHour = controller.getStartDateTime().getHour();
        if (setStartHour < startHour || setStartHour >= endHour) {
            errorMessageToDisplay += "\n-Invalid start time--outside of business hours.";
            return false;
        }
        return true;
    }

    /**
     * Checks whether the end time selected by the user is within business hours by converting system time to EST.
     * If any date/time parameters have been left blank, the method simply returns false and doesn't
     * do anything.*/
    private static boolean validateEnd(AppointmentController controller) {
        if (controller.hasEmptyDateTimeCombo()) {return false;}
        int setEndMinute = controller.getEndDateTime().getMinute();
        int setEndHour = controller.getEndDateTime().getHour();
        if (setEndHour <= startHour || setEndHour > endHour) {
            errorMessageToDisplay += "\n-Invalid end time--outside of business hours.";
            return false;
        } else if (setEndHour == endHour && setEndMinute != 0) {
            errorMessageToDisplay += "\n-Invalid end time--outside of business hours.";
            return false;
        }
        return true;
    }

    /**
     * Checks that the entries in the time fields make logical sense (start time isn't before end time
     * Simply returns false if any time combos are blank.*/
    private static boolean validateTimeLogic(AppointmentController controller) {
        if (controller.hasEmptyDateTimeCombo()) {return false;}
        int startHour = controller.getStartDateTime().getHour();
        int endHour = controller.getEndDateTime().getHour();
        int startMinute = controller.getStartDateTime().getMinute();
        int endMinute = controller.getEndDateTime().getMinute();


        if (startHour < endHour) {
            return true;
        } else if(startHour > endHour) {
            errorMessageToDisplay += "\n-Start time must be before end time.";
            return false;
        } else {
            if (startMinute < endMinute) {
                return true;
            } else {
                errorMessageToDisplay += "\n-Start time must be before end time.";
                return false;
            }
        }
    }

    /**
     * Ensures that a selection has been made in the customer combo box.*/
    private static boolean validateCustomerCombo(AppointmentController controller) {
        if (controller.appCustomerCombo.getSelectionModel().isEmpty()) {
            errorMessageToDisplay += "\n-No Customer selected.";
            return false;
        }
        return true;
    }

    /**
     * Returns false if there is a DateTime overlap for the selected customer. Determines overlap by converting
     * user input to milliseconds, querying the database for all customer appointments and performing a comparison
     * between the max of the start times and the min of the end times. If comparison returns true, then there is overlap
     * between the two dates.
     *<br>
     * Initially this method was return false when attempting to save from the modify customer screen if the date/time
     * values overlapped with the old date/time values, which is unintended behavior. To work around this, the code now
     * checks each appointment ID in matchingCustomerApppointments against the selectedAppointment ID so that the
     * validation comparisons are skipped on a match. This prevents the appointment from being compared to the existing
     * record in the database for itself.*/
    private static boolean validateCustomerConflicts(AppointmentController controller) {

        Instant startTimeInstant = controller.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant();
        long startTimeMillis = startTimeInstant.toEpochMilli();
        Instant endTimeInstant = controller.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant();
        long endTimeMillis = endTimeInstant.toEpochMilli();
        ArrayList<Appointment> matchingCustomerAppointments = AppointmentDao.getAppointmentsForCustomer(controller.getSelectedCustomerId());

        for (Appointment appointment : matchingCustomerAppointments) {
            try {
                ModifyAppointmentController modController = (ModifyAppointmentController) controller;
                if (appointment.getAppointmentId() == modController.getSelectedAppointment().getAppointmentId()) {
                    continue;
                }
            } catch (Exception ignored) {
            }
                Instant appStartInstant = appointment.getStart().atZone(ZoneId.systemDefault()).toInstant();
                long appStartMillis = appStartInstant.toEpochMilli();
                Instant appEndInstant = appointment.getEnd().atZone(ZoneId.systemDefault()).toInstant();
                long appEndMillis = appEndInstant.toEpochMilli();
                //Found this solution to determine overlap on StackOverflow: https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap
                if (Long.max(startTimeMillis, appStartMillis) < Long.min(endTimeMillis, appEndMillis)) {
                    System.out.println("conflict w/ this app: " + appointment.getAppointmentId());
                    errorMessageToDisplay += "\n-Selected customer already has appointment scheduled for date/time range.";
                    return false;
            }

            }
        return true;
    }

    /**
     * Ensures a selection has been made in the user combo box.*/
    private static boolean validateUserCombo(AppointmentController controller) {
        if (controller.appUserCombo.getSelectionModel().isEmpty()) {
            errorMessageToDisplay += "\n-No User selected.";
            return false;
        }
        return true;
    }

    /**
     * Ensures a selection has been made in the contact combo box.*/
    private static boolean validateContactCombo(AppointmentController controller) {
        if (controller.appContactCombo.getSelectionModel().isEmpty()) {
            errorMessageToDisplay += "\n-No Contact selected";
            return false;
        }
        return true;
    }

    /**
     * Ensures that the name field is not blank and that it does not exceed 50 characters.*/
    private static boolean validateName(CustomerController controller) {
        if (controller.customerNameField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Name cannot be blank.";
            return false;
        } else if (controller.customerNameField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Name must be fewer than 50 characters.";
            return false;
        }
        return true;
    }

    /**
     * Ensures that the address field is not blank and that it does not exceed 100 characters.*/
    private static boolean validateAddress(CustomerController controller) {
       if (controller.customerAddressField.getText().isBlank()) {
           errorMessageToDisplay += "\n-Address cannot be blank.";
           return false;
       } else if (controller.customerAddressField.getText().length() > 100) {
            errorMessageToDisplay += "\n-Address must be fewer than 100 characters.";
            return false;
        } else {
            return true;
        }
    }

    /**
     * Ensures that the postal code field is not blank and that it does not exceed 50 characters.*/
    private static boolean validatePostalCode(CustomerController controller) {
        if (controller.customerPostalField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Postal code cannot be blank.";
            return false;
        } else if (controller.customerPostalField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Postal code must be fewer than 50 characters.";
            return false;
        } else {
            return true;
        }
    }

    /**
     * Ensures that the phone field is not blank and that it does not exceed 50 characters.*/
    private static boolean validatePhone(CustomerController controller) {
        if (controller.customerPhoneField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Phone number cannot be blank.";
            return false;
        } else if (controller.customerPhoneField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Phone number must be fewer than 50 characters.";
            return false;
        } else {
            return true;
        }
    }

    /**
     * Ensures that the division combo box has been selected.*/
    private static boolean validateDivision(CustomerController controller) {
        try {
            if (FirstLevelDivisionDao.getDivisionId(
                    controller.customerDivisionCombo.getSelectionModel().getSelectedItem()) == -1
            ) {
                errorMessageToDisplay += "\n-Valid division must be selected.";
                return false;
            } else {
                return true;
            }
        } catch (NullPointerException e) {
            errorMessageToDisplay += "\n-No division selected.";
            return false;
        }
    }
}
