/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.genes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="cmbGeni"
	private ComboBox<Genes> cmbGeni; // Value injected by FXMLLoader

	@FXML // fx:id="btnGeniAdiacenti"
	private Button btnGeniAdiacenti; // Value injected by FXMLLoader

	@FXML // fx:id="txtIng"
	private TextField txtIng; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML void doCreaGrafo(ActionEvent event)
	{
		//resetto testo
		this.txtResult.clear();
    	this.txtResult.appendText("Crea grafo...\n");

    	//creo grafo
    	this.model.creaGrafo();
    	txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d",
				this.model.getNumVertici(),
				this.model.getNumArchi()));

		//cliccabili
		this.btnGeniAdiacenti.setDisable(false);
		this.btnSimula.setDisable(false);
		this.txtIng.setDisable(false);
		this.cmbGeni.setDisable(false);

		//aggiungo risultati alla combobox 
		this.cmbGeni.getItems().clear();
		this.cmbGeni.getItems().addAll(this.model.getVertici()); 
	}

	@FXML void doGeniAdiacenti(ActionEvent event)
	{
		Genes g = this.cmbGeni.getValue(); 
		if (g == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		} 
		
		this.txtResult.appendText("\n\nGeni adiacenti a " + g + ":\n");
		this.txtResult.appendText(this.model.stampaAdiacenti(g));
	}

	
	
	@FXML void doSimula(ActionEvent event)
	{
		Integer numIng;
		try
		{
			numIng = Integer.parseInt(this.txtIng.getText());
			if(numIng < 0)
				throw new NumberFormatException(); 
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto");
			return;
		} 
		
		Genes g = this.cmbGeni.getValue(); 
		if (g == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		} 
		
		
		model.simula(numIng,g);
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbGeni != null : "fx:id=\"cmbGeni\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnGeniAdiacenti != null
				: "fx:id=\"btnGeniAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtIng != null : "fx:id=\"txtIng\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model)
	{
		this.model = model;
	}

}
