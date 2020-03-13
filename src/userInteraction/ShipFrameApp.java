package userInteraction;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import fileHelper.FileHelper;
import ship.Ship;
import ship.Ship.ShipException;

/*
 *    Plik: ShipFrameApp.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
public class ShipFrameApp extends JFrame{
	/*
	 * 	Contains ContentPane that contains cards(panels with components inside) of different states in application. Demanding on user action app will swap to destinated card.
	 */
	private static final long serialVersionUID = 1L;

	private final Dimension size = new Dimension(600,300);
	private final Dimension shipTablePaneSize = new Dimension(550,150);
	
	
	private ArrayList<Ship> shipList = new ArrayList<Ship>();

	private PopupDialog popupDialog = new PopupDialog();
	private FileHelper fileHelper = new FileHelper();
	
	public ShipFrameApp() {
		populateShipList();
		add(new ContentPane());
		setFrame();
	}
	
	private void populateShipList() {
		try {
			shipList.addAll(fileHelper.readShips());
		}catch(NullPointerException e) {
			popupDialog.popMessage("brak danych statków do zaimportowania z folderu " + fileHelper.SHIPS_DIRECTORY);
		}
	}
	
	private void setFrame() {
		setTitle("...:::SHIP CONSOLE APP:::...");
		setSize(size);
		setPreferredSize(size);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setFocusable(true);
		setVisible(true);
	}
	
	
	private class ContentPane extends JPanel{
		private static final long serialVersionUID = 1L;
		/* 
		 * 		is a JPanel with all components inside, including its layout data and action
		 */
		private ArrayList <JComponent> componentList = new ArrayList<>();
		
		private LayoutHelper layoutHelper = new LayoutHelper();
		//all JComponents are stored in shipList (ArrayList), and are being indexed as follows:
		private final int NEWSHIP_BUT = 0, READSHIP_BUT = 1, SHIPLIST_BUT = 2, 												   				//menu panel
				
		NAME_TEXTFIELD = 3, TYPE_TEXTFIELD = 4, YEAR_TEXTFIELD = 5, CREW_TEXTFIELD = 6 , BEDS_TEXTFIELD = 7, NAME_LABEL = 8,  			    //new ship panel
		TYPE_LABEL = 9, YEAR_LABEL = 10, CREW_LABEL = 11, BEDS_LABEL = 12, CLEARSHIP_BUT = 13, SAVECURRSHIP_BUT = 14, BACKTOMENU_BUT1 = 15, //new ship panel
		
								DELETESHIP_BUT = 16, EDITSHIP_BUT = 17, BACKTOMENU_BUT2 = 18, OPENFOLDER_BUT = 19, SHIPTABLE_PANE = 20,		//shipListPanel	
								
		READNAME_TEXTFIELD = 21, READTYPE_TEXTFIELD = 22, READYEAR_TEXTFIELD = 23,															//readShipDialog
		READNAME_LABEL = 24, READTYPE_LABEL = 25, READYEAR_LABEL = 26, BACKTOMENU_BUT3 = 27, OK_BUT = 28, CLEARSHIP_BUT2 = 29;				//readShipDialog
		
	//CardLayout:
		private CardLayout cards = new CardLayout();
		private final String MENU_PANEL = "menu panel";
		private final String NEWSHIP_PANEL= "new ship";
		private final String SHIPLIST_PANEL = "ship list";
		private final String READSHIP_PANEL = "read ship";
	//panels containing components: (ready to add boxes)
		private JPanel menuPanel = new JPanel();
		private JPanel newShipPanel = new JPanel();
		private JPanel shipListPanel = new JPanel();
		private JPanel readShipPanel = new JPanel();
		
	//listeners:
		ButtonListener buttonListener = new ButtonListener();
		
		
//components:	
	//menuPanel:
		JButton newShipBut = new JButton("new ship");//swap to new ship panel
		JButton readShipBut = new JButton("read ship from file");
		JButton shipListBut = new JButton("show ship list");//click on ship to show message window with ship data
		
	//newShipPanel:
		JTextField nameTextField = new JTextField(30);
		JTextField typeTextField = new JTextField(30);
		JTextField yearTextField = new JTextField(30);
		JTextField crewTextField = new JTextField(30);
		JTextField bedsTextField = new JTextField(30);
		
		JLabel nameLabel = new JLabel("name");
		JLabel typeLabel = new JLabel("ship type: [--,passenger,tanker,fishing,racing]");
		JLabel yearLabel = new JLabel("construction year (1800-2019)");
		JLabel crewLabel = new JLabel("crew number(0-2000)");
		JLabel bedsLabel = new JLabel("beds number(0 - 5000)");

		JButton clearShipBut = new JButton("clear fields");//clear all fields
		JButton saveCurrentShipBut = new JButton("save ship to file");//
		JButton backToMenuBut1 = new JButton("back to menu");

	//shipListPanel
		JButton deleteShipBut = new JButton("delete selected");//delete current selection
		JButton editShipBut = new JButton("edit selected");// create popup frame with ship data with possibility to edit its input
		JButton backToMenuBut2 = new JButton("back to menu");
		JButton openFolderBut = new JButton("open folder containing files");
		
		JTable shipTable;
		ShipTableModel shipTableModel;
		JScrollPane shipTablePane;
		
	//readShipDialog:
		JButton clearShipBut2 = new JButton("clear fields");
		
		JTextField readNameTextField = new JTextField(30);
		JTextField readTypeTextField = new JTextField(30);
		JTextField readYearTextField = new JTextField(30);
		JLabel readNameLabel = new JLabel("name: ");
		JLabel readTypeLabel = new JLabel("ship type: [--,passenger,tanker,fishing,racing]");
		JLabel readYearLabel = new JLabel("construction year (1800-2019)");
		
		JButton backToMenuBut3 = new JButton("back to menu");
		JButton okBut = new JButton("read now");
		
		ContentPane(){
			setComponents();
			setLayout();
			addListeners();
			
			setPanelColor();
		}
		
		private void setPanelColor() {
			setBackground(new Color(255,255,255));
		}
		
		private void setComponents() {
			setShipTablePane();
			
			populateComponentList(
					newShipBut,readShipBut,shipListBut,																																			 //menuPanel
					nameTextField,typeTextField,yearTextField,crewTextField,bedsTextField,	nameLabel,typeLabel,yearLabel,crewLabel,bedsLabel,	clearShipBut, saveCurrentShipBut, backToMenuBut1,//newShipPanel
					deleteShipBut,editShipBut,backToMenuBut2,openFolderBut,shipTablePane,																								         //shipListPanel
						readNameTextField, readTypeTextField, readYearTextField, readNameLabel, readTypeLabel, readYearLabel, backToMenuBut3, okBut, clearShipBut2								 //readShipPanel
								);	
		}
		
		private void setShipTablePane() {
			shipTableModel = new ShipTableModel();
			shipTable = new JTable(shipTableModel);
			shipTablePane = new JScrollPane(shipTable,		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			shipTablePane.setPreferredSize(shipTablePaneSize);
		}
		
		private void populateComponentList(JComponent ... components) {
			for(JComponent c : components)
				componentList.add(c);
		}
		
		private void setLayout() {
			//cards (CardLayout)
			setLayout(cards);
			//add panels as cards to contentPane
			add(menuPanel, MENU_PANEL);
			add(newShipPanel, NEWSHIP_PANEL);
			add(shipListPanel, SHIPLIST_PANEL);
			add(readShipPanel,READSHIP_PANEL);

			addComponentsToPanels();

			//starting card:
			cards.show(this, MENU_PANEL);
		}
		
		private void addComponentsToPanels() {			
			layoutHelper.addComponents(MENU_PANEL, menuPanel , componentList);	
			layoutHelper.addComponents(NEWSHIP_PANEL, newShipPanel , componentList);	
			layoutHelper.addComponents(SHIPLIST_PANEL, shipListPanel , componentList);
			layoutHelper.addComponents(READSHIP_PANEL, readShipPanel, componentList);
		}
		
		private void swapCard(String MODE) {
			if(MODE.equals(MENU_PANEL))
				cards.show(this, MENU_PANEL);
			else if(MODE.equals(NEWSHIP_PANEL))
				cards.show(this, NEWSHIP_PANEL);
			else if(MODE.equals(SHIPLIST_PANEL))
				cards.show(this, SHIPLIST_PANEL);
			else if(MODE.equals(READSHIP_PANEL)) {
				cards.show(this, READSHIP_PANEL);
			}
		}
		
		private void addListeners() {
			for(JComponent comp : componentList) {
				if(comp instanceof JButton) {
					((JButton) comp).addActionListener(buttonListener);
				}
			}
		}
		
	//buttons action methods:
		private void clearTextFields() {
			for(JComponent jc : componentList) {
				if(jc instanceof JTextField) {
					((JTextField) jc).setText("");
				}
			}
		}
		
		private void saveToFile() {
			Ship ship = getCurrentShip();
			
			if(isShipRepetition(ship)) {
				popupDialog.popMessage("Statek o tych danych jest ju¿ zapisany!");
				return;
			}
			
			if(ship != null) {
				fileHelper.checkFoldersExists();
				ship.saveToFile();
				shipList.add(ship);
				addTableRow(ship);
				popupDialog.popMessage("ZAPISANO STATEK DO PLIKU: \n" + ship.getFile());
			}
		}
		
		private void readShipFromFile(){
			if(isAnyTextFieldEmpty(readNameTextField,readTypeTextField,readYearTextField)) {
				popupDialog.popMessage("Wype³nij wszystkie pola.");
				return;
			}
			
			String name = readNameTextField.getText();
			String type = readTypeTextField.getText();
			String year = readYearTextField.getText();
			
			
			String fileName = name + " " + type + " " + year;
			Ship ship = null;
			try {
				ship = fileHelper.readShipFromFile(fileName);
			} catch (IOException e) {
				popupDialog.popMessage("File not found: (filename): " + fileName);
			} catch (ShipException e) {
				popupDialog.popMessage("Invalid data inside file: " + fileName);
			}
			if(isShipRepetition(ship)) {
				popupDialog.popMessage("Statek o tych danych jest ju¿ zapisany!");
				return;
			}
			
			if(ship != null) {
				shipList.add(ship);
				addTableRow(ship);
				popupDialog.popMessage("DODANO STATEK DO LISTY STATKÓW\n" + ship.getFile());
				swapCard(SHIPLIST_PANEL);
			}
		}
		
		private void addTableRow(Ship ship) {
			shipTableModel.addRow(ship.getData());
			shipTable.setModel(shipTableModel);
		}
		
		private void deleteTableRow(int rowToDel) {
			if(!shipList.isEmpty() && rowToDel != -1) {
				shipTableModel.deleteSelectedRow(rowToDel);
				shipList.remove(rowToDel);
				shipTable.setModel(shipTableModel);
			}else
				popupDialog.popMessage("Zaznacz wiersz do usuniêcia");
		}

		private void editSelectedShip() {
			int row = shipTable.getSelectedRow();
			if(row==-1 || shipList.isEmpty()) {
				popupDialog.popMessage("Zaznacz wiersz do edycji");
				return;
			}
			
			Ship shipToEdit = shipList.get(row);
			//set new ship
			EditShipWindow editShipWindow = new EditShipWindow(shipToEdit, row);
		}
		
		
		private void openShipFolder() {
			fileHelper.openShipFolder();
		}
		
		private Ship getCurrentShip() {
			if(isAnyTextFieldEmpty(nameTextField,typeTextField,yearTextField,crewTextField,bedsTextField)) {
				popupDialog.popMessage("Uzupe³nij wszystkie pola.");
				return null;
			}
			Ship ship = null;
			String name = nameTextField.getText();
			String shipType = typeTextField.getText();
			String constructionYear = yearTextField.getText();
			String crew = crewTextField.getText();
			String beds = bedsTextField.getText();
			try {
				ship = new Ship(name, shipType, constructionYear, crew, beds, fileHelper);
			} catch (ShipException e) {
				popupDialog.popMessage("Wprowadzono niepoprawne dane.");
			}
			
			return ship;
		}
		
		private boolean isAnyTextFieldEmpty(JTextField ... textFields ) {
			for(JTextField tf : textFields)
				if(tf.getText().isEmpty())
					return true;
			
			return false;
		}
		
		private boolean isShipRepetition(Ship shipToCheck) {
			if(shipToCheck != null)
				for(Ship ship : shipList) {
					if(ship.equals(shipToCheck)) {
						return true;
					}
				}
			return false;
		}
		
	//class ShipTableModel
		private class ShipTableModel extends DefaultTableModel{
			private static final long serialVersionUID = 1L;
			/*
			 * 		defines DefaultTableModel of ShipTable (JTable) as needed
			 */
			
			private final String[] colNames = {"Name","Ship Type", "Construction Year", "Crew", "Beds"};
			
			private ShipTableModel() {
				setDataVector(getTableData(), colNames);
				
			}
			
			private String[][] getTableData(){
				String[][] data = new String[shipList.size()][Ship.DATA_AMOUNT];
				
				for(int i =0; i<shipList.size();i++) {
					Ship ship = shipList.get(i);
					data[i] = ship.getData();
				}
				return data;
			}
			
			private void deleteSelectedRow(int rowToDelete) {
				Ship shipToDelete = shipList.get(rowToDelete);
					
				removeRow(rowToDelete);
					
				//delete file
				fileHelper.deleteFile(shipToDelete.getFile());
			}
			
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
			
		}//class ShipTableModel
		
	//class ButtonListener
		private class ButtonListener implements ActionListener{
			/*
			 * 		reads buttons action
			 */

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton)e.getSource();
				
				//menuPanel:
				if(button == componentList.get(NEWSHIP_BUT)) {
					swapCard(NEWSHIP_PANEL);
				}else if(button == componentList.get(SHIPLIST_BUT)) {
					swapCard(SHIPLIST_PANEL);
				}else if(button == componentList.get(READSHIP_BUT)) {
					swapCard(READSHIP_PANEL);
				}
				
				//newShipPanel:
				else if(button == componentList.get(SAVECURRSHIP_BUT)) {
					saveToFile();
				}
				else if(button == componentList.get(CLEARSHIP_BUT) || button == componentList.get(CLEARSHIP_BUT2)) {
					clearTextFields();
				}
				
				//shipListPanel:
				else if(button == componentList.get(BACKTOMENU_BUT1) || button == componentList.get(BACKTOMENU_BUT2) || button == componentList.get(BACKTOMENU_BUT3)) {//back to menu
					swapCard(MENU_PANEL);
				}else if(button == componentList.get(DELETESHIP_BUT)) {//del
					deleteTableRow(shipTable.getSelectedRow());
				}else if(button == componentList.get(EDITSHIP_BUT)) {//edit
					editSelectedShip();
				}else if(button == componentList.get(OPENFOLDER_BUT)) {//open folder
					openShipFolder();
				}
				
				//readShipPanel:
				else if(button == componentList.get(OK_BUT)) {
					readShipFromFile();
				}
			}
		}//class ButtonListener
		
	//class LayoutHelper
		private class LayoutHelper {
			/*
			 * 		LayoutHelper helps to deal with layout code of components
			 * 				- grants programmer to set layout of any JComponent in easy way
			 */
			public void addComponents(String MODE, JPanel panel, ArrayList<JComponent> components) {
		//I
				 if(MODE.equals(MENU_PANEL)) 
					addMenuPanelComponents(panel, components);
		//II
			else if(MODE.equals(NEWSHIP_PANEL))
					addNewShipPanelComponents(panel, components);
		//III
			else if(MODE.equals(SHIPLIST_PANEL))
					addShipListPanelComponents(panel, components);
		//IV		 
			else if(MODE.equals(READSHIP_PANEL))
					addReadShipPanelComponents(panel, components);
			}
			
		//I:
			private void addMenuPanelComponents(JPanel panel, ArrayList<JComponent> components) {
				JPanel componentPanel = new JPanel();
				componentPanel.setLayout(new GridBagLayout());
				//buttons:
				addComponent(componentPanel, components.get(NEWSHIP_BUT),  0, 0, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(READSHIP_BUT), 0, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(SHIPLIST_BUT), 0, 2, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
				
				componentPanel.setBackground(new Color(200,100,10));
		        panel.add(componentPanel);
			}
		//II:
			private void addNewShipPanelComponents(JPanel panel, ArrayList<JComponent> components) {
				JPanel componentPanel = new JPanel();
				componentPanel.setLayout(new GridBagLayout());
				//labels and textfields
		        addComponent(componentPanel,components.get(NAME_LABEL),     0,0,1,1,0,0,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(NAME_TEXTFIELD), 1,0,1,1,0,0,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(TYPE_LABEL),     0,1,1,1,0,0,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(TYPE_TEXTFIELD), 1,1,1,1,0,0,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(YEAR_LABEL),     0,2,1,1,0,0,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(YEAR_TEXTFIELD), 1,2,1,1,0,0,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(CREW_LABEL),     0,3,1,1,0,0,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(CREW_TEXTFIELD), 1,3,1,1,0,0,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(BEDS_LABEL),     0,4,1,1,0,0,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE);
		        addComponent(componentPanel,components.get(BEDS_TEXTFIELD), 1,4,1,1,0,0,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE);
		        //buttons:
		        addComponent(componentPanel, components.get(SAVECURRSHIP_BUT), 0, 5, 2, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		        addComponent(componentPanel, components.get(CLEARSHIP_BUT),    0, 6, 2, 1, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
		        addComponent(componentPanel, components.get(BACKTOMENU_BUT1),  0, 7, 2, 1, 0, 0, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL);

				componentPanel.setBackground(new Color(200,100,10));
				panel.add(componentPanel);
			}
		//III:
			private void addShipListPanelComponents(JPanel panel, ArrayList<JComponent> components) {
				JPanel componentPanel = new JPanel();
				componentPanel.setLayout(new GridBagLayout());
				//table:
		        addComponent(componentPanel, components.get(SHIPTABLE_PANE),    0, 0, 2, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		        //buttons:
		        addComponent(componentPanel, components.get(DELETESHIP_BUT),    0, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		        addComponent(componentPanel, components.get(EDITSHIP_BUT),      1, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		        addComponent(componentPanel, components.get(BACKTOMENU_BUT2),   0, 2, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		        addComponent(componentPanel, components.get(OPENFOLDER_BUT),    1, 2, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

				componentPanel.setBackground(new Color(200,100,10));
		        panel.add(componentPanel);
			}
		//IV:
			private void addReadShipPanelComponents(JPanel panel, ArrayList<JComponent> components) {
				JPanel componentPanel = new JPanel();
				componentPanel.setLayout(new GridBagLayout());
				//textfield and label:
				addComponent(componentPanel, components.get(READNAME_LABEL),     0, 0, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(READNAME_TEXTFIELD), 1, 0, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(READTYPE_LABEL),     0, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(READTYPE_TEXTFIELD), 1, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(READYEAR_LABEL),     0, 2, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(READYEAR_TEXTFIELD), 1, 2, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);
				//buttons:
				addComponent(componentPanel, components.get(CLEARSHIP_BUT2),     0, 3, 2, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(BACKTOMENU_BUT3),    0, 4, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(OK_BUT),		     1, 4, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

				panel.add(componentPanel);
			}
			
			private void addEditShipWindowComponents(JPanel panel, ArrayList<JComponent> components) {
				JPanel componentPanel = new JPanel();
				componentPanel.setLayout(new GridBagLayout());
				
				//labels and textfields
				addComponent(componentPanel, components.get(EditShipWindow.NAME_LABEL), 		0, 0, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.NAME_TEXTFIELD), 	1, 0, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.TYPE_LABEL), 		0, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.TYPE_TEXTFIELD), 	1, 1, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.YEAR_LABEL), 	 	0, 2, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.YEAR_TEXTFIELD) , 	1, 2, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.CREW_LABEL),	    	0, 3, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.CREW_TEXTFIELD),  	1, 3, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.BEDS_LABEL), 	    0, 4, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);
				addComponent(componentPanel, components.get(EditShipWindow.BEDS_TEXTFIELD),  	1, 4, 1, 1, 0, 0, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);

				addComponent(componentPanel, components.get(EditShipWindow.BACK_BUTTON), 		0, 5, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
				addComponent(componentPanel, components.get(EditShipWindow.EDIT_BUTTON), 		1, 5, 1, 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
				
				panel.add(componentPanel);
			}
			
			//adds component to panel with specified position (GridBagLayout parameters)
		    private void addComponent(JComponent panel, JComponent component, int x, int y, int w, int h, double wx, double wy, int padx, int pady, int place, int stretch){
		        GridBagConstraints gc = new GridBagConstraints();
		        gc.gridx = x;
		        gc.gridy = y;
		        gc.gridwidth = w;
		        gc.gridheight = h;
		        gc.weightx = wx;
		        gc.weighty = wy;
		        gc.ipadx = padx;
		        gc.ipady = pady;
		        gc.insets = new Insets(3,3,3,3);
		        gc.anchor = place;
		        gc.fill = stretch;
		        panel.add(component,gc);
		    }
		    
			}//class layoutHelper
		
		//class EditShipWindow	
				private class EditShipWindow extends JDialog {
					private static final long serialVersionUID = 1L;
					/*
					 * 		EditShip Window is allowed edit ship data
					 */
					private final Dimension size = new Dimension(600,300);
					
					public static final int NAME_LABEL = 0, TYPE_LABEL = 1, YEAR_LABEL = 2, CREW_LABEL = 3, BEDS_LABEL = 4, 
									  NAME_TEXTFIELD = 5, TYPE_TEXTFIELD = 6, YEAR_TEXTFIELD = 7, CREW_TEXTFIELD = 8, BEDS_TEXTFIELD = 9, 
									  EDIT_BUTTON = 10, BACK_BUTTON = 11;
					
					private JLabel nameLabel = new JLabel("name: ");
					private JLabel typeLabel = new JLabel("ship type: [--,passenger,tanker,fishing,racing]");
					private JLabel yearLabel = new JLabel("year: ");
					private JLabel crewLabel = new JLabel("crew: ");
					private JLabel bedsLabel = new JLabel("beds: ");
					
					private JTextField nameTextField = new JTextField(18);
					private JTextField typeTextField = new JTextField(18);
					private JTextField yearTextField = new JTextField(18);
					private JTextField crewTextField = new JTextField(18);
					private JTextField bedsTextField = new JTextField(18);
					
					private JButton editButton = new JButton("edit now");
					private JButton backButton = new JButton("back to menu");
					
					private JPanel panel = new JPanel();

					private Ship shipToEdit = null;
					private Ship newShip = null;
					private int rowToEdit = -1;

					private ArrayList<JComponent> editShipComponents = new ArrayList<JComponent>();
					
					
					private EditShipWindow(Ship shipToEdit, int rowToEdit) {
						super((JFrame)null, true);
						addButtonListener();
						this.rowToEdit = rowToEdit;
						this.shipToEdit = shipToEdit;
						initComponents(shipToEdit);
						initDialog();
					}
					
					private void editShip(int rowToEdit) {
						newShip = getShip();

						deleteTableRow(rowToEdit);
						addTableRow(newShip);
						
						shipList.add(newShip);
						
						//delete old file and create new
						fileHelper.deleteFile(shipToEdit.getFile());
						newShip.saveToFile();
					}
					
					private void initDialog() {
						setTitle("Edit ship window");
						setSize(size);
						setPreferredSize(size);
						setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						setLocationRelativeTo(null);
						setFocusable(true);
						setVisible(true);
					}
					
					private void initComponents(Ship ship) {
						setLayout(new BorderLayout());

						
						populateComponentList( 
								nameLabel,typeLabel,yearLabel,crewLabel,bedsLabel,
								nameTextField,typeTextField,yearTextField,crewTextField,bedsTextField,
								editButton,backButton
											);

						layoutHelper.addEditShipWindowComponents(panel, editShipComponents);
						
						nameTextField.setText(ship.getName());
						typeTextField.setText(ship.getShipType().toString());
						yearTextField.setText(ship.getConstructionYear());
						crewTextField.setText(Integer.toString(ship.getCrew()));
						bedsTextField.setText(Integer.toString(ship.getBeds()));

						add(panel);
					}
					
					private void addButtonListener() {
						EditShipButtonListener editShipButtonListener = new EditShipButtonListener(this);
						editButton.addActionListener(editShipButtonListener);
						backButton.addActionListener(editShipButtonListener);
					}
					
					private void setShip() {
						if(isAnyTextFieldEmpty(nameTextField,typeTextField,yearTextField,crewTextField,bedsTextField)) {
							popupDialog.popMessage("Wype³nij wszystkie pola");
							return;
						}
						String name = nameTextField.getText();
						String type = typeTextField.getText();
						String year = yearTextField.getText();
						String crew = crewTextField.getText();
						String beds = bedsTextField.getText();
						
						try {
							newShip = new Ship(name,type,year,crew,beds, fileHelper);
						} catch (ShipException e) {
							popupDialog.popMessage("Podano nieprawid³owe dane.");
						}
					}
					
					public Ship getShip() {
						setShip();
						return newShip;
					}
					
					private void populateComponentList(JComponent ... comps) {
						for(JComponent comp : comps)
							editShipComponents.add(comp);
					}
					
					@Override
					public void dispose() {
						super.dispose();
					}
					
				//class EditShipButtonListener	
					private class EditShipButtonListener implements ActionListener{
						
						EditShipWindow parentDialog;
						EditShipButtonListener(EditShipWindow parentDialog){
							this.parentDialog = parentDialog;
						}
						
						@Override
						public void actionPerformed(ActionEvent e) {
							JButton button = (JButton) e.getSource();

							if(button == parentDialog.editShipComponents.get(EditShipWindow.EDIT_BUTTON)) {
								editShip(rowToEdit);
								parentDialog.dispose();
								parentDialog = null;
							}else if(button == parentDialog.editShipComponents.get(EditShipWindow.BACK_BUTTON)) {
								parentDialog.dispose();
								parentDialog = null;
							}
						}
					}//class EditShipButtonListener
				}//class EditShipWindow
		}//class ContentPane
	}//class ShipFrameApp
