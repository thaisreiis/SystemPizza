package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Clientes;
import service.SaboresService;

public class MenuController implements Initializable {

	@FXML
	private Button btNovoPedido;

	@FXML
	private Button btSabores;

	@FXML
	private Button btIngredientes;
	
	@FXML
	private Button btClientes;

	@FXML
	private Button btAjuda;

	@FXML
	private TextField txPesquisar;

	@FXML
	private TableView<Clientes> tableViewClientes;

	@FXML
	private TableColumn<Clientes, String> tableColumnNome;

	@FXML
	private TableColumn<Clientes, String> tableColumnTelefone;

	@FXML
	private TableColumn<Clientes, String> tableColumnSituacao;

	public void onBtNovoPedido() {

	}

	public void onBtIngredientes() {

	}
	
	public void onBtClientes() {

	}

	public void onBtAjuda() {

	}

	public void onBtSabores(ActionEvent event) {
		loadView("/gui/SaboresView.fxml", (SaboresController controller) -> {
			controller.setSaboresService(new SaboresService());
			controller.updateTableView();
		});
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
