import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemDialog extends JDialog {

    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField incomePriceField;
    private JTextField sellingPriceField;
    private JTextField barcodeField; // <-- YENİ: Barkod için metin alanı
    private JButton saveButton;
    private JButton cancelButton;

    private Item item;
    private ItemRepository itemRepository;

    public ItemDialog(Frame owner, Item itemToEdit) {
        super(owner, true);
        this.item = itemToEdit;
        // Ana pencereden repository'i al (Eğer ItemManagementApp değilse cast'i
        // düzeltmelisiniz)
        // Önceki kodda bu satır yoktu, ekleyelim.
        if (owner instanceof ItemManagementApp) {
            this.itemRepository = ((ItemManagementApp) owner).getItemRepository();
        } else {
            // Hata durumu veya farklı bir ana pencere için bir yedek plan.
            // Bu örnekte programı kapatıyoruz.
            System.err.println("Dialog owner is not an instance of ItemManagementApp!");
            System.exit(1);
        }

        setTitle(item == null ? "Add New Item" : "Edit Item");
        // Bir alan daha eklediğimiz için satır sayısını 6'ya çıkarıyoruz
        setLayout(new GridLayout(6, 2, 10, 10)); // <-- DEĞİŞTİ

        // Form Elemanları
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

        // <-- YENİ BÖLÜM: Barkod alanı ekleniyor -->
        add(new JLabel("Barcode:"));
        barcodeField = new JTextField();
        add(barcodeField);
        // <-- YENİ BÖLÜM SONU -->

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        add(saveButton);
        add(cancelButton);

        // Eğer düzenleme modundaysak, alanları doldur
        if (item != null) {
            nameField.setText(item.getName());
            descriptionField.setText(item.getDescription());
            incomePriceField.setText(String.valueOf(item.getIncomePrice()));
            sellingPriceField.setText(String.valueOf(item.getSellingPrice()));
            barcodeField.setText(item.getBarcode()); // <-- YENİ: Barkod alanını doldur
        }

        // --- Event Listeners ---
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
        // Alanlardan verileri oku
        String name = nameField.getText();
        String description = descriptionField.getText();
        String barcode = barcodeField.getText(); // <-- YENİ: Barkod alanından veriyi oku

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
            if (item == null) { // Yeni item ekleme
                // Constructor'a barkodu da ekliyoruz
                Item newItem = new Item(name, description, incomePrice, sellingPrice, barcode); // <-- DEĞİŞTİ
                itemRepository.addItem(newItem);
            } else { // Mevcut item'ı güncelleme
                item.setName(name);
                item.setDescription(description);
                item.setIncomePrice(incomePrice);
                item.setSellingPrice(sellingPrice);
                item.setBarcode(barcode); // <-- DEĞİŞTİ: Barkodu da set et
                itemRepository.updateItem(item);
            }
            dispose(); // Başarılı olursa pencereyi kapat
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving item: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}