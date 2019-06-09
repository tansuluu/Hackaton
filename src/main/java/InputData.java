import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputData {

    List<Item> items = Arrays.asList(
            new Item("Candy"),
            new Item("Drink"),
            new Item("Soda"),
            new Item("Popcorn"),
            new Item("Snacks")
    );
    Map<Users, HashMap<Item, Double>> data;
}
