package financeflow.com;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CategoriesTab {
    private ListView<Category> list;
    private ObservableList<Category> data;

    private final FinanceManager manager;
    private final DataStore store;

    private Label title;

    public CategoriesTab(FinanceManager financeManager, DataStore dataStore){
        manager = financeManager;
        store = dataStore;
    }

    public Tab createTab(){
        list = new ListView<>();
        data = FXCollections.observableArrayList(manager.getCategories());
        list.setItems(data);
        title = new Label();
        title.setText("List of Categories");
        
        //Buttons
        Button addCategoryBtn = new Button("Add Category");
        Button deleteCategoryBtn = new Button("Delete Category");
        Button renameCategoryBtn = new Button ("Rename Category");

        addCategoryBtn.setOnAction(e -> addCategory());
        deleteCategoryBtn.setOnAction(e -> deleteCategory());
        renameCategoryBtn.setOnAction(e -> renameCategory());

        HBox buttons = new HBox(10, addCategoryBtn, deleteCategoryBtn, renameCategoryBtn);

        VBox root = new VBox(10, title, list, buttons);
        root.setPadding(new Insets(15));

        Tab tab = new Tab("Categories");
        tab.setContent(root);
        tab.setClosable(false);
        return tab;
    }

    private void addCategory() {
        try {
            TextInputDialog categoryDialog = new TextInputDialog();
            categoryDialog.setHeaderText("Enter Category");
            String categoryName = categoryDialog.showAndWait().orElse(null);

            Category category = manager.findCategoryByName(categoryName);
            if (category == null) {
                manager.addCategory(categoryName);
            }

            saveData();
            data.setAll(manager.getCategories());

        } catch (Exception e) {
            showError("Invalid input.");
        }
    }

    private void deleteCategory() {
        try {
            int index = list.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                manager.deleteCategory(manager.getCategories().get(index).getName());
                data.setAll(manager.getCategories());

                saveData();
            }
        } catch (Exception e) {
            showError("Category is being used by a transaction. Delete transaction and try again.");
        }
    }

    private void renameCategory() {
        try {
            int index = list.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                String oldname = manager.getCategories().get(index).getName();
                TextInputDialog categoryDialog = new TextInputDialog();
                categoryDialog.setHeaderText("Rename Category");
                String newName = categoryDialog.showAndWait().orElse(null);
                manager.renameCategory(oldname, newName);
                data.setAll(manager.getCategories());

                saveData();
            }

        } catch (Exception e) {
            showError("Invalid input.");
        }
    }

    private void saveData() {
        try {
            store.save(manager, "data/finance_data.txt");
        } catch (Exception e) {
            showError("Failed to save data.");
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

}