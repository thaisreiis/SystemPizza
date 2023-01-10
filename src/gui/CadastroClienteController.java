package gui;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DB.DB;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.TextFieldFormatter;
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
import model.Clientes;
import model.dao.ClientesDao;
import model.dao.DaoFactory;
import model.exceptions.ValidationException;
import service.ClientesService;

public class CadastroClienteController implements Initializable {

	private Clientes novoCliente;

	private ClientesService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtNome;

	@FXML
	private Label labelErrorNome;

	@FXML
	private TextField txtSobrenome;

	@FXML
	private Label labelErrorSobrenome;

	@FXML
	private TextField txtTelefone;

	@FXML
	private Label labelErrorTelefone;

	@FXML
	private Button btCadastrar;

	@FXML
	private Button tbLimpar;
	
	@FXML
	private void tfTelefone() {
		TextFieldFormatter tff = new TextFieldFormatter();
		tff.setMask("(##)#####-####");
		tff.setCaracteresValidos("0123456789");
		tff.setTf(txtTelefone);
		tff.formatter();
	}
	
	public void setClientes(Clientes novoCliente) {
		this.novoCliente = novoCliente;
	}

	public void setClientesService(ClientesService service) {
		this.service = service;
	}
	
	//Metodo para atualizar a lista de clientes quando o formulario for preenchido
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Connection conn = DB.getConnection();
		ClientesDao clientesDao = DaoFactory.createClienteDao();

		try {
			novoCliente = getFormData();
			clientesDao.insert(novoCliente);
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

	private Clientes getFormData() {

		Clientes obj = new Clientes();

		ValidationException exception = new ValidationException("Validation error");

		obj.setNome(txtNome.getText());
		obj.setSobrenome(txtSobrenome.getText());		
	//	obj.setTelefone(Integer.parseInt(txtTelefone.getText()));		
		
		String str = txtTelefone.getText();
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(str);
		StringBuilder numeroTelefone = new StringBuilder();
		while (m.find()) {
			numeroTelefone.append(m.group().trim() + "");
		}

		String x = numeroTelefone.toString();
		obj.setTelefone(x);
 
		obj.getId();

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campos obrigatórios não preenchidos.");
		}
		obj.setNome(txtNome.getText());

		if (txtSobrenome.getText() == null || txtSobrenome.getText().trim().equals("")) {
			exception.addError("sobrenome", "Campos obrigatórios não preenchidos.");
		}
		obj.setSobrenome(txtSobrenome.getText());

		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addError("telefone", "Campos obrigatórios não preenchidos.");
		}
		obj.setTelefone(x);

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
			txtNome.setText("");
			txtSobrenome.setText("");
			txtTelefone.setText("");
		} else {
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtNome, 100);
		Constraints.setTextFieldMaxLength(txtSobrenome, 100);
		Constraints.restrictLettersOnly(txtNome);
		Constraints.restrictLettersOnly(txtSobrenome);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}

		if (fields.contains("sobrenome")) {
			labelErrorSobrenome.setText(errors.get("sobrenome"));
		}

		if (fields.contains("telefone")) {
			labelErrorTelefone.setText(errors.get("telefone"));
		}		
	}

}
