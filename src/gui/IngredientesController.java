package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import DB.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Ingredientes;
import service.IngredientesService;

public class IngredientesController implements Initializable, DataChangeListener {
	
	private ObservableList<Ingredientes> obsList;
	
	private IngredientesService service = new IngredientesService();
	
	public void setIngredientesService(IngredientesService service) {
		this.service = service;
	}

	@FXML
	private TableView<Ingredientes> tableViewIngredientes;

	@FXML
	private TableColumn<Ingredientes, String> tableColumnNome;
	
	@FXML
	private TableColumn<Ingredientes, Ingredientes> tableColumnEDIT;

	@FXML
	private TableColumn<Ingredientes, Ingredientes> tableColumnREMOVE;
	
	@FXML
	private TextField txtPesquisar;


	@FXML
	private Button btNovoIngrediente;

	@FXML
	public void onBtNovoIngredienteeAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Ingredientes obj = new Ingredientes();
		createDialogForm(obj, "/gui/CadastroIngredientesView.fxml", parentStage);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nomeIngrediente"));
		updateTableView();
		searchClientes();		
	}
	

	private void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service está nulo. Injetar o service.");
		}
		List<Ingredientes> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewIngredientes.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		searchClientes();
		
	}
	
	private void createDialogForm(Ingredientes obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CadastroIngredientesController controller = loader.getController();
			controller.setIngredientes(obj);
			controller.setIngredienteService(new IngredientesService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro - ingrediente");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
		}
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();

	}	

	private void searchClientes() {
		FilteredList<Ingredientes> filteredData = new FilteredList<>(obsList, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(Ingredientes -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name field in your object with filter.
				String lowerCaseFilter = newValue.toLowerCase();

				if (String.valueOf(Ingredientes.getNomeIngrediente()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
					// Filter matches first name.
				} 

				
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Ingredientes> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(tableViewIngredientes.comparatorProperty());
		// 5. Add sorted (and filtered) data to the table.
		tableViewIngredientes.setItems(sortedData);
		
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Ingredientes, Ingredientes>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Ingredientes obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CadastroIngredientesView.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Ingredientes, Ingredientes>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Ingredientes obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Ingredientes obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Você tem certeza que deseja excluir?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service está nulo!");
			}
			try {
			service.remove(obj);
			updateTableView();
		} catch (DbIntegrityException e) {
			Alerts.showAlert("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
		}
		}
	}
	
	
}
