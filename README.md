# Hospital Appointment Booking System 🏥

A Spring Boot REST API for managing hospital appointments and doctor schedules.

## Features 🌟

- Book appointments with doctors 📅
- Manage doctor availability 👨‍⚕️
- View appointment statistics 📊 
- Transfer appointments between doctors 🔄
- Delete/Cancel appointments ❌

## Tech Stack 💻

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL Database
- Gradle
- Lombok
- JUnit for testing

## API Endpoints 🛣️

### Appointments

- `POST /setAppointment` - Book a new appointment
- `GET /appointments` - Get all appointments
- `DELETE /deleteAppointment?id={id}` - Cancel an appointment
- `GET /statisticsDay` - Get appointments per day
- `GET /statisticsDoc` - Get appointments per doctor

### Doctors

- `POST /newDoctor` - Add a new doctor
- `GET /allDoctorslist` - List all doctors
- `GET /availableDatesByDoctor?doc={doctorName}` - Get available slots for a doctor
- `DELETE /deleteDoctor?doc={doctorName}` - Remove a doctor

## Data Models 📝

### Appointment

```json
{
    "idApp": 1,
    "doctor": "dr. house",
    "patient": "john doe", 
    "date": "2023-12-25"
}
```

### Doctor

```json
{
    "id": 1,
    "doctorName": "dr. house"
    "availableDates": ["date 1", "date 2", ...]
}
```

## Setup & Installation 🚀

1. Clone the repository
2. Configure database settings in 

application.properties


3. Run with Gradle:

```bash
./gradlew bootRun
```

## Testing 🧪

Run the test suite with:

```bash
./gradlew test
```

## Business Rules 📋

- Cannot book appointments for "director" (head physician)
- When a doctor is deleted, their appointments transfer to "director"
- Each doctor has 4 available slots starting from next day
- All names are stored in lowercase

## Project Structure 🏗️

```
src/
├── main/
│   ├── java/
│   │   └── AppointToDoctorRestService/
│   │       ├── controllers/
│   │       ├── entities/
│   │       ├── service/
│   │       ├── repo/
│   │       └── errors/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
```

## Contributing 🤝

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch 
5. Open pull request

## Acknowledgments 🙏

- Built as part of the Hyperskill Java Developer track
- Uses Spring Boot framework for rapid development
- Implements RESTful best practices
