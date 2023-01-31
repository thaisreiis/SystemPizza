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
import model.Clientes;
import service.ClientesService;

public class ClientesController implements Initializable, DataChangeListener {

	//private Clientes clientes;

	private ClientesService service = new ClientesService();

	List<Clientes> list = service.findAll();

	@FXML
	private TableView<Clientes> tableViewClientes;

	@FXML
	private TableColumn<Clientes, String> tableColumnNome;

	@FXML
	private TableColumn<Clientes, String> tableColumnSobrenome;

	@FXML
	private TableColumn<Clientes, String> tableColumnTelefone;

	@FXML
	private TextField txtPesquisar;

	@FXML
	private Button btPesquisar;

	@FXML
	private TableColumn<Clientes, Clientes> tableColumnEDIT;

	@FXML
	private TableColumn<Clientes, Clientes> tableColumnREMOVE;

	@FXML
	private Button btNovoCliente;

	private ObservableList<Clientes> obsList; // nesse obsList que vamos carregar os Clientes

	@FXML
	public void onBtNovoClienteAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Clientes obj = new Clientes();
		createDialogForm(obj, "/gui/CadastroCliente.fxml", parentStage);
	}

	public void setClientesService(ClientesService service) {
		this.service = service;
	}

	//public void setClientes(Clientes clientes) {
	//	this.clientes = clientes;
	//}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnSobrenome.setCellValueFactory(new PropertyValueFactory<>("Sobrenome"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("Telefone"));

		updateTableView();
		searchClientes();
		
	}

	// Metodo responsavel por acessar o serviço, carregar os Clientes e e jogar
	// esses Clientes
	// na minha obsList (ObservableList).Esse obsList vou associar com meu tableView
	// e vai aparecer
	// a lista de clientes lá.
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service está nulo. Injetar o service.");
		}
		List<Clientes> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewClientes.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		searchClientes();
	}

	private void createDialogForm(Clientes obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CadastroClienteController controller = loader.getController();
			controller.setClientes(obj);
			controller.setClientesService(new ClientesService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro - cliente");
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
		FilteredList<Clientes> filteredData = new FilteredList<>(obsList, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(Clientes -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name field in your object with filter.
				String lowerCaseFilter = newValue.toLowerCase();

				if (String.valueOf(Clientes.getNome()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
					// Filter matches first name.

				} else if (String.valueOf(Clientes.getSobrenome()).toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				}

				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Clientes> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(tableViewClientes.comparatorProperty());
		// 5. Add sorted (and filtered) data to the table.
		tableViewClientes.setItems(sortedData);
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Clientes, Clientes>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Clientes obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CadastroCliente.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Clientes, Clientes>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Clientes obj, boolean empty) {
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

	private void removeEntity(Clientes obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Você tem certeza que deseja cancelar?");
		
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
