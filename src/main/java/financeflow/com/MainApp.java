package financeflow.com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final FinanceManager manager = new FinanceManager();
    private final DataStore store = new DataStore();
    private final TransactionTab transactionTabContent = new TransactionTab(manager, store);

    @Override
    public void start(Stage stage) {

        // Load data
        try {
            store.load(manager, "data/finance_data.txt");
        } catch (Exception e) {
            System.out.println("No previous data found.");
        }
        
        TabPane tabPane = new TabPane();
        Tab dashboardTab = new Tab("Dashboard");
        Tab transactionsTab = transactionTabContent.createTab();
        Tab categoriesTab = new Tab("Categories");
        Tab budgetsTab = new Tab("Budgets");
        Tab reportsTab = new Tab("Reports");

        dashboardTab.setClosable(false);
        categoriesTab.setClosable(false);
        budgetsTab.setClosable(false);
        reportsTab.setClosable(false);
        
        tabPane.getTabs().addAll(dashboardTab, transactionsTab, categoriesTab, budgetsTab, reportsTab);

        Scene scene = new Scene(tabPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("FinanceFlow");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}