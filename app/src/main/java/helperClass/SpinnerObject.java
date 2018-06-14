package helperClass;

/**
 * Created by aruna.ramakrishnan on 3/12/2018.
 */

public class SpinnerObject {

    private String databaseId;
    private String databaseValue;
    private String databaseCode;
    private String isRenewalRequired;

    public SpinnerObject() {
    }

    public SpinnerObject(String databaseId, String databaseValue) {
        this.databaseId = databaseId;
        this.databaseValue = databaseValue;
    }

    public String getId() {
        return databaseId;
    }

    public String getValue() {
        return databaseValue;
    }

    @Override
    public String toString() {
        return databaseValue;
    }

    public String getDatabaseCode() {
        return databaseCode;
    }

    public String getIsRenewalRequired() {
        return isRenewalRequired;
    }

    public void setIsRenewalRequired(String isRenewalRequired) {
        this.isRenewalRequired = isRenewalRequired;
    }
}

