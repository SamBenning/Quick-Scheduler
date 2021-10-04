package sample.util;

import sample.controller.customerControllers.AddCustomerController;
import sample.dao.FirstLevelDivisionDao;

public abstract class ValidationUtil {

    private static String errorMessageToDisplay;

    public static boolean validateAddCustomer(AddCustomerController controller) {

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
            AlertUtil.showCustomerErrorAlert(errorMessageToDisplay);
        }
        errorMessageToDisplay = "";
        return false;
    }

    private static boolean validateName(AddCustomerController controller) {
        if (controller.customerNameField.getText().isBlank()) {
            errorMessageToDisplay += "\n-Name cannot be blank.";
            return false;
        } else if (controller.customerNameField.getText().length() > 50) {
            errorMessageToDisplay += "\n-Name must be fewer than 50 characters.";
            return false;
        }
        return true;
    }

    private static boolean validateAddress(AddCustomerController controller) {
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

    private static boolean validatePostalCode(AddCustomerController controller) {
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

    private static boolean validatePhone(AddCustomerController controller) {
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

    private static boolean validateDivision(AddCustomerController controller) {
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
