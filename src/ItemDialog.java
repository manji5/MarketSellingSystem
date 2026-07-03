import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemDialog extends JDialog {

    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField incomePriceField;
    private JTextField sellingPriceField;
    private JTextField barcodeField;
    private JButton saveButton;
    private JButton cancelButton;

    private Item item;
    private ItemRepository itemRepository;

    public ItemDialog(Frame owner, Item itemToEdit) {
        super(owner, true);
        this.item = itemToEdit;
        if (owner instanceof ItemManagementApp) {
            this.itemRepository = ((ItemManagementApp) owner).getItemRepository();
        } else {
            System.err.println("Dialog owner is not an instance of ItemManagementApp!");
            System.exit(1);
        }

        setTitle(item == null ? "Add New Item" : "Edit Item");
        setLayout(new GridLayout(6, 2, 10, 10)); 

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("Income Price:"));
        incomePriceField = new JTextField();
        add(incomePriceField);

        add(new JLabel("Selling Price:"));
        sellingPriceField = new JTextField();
        add(sellingPriceField);

        add(new JLabel("Barcode:"));
        barcodeField = new JTextField();
        add(barcodeField);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        add(saveButton);
        add(cancelButton);

        if (item != null) {
            nameField.setText(item.getName());
            descriptionField.setText(item.getDescription());
            incomePriceField.setText(String.valueOf(item.getIncomePrice()));
            sellingPriceField.setText(String.valueOf(item.getSellingPrice()));
            barcodeField.setText(item.getBarcode());
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveItem();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(owner);
    }

    private void saveItem() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String barcode = barcodeField.getText();

        double incomePrice, sellingPrice;
        try {
            incomePrice = Double.parseDouble(incomePriceField.getText());
            sellingPrice = Double.parseDouble(sellingPriceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prices must be valid numbers.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (item == null) {
                Item newItem = new Item(name, description, incomePrice, sellingPrice, barcode);
                itemRepository.addItem(newItem);
            } else { 
                item.setName(name);
                item.setDescription(description);
                item.setIncomePrice(incomePrice);
                item.setSellingPrice(sellingPrice);
                item.setBarcode(barcode); 
                itemRepository.updateItem(item);
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving item: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
