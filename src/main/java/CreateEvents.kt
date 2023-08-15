// Import Nylas packages
import com.nylas.NylasClient
import com.nylas.models.*

// Import DotEnv to handle .env files
import io.github.cdimascio.dotenv.dotenv

// Import Kotlin packages
import java.time.LocalDateTime
import java.time.ZoneOffset

fun main(args: Array<String>) {
    // Load our env variable
    val dotenv = dotenv()

    // Initialize Nylas client
    val nylas: NylasClient = NylasClient(
        apiKey = dotenv["V3_TOKEN"],
        baseUrl = dotenv["BASE_URL"],
    )

    // Get today's day
    var startDate = LocalDateTime.now()
    // Set time. As we're using UTC we need to add the hours in difference
    // from our own Timezone
    startDate = startDate.withHour(13);
    startDate = startDate.withMinute(0);
    startDate = startDate.withSecond(0);
    val endDate = startDate.withMinute(30);

    // Convert the dates into Integer from Epoch
    val iStartDate: Int = startDate.toEpochSecond(ZoneOffset.UTC).toInt()
    val iEndDate: Int = endDate.toEpochSecond(ZoneOffset.UTC).toInt()

    // Create the timespan for the event
    val eventWhenObj: CreateEventRequest.When = CreateEventRequest.When.Timespan(iStartDate, iEndDate);
    // Define title, location and description of the event
    val title: String = "Let's learn some Nylas Kotlin SDK!"
    val location: String = "Blag's Den!"
    val description: String = "Using the Nylas API with the Java SDK is easy. Come join us!\""

    // Create the list of participants
    val participant: CreateEventRequest.Participant = CreateEventRequest.Participant(dotenv["CALENDAR_ID"], ParticipantStatus.NOREPLY, "Blag")
    val participants: List<CreateEventRequest.Participant> = listOf(participant)

    // Create the event request which adds date/time, title, location, description and participants
    val eventRequest: CreateEventRequest = CreateEventRequest(eventWhenObj, title, location, description,participants)

    // Set the event parameters
    val eventQueryParams: CreateEventQueryParams = CreateEventQueryParams(dotenv["CALENDAR_ID"])
    // Create the event itself
    val event: Response<Event> = nylas.events().create(dotenv["CALENDAR_ID"], eventRequest, eventQueryParams)
    // Did we have a problem or could we create the event?
    if(event.requestId != "") {
        print("Event created successfully")
    }else{
        print("There was an error creating the event")
    }
}
