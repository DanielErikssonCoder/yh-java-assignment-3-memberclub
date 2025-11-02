import com.memberclub.system.*;
import com.memberclub.ui.*;

public class Main {

    public static void main(String[] args) {

        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("console.encoding", "UTF-8");

        ClubSystem system = new ClubSystem();
        ConsoleMenu menu = new ConsoleMenu(system);

        menu.start();
    }
}