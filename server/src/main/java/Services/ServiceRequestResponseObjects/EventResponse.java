package Services.ServiceRequestResponseObjects;

public class EventResponse
{
    EventResponseDetail[] data;

    public EventResponse(EventResponseDetail[] data)
    {
        this.data = data;
    }

    public EventResponseDetail[] GetData()
    {
        return data;
    }
}
