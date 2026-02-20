import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemRepository {
    private Map<String, Item> itemMap = new HashMap<>();
    private final String FILE_PATH = "items.json";
    private ObjectMapper mapper = new ObjectMapper();

    public ItemRepository() throws Exception {
        loadItems();
    }

    private void loadItems() throws Exception {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            file.createNewFile();
            mapper.writeValue(file, new ArrayList<Item>());
            return;
        }

        List<Item> items = mapper.readValue(
                file,
                new TypeReference<List<Item>>() {
                });

        for (Item item : items) {
            itemMap.put(item.getUniqueId(), item);
        }
    }

    public void addItem(Item item) throws Exception {
        itemMap.put(item.getUniqueId(), item);
        saveToFile();
    }

    private void saveToFile() throws Exception {
        List<Item> items = new ArrayList<>(itemMap.values());
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(FILE_PATH), items);
    }

    public void updateItem(Item updatedItem) throws Exception {
        String id = updatedItem.getUniqueId();
        if (itemMap.containsKey(id)) {
            itemMap.put(id, updatedItem);
            saveToFile();
        } else {
            throw new IllegalArgumentException("Item not found: " + id);
        }
    }

    public Item findById(String id) {
        return itemMap.get(id);
    }

    public List<Item> searchByName(String keyword) {
        List<Item> result = new ArrayList<>();

        if (keyword == null || keyword.isEmpty()) {
            return getAllItems();
        }

        String lowerKeyword = keyword.toLowerCase();

        for (Item item : itemMap.values()) {
            if (item.getName().toLowerCase().contains(lowerKeyword)) {
                result.add(item);
            }
        }

        return result;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(itemMap.values());
    }

    public void showAll() {
        List<Item> items = getAllItems();
        if (items.isEmpty()) {
            System.out.println("There is not found product.");
            return;
        }
        for (Item item : items) {
            System.out.println("ID: " + item.getUniqueId());
            System.out.println("Name: " + item.getName());
            System.out.println("Description: " + item.getDescription());
            System.out.println("Income Price: " + item.getIncomePrice());
            System.out.println("Selling Price: " + item.getSellingPrice());
            System.out.println("-----------------------------");
        }
    }

    public void deleteItem(String id) throws Exception {
        if (itemMap.containsKey(id)) {
            itemMap.remove(id);
            saveToFile();
        } else {
            throw new IllegalArgumentException("Item not found: " + id);
        }
    }

    public Item findByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            return null;
        }
        for (Item item : itemMap.values()) {
            if (barcode.equals(item.getBarcode())) {
                return item;
            }
        }
        return null; // when barcode is not found
    }
}
