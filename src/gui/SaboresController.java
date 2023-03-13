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
import model.Sabores;
import service.IngredientesService;
import service.SaboresService;

public class SaboresController implements Initializable, DataChangeListener {
	
	private ObservableList<Sabores> obsList;
	
	private IngredientesService serviceIngredientes = new IngredientesService();
	
	private SaboresService service = new SaboresService();
	
	public void setSaboresService(SaboresService service) {
		this.service = service;
	}
	
	private Sabores novoSabor;
	public void setSabores(Sabores novoSabor) {
		this.novoSabor = novoSabor;
	}

	
	
	@FXML
	private TableView<Sabores> tableViewSabores;
	
	@FXML
	private TableColumn<Sabores, String> tableColumnNome;
	
	@FXML
	private TableColumn<Sabores, String> tableColumnIngrediente1;
	
	@FXML
	private TableColumn<Sabores, String> tableColumnIngrediente2;
	
	@FXML
	private TableColumn<Sabores, String> tableColumnIngrediente3;
	
	@FXML
	private TableColumn<Sabores, String> tableColumnIngrediente4;
	
	@FXML
	private TableColumn<Sabores, String> tableColumnIngrediente5;	
	
	@FXML
	private TableColumn<Sabores, Sabores> tableColumnEDIT;

	@FXML
	private TableColumn<Sabores, Sabores> tableColumnREMOVE;
	
	@FXML
	private TextField txtPesquisar;

	@FXML
	private Button btPesquisar;
	
	@FXML
	private Button btNovoSabor;
	
	@FXML
	public void onBtNovoSaborAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Sabores obj = new Sabores();
		createDialogForm(obj, "/gui/SaboresCadastroView.fxml", parentStage);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnIngrediente1.setCellValueFactory(new PropertyValueFactory<>("idIngrediente1"));
		tableColumnIngrediente2.setCellValueFactory(new PropertyValueFactory<>("idIngrediente2"));
		tableColumnIngrediente3.setCellValueFactory(new PropertyValueFactory<>("idIngrediente3"));
		tableColumnIngrediente4.setCellValueFactory(new PropertyValueFactory<>("idIngrediente4"));
		tableColumnIngrediente5.setCellValueFactory(new PropertyValueFactory<>("idIngrediente5"));		
		updateTableView();
		searchClientes();	
	}

	void updateTableView() {
		Sabores sabores = new Sabores();
		if (service == null) {
			throw new IllegalStateException("Service está nulo. Injetar o service.");
		}
		List<Sabores> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSabores.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		searchClientes();			
		sabores.getNome();
	}
	
	private void createDialogForm(Sabores obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SaboresCadastroController controller = loader.getController();
			controller.setSabores(obj);
			
			controller.setSaboresService(new SaboresService());
			controller.subscribeDataChangeListener(this);	
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro - Sabor");
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
	
	
	
	public void onDataChanged() {
		updateTableView();
	}	
	
	private void searchClientes() {
		List<Sabores> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSabores.setItems(obsList);
		
		FilteredList<Sabores> filteredData = new FilteredList<>(obsList, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(Ingredientes -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name field in your object with filter.
				String lowerCaseFilter = newValue.toLowerCase();

				if (String.valueOf(Ingredientes.getNome()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
					// Filter matches first name.
				} 

				
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Sabores> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(tableViewSabores.comparatorProperty());
		// 5. Add sorted (and filtered) data to the table.
		tableViewSabores.setItems(sortedData);
		
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Sabores, Sabores>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Sabores obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SaboresCadastroView.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Sabores, Sabores>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Sabores obj, boolean empty) {
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

	private void removeEntity(Sabores obj) {
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
