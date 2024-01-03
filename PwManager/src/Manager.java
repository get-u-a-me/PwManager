

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Manager {
    private ArrayList<Account> list = new ArrayList<>();
    private ArrayList<String> platformList = new ArrayList<>();
    private final File file = new File("savedPasswords.txt");

    public ArrayList<Account> getList() {
        return list;
    }

    // Adding accounts to list check if account already in list
    public void add(Account account){
        if(list.contains(account)){
            return;
        }
        list.add(account);
    }

    // remove account from list
    public void remove(Account account){
        if(list.contains(account)){
            list.remove(account);
        }
    }

    // edit Account
    public void edit(Account account, int value, String newValue){
        if(account == null){
            return;
        }

        switch (value){
            case 0 -> list.set(list.indexOf(account), new Account(newValue, account.userName(), account.password(), account.email()));
            case 1 -> list.set(list.indexOf(account), new Account(account.platform(), newValue, account.password(), account.email()));
            case 2 -> list.set(list.indexOf(account), new Account(account.platform(), account.userName(), newValue, account.email()));
            case 3 -> list.set(list.indexOf(account), new Account(account.platform(), account.userName(), account.password(), newValue));
        }

    }

    //Creating String array with the Platforms we have in list
    public String[] getPlatformList(){
        String[] platforms = new String[list.size()];
        int counter = 0;
        for (Account account : list) {
            if (account != null && account.platform() != null) {
                platforms[counter] = account.platform();
                counter++;
            }
        }

        return platforms;
    }

    // save list to textfile, path defined in class parameter
    public void save(){
        try {
            if(!file.exists()){ //check if the file is already existent
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);

            for(Account value : list){ //write all to the file with format || platform:name:password\n
                writer.write(value.platform() + ":" + value.userName() + ":" + value.password() + ":" + value.email() + "\n");
            }

            writer.close();

        } catch(IOException e){
            System.out.println("could not save Passwords.");
        }
    }

    //load list from textfile, path defined in class parameter
    public void load(){
        try{
            if(!file.exists()){ //check if the file is already existent else return
                return;
            }

            FileReader reader = new FileReader(file);

            //reading data
            String fullString = "";
            int tmp = 0;
            while ((tmp = reader.read()) != -1){
                fullString+= (char)tmp;
            }
            String[] zeilen = fullString.split("\n");
            if(!zeilen[0].isEmpty()){
                for (String value : zeilen){
                    String[] daten = value.split(":");

                    add(new Account(daten[0], daten[1], daten[2], daten[3]));
                }
            }
            reader.close();

        } catch (IOException e){
            System.out.println("Could not load File.");
        }
    }

}
