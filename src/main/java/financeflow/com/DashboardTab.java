package financeflow.com;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardTab {
    private final FinanceManager manager;
    private final DataStore store;

    private Label title;
    private Label subTitle;

    private Label incomeLabel;
    private Label expenseLabel;
    private Label balanceLabel;

    private Label incomeValue;
    private Label expenseValue;
    private Label balanceValue;

    public DashboardTab(FinanceManager financeManager, DataStore dataStore){
        manager = financeManager;
        store = dataStore;
    }

    public Tab createTab(){
        title = new Label();
        subTitle = new Label();
        incomeLabel = new Label();
        expenseLabel = new Label();
        balanceLabel = new Label();
        incomeValue = new Label();
        expenseValue = new Label();
        balanceValue = new Label();

        title.setText("Welcome to FinanceFlow!");
        title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 32));
        subTitle.setText("Select tabs above to set finance goals!");
        subTitle.setFont(Font.font("Arial", 18));

        incomeLabel.setText("Total Income:");
        incomeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));
        incomeValue.setText("0");
        incomeValue.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        expenseLabel.setText("Total expense:");
        expenseLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));
        expenseValue.setText("0");
        expenseValue.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        VBox subSummary = new VBox(10, incomeLabel, incomeValue, expenseLabel, expenseValue);
        subSummary.setAlignment(Pos.BASELINE_CENTER);

        balanceLabel.setText("Remaining Balance:");
        balanceLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 24));
        balanceValue.setText("0");
        balanceValue.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        VBox balance = new VBox(20, balanceLabel, balanceValue);
        balance.setAlignment(Pos.BASELINE_CENTER);

        HBox summary = new HBox(100, subSummary, balance);
        summary.setAlignment(Pos.BASELINE_CENTER);

        Button updateBtn = new Button("Update Summary");
        updateBtn.setOnAction(e -> updateSummary());

        VBox root = new VBox(20, title, subTitle, summary, updateBtn);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        updateSummary();
        
        Tab tab = new Tab("Dashboard");
        tab.setContent(root);
        tab.setClosable(false);
        return tab;
    }

    private void updateSummary() {
        double income = manager.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expenses = manager.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        incomeValue.setText(String.valueOf(income));
        expenseValue.setText(String.valueOf(expenses));
        balanceValue.setText(String.valueOf(income - expenses));
    }
}
