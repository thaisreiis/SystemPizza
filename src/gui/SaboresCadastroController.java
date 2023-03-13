//Codigo certo

package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.CheckComboBox;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.ObservableList;
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
import model.Sabores;
import model.dao.DaoFactory;
import model.dao.SaboresDao;
import model.exceptions.ValidationException;
import service.IngredientesService;
import service.SaboresService;

public class SaboresCadastroController implements Initializable {

	private SaboresService service;

	private Sabores novoSabor;

	private IngredientesService serviceIngredientes = new IngredientesService();

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	public void setSabores(Sabores novoSabor) {
		this.novoSabor = novoSabor;
	}

	public void setSaboresService(SaboresService service) {
		this.service = service;
	}

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorIngredientes;

	@FXML
	private CheckComboBox<String> checkBoxIngredientes;

	@FXML
	private Button btCadastrar;

	@FXML
	private Button btCancelar;

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Confirmação");
		alert.setContentText("Deseja realmente limpar os campos?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
			txtNome.setText("");
			labelErrorNome.setText("");
			labelErrorIngredientes.setText("");
			checkBoxIngredientes.getCheckModel().clearChecks();
		} else {
		}
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		try {
			novoSabor = getFormData();
			service.saveOrUpdate(novoSabor);
			//saborDao.insert(novoSabor);
			Alerts.showAlert("Confirmação", null, "Cadastro concluído com sucesso!", AlertType.CONFIRMATION);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void listarIngredientesCheckBox() {
		Sabores sabores = new Sabores();

		ObservableList<String> list = checkBoxIngredientes.getCheckModel().getCheckedItems();

		sabores.setIdIngrediente1(list.get(0));
		sabores.setIdIngrediente2(list.get(1));
		sabores.setIdIngrediente3(list.get(2));
		sabores.setIdIngrediente4(list.get(3));
		sabores.setIdIngrediente5(list.get(4));
	}

	private Sabores getFormData() {
		// Metodo para armazenar no List todos os ingredientes selecionados no checkBox
		ObservableList<String> list = checkBoxIngredientes.getCheckModel().getCheckedItems();

		Sabores obj = new Sabores();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", " Campo obrigatório não preenchido.");
		}
		obj.setNome(txtNome.getText());

		if (list.size() == 0) {
			exception.addError("ingredientes", " Selecione os ingredientes.");
		}
		
		if (list.size() > 5) {
			exception.addError("ingredientes", " Selecione no máximo 5 ingredientes.");
		}

		if (list.size() == 1) {
			obj.setIdIngrediente1(list.get(0));
			obj.setIdIngrediente2("");
			obj.setIdIngrediente3("");
			obj.setIdIngrediente4("");
			obj.setIdIngrediente5("");
		}

		if (list.size() == 2) {
			obj.setIdIngrediente1(list.get(0));
			obj.setIdIngrediente2(list.get(1));
			obj.setIdIngrediente3("");
			obj.setIdIngrediente4("");
			obj.setIdIngrediente5("");
		}

		if (list.size() == 3) {
			obj.setIdIngrediente1(list.get(0));
			obj.setIdIngrediente2(list.get(1));
			obj.setIdIngrediente3(list.get(2));
			obj.setIdIngrediente4("");
			obj.setIdIngrediente5("");
		}

		if (list.size() == 4) {
			obj.setIdIngrediente1(list.get(0));
			obj.setIdIngrediente2(list.get(1));
			obj.setIdIngrediente3(list.get(2));
			obj.setIdIngrediente4(list.get(3));
			obj.setIdIngrediente5("");
		}

		if (list.size() == 5) {
			obj.setIdIngrediente1(list.get(0));
			obj.setIdIngrediente2(list.get(1));
			obj.setIdIngrediente3(list.get(2));
			obj.setIdIngrediente4(list.get(3));
			obj.setIdIngrediente5(list.get(4));
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle bd) {
		listarIngredientes();
		Constraints.setTextFieldInteger(txtId);
		// Constraints.setTextFieldMaxLength(txtNome, 100);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}

		if (fields.contains("ingredientes")) {
			labelErrorIngredientes.setText(errors.get("ingredientes"));
		}
	}

	private void listarIngredientes() {
		List<Ingredientes> listaIngredientes = serviceIngredientes.findAll();

		for (Ingredientes obj : listaIngredientes) {
			checkBoxIngredientes.getItems().addAll(obj.getNomeIngrediente());
		}
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void updateFormData() {
		if (novoSabor == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(novoSabor.getId()));
		txtNome.setText(novoSabor.getNome());

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}
	

	

}
