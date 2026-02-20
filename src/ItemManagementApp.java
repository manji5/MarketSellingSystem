import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ItemManagementApp extends JFrame {
    private ItemRepository itemRepository;
    private JTable itemTable;
    private DefaultTableModel itemTableModel;
    private JTextField searchField;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private List<OrderItem> cartItems = new ArrayList<>();
    private JTextField barcodeInputField;
    private JSpinner quantitySpinner;
    private JLabel totalLabel;

    public ItemManagementApp() {
        try {
            itemRepository = new ItemRepository();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading items: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Item Management and Sales System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel managementPanel = createManagementPanel();
        JPanel salesPanel = createSalesPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, managementPanel, salesPanel);
        splitPane.setDividerLocation(400);

        add(splitPane);
        refreshItemTable();
    }

    // --- createManagementPanel() ve diğer üst panel metotları aynı kalıyor ---
    private JPanel createManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Item Management"));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by Name:"));
        searchField = new JTextField(30);
        searchPanel.add(searchField);
        panel.add(searchPanel, BorderLayout.NORTH);
        String[] itemColumns = { "ID", "Name", "Description", "Selling Price (USD)", "Barcode" };
        itemTableModel = new DefaultTableModel(itemColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable = new JTable(itemTableModel);
        panel.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        addManagementListeners(addButton, editButton, deleteButton, refreshButton);
        return panel;
    }

    // --- createSalesPanel() aynı kalıyor ---
    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Sales Cart"));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Barcode/ID/Name:"));
        barcodeInputField = new JTextField(20);
        topPanel.add(barcodeInputField);
        topPanel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        topPanel.add(quantitySpinner);
        JButton addToCartButton = new JButton("Add to Cart");
        topPanel.add(addToCartButton);
        panel.add(topPanel, BorderLayout.NORTH);
        String[] cartColumns = { "Name", "Quantity", "Unit Price", "Total Price" };
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: 0.00 USD");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        JPanel confirmPanel = new JPanel();
        JButton confirmOrderButton = new JButton("Confirm Order");
        JButton clearCartButton = new JButton("Clear Cart");
        confirmPanel.add(clearCartButton);
        confirmPanel.add(confirmOrderButton);
        bottomPanel.add(confirmPanel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        addSalesListeners(addToCartButton, confirmOrderButton, clearCartButton);
        return panel;
    }

    // --- addManagementListeners() aynı kalıyor ---
    private void addManagementListeners(JButton add, JButton edit, JButton del, JButton refresh) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterItemTable();
            }

            public void removeUpdate(DocumentEvent e) {
                filterItemTable();
            }

            public void changedUpdate(DocumentEvent e) {
                filterItemTable();
            }
        });
        refresh.addActionListener(e -> refreshItemTable());
        add.addActionListener(e -> {
            ItemDialog dialog = new ItemDialog(this, null);
            dialog.setVisible(true);
            refreshItemTable();
        });
        edit.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow >= 0) {
                String itemId = (String) itemTableModel.getValueAt(selectedRow, 0);
                Item selectedItem = itemRepository.findById(itemId);
                if (selectedItem != null) {
                    ItemDialog dialog = new ItemDialog(this, selectedItem);
                    dialog.setVisible(true);
                    refreshItemTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to edit.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        del.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    String itemId = (String) itemTableModel.getValueAt(selectedRow, 0);
                    try {
                        itemRepository.deleteItem(itemId);
                        refreshItemTable();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error deleting item: " + ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to delete.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void addSalesListeners(JButton addButton, JButton confirmButton, JButton clearButton) {
        ActionListener addItemAction = e -> addItemToCart();
        addButton.addActionListener(addItemAction);
        barcodeInputField.addActionListener(addItemAction);

        clearButton.addActionListener(e -> {
            cartItems.clear();
            updateCartTable();
        });

        confirmButton.addActionListener(e -> {
            if (cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double totalAmount = 0;
            for (OrderItem oi : cartItems) {
                totalAmount += oi.getTotalPrice();
            }

            String[] paymentOptions = { "Cash", "Credit Card" };
            int paymentChoice = JOptionPane.showOptionDialog(this,
                    String.format("Total Amount: %.2f USD\nPlease select your payment method:", totalAmount),
                    "Payment Method",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, paymentOptions, paymentOptions[0]);

            if (paymentChoice == 0) {
                handleCashPayment(totalAmount);
            } else if (paymentChoice == 1) {
                handleCardPayment(totalAmount);
            }
        });
    }

    private void handleCashPayment(double totalAmount) {
        String cashInput = JOptionPane.showInputDialog(this, "Enter the amount of cash received:", "Cash Payment",
                JOptionPane.PLAIN_MESSAGE);

        if (cashInput == null) {
            return;
        }

        try {
            double cashReceived = Double.parseDouble(cashInput);
            if (cashReceived < totalAmount) {
                JOptionPane.showMessageDialog(this,
                        "The amount of cash received cannot be less than the total amount.!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            double change = cashReceived - totalAmount;
            JOptionPane.showMessageDialog(this, String.format("Reminder Money: %.2f USD", change),
                    "Transaction Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            finalizeOrder("Nakit", totalAmount);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number..", "Invalid Login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCardPayment(double totalAmount) {
        String[] cardOptions = { "One Time", "Installment" };
        int cardChoice = JOptionPane.showOptionDialog(this, "Select your card payment type:", "Credit Card",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cardOptions, cardOptions[0]);

        if (cardChoice == 0) {
            finalizeOrder("Credit Card - Debit Card", totalAmount);
        } else if (cardChoice == 1) {
            String[] installmentOptions = { "3 installment", "6 installment", "9 installment", "12 installment" };
            String selectedInstallment = (String) JOptionPane.showInputDialog(this,
                    "Choose the number of installments:",
                    "Taksit Seçenekleri",
                    JOptionPane.QUESTION_MESSAGE, null, installmentOptions, installmentOptions[0]);

            if (selectedInstallment != null) {
                finalizeOrder("Credit Card - " + selectedInstallment, totalAmount);
            }
        }
    }

    private void finalizeOrder(String paymentMethod, double amount) {
        System.out.println("Order Confirmed!");
        System.out.println("Payment Method: " + paymentMethod);
        System.out.printf("Total Amount: %.2f USD\n", amount);
        System.out.println("--------------------");

        // Sepeti temizle
        cartItems.clear();
        updateCartTable();

        JOptionPane.showMessageDialog(this, "Payment completed successfully!", "Transaction Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void addItemToCart() {
        String input = barcodeInputField.getText().trim();
        if (input.isEmpty())
            return;
        int quantity = (Integer) quantitySpinner.getValue();
        Item foundItem = itemRepository.findByBarcode(input);
        if (foundItem == null)
            foundItem = itemRepository.findById(input);
        if (foundItem == null) {
            List<Item> itemsByName = itemRepository.searchByName(input);
            if (!itemsByName.isEmpty())
                foundItem = itemsByName.get(0);
        }
        if (foundItem != null) {
            boolean itemExists = false;
            for (OrderItem orderItem : cartItems) {
                if (orderItem.getItem().getUniqueId().equals(foundItem.getUniqueId())) {
                    orderItem.setQuantity(orderItem.getQuantity() + quantity);
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists)
                cartItems.add(new OrderItem(foundItem, quantity));
            updateCartTable();
            barcodeInputField.setText("");
            quantitySpinner.setValue(1);
            barcodeInputField.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        double total = 0;
        for (OrderItem orderItem : cartItems) {
            Object[] row = {
                    orderItem.getItem().getName(),
                    orderItem.getQuantity(),
                    orderItem.getItem().getSellingPrice(),
                    orderItem.getTotalPrice()
            };
            cartTableModel.addRow(row);
            total += orderItem.getTotalPrice();
        }
        totalLabel.setText(String.format("Total: %.2f USD", total));
    }

    private void filterItemTable() {
        List<Item> filtered = itemRepository.searchByName(searchField.getText());
        updateItemTableWithData(filtered);
    }

    private void refreshItemTable() {
        searchField.setText("");
        List<Item> allItems = itemRepository.getAllItems();
        updateItemTableWithData(allItems);
    }

    private void updateItemTableWithData(List<Item> items) {
        itemTableModel.setRowCount(0);
        for (Item item : items) {
            Object[] row = { item.getUniqueId(), item.getName(), item.getDescription(), item.getSellingPrice(),
                    item.getBarcode() };
            itemTableModel.addRow(row);
        }
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ItemManagementApp().setVisible(true));
    }
}