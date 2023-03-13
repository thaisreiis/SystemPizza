package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Ingredientes;
import model.exceptions.ValidationException;
import service.IngredientesService;

public class CadastroIngredientesController implements Initializable {

	private Ingredientes novoIngrediente;

	private IngredientesService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNomeIngrediente;

	@FXML
	private Label labelErrorNomeIngrediente;

	@FXML
	private Button btCadastrar;

	@FXML
	private Button btLimpar;
	
	
	public void setIngredientes(Ingredientes novoIngrediente) {
		this.novoIngrediente = novoIngrediente;
	}

	public void setIngredienteService(IngredientesService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtCadastrarAction(ActionEvent event) {		
		try {
			novoIngrediente = getFormData();
			service.saveOrUpdate(novoIngrediente);
			Alerts.showAlert("Confirmação", null, "Cadastro concluído com sucesso!", AlertType.CONFIRMATION);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();

		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}
	
	private Ingredientes getFormData() {

		Ingredientes obj = new Ingredientes();

		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));	

		if (txtNomeIngrediente.getText() == null || txtNomeIngrediente.getText().trim().equals("")) {
			exception.addError("nome", "Campos obrigatórios não preenchidos.");
		}
		obj.setNomeIngrediente(txtNomeIngrediente.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	
	@FXML
	public void onBtLimparAction(ActionEvent event) {

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Confirmação");
		alert.setContentText("Deseja realmente limpar os campos?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
			txtNomeIngrediente.setText("");
			labelErrorNomeIngrediente.setText("");
		} else {
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNomeIngrediente, 100);
		//Constraints.restrictLettersOnly(txtNomeIngrediente);
	}
	

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErrorNomeIngrediente.setText(errors.get("nome"));
		}	
	}
	
	public void updateFormData() {
		if (novoIngrediente == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(novoIngrediente.getId()));
		txtNomeIngrediente.setText(novoIngrediente.getNomeIngrediente());
	}
}
