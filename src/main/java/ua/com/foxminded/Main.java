package ua.com.foxminded;

public class Main {

    public static void main(String[] args) throws Exception {
        SqlScript sqlScript = new SqlScript();
        sqlScript.executeScript("schema.sql");
        Menu menu = new Menu();
        menu.getMenu();

    }

}
