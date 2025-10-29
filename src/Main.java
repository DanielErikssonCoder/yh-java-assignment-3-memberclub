import com.memberclub.model.*;
import com.memberclub.model.fishing.*;
import com.memberclub.model.camping.*;
import com.memberclub.model.vehicles.*;
import com.memberclub.pricing.*;
import com.memberclub.service.*;
import com.memberclub.system.*;
import com.memberclub.ui.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ClubSystem system = new ClubSystem();
        ConsoleMenu menu = new ConsoleMenu(system);
        menu.start();

    }
}