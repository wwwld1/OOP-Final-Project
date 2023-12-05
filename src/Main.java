import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Category {
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
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
}

class FinancialManager extends JFrame {
    private List<Expense> expenses;

    public FinancialManager() {
        expenses = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddExpenseDialog();
            }
        });

        JButton browseExpensesButton = new JButton("Browse Expenses");
        browseExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBrowseExpensesDialog();
            }
        });

        JButton deleteExpenseButton = new JButton("Delete Expense");
        deleteExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteExpenseDialog();
            }
        });

        JPanel panel = new JPanel();
        panel.add(addExpenseButton);
        panel.add(browseExpensesButton);
        panel.add(deleteExpenseButton);

        this.getContentPane().add(panel);

        pack();
    }

    private void showAddExpenseDialog() {
        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField descriptionField = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Expense", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            double amount = Float.parseFloat(amountField.getText());
            Category category = new Category(categoryField.getText());
            String date = dateField.getText();
            String description = descriptionField.getText();

            Expense newExpense = new Expense(name, amount, category, date, description);
            expenses.add(newExpense);

        }
    }

    private void showBrowseExpensesDialog() {
        StringBuilder expensesText = new StringBuilder("Expenses:\n");

        for (Expense expense : expenses) {
            expensesText.append(String.format("Name: %s, Amount: %.2f, Category: %s, Date: %s, Description: %s\n",
                    expense.getName(), expense.getAmount(), expense.getCategory().getCategoryName(),
                    expense.getDate(), expense.getDescription()));
        }

        JOptionPane.showMessageDialog(null, new JTextArea(expensesText.toString()),
                "Browse Expenses", JOptionPane.PLAIN_MESSAGE);
    }

    private void showDeleteExpenseDialog() {
        Object[] expenseOptions = expenses.toArray();
        Object selectedExpense = JOptionPane.showInputDialog(null,
                "Select an expense to delete:", "Delete Expense",
                JOptionPane.QUESTION_MESSAGE, null, expenseOptions, expenseOptions[0]);

        if (selectedExpense != null) {
            expenses.remove((Expense) selectedExpense);
            JOptionPane.showMessageDialog(null, "Expense deleted successfully.",
                    "Delete Expense", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FinancialManager financialManager = new FinancialManager();
                financialManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                financialManager.setVisible(true);
            }
        });
    }
}
