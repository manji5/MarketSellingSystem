# Inventory and Sales Management System (Alpha Version)

This project is a desktop-based Inventory Management and Point of Sale (POS) application developed using Java Swing. It's designed for small to medium-sized businesses to manage products, search by barcode or name, and process orders through a simple sales interface.

**Note:** This project is currently in its **alpha** stage. While core features are functional, it is still under active development.

![Application Screenshot](screenshot.png)
*(Replace this with a screenshot of your application. Add the `screenshot.png` file to the root directory of your project.)*

## Features

### Item Management
- **CRUD Operations:** Easily add, edit, and delete products through the graphical user interface. You can manage essential details like name, description, price, and barcode.
- **Persistent Storage:** All product data is stored in an `items.json` file located in the project's root directory. The application reads from this file on startup and saves any changes back to it.
- **Live Search:** Instantly filter the product list by typing in the search bar, allowing you to find items quickly.

### Point of Sale (POS) Interface
- **Flexible Product Entry:** Add items to the sales cart using their **Barcode**, **ID**, or **Name**. The system is compatible with barcode scanners that emulate keyboard input.
- **Quantity Control:** Easily specify the quantity for each item being added to the cart using a spinner.
- **Dynamic Shopping Cart:** The cart table provides a real-time view of all items, including their quantity, unit price, and total price.
- **Automatic Total Calculation:** The grand total is automatically updated as items are added to or removed from the cart.

### Advanced Payment Workflow
- **Multiple Payment Options:** After confirming an order, the user can choose between **Cash** or **Credit Card** payments.
- **Cash Payment:** Prompts the user to enter the amount of cash received and automatically calculates the change.
- **Credit Card Payment:** Provides options for a **Single Payment** or **Installments** (3, 6, 9, or 12 months).

## Technology Stack
- **Language:** Java (JDK 11+)
- **GUI Framework:** Java Swing
- **Data Serialization:** Jackson Library (for reading from and writing to JSON)
- **Build & Dependency Management:** Apache Maven

## Setup and Installation

Follow these steps to run the project on your local machine.

### Prerequisites
- Java Development Kit (JDK) 11 or newer.
- Apache Maven.
- Git (for cloning the repository).

### Steps
1.  **Clone the Repository:**
    ```bash
    git clone [YOUR_PROJECT_URL]
    cd [YOUR_PROJECT_DIRECTORY]
    ```

2.  **Install Dependencies:**
    The project relies on the Jackson library. Maven will automatically download this dependency from the `pom.xml` file.
    ```bash
    mvn clean install
    ```

3.  **Run the Application:**
    - **Via an IDE (Recommended):** Open the project in an IDE like IntelliJ IDEA or Eclipse as an "Existing Maven Project." Locate and run the `ItemManagementApp.java` file in `src/main/java`.
    - **Via Command Line:**
      ```bash
      mvn exec:java -Dexec.mainClass="ItemManagementApp"
      ```

The application will automatically create an `items.json` file in the project directory on its first run.

## How to Use
1.  Launch the application.
2.  **Item Management (Top Panel):**
    - Click "Add Item" to create new products. Make sure to enter a unique barcode for each item.
    - Select an item from the list to use the "Edit Selected" or "Delete Selected" buttons.
3.  **Sales Interface (Bottom Panel):**
    - Enter a product's barcode, ID, or name into the "Barcode/ID/Name" field.
    - Adjust the quantity and press the "Add to Cart" button (or hit `Enter`).
    - Once the cart is ready, click "Confirm Order."
    - Follow the on-screen prompts to complete the payment process.

## Future Plans & To-Do
- [ ] **Database Integration:** Replace the `items.json` file with a robust database system like SQLite, H2, or MySQL.
- [ ] **Stock Management:** Automatically deduct item quantities from stock after a sale is completed.
- [ ] **Reporting:** Generate daily, weekly, or monthly sales reports.
- [ ] **User Authentication:** Implement a login system with different user roles (e.g., admin, cashier).
- [ ] **UI/UX Enhancements:** Use a modern Look and Feel library (e.g., FlatLaf) to improve the visual design.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.