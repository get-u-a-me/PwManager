public class Main {
    public static void main(String[] args) {
        //Creating the Account Manager
        Manager manager = new Manager();
        //load Accounts from save file
        manager.load();
        //creating the UI
        new GUI(manager);
    }
}