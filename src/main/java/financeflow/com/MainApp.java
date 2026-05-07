package financeflow.com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final FinanceManager manager = new FinanceManager();
    private final DataStore store = new DataStore();
    private final DashboardTab dashboardTabcontent = new DashboardTab(manager, store);
    private final TransactionTab transactionTabContent = new TransactionTab(manager, store);
    private final CategoriesTab categoriesTabContent = new CategoriesTab(manager, store);
    private final BudgetsTab budgetsTabContent = new BudgetsTab(manager, store);
    private final ReportsTab reportsTabContent = new ReportsTab(manager, store);

    @Override
    public void start(Stage stage) {

        // Load data
        try {
            store.load(manager, "data/finance_data.txt");
        } catch (Exception e) {
            System.out.println("No previous data found.");
        }
        
        TabPane tabPane = new TabPane();
        Tab dashboardTab = dashboardTabcontent.createTab();
        Tab transactionsTab = transactionTabContent.createTab();
        Tab categoriesTab = categoriesTabContent.createTab();
        Tab budgetsTab = budgetsTabContent.createTab();
        Tab reportsTab = reportsTabContent.createTab();

        dashboardTab.setClosable(false);
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