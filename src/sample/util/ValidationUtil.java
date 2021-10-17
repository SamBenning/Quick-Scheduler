package sample.util;

import sample.controller.appointmentControllers.AppointmentController;
import sample.controller.customerControllers.AddCustomerController;
import sample.controller.customerControllers.CustomerController;
import sample.dao.FirstLevelDivisionDao;
import sample.model.Appointment;

import java.time.*;

/**
 * Contains public methods which take in a controller object and perform validation on user input.
 * Generates and error message string and calls AlertUtil to display an error message if any validation
 * fails.*/
public abstract class ValidationUtil {

    private static String errorMessageToDisplay;

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

        if (
                validateTitle(controller) &
                validateDescription(controller) &
                validateLocation(controller) &
                validateType(controller) &
                validateStart(controller) &
                validateEnd(controller) &
                validateTimeLogic(controller) &
                validateCustomerCombo(controller) &
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
    private static  boolean validateStart(AppointmentController controller) {
        if (controller.hasEmptyDateTimeCombo()) {return false;}
        ZonedDateTime adjustedDateTime = controller.getStartDateTime().atZone(ZoneId.of("America/New_York"));
        if (adjustedDateTime.getHour() < 8 || adjustedDateTime.getHour() >= 22) {
            errorMessageToDisplay += "\n-Invalid start time--outside of business hours.";
            return false;
        }
        return true;
    }

    private static boolean validateEnd(AppointmentController controller) {
        if (controller.hasEmptyDateTimeCombo()) {return false;}
        ZonedDateTime adjustedDateTime = controller.getEndDateTime().atZone(ZoneId.of("America/New_York"));
        if (adjustedDateTime.getHour() <= 8 || adjustedDateTime.getHour() > 22) {
            errorMessageToDisplay += "\n-Invalid end time--outside of business hours.";
            return false;
        } else if (adjustedDateTime.getHour() == 22 && adjustedDateTime.getMinute() != 0) {
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
