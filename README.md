# SafeFlight

SafeFlight is a quality-driven flight booking application developed using
Agile methodologies and Software Quality Assurance practices.
# SafeFlight
SafeFlight is a quality-driven flight booking application developed using Agile methodologies and Software Quality Assurance practices.

## Objectives
- Apply SQA throughout SDLC
- Demonstrate unit, integration, and system testing
- Handle boundary, concurrency, and validation scenarios
- Integrate continuous testing and static code analysis

## Features
SafeFlight application has several distinctive features:

- Management of user accounts i.e. signup and login validation
- Search for available flights (one-way or round-trip)
- Booking and Cancelling of a specific flight
- Maintenance of user's flight information (history of booking and cancellation)

## Tech Stack
- Frontend: HTML, CSS, Thymeleaf
- Backend: Java, Spring Boot
- Testing: JUnit, Kiwi TCMS, Mutation Testing (PIT)
- CI: GitHub Actions
- Static Analysis: SonarCloud
- Security: Snyk (Dependency Scanning & SCA)

## System Requirements
- **Java**: JDK 17 or higher
- **Maven**: 3.6+ (for dependency management)
- **Git**: For version control
- **Web Browser**: Chrome, Firefox, or Edge (latest versions)

## Project Structure
```
SafeFlight-sqa/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/safeflight/backend/
│   │   │   │   ├── BackendApplication.java
│   │   │   │   ├── config/
│   │   │   │   ├── controller/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   ├── service/
│   │   │   │   └── exception/
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── templates/     
│   │   │       └── static/        # CSS
│   │   └── test/
│   ├── pom.xml
│   └── mvnw/mvnw.cmd
├── data/                          # database files
└── README.md
```


## Application Start

### 1. Clone the Repository
```bash
git clone https://github.com/himanshukapse2/SafeFlight-sqa.git
cd SafeFlight-sqa
```

### 2. Navigate to Backend Directory
```bash
cd backend
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

### 4. Open Frontend
```bash
# enter the following URL into your local browser
localhost:8082
```

## Usage

### 1. Signup and Login
As shown in the following image, you can enter your full name, email address and password to sign up for a new account. This newly created account can later be used to sign in to the system.

<img width="500" height="350" alt="Screenshot 2026-04-02 at 21 49 00" src="https://github.com/user-attachments/assets/b383f92f-73ce-4cc6-8f0d-156d8ed15bce" />

### 2. Search for Flights
After you log in, the following page will show up. You can choose to search for either single trip or round trip. Then you need to enter the From City(Source), ToC ity(Destination), Travel Date and Return Date (in the case of round trip). 
Then you can click the search button to view the search results.

**Note**: The initial data is provided in /backend/src/main/java/com/safeflight/backend/config/DataSeeder.java file. Only the flights provided in that file will be displayed as search results.
The same data will be seeded every time when running the application. If you want change this, you can set the property app.seed-data.force-reload to false in the /backend/src/main/resources/application.properties file.

<img width="500" height="350" alt="Screenshot 2026-04-02 at 21 54 33" src="https://github.com/user-attachments/assets/ba63ef31-698c-4e28-948e-f1690b8c8971" />

As shown in the following image, in the round trip case, you can select one go trip and one return trip. In the single trip case, you can only select one go trip.

<img width="500" height="350" alt="Screenshot 2026-04-02 at 21 54 59" src="https://github.com/user-attachments/assets/28276d0e-0802-4443-8160-142130a888d8" />

### 3. Book a Flight

In the following page, you can review the information of your selected flight. You need to enter Passenger Name, Age, Extra Baggage and choose whether you're a military personnel. 
You can click Back to Search button to select another flight or click Confirm Booking button to book this flight.

<img width="500" height="350" alt="Screenshot 2026-04-02 at 21 55 24" src="https://github.com/user-attachments/assets/8c1e5ee8-f20f-44b8-85c1-f5e39e6d172e" />

### 4. Cancel a Flight

In the following page, you can click Go Back button to go back to My Flights page without cancellation or click Confirm Cancellation to cancel the selected flight.

<img width="500" height="350" alt="Screenshot 2026-04-02 at 21 55 48" src="https://github.com/user-attachments/assets/636d2b53-9c1d-453d-a016-9bc71086141e" />

### 5. Reviewing my Flight

In the My Flight page, you can review all the booked flights and cancelled flights. You can click the Cancel button of a certain flight to cancel that booked flight.

<img width="500" height="350" alt="Screenshot 2026-04-02 at 21 55 58" src="https://github.com/user-attachments/assets/694dea8d-39af-40b3-bfa4-05176042a624" />

## Declaration of Authorship and AI Non-Usage

Course: CSC1137 – Software Process Quality

Project Phase: 4

Team Name: Group 5

By submitting this project, we, the undersigned team members, hereby declare that:

1.⁠ ⁠Original Work: The accompanying report and software source code are the original work of the team members listed below.

2.⁠ ⁠No AI Text Generation: We have not used Large Language Models (LLMs) or AI-based text generation tools (including but not limited to ChatGPT, Claude, Gemini, Grok, Mistral, DeepSeek, or any other foundational model) to draft, write, or rewrite any portion of the project report, documentation, or presentation materials.

3.⁠ ⁠No AI Code Generation: We have not used AI-based coding assistants (including but not
limited to GitHub Copilot, ChatGPT, or Claude Code or anyother) to generate, autocomplete, or refactor any part of the source code submitted in this project.

4.⁠ ⁠Academic Integrity: We understand that the use of unauthorized AI tools to generate content constitutes a breach of academic integrity and may result in penalties as outlined in the university's academic misconduct policy.

Student Names :

  Yang Fu \
  Himanshu Kapse \
  Shresha Lakshman \
  Angad Singh Gurnam Singh \
  Kartik Sharma \
  Mohammad Umar

