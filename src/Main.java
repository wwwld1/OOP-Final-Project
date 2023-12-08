import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Category {
    private String categoryName;
    private double monthlyBudget;

    public Category(String categoryName, double monthlyBudget) {
        this.categoryName = categoryName;
        this.monthlyBudget = monthlyBudget;
    }

    public String getCategoryName() {
        return categoryName;
    }
}

class Expense {
    private String name;
    private double amount;
    private Category category;
    private String date;
    private String description;

    public Expense(String name, double amount, Category category, String date, String description) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.getName() + " $" + this.getAmount() + " " + this.getCategory().getCategoryName() + " " + this.getDate() + " " + this.getDescription();
    }
}

class MainGUI extends JFrame{
    private FinancialManager financialManager;

    public MainGUI(FinancialManager financialManager){
        this.financialManager = financialManager;
        initComponents();
    }

    private void initComponents() {
        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddGUI addgui = new AddGUI(financialManager);
                addgui.showAddExpenseDialog();
            }
        });

        JButton browseExpensesButton = new JButton("Browse Expenses");
        browseExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseGUI browsegui = new BrowseGUI(financialManager);
                browsegui.showBrowseExpensesDialog();
            }
        });

        JButton deleteExpenseButton = new JButton("Delete Expense");
        deleteExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteGUI deletegui = new DeleteGUI(financialManager);
                deletegui.showDeleteExpenseDialog();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Add space between buttons

        panel.add(addExpenseButton, gbc);
        panel.add(browseExpensesButton, gbc);
        panel.add(deleteExpenseButton, gbc);

        this.getContentPane().add(panel);

        pack();
        setSize(300, 200);
        setLocationRelativeTo(null); // Center the window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FinancialManager financialManager = new FinancialManager();
                MainGUI maingui = new MainGUI(financialManager);
                maingui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                maingui.setVisible(true);
            }
        });
    }
}

class AddGUI extends JFrame{
    private FinancialManager financialManager;
    private JComboBox<String> categoryComboBox;

    public AddGUI(FinancialManager financialManager) {
        this.financialManager = financialManager;
    }

    public void showAddExpenseDialog() {
        ImageIcon icon = new ImageIcon("src/add.png");
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);

        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        //JTextField categoryField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField descriptionField = new JTextField();

        categoryComboBox = new JComboBox<>();
        updateCategoryComboBox();
        //categoryComboBox.addItem("Add Category...");
        JPanel panel = new JPanel();

        categoryComboBox.addActionListener(e -> {
            if ("Add Category...".equals(categoryComboBox.getSelectedItem())) {
                showAddCategoryDialog();
            }
        });

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Add Expense", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);

        if (result == JOptionPane.OK_OPTION) {
            try{
                String name = nameField.getText();
                double amount = Float.parseFloat(amountField.getText());
                String categoryName = (String)categoryComboBox.getSelectedItem();
                Category category = financialManager.getCategoryByName(categoryName);
                String date = dateField.getText();
                String description = descriptionField.getText();

                Expense newExpense = new Expense(name, amount, category, date, description);
                financialManager.addExpense(newExpense);
            }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

            

        }
    }

    private void updateCategoryComboBox() {
        String currentSelection = (String) categoryComboBox.getSelectedItem();

        categoryComboBox.removeAllItems();
        for (Category categoryName : financialManager.getCategories()) {
            categoryComboBox.addItem(categoryName.getCategoryName());
        }
        categoryComboBox.addItem("Add Category..."); // 保证这个选项始终在最后

        if (currentSelection != null) {
            categoryComboBox.setSelectedItem(currentSelection);
        }
    }

    private void showAddCategoryDialog() {
        JTextField nameField = new JTextField();
        JTextField budgetField = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Category Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Monthly Budget:"));
        panel.add(budgetField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Category", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            double monthlyBudget = Double.parseDouble(budgetField.getText());
            financialManager.addCategory(name, monthlyBudget);
            updateCategoryComboBox(); // 更新下拉菜单
        }
    }

}

class BrowseGUI extends JFrame{
    private FinancialManager financialManager;

    public BrowseGUI(FinancialManager financialManager) {
        this.financialManager = financialManager;
    }
    public void showBrowseExpensesDialog() {
        ImageIcon icon = new ImageIcon("src/browse.png");
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Amount");
        model.addColumn("Category");
        model.addColumn("Date");
        model.addColumn("Description");

        // Add a row to the model for each expense
        for (Expense expense : financialManager.getExpenses()) {
            Object[] row = new Object[] {
                    expense.getName(),
                    expense.getAmount(),
                    expense.getCategory().getCategoryName(),
                    expense.getDate(),
                    expense.getDescription()
            };
            model.addRow(row);
        }

        // Create table with model
        JTable table = new JTable(model);

        // Create scroll pane and add table to it
        JScrollPane scrollPane = new JScrollPane(table);

        // Show dialog with scroll pane
        JOptionPane.showMessageDialog(null, scrollPane, "Browse Expenses", JOptionPane.PLAIN_MESSAGE, icon);
    }
}

    
class DeleteGUI extends JFrame{
    private FinancialManager financialManager;

    public DeleteGUI(FinancialManager financialManager) {
        this.financialManager = financialManager;
    }
    public void showDeleteExpenseDialog() {
        ImageIcon icon = new ImageIcon("src/delete.png");
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);

        if (financialManager.getExpenses().isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no expenses to delete.",
                    "Delete Expense", JOptionPane.INFORMATION_MESSAGE, icon);
            return;
        }
        Object[] expenseOptions = financialManager.getExpenses().toArray();
        Object selectedExpense = JOptionPane.showInputDialog(null,
                "Select an expense to delete:", "Delete Expense",
                JOptionPane.QUESTION_MESSAGE, icon, expenseOptions, expenseOptions[0]);

        if (selectedExpense != null) {
            financialManager.deleteExpense((Expense) selectedExpense);
            JOptionPane.showMessageDialog(null, "Expense deleted successfully.",
                    "Delete Expense", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
    
class FinancialManager{
    private List<Expense> expenses;
    private List<Category> categories;

    public FinancialManager() {
        expenses = new ArrayList<>();
        categories = new ArrayList<>();
    }
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
    public List<Expense> getExpenses() {
        return expenses;
    }
    public void deleteExpense(Expense expense) {
        expenses.remove(expense);
    }
    public void addCategory(String name, double monthlyBudget) {
        categories.add(new Category(name, monthlyBudget));
    }
    public List<Category> getCategories() {
        return categories;
    }
    public Category getCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getCategoryName().equals(name)) {
                return category;
            }
        }
        return null; // Or handle this case as per your application's requirements
    }
}
