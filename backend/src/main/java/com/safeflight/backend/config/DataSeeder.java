package com.safeflight.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.FlightRepo;
import com.safeflight.backend.repository.BookingRepo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


/*
Loads dummy flight data into the database at application startup
Configured in application.properties, the below command loads the data in local database when the application is run for the first time.
spring.jpa.hibernate.ddl-auto=update
*/
@Configuration
public class DataSeeder {
    private static final String DUBLIN = "Dublin";
    private static final String CORK = "Cork";
    private static final String BANGALORE = "Bangalore";
    private static final String CHENNAI = "Chennai";
    private static final String HYDERABAD = "Hyderabad";
    private static final String LONDON = "London";
    private static final String DUBAI = "Dubai";
    private static final String SINGAPORE = "Singapore";
    private static final String GOA = "Goa";
    private static final String AER_LINGUS = "Aer lingus";
    private static final String ETIHAD = "Etihad";
    private static final String TURKISH = "Turkish";
    private static final String QATAR = "Qatar";
    private static final String EMIRATES = "Emirates";
    private static final String SINGAPORE_AIRLINES = "Singapore Airlines";
    private static final String BRITISH= "British Airways";


    @Value("${app.seed-data.force-reload:false}")
    private boolean forceReload;

    @Bean
    public CommandLineRunner loadDummyFlights(FlightRepo flightRepo, BookingRepo bookingRepo) {
        return args -> {
            if (forceReload) {
                System.out.println("Force reload is enabled. Clearing database...");
                bookingRepo.deleteAll();
                flightRepo.deleteAll();
            }

            if (flightRepo.count() == 0) {
                flightRepo.saveAll(List.of(
            		new Flight(null, "SK1001", AER_LINGUS, DUBLIN, CORK, LocalDate.of(2026, 3, 10),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4500.0, 45, true),
                        new Flight(null, "SK1002", ETIHAD, DUBLIN, CORK, LocalDate.of(2026, 3, 10),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 3800.0, 30, true),
                        new Flight(null, "SK1003", TURKISH, DUBLIN, CORK, LocalDate.of(2026, 3, 10),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3200.0, 60, true),
                        new Flight(null, "SK1004", QATAR, DUBLIN, CORK, LocalDate.of(2026, 3, 10),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5200.0, 20, true),
                        new Flight(null, "SK1005", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 10),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4600.0, 48, true),
                        new Flight(null, "SK1006", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 10),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 3900.0, 35, true),
                        new Flight(null, "SK1007", ETIHAD, BANGALORE, CHENNAI, LocalDate.of(2026, 3, 10),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2500.0, 70, true),
                        new Flight(null, "SK1008", AER_LINGUS, CHENNAI, BANGALORE, LocalDate.of(2026, 3, 10),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2600.0, 60, true),
                        new Flight(null, "SK1009", ETIHAD, BANGALORE, HYDERABAD, LocalDate.of(2026, 3, 10),
                                LocalTime.of(8, 0), LocalTime.of(9, 15), 2800.0, 52, true),
                        new Flight(null, "SK1010", AER_LINGUS, HYDERABAD, BANGALORE, LocalDate.of(2026, 3, 10),
                                LocalTime.of(10, 15), LocalTime.of(11, 30), 3000.0, 43, true),
                        new Flight(null, "SK1011", QATAR, DUBLIN, BANGALORE, LocalDate.of(2026, 3, 10),
                                LocalTime.of(6, 45), LocalTime.of(9, 30), 5500.0, 40, true),
                        new Flight(null, "SK1012", ETIHAD, BANGALORE, DUBLIN, LocalDate.of(2026, 3, 10),
                                LocalTime.of(19, 0), LocalTime.of(21, 45), 5000.0, 38, true),
                        new Flight(null, "SK1013", AER_LINGUS, CORK, GOA, LocalDate.of(2026, 3, 10),
                                LocalTime.of(11, 0), LocalTime.of(12, 30), 3500.0, 65, true),
                        new Flight(null, "SK1014", ETIHAD, GOA, CORK, LocalDate.of(2026, 3, 10),
                                LocalTime.of(13, 15), LocalTime.of(14, 45), 3400.0, 58, true),
                        new Flight(null, "SK1015", EMIRATES, DUBLIN, DUBAI, LocalDate.of(2026, 3, 10),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 18000.0, 20, false),
                        new Flight(null, "SK1016", AER_LINGUS, DUBLIN, DUBAI, LocalDate.of(2026, 3, 10),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 15000.0, 30, false),
                        new Flight(null, "SK1017", BRITISH, CORK, LONDON, LocalDate.of(2026, 3, 10),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 42000.0, 15, false),
                        new Flight(null, "SK1018", AER_LINGUS, DUBLIN, LONDON, LocalDate.of(2026, 3, 10),
                                LocalTime.of(3, 0), LocalTime.of(8, 15), 40000.0, 18, false),
                        new Flight(null, "SK1019", SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, LocalDate.of(2026, 3, 10),
                                LocalTime.of(23, 15), LocalTime.of(3, 30), 22000.0, 25, false),
                        new Flight(null, "SK1020", ETIHAD, CORK, DUBAI, LocalDate.of(2026, 3, 10),
                                LocalTime.of(21, 0), LocalTime.of(23, 45), 16000.0, 32, false),

                        // ===== 2026-03-11 =====
                        new Flight(null, "SK1021", AER_LINGUS, DUBLIN, CORK, LocalDate.of(2026, 3, 11),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4550.0, 44, true),
                        new Flight(null, "SK1022", ETIHAD, DUBLIN, CORK, LocalDate.of(2026, 3, 11),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 3820.0, 31, true),
                        new Flight(null, "SK1023", TURKISH, DUBLIN, CORK, LocalDate.of(2026, 3, 11),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3250.0, 59, true),
                        new Flight(null, "SK1024", QATAR, DUBLIN, CORK, LocalDate.of(2026, 3, 11),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5250.0, 21, true),
                        new Flight(null, "SK1025", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 11),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4650.0, 47, true),
                        new Flight(null, "SK1026", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 11),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 3920.0, 34, true),
                        new Flight(null, "SK1027", ETIHAD, BANGALORE, CHENNAI, LocalDate.of(2026, 3, 11),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2520.0, 69, true),
                        new Flight(null, "SK1028", AER_LINGUS, CHENNAI, BANGALORE, LocalDate.of(2026, 3, 11),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2620.0, 58, true),
                        new Flight(null, "SK1029", ETIHAD, BANGALORE, HYDERABAD, LocalDate.of(2026, 3, 11),
                                LocalTime.of(8, 0), LocalTime.of(9, 15), 2820.0, 51, true),
                        new Flight(null, "SK1030", AER_LINGUS, HYDERABAD, BANGALORE, LocalDate.of(2026, 3, 11),
                                LocalTime.of(10, 15), LocalTime.of(11, 30), 3020.0, 42, true),
                        new Flight(null, "SK1031", QATAR, DUBLIN, BANGALORE, LocalDate.of(2026, 3, 11),
                                LocalTime.of(6, 45), LocalTime.of(9, 30), 5520.0, 41, true),
                        new Flight(null, "SK1032", ETIHAD, BANGALORE, DUBLIN, LocalDate.of(2026, 3, 11),
                                LocalTime.of(19, 0), LocalTime.of(21, 45), 5020.0, 39, true),
                        new Flight(null, "SK1033", AER_LINGUS, CORK, GOA, LocalDate.of(2026, 3, 11),
                                LocalTime.of(11, 0), LocalTime.of(12, 30), 3520.0, 64, true),
                        new Flight(null, "SK1034", ETIHAD, GOA, CORK, LocalDate.of(2026, 3, 11),
                                LocalTime.of(13, 15), LocalTime.of(14, 45), 3420.0, 57, true),
                        new Flight(null, "SK1035", EMIRATES, DUBLIN, DUBAI, LocalDate.of(2026, 3, 11),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 18100.0, 19, false),
                        new Flight(null, "SK1036", AER_LINGUS, DUBLIN, DUBAI, LocalDate.of(2026, 3, 11),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 15100.0, 29, false),
                        new Flight(null, "SK1037", BRITISH, CORK, LONDON, LocalDate.of(2026, 3, 11),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 42100.0, 14, false),
                        new Flight(null, "SK1038", AER_LINGUS, DUBLIN, LONDON, LocalDate.of(2026, 3, 11),
                                LocalTime.of(3, 0), LocalTime.of(8, 15), 40100.0, 17, false),
                        new Flight(null, "SK1039", SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, LocalDate.of(2026, 3, 11),
                                LocalTime.of(23, 15), LocalTime.of(3, 30), 22100.0, 24, false),
                        new Flight(null, "SK1040", ETIHAD, CORK, DUBAI, LocalDate.of(2026, 3, 11),
                                LocalTime.of(21, 0), LocalTime.of(23, 45), 16100.0, 31, false),

                        // ===== 2026-03-12 =====
                        new Flight(null, "SK1041", AER_LINGUS, DUBLIN, CORK, LocalDate.of(2026, 3, 12),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4580.0, 43, true),
                        new Flight(null, "SK1042", ETIHAD, DUBLIN, CORK, LocalDate.of(2026, 3, 12),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 3840.0, 29, true),
                        new Flight(null, "SK1043", TURKISH, DUBLIN, CORK, LocalDate.of(2026, 3, 12),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3280.0, 58, true),
                        new Flight(null, "SK1044", QATAR, DUBLIN, CORK, LocalDate.of(2026, 3, 12),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5280.0, 22, true),
                        new Flight(null, "SK1045", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 12),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK1046", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 12),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),
                        new Flight(null, "SK1047", ETIHAD, BANGALORE, CHENNAI, LocalDate.of(2026, 3, 12),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2540.0, 68, true),
                        new Flight(null, "SK1048", AER_LINGUS, CHENNAI, BANGALORE, LocalDate.of(2026, 3, 12),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2640.0, 57, true),
                        new Flight(null, "SK1049", ETIHAD, BANGALORE, HYDERABAD, LocalDate.of(2026, 3, 12),
                                LocalTime.of(8, 0), LocalTime.of(9, 15), 2840.0, 50, true),
                        new Flight(null, "SK1050", AER_LINGUS, HYDERABAD, BANGALORE, LocalDate.of(2026, 3, 12),
                                LocalTime.of(10, 15), LocalTime.of(11, 30), 3040.0, 41, true),
                        new Flight(null, "SK1051", QATAR, DUBLIN, BANGALORE, LocalDate.of(2026, 3, 12),
                                LocalTime.of(6, 45), LocalTime.of(9, 30), 5540.0, 39, true),
                        new Flight(null, "SK1052", ETIHAD, BANGALORE, DUBLIN, LocalDate.of(2026, 3, 12),
                                LocalTime.of(19, 0), LocalTime.of(21, 45), 5040.0, 37, true),
                        new Flight(null, "SK1053", AER_LINGUS, CORK, GOA, LocalDate.of(2026, 3, 12),
                                LocalTime.of(11, 0), LocalTime.of(12, 30), 3540.0, 63, true),
                        new Flight(null, "SK1054", ETIHAD, GOA, CORK, LocalDate.of(2026, 3, 12),
                                LocalTime.of(13, 15), LocalTime.of(14, 45), 3440.0, 56, true),
                        new Flight(null, "SK1055", EMIRATES, DUBLIN, DUBAI, LocalDate.of(2026, 3, 12),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 18200.0, 18, false),
                        new Flight(null, "SK1056", AER_LINGUS, DUBLIN, DUBAI, LocalDate.of(2026, 3, 12),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 15200.0, 28, false),
                        new Flight(null, "SK1057", BRITISH, CORK, LONDON, LocalDate.of(2026, 3, 12),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 42200.0, 13, false),
                        new Flight(null, "SK1058", AER_LINGUS, DUBLIN, LONDON, LocalDate.of(2026, 3, 12),
                                LocalTime.of(3, 0), LocalTime.of(8, 15), 40200.0, 16, false),
                        new Flight(null, "SK1059", SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, LocalDate.of(2026, 3, 12),
                                LocalTime.of(23, 15), LocalTime.of(3, 30), 22200.0, 23, false),
                        new Flight(null, "SK1060", ETIHAD, CORK, DUBAI, LocalDate.of(2026, 3, 12),
                                LocalTime.of(21, 0), LocalTime.of(23, 45), 16200.0, 30, false),

                        new Flight(null, "SK1041", AER_LINGUS, DUBLIN, CORK, LocalDate.of(2026, 3, 23),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4580.0, 43, true),
                        new Flight(null, "SK1042", ETIHAD, DUBLIN, CORK, LocalDate.of(2026, 3, 23),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 3840.0, 29, true),
                        new Flight(null, "SK1043", TURKISH, DUBLIN, CORK, LocalDate.of(2026, 3, 23),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3280.0, 58, true),
                        new Flight(null, "SK1044", QATAR, DUBLIN, CORK, LocalDate.of(2026, 3, 23),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5280.0, 22, true),
                        new Flight(null, "SK1045", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 23),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK1046", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 23),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),
                        new Flight(null, "SK1047", ETIHAD, BANGALORE, CHENNAI, LocalDate.of(2026, 3, 23),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2540.0, 68, true),
                        new Flight(null, "SK1048", AER_LINGUS, CHENNAI, BANGALORE, LocalDate.of(2026, 3, 23),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2640.0, 57, true),
                        new Flight(null, "SK1049", ETIHAD, BANGALORE, HYDERABAD, LocalDate.of(2026, 3, 23),
                                LocalTime.of(8, 0), LocalTime.of(9, 15), 2840.0, 50, true),
                        new Flight(null, "SK1050", AER_LINGUS, HYDERABAD, BANGALORE, LocalDate.of(2026, 3, 23),
                                LocalTime.of(10, 15), LocalTime.of(11, 30), 3040.0, 41, true),
                        new Flight(null, "SK1051", QATAR, DUBLIN, BANGALORE, LocalDate.of(2026, 3, 23),
                                LocalTime.of(6, 45), LocalTime.of(9, 30), 5540.0, 39, true),
                        new Flight(null, "SK1052", ETIHAD, BANGALORE, DUBLIN, LocalDate.of(2026, 3, 23),
                                LocalTime.of(19, 0), LocalTime.of(21, 45), 5040.0, 37, true),
                        new Flight(null, "SK1053", AER_LINGUS, CORK, GOA, LocalDate.of(2026, 3, 23),
                                LocalTime.of(11, 0), LocalTime.of(12, 30), 3540.0, 63, true),
                        new Flight(null, "SK1054", ETIHAD, GOA, CORK, LocalDate.of(2026, 3, 23),
                                LocalTime.of(13, 15), LocalTime.of(14, 45), 3440.0, 56, true),
                        new Flight(null, "SK1055", EMIRATES, DUBLIN, DUBAI, LocalDate.of(2026, 3, 23),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 18200.0, 18, false),
                        new Flight(null, "SK1056", AER_LINGUS, DUBLIN, DUBAI, LocalDate.of(2026, 3, 23),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 15200.0, 28, false),
                        new Flight(null, "SK1057", BRITISH, CORK, LONDON, LocalDate.of(2026, 3, 23),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 42200.0, 13, false),
                        new Flight(null, "SK1058", AER_LINGUS, DUBLIN, LONDON, LocalDate.of(2026, 3, 23),
                                LocalTime.of(3, 0), LocalTime.of(8, 15), 40200.0, 16, false),
                        new Flight(null, "SK1059", SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, LocalDate.of(2026, 3, 23),
                                LocalTime.of(23, 15), LocalTime.of(3, 30), 22200.0, 23, false),
                        new Flight(null, "SK1060", ETIHAD, CORK, DUBAI, LocalDate.of(2026, 3, 23),
                                LocalTime.of(21, 0), LocalTime.of(23, 45), 16200.0, 30, false),
                        new Flight(null, "SK1041", AER_LINGUS, DUBLIN, CORK, LocalDate.of(2026, 4, 2),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4580.0, 43, true),
                        new Flight(null, "SK1042", ETIHAD, DUBLIN, CORK, LocalDate.of(2026, 4, 2),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 3840.0, 29, true),
                        new Flight(null, "SK1043", TURKISH, DUBLIN, CORK, LocalDate.of(2026, 4, 2),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3280.0, 58, true),
                        new Flight(null, "SK1044", QATAR, DUBLIN, CORK, LocalDate.of(2026, 4, 2),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5280.0, 22, true),
                        new Flight(null, "SK1045", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 4, 2),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK1046", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 4, 2),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),
                        new Flight(null, "SK1047", ETIHAD, BANGALORE, CHENNAI, LocalDate.of(2026, 4, 2),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2540.0, 68, true),
                        new Flight(null, "SK1048", AER_LINGUS, CHENNAI, BANGALORE, LocalDate.of(2026, 4, 2),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2640.0, 57, true),
                        new Flight(null, "SK1049", ETIHAD, BANGALORE, HYDERABAD, LocalDate.of(2026, 4, 2),
                                LocalTime.of(8, 0), LocalTime.of(9, 15), 2840.0, 50, true),
                        new Flight(null, "SK1050", AER_LINGUS, HYDERABAD, BANGALORE, LocalDate.of(2026, 4, 2),
                                LocalTime.of(10, 15), LocalTime.of(11, 30), 3040.0, 41, true),
                        new Flight(null, "SK1051", QATAR, DUBLIN, BANGALORE, LocalDate.of(2026, 4, 2),
                                LocalTime.of(6, 45), LocalTime.of(9, 30), 5540.0, 39, true),
                        new Flight(null, "SK1052", ETIHAD, BANGALORE, DUBLIN, LocalDate.of(2026, 4, 2),
                                LocalTime.of(19, 0), LocalTime.of(21, 45), 5040.0, 37, true),
                        new Flight(null, "SK1053", AER_LINGUS, CORK, GOA, LocalDate.of(2026, 4, 2),
                                LocalTime.of(11, 0), LocalTime.of(12, 30), 3540.0, 63, true),
                        new Flight(null, "SK1054", ETIHAD, GOA, CORK, LocalDate.of(2026, 4, 2),
                                LocalTime.of(13, 15), LocalTime.of(14, 45), 3440.0, 56, true),
                        new Flight(null, "SK1055", EMIRATES, DUBLIN, DUBAI, LocalDate.of(2026, 4, 2),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 18200.0, 18, false),
                        new Flight(null, "SK1056", AER_LINGUS, DUBLIN, DUBAI, LocalDate.of(2026, 4, 2),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 15200.0, 28, false),
                        new Flight(null, "SK1057", BRITISH, CORK, LONDON, LocalDate.of(2026, 4, 2),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 42200.0, 13, false),
                        new Flight(null, "SK1058", AER_LINGUS, DUBLIN, LONDON, LocalDate.of(2026, 4, 2),
                                LocalTime.of(3, 0), LocalTime.of(8, 15), 40200.0, 16, false),
                        new Flight(null, "SK1059", SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, LocalDate.of(2026, 4, 2),
                                LocalTime.of(23, 15), LocalTime.of(3, 30), 22200.0, 23, false),
                        new Flight(null, "SK1060", ETIHAD, CORK, DUBAI, LocalDate.of(2026, 4, 2),
                                LocalTime.of(21, 0), LocalTime.of(23, 45), 16200.0, 30, false),
                        new Flight(null, "SK1041", AER_LINGUS, DUBLIN, CORK, LocalDate.of(2026, 3, 13),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4580.0, 43, true),
                        new Flight(null, "SK1042", ETIHAD, DUBLIN, CORK, LocalDate.of(2026, 3, 13),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 3840.0, 29, true),
                        new Flight(null, "SK1043", TURKISH, DUBLIN, CORK, LocalDate.of(2026, 3, 13),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3280.0, 58, true),
                        new Flight(null, "SK1044", QATAR, DUBLIN, CORK, LocalDate.of(2026, 3, 13),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5280.0, 22, true),
                        new Flight(null, "SK1045", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 13),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK1046", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 13),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),
                        new Flight(null, "SK1047", ETIHAD, BANGALORE, CHENNAI, LocalDate.of(2026, 3, 13),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2540.0, 68, true),
                        new Flight(null, "SK1048", AER_LINGUS, CHENNAI, BANGALORE, LocalDate.of(2026, 3, 13),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2640.0, 57, true),
                        new Flight(null, "SK1049", ETIHAD, BANGALORE, HYDERABAD, LocalDate.of(2026, 3, 13),
                                LocalTime.of(8, 0), LocalTime.of(9, 15), 2840.0, 50, true),
                        new Flight(null, "SK1050", AER_LINGUS, HYDERABAD, BANGALORE, LocalDate.of(2026, 3, 13),
                                LocalTime.of(10, 15), LocalTime.of(11, 30), 3040.0, 41, true),
                        new Flight(null, "SK1051", QATAR, DUBLIN, BANGALORE, LocalDate.of(2026, 3, 13),
                                LocalTime.of(6, 45), LocalTime.of(9, 30), 5540.0, 39, true),
                        new Flight(null, "SK1052", ETIHAD, BANGALORE, DUBLIN, LocalDate.of(2026, 3, 13),
                                LocalTime.of(19, 0), LocalTime.of(21, 45), 5040.0, 37, true),
                        new Flight(null, "SK1053", AER_LINGUS, CORK, GOA, LocalDate.of(2026, 3, 13),
                                LocalTime.of(11, 0), LocalTime.of(12, 30), 3540.0, 63, true),
                        new Flight(null, "SK1054", ETIHAD, GOA, CORK, LocalDate.of(2026, 3, 13),
                                LocalTime.of(13, 15), LocalTime.of(14, 45), 3440.0, 56, true),
                        new Flight(null, "SK1055", EMIRATES, DUBLIN, DUBAI, LocalDate.of(2026, 3, 13),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 18200.0, 18, false),
                        new Flight(null, "SK1056", AER_LINGUS, DUBLIN, DUBAI, LocalDate.of(2026, 3, 13),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 15200.0, 28, false),
                        new Flight(null, "SK1057", BRITISH, CORK, LONDON, LocalDate.of(2026, 3, 13),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 42200.0, 13, false),
                        new Flight(null, "SK1058", AER_LINGUS, DUBLIN, LONDON, LocalDate.of(2026, 3, 13),
                                LocalTime.of(3, 0), LocalTime.of(8, 15), 40200.0, 16, false),
                        new Flight(null, "SK1059", SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, LocalDate.of(2026, 3, 13),
                                LocalTime.of(23, 15), LocalTime.of(3, 30), 22200.0, 23, false),
                        new Flight(null, "SK1060", ETIHAD, CORK, DUBAI, LocalDate.of(2026, 3, 13),
                                LocalTime.of(21, 0), LocalTime.of(23, 45), 16200.0, 30, false),
                        new Flight(null, "SK2781", AER_LINGUS, DUBLIN, CORK, LocalDate.of(2026, 6, 9),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4990.0, 36, true),
                        new Flight(null, "SK2782", ETIHAD, DUBLIN, CORK, LocalDate.of(2026, 6, 9),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 4190.0, 24, true),
                        new Flight(null, "SK2783", TURKISH, DUBLIN, CORK, LocalDate.of(2026, 6, 9),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3590.0, 49, true),
                        new Flight(null, "SK2784", QATAR, DUBLIN, CORK, LocalDate.of(2026, 6, 9),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5590.0, 18, true),
                        new Flight(null, "SK2785", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 6, 9),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 5090.0, 40, true),
                        new Flight(null, "SK2786", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 6, 9),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 4290.0, 27, true),
                        new Flight(null, "SK2787", ETIHAD, BANGALORE, CHENNAI, LocalDate.of(2026, 6, 9),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2890.0, 55, true),
                        new Flight(null, "SK2788", AER_LINGUS, CHENNAI, BANGALORE, LocalDate.of(2026, 6, 9),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2990.0, 46, true),
                        new Flight(null, "SK2789", ETIHAD, BANGALORE, HYDERABAD, LocalDate.of(2026, 6, 9),
                                LocalTime.of(8, 0), LocalTime.of(9, 15), 3090.0, 44, true),
                        new Flight(null, "SK2790", AER_LINGUS, HYDERABAD, BANGALORE, LocalDate.of(2026, 6, 9),
                                LocalTime.of(10, 15), LocalTime.of(11, 30), 3290.0, 39, true),
                        new Flight(null, "SK2791", QATAR, DUBLIN, BANGALORE, LocalDate.of(2026, 6, 9),
                                LocalTime.of(6, 45), LocalTime.of(9, 30), 5890.0, 33, true),
                        new Flight(null, "SK2792", ETIHAD, BANGALORE, DUBLIN, LocalDate.of(2026, 6, 9),
                                LocalTime.of(19, 0), LocalTime.of(21, 45), 5390.0, 31, true),
                        new Flight(null, "SK2793", AER_LINGUS, CORK, GOA, LocalDate.of(2026, 6, 9),
                                LocalTime.of(11, 0), LocalTime.of(12, 30), 3890.0, 51, true),
                        new Flight(null, "SK2794", ETIHAD, GOA, CORK, LocalDate.of(2026, 6, 9),
                                LocalTime.of(13, 15), LocalTime.of(14, 45), 3790.0, 47, true),
                        new Flight(null, "SK2795", EMIRATES, DUBLIN, DUBAI, LocalDate.of(2026, 6, 9),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 20900.0, 12, false),
                        new Flight(null, "SK2796", AER_LINGUS, DUBLIN, DUBAI, LocalDate.of(2026, 6, 9),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 17900.0, 19, false),
                        new Flight(null, "SK2797", BRITISH, CORK, LONDON, LocalDate.of(2026, 6, 9),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 44900.0, 9, false),
                        new Flight(null, "SK2798", AER_LINGUS, DUBLIN, LONDON, LocalDate.of(2026, 6, 9),
                                LocalTime.of(3, 0), LocalTime.of(8, 15), 42900.0, 11, false),
                        new Flight(null, "SK2799", SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, LocalDate.of(2026, 6, 9),
                                LocalTime.of(23, 15), LocalTime.of(3, 30), 24900.0, 17, false),
                        new Flight(null, "SK2800", ETIHAD, CORK, DUBAI, LocalDate.of(2026, 6, 9),
                                LocalTime.of(21, 0), LocalTime.of(23, 45), 18900.0, 22, false),

                        // ===== Return flights for Dublin to Cork (Cork to Dublin, 5 days later) =====
                        new Flight(null, "SK3001", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 15), LocalTime.of(7, 0), LocalTime.of(9, 15), 4600.0, 48, true),
                        new Flight(null, "SK3002", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 15), LocalTime.of(10, 30), LocalTime.of(12, 45), 3900.0, 35, true),

                        new Flight(null, "SK3003", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 16), LocalTime.of(7, 0), LocalTime.of(9, 15), 4650.0, 47, true),
                        new Flight(null, "SK3004", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 16), LocalTime.of(10, 30), LocalTime.of(12, 45), 3920.0, 34, true),

                        new Flight(null, "SK3005", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 17), LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK3006", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 17), LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),

                        new Flight(null, "SK3007", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 18), LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK3008", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 18), LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),

                        new Flight(null, "SK3009", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 3, 28), LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK3010", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 3, 28), LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),

                        new Flight(null, "SK3011", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 4, 7), LocalTime.of(7, 0), LocalTime.of(9, 15), 4680.0, 46, true),
                        new Flight(null, "SK3012", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 4, 7), LocalTime.of(10, 30), LocalTime.of(12, 45), 3940.0, 33, true),

                        new Flight(null, "SK3013", AER_LINGUS, CORK, DUBLIN, LocalDate.of(2026, 6, 14), LocalTime.of(7, 0), LocalTime.of(9, 15), 5090.0, 40, true),
                        new Flight(null, "SK3014", ETIHAD, CORK, DUBLIN, LocalDate.of(2026, 6, 14), LocalTime.of(10, 30), LocalTime.of(12, 45), 4290.0, 27, true)
                    ));

                System.out.println("Dummy flights successfully seeded into the database.");
            } else {
                System.out.println("Flights already exist. No seeding required.");
            }
        };
    }
}
