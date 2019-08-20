package Services.ServiceRequestResponseObjects;

public class PersonResponseDetail
{
        String associatedUsername;
        String personID;
        String firstName;
        String lastName;
        String gender;
        String fatherID;
        String motherID;
        String spouseID;

        public PersonResponseDetail(String associatedUsername, String personID, String firstName,
                                    String lastName, String gender, String fatherID, String motherID,
                                    String spouseID)
        {
                this.associatedUsername = associatedUsername;
                this.personID = personID;
                this.firstName = firstName;
                this.lastName = lastName;
                this.gender = gender;
                this.fatherID = fatherID;
                this.motherID = motherID;
                this.spouseID = spouseID;
        }

        public String GetAssociatedUsername()
        {
                return associatedUsername;
        }

        public String GetPersonID()
        {
                return personID;
        }

        public String GetGender()
        {
                return gender;
        }

        public String GetFatherID()
        {
                return fatherID;
        }

        public String GetMotherID()
        {
                return motherID;
        }

        public String GetSpouseID()
        {
                return spouseID;
        }

        public String GetFirstName()
        {
                return firstName;
        }

        public String GetLastName()
        {
                return lastName;
        }


}
