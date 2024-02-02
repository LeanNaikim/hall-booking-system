package co.cstad.mtb;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MTBApplication {
    private static String[] studentIDs;
    private static String[] hallNames;
    private static String[] seatIdentifiers;
    private static String[] dates;
    private static int bookingCount = 0;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String validationNumber(String messages, Scanner input, String regex) {
        while (true) {
            System.out.print(messages);
            String userInput = input.nextLine();

            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(userInput).matches()) {
                return userInput;
            } else {
                System.out.println("Error! Invalid Please Input Number [0-9]: ");
            }
        }

    }

    public static String validationLetter(String messages, Scanner input, String regex) {
        while (true) {
            System.out.print(messages);
            String userInput = input.nextLine();

            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(userInput).matches()) {
                return userInput;
            } else {
                System.out.println("Error! Invalid Please Input Number [a-zA-Z]: ");
            }
        }
    }

    public static String[][] initializeSeatStatus(int rows, int columns) {
        char[][] alphabetMatrix = new char[rows][columns];
        char[] alphabet = new char[26];
        int index = 0;

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            alphabet[index] = ch;
            index++;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                alphabetMatrix[i][j] = alphabet[i % 26];
            }
        }

        String[][] seatStatus = new String[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seatStatus[i][j] = "AV";
            }
        }
        return seatStatus;
    }

    public static void selectAndBookSeats( String[][] seatStatus, Scanner scanner,String hallInput) {
        System.out.print("> Please select available seats: ");
        String seatInput = scanner.nextLine().toUpperCase();
        String[] seatIdentifiersArray = seatInput.split(",");
        System.out.print("Enter your student ID for seats " + seatInput + ": ");
        String studentCardID = scanner.nextLine();

        boolean valid = true;

        for (String seatIdentifier : seatIdentifiersArray) {
            String[] seatParts = seatIdentifier.split("-");
            if (seatParts.length == 2) {
                char chosenRow = seatParts[0].charAt(0);
                int chosenColumn = Integer.parseInt(seatParts[1]) - 1;

                if (isValidSeat(chosenRow, chosenColumn, seatStatus)) {
                    seatStatus[chosenRow - 'A'][chosenColumn] = "BO";
                    valid = true;
                } else {
                    System.out.println("Sorry, the chosen seat " + seatIdentifier + " is already booked or invalid.");
                    valid = false;
                }
            } else {
                System.out.println("Invalid seat format: " + seatIdentifier);
            }
        }
        if(valid){
            String dateTime = getCurrentDateTime();
            studentIDs[bookingCount] = studentCardID;
            hallNames[bookingCount] = hallInput;
            seatIdentifiers[bookingCount] = seatInput;
            dates[bookingCount] = dateTime;
            bookingCount++;
            System.out.println("Booking Successfully!");
        }

    }
    public static void displayBookingHistory() {
        if (bookingCount == 0){
            System.out.println("There is no history!!");
        }else {
            for (int i = 0; i < bookingCount; i++) {
                String studentID = studentIDs[i];
                String hallInput = hallNames[i];
                String seatIdentifier = seatIdentifiers[i];
                String date = dates[i];
                System.out.println("# NO: "+ (i+1));
                System.out.println("# SEATS: " + seatIdentifier);
                System.out.println("# HALL: " +hallInput);
                System.out.println("# STU.ID: "+ studentID);
                System.out.println("# CREATED AT: "+ date);
                System.out.println("---------------------------------------");
            }
        }
    }
    private static String getCurrentDateTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
    public static boolean isValidSeat(char row, int column, String[][] seatStatus) {
        return row >= 'A' && row < 'A' + seatStatus.length &&
                column >= 0 && column < seatStatus[0].length &&
                seatStatus[row - 'A'][column].equals("AV");
    }
    public static void displaySeatStatus(String[][] seatStatus) {
        char[][] alphabetMatrix = new char[seatStatus.length][seatStatus[0].length];

        for (int i = 0; i < seatStatus.length; i++) {
            for (int j = 0; j < seatStatus[0].length; j++) {
                alphabetMatrix[i][j] = (char) ('A' + i);
            }
        }

        for (int i = 0; i < seatStatus.length; i++) {
            for (int j = 0; j < seatStatus[0].length; j++) {
                String status = seatStatus[i][j];
                System.out.print("  |" + alphabetMatrix[i][j] + "-" + (j + 1) + "::" + status + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("CSTAD HALL BOOKING SYSTEM");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");

        int rows = Integer.parseInt(validationNumber("Config total rows in hall: ",scanner,"[0-9]+"));
        int columns = Integer.parseInt(validationNumber("Config total seats per row in hall: " , scanner , "[0-9]+"));

        studentIDs = new String[rows * columns * 3];
        hallNames = new String[rows * columns * 3];
        seatIdentifiers = new String[rows * columns * 3];
        dates = new String[rows * columns * 3];

        String[][] seatMorning = initializeSeatStatus(rows, columns);
        String[][] seatAfternoon = initializeSeatStatus(rows, columns);
        String[][] seatEvening = initializeSeatStatus(rows, columns);

        String choice;
        boolean isContinued = true;
        do {
            System.out.println("[[ Application Menu ]]");
            System.out.println("<A> Booking ");
            System.out.println("<B> Hall ");
            System.out.println("<C> Showtime");
            System.out.println("<D> Reboot Showtime");
            System.out.println("<E> History");
            System.out.println("<F> Exit");

            choice = validationLetter("Please Select Menu Numbers: ",scanner,"[a-zA-Z]+");
            String arrayChoice;
            switch (choice) {
                case "a","A" ->
                {
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Start booking process");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Showtime Information");
                    System.out.println("# A) Morning (10:00AM - 12:30PM)");
                    System.out.println("# B) Afternoon (03:00PM - 05:30PM)");
                    System.out.println("# C) Night (07:00PM - 09:30PM)");

                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    arrayChoice = validationLetter("Please Select Show Time (A | B | C): ",scanner, "[a-zA-Z]+");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");

                    switch (arrayChoice){
                        case "a","A" -> {
                            System.out.println("# HALL A");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            System.out.println("# INSTRUCTION");
                            System.out.println("# Single: C-1");
                            System.out.println("# Multiple (separate by comma): C-1,C-2");
                            selectAndBookSeats(seatMorning, scanner,"Hall A");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            displaySeatStatus(seatMorning);
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                        }
                        case "b","B" -> {
                            System.out.println("# HALL B");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            System.out.println("# INSTRUCTION");
                            System.out.println("# Single: C-1");
                            System.out.println("# Multiple (separate by comma): C-1,C-2");
                            selectAndBookSeats(seatAfternoon, scanner,"Hall B");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            displaySeatStatus(seatAfternoon);
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                        }
                        case "c","C" -> {
                            System.out.println("# HALL C");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            System.out.println("# INSTRUCTION");
                            System.out.println("# Single: C-1");
                            System.out.println("# Multiple (separate by comma): C-1,C-2");
                            selectAndBookSeats(seatEvening, scanner,"Hall C");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            displaySeatStatus(seatEvening);
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                        }
                        default -> System.out.println("Invalid Option!");
                    }
                }
                case "b","B" -> {
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall Information");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Morning");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    displaySeatStatus(seatMorning);
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Afternoon");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    displaySeatStatus(seatAfternoon);
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Evening");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    displaySeatStatus(seatEvening);
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
                case "c","C" -> {
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Daily Showtime of CSTAD Hall");
                    System.out.println("# A) Morning (10:00AM - 12:30PM");
                    System.out.println("# B) Afternoon (03:00PM - 05:30PM");
                    System.out.println("# C) Night (07:00PM - 09:30PM");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
                case "d","D" -> {
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("Start rebooting the hall... ");
                    bookingCount = 0;
                    seatMorning = initializeSeatStatus(rows, columns);
                    seatAfternoon = initializeSeatStatus(rows, columns);
                    seatEvening = initializeSeatStatus(rows, columns);
                    System.out.println("Rebooted Successfully.");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
                case "e","E" -> {
                    System.out.println("------------$ Booking History $----------");
                    displayBookingHistory();
                }
                case "f","F" -> {
                    System.out.println("Exited. Say Good Bye!! \uD83D\uDC7B\n");
                    isContinued = false;
                }
                default -> System.out.println("Invalid Option!");
            }
        }while (isContinued);
    }
}
