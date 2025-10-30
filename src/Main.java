import com.memberclub.system.*;
import com.memberclub.ui.*;

public class Main {

    public static void main(String[] args) {

        ClubSystem system = new ClubSystem();

        ConsoleMenu menu = new ConsoleMenu(system);

        menu.start();

    }
}