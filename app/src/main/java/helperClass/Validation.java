package helperClass;

/**
 * Created by aruna.ramakrishnan on 2/14/2018.
 */

import android.widget.EditText;

public class Validation {


    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText, String errorMsg) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(errorMsg);
            return false;
        }
        return true;
    }
}
