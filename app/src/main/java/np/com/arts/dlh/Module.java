package np.com.arts.dlh;

/**
 * Created by Antariksha on 2/19/2017.
 */
public class Module {
    public String letterId;
    public String letterCreatedBy;
    public String letterApplicant;
    public String letterRegistrationId;
    public String letterRegistrationNo;
    public String letterTo;
    public String letterContent;
    public String letterCC;
    public String letterSubject;
    private String createdBy;

    public String  assignedByLetter() {
        return  letterCreatedBy;
    }

    public String getLetterNo() {
        return letterId;
    }

    public String getApplicantName() {
        return letterApplicant;
    }

    public String getRegistrationId() {
        return letterRegistrationId;
    }

    public String getRegistrationNO() {
        return letterRegistrationNo;
    }

    public String getLetterTo() {
        return letterTo;
    }

    public String getLetterContent() {
        return letterContent;
    }

    public String getLetterCC() {
        return letterCC;
    }

    public String getLetterSubject() {
        return letterSubject;
    }
}
