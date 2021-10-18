package sample.util;

import sample.controller.appointmentControllers.AppointmentController;
import sample.controller.customerControllers.AddCustomerController;
import sample.controller.customerControllers.CustomerController;
import sample.dao.AppointmentDao;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Appointment;

import java.time.*;
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

    private static void setDiff() {
        ZoneId localZone = ZoneId.systemDefault();
        ZoneId easternZone = ZoneId.of("America/New_York");
        LocalDate today = LocalDate.now();
        diff = Duration.between(today.atStartOfDay(localZone), today.atStartOfDay(easternZone));
    }

    private static void setHours() {
        int hourOffset = (int)diff.toHours();
        startHour =  8 + hourOffset;
        endHour = 22 + hourOffset;
    }

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

    private static boolean validateType(AppointmentController controller) {
        if (controller.appTypeField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Type cannot be blank.";
            return false;
        } else if (controller.appTypeField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Type must be fewer than 50 characters.";
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
     * between the two dates.*/
    private static boolean validateCustomerConflicts(AppointmentController controller) {

        Instant startTimeInstant = controller.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant();
        long startTimeMillis = startTimeInstant.toEpochMilli();
        Instant endTimeInstant = controller.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant();
        long endTimeMillis = endTimeInstant.toEpochMilli();
        ArrayList<Appointment> matchingCustomerAppointments = AppointmentDao.getAppointmentsForCustomer(controller.getSelectedCustomerId());

        for (Appointment appointment : matchingCustomerAppointments) {
            Instant appStartInstant = appointment.getStart().atZone(ZoneId.systemDefault()).toInstant();
            long appStartMillis = appStartInstant.toEpochMilli();
            Instant appEndInstant = appointment.getEnd().atZone(ZoneId.systemDefault()).toInstant();
            long appEndMillis = appEndInstant.toEpochMilli();
            //Found this solution to determine overlap on StackOverflow: https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap
            if (Long.max(startTimeMillis, appStartMillis) < Long.min(endTimeMillis, appEndMillis)) {
                errorMessageToDisplay += "\n-Selected customer already has appointment scheduled for date/time range.";
                return false;
            }
        }
        return true;
    }

    private static boolean validateUserCombo(AppointmentController controller) {
        if (controller.appUserCombo.getSelectionModel().isEmpty()) {
            errorMessageToDisplay += "\n-No User selected.";
            return false;
        }
        return true;
    }

    private static boolean validateContactCombo(AppointmentController controller) {
        if (controller.appContactCombo.getSelectionModel().isEmpty()) {
            errorMessageToDisplay += "\n-No Contact selected";
            return false;
        }
        return true;
    }

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

    private static boolean validateDivision(CustomerController controller) {
        try {
            if (FirstLevelDivisionDao.getDivisionId(
                    controller.customerDivisionCombo.getSelectionModel().getSelectedItem().toString()) == -1
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
