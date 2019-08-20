package Services.ServiceRequestResponseObjects;

public class PersonResponse
{
    PersonResponseDetail[] data;

    public PersonResponse(PersonResponseDetail[] data)
    {
        this.data = data;
    }

    public PersonResponseDetail[] GetData()
    {
        return data;
    }
}
