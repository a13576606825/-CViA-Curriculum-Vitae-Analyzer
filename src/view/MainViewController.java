package view;

import java.io.File;
import java.util.ArrayList;

import com.sun.prism.impl.Disposer.Record;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import logic.Interpreter;
import parser.CVReader;

@SuppressWarnings("restriction")
public class MainViewController extends VBox {
	
	private static final String DOC = "doc";
	private static final String DOCX = "docx";
	private static final String HTML = "html";
	private static final String PDF = "pdf";
	private static final String TXT = "txt";
	private static final String XLS = "xls";
	private static final String XML = "xml";
	
	private static final String IMPORT_SINGLE_BUTTON_TEXT = "Add a CV";
	private static final String IMPORT_MULTIPLE_BUTTON_TEXT = "Add CVs";
	private static final String SETTING_BUTTON_TEXT = "Add Filter";
	private static final String PROCESS_BUTTON_TEXT = "Analyse";
	private static final String EXPORT_BUTTON_TEXT = "Export";
	
	private static final double TABLE_WIDTH = 280;
	private static final double TABLE_HEIGHT = 300;
	private static final double BUTTON_WIDTH = 130;
	private static final double BUTTON_HEIGHT = 30;
	
	private static final double RESULT_TABLE_WIDTH = 700;
	private static final double RESULT_TABLE_HEIGHT = 330;
	
	private static final double PADDING_SIZE = 20;
	
	private static final String[] FILE_TABLE_COLUMN = {"Type", "Filename"};
	private static final String[] FILE_TABLE_COLUMN_PROPERTY = {"type", "filename"};
	
	private static final String[] SETTING_TABLE_COLUMN = {"Attribute", "Constraint"};
	private static final String[] SETTING_TABLE_COLUMN_PROPERTY = {"attribute", "constraint"};
	
	private ObservableList<FileObject> fileData = FXCollections.observableArrayList(
//				new FileObject("pdf", "1.pdf"),
//				new FileObject("txt", "2.txt"),
//				new FileObject("xls", "3.xls"),
//				new FileObject("doc", "4.doc")
			);
	private ObservableList<Filter> filterData = FXCollections.observableArrayList(
				new Filter("Skill", "Java"),
				new Filter("Major", "Computer Science"),
				new Filter("Skill", "Software Engineer"),
				new Filter("Skill", "Android")
			);
	
	private double stageWidth;
	@SuppressWarnings("unused")
	private double stageHeight;
	
	private double widthBetweenTable;
	private double widthBetweenButton;
	
	private Button importSingleButton;
	private Button importMultipleButton;
	private Button settingButton;
	private Button processButton;
	private Button exportButton;
	
	private HBox buttonBox;
	private HBox preprocessBox;
	
	private VBox resultBox;
	
	private TableView<FileObject> fileTable;
	private TableView<Filter> settingTable;
	@SuppressWarnings("rawtypes")
	private TableView resultTable;
	
	private ArrayList<File> originalFileList;
	private ArrayList<File> textFileList;
	private ArrayList<String> textFilePathList;
	
	private ArrayList<String> filterList;
	
	private ArrayList<Integer> scoreList;
	
	public MainViewController(double stageWidth, double stageHeight) {
		initializeList();
		initializeStageSize(stageWidth, stageHeight);
		initializeMainView();
		initializePreprocessBox();
		initializeButtonBox();
		initializeResultPane();
		initializeStyleClass();
		initializeButtonEventHandler();
	}
	
	private void initializeList() {
		originalFileList = new ArrayList<File>();
		textFileList = new ArrayList<File>();
		textFilePathList = new ArrayList<String>();
		
		filterList = new ArrayList<String>();
		
		scoreList = new ArrayList<Integer>();
	}
	
	private void initializeStageSize(double stageWidth, double stageHeight) {
		this.stageWidth = stageWidth;
		this.stageHeight = stageHeight;
		
		this.widthBetweenButton = (this.stageWidth - MainViewController.BUTTON_WIDTH*5)/6;
		this.widthBetweenTable = (this.stageWidth - MainViewController.TABLE_WIDTH*2)/3;
	}
	
	@SuppressWarnings("rawtypes")
	private void initializeMainView() {
		importSingleButton = new Button(IMPORT_SINGLE_BUTTON_TEXT);
		importMultipleButton = new Button(IMPORT_MULTIPLE_BUTTON_TEXT);
		settingButton = new Button(SETTING_BUTTON_TEXT);
		processButton = new Button(PROCESS_BUTTON_TEXT);
		exportButton = new Button(EXPORT_BUTTON_TEXT);
		
		buttonBox = new HBox();
		buttonBox.getChildren().addAll(importSingleButton, importMultipleButton, settingButton, processButton, exportButton);
		
		fileTable = new TableView<FileObject>();
		initializeFileTableView(fileTable, FILE_TABLE_COLUMN, FILE_TABLE_COLUMN_PROPERTY);
		fileTable.setItems(fileData);
		
		settingTable = new TableView<Filter>();
		initializeSettingTableView(settingTable, SETTING_TABLE_COLUMN, SETTING_TABLE_COLUMN_PROPERTY);
		settingTable.setItems(filterData);
		
		preprocessBox = new HBox();
		preprocessBox.getChildren().addAll(fileTable, settingTable);
		
		resultTable = new TableView();
		
		resultBox = new VBox();
		resultBox.getChildren().addAll(resultTable);
		
		
		//this.getChildren().addAll(buttonBox, preprocessBox);
		Pane blankPaneOne = new Pane();
		blankPaneOne.setMaxSize(Double.MAX_VALUE, 10);
		blankPaneOne.setMinSize(Double.MAX_VALUE, 10);
		
		Pane blankPaneTwo = new Pane();
		blankPaneTwo.setMaxSize(Double.MAX_VALUE, 10);
		blankPaneTwo.setMinSize(Double.MAX_VALUE, 10);
		
		this.getChildren().addAll(buttonBox, blankPaneOne, preprocessBox, blankPaneTwo, resultBox);
	}
	
	private void initializeFileTableView(TableView<FileObject> table, String[] columns, String[] columnProperties) {
		for (int i=0; i<columns.length; i++) {
			int currentIndex = i;
			TableColumn<FileObject, String> tableColumn = new TableColumn<FileObject, String>(columns[i]);
			tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			tableColumn.setOnEditCommit(new EventHandler<CellEditEvent<FileObject, String>>() {
				@Override
				public void handle(CellEditEvent<FileObject, String> event) {
					FileObject file = (FileObject) event.getTableView().getItems().get(event.getTablePosition().getRow());
					switch (currentIndex) {
						case 0:
							file.setType(event.getNewValue());
							break;
						case 1:
							file.setFilename(event.getNewValue());
							break;
						default:
							break;
					}
				}	
			});
			tableColumn.setCellValueFactory(new PropertyValueFactory<FileObject, String>(columnProperties[i]));
			table.getColumns().add(tableColumn);
		}
		addDeleteButtonToFileTable();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		// User should not modify the file attributes except deletion
		//table.setEditable(true);
	}
	
	private void initializeSettingTableView(TableView<Filter> table, String[] columns, String[] columnProperties) {
		for (int i=0; i<columns.length; i++) {
			int currentIndex = i;
			TableColumn<Filter, String> tableColumn = new TableColumn<Filter, String>(columns[i]);
			tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			tableColumn.setOnEditCommit(new EventHandler<CellEditEvent<Filter, String>>() {
				@Override
				public void handle(CellEditEvent<Filter, String> event) {
					Filter file = (Filter) event.getTableView().getItems().get(event.getTablePosition().getRow());
					switch (currentIndex) {
						case 0:
							file.setAttribute(event.getNewValue());
							break;
						case 1:
							file.setConstraint(event.getNewValue());
							break;
						default:
							break;
					}
				}	
			});
			tableColumn.setCellValueFactory(new PropertyValueFactory<Filter, String>(columnProperties[i]));
			table.getColumns().add(tableColumn);
		}
		addDeleteButtonToSettingTable();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setEditable(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDeleteButtonToFileTable() {
		TableColumn deleteColumn = new TableColumn<>("Action");
		fileTable.getColumns().add(deleteColumn);
		
		deleteColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Record, Boolean>, 
                ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
		
		deleteColumn.setCellFactory(
                new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

            @Override
            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
                return new FileTableButtonCell();
            }
        
        });
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDeleteButtonToSettingTable() {
		TableColumn deleteColumn = new TableColumn<>("Action");
		settingTable.getColumns().add(deleteColumn);
		
		deleteColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Record, Boolean>, 
                ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
		
		deleteColumn.setCellFactory(
                new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

            @Override
            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
                return new SettingTableButtonCell();
            }
        
        });
	}
	
	private void initializePreprocessBox() {
		HBox.setMargin(fileTable, new Insets(PADDING_SIZE, widthBetweenTable/2, PADDING_SIZE, widthBetweenTable));
		HBox.setMargin(settingTable, new Insets(PADDING_SIZE, widthBetweenTable, PADDING_SIZE, widthBetweenTable/2));
		fileTable.setMaxSize(TABLE_WIDTH, TABLE_HEIGHT);
		fileTable.setMinSize(TABLE_WIDTH, TABLE_HEIGHT);
		settingTable.setMaxSize(TABLE_WIDTH, TABLE_HEIGHT);
		settingTable.setMinSize(TABLE_WIDTH, TABLE_HEIGHT);
	}
	
	private void initializeButtonBox() {
		HBox.setMargin(importSingleButton, new Insets(PADDING_SIZE, widthBetweenButton/2, PADDING_SIZE, widthBetweenButton));
		HBox.setMargin(importMultipleButton, new Insets(PADDING_SIZE, widthBetweenButton/2, PADDING_SIZE, widthBetweenButton/2));
		HBox.setMargin(settingButton, new Insets(PADDING_SIZE, widthBetweenButton/2, PADDING_SIZE, widthBetweenButton/2));
		HBox.setMargin(processButton, new Insets(PADDING_SIZE, widthBetweenButton/2, PADDING_SIZE, widthBetweenButton/2));
		HBox.setMargin(exportButton, new Insets(PADDING_SIZE, widthBetweenButton, PADDING_SIZE, widthBetweenButton/2));
		
		importSingleButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		importSingleButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		importMultipleButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		importMultipleButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		settingButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		settingButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		processButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		processButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		exportButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		exportButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	
	private void initializeResultPane() {
		VBox.setMargin(resultTable, new Insets(PADDING_SIZE, PADDING_SIZE, PADDING_SIZE, PADDING_SIZE));
		
		resultBox.setMaxSize(RESULT_TABLE_WIDTH, RESULT_TABLE_HEIGHT);
		resultBox.setMinSize(RESULT_TABLE_WIDTH, RESULT_TABLE_HEIGHT);
	}
	
	private void initializeStyleClass() {
		preprocessBox.getStyleClass().add("preprocess-box");
		buttonBox.getStyleClass().add("button-box");
		resultBox.getStyleClass().add("result-pane");
	}
	
	private void initializeButtonEventHandler() {
		importSingleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose a file...");
				fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
				File file = fileChooser.showOpenDialog(importSingleButton.getScene().getWindow());
                // Process the file
				if (file != null && file.isFile()) {
					System.out.println(file.getName());
					
					addFile(false, file);
				}
			}
		});
		
		importMultipleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser dirChooser = new DirectoryChooser();
				dirChooser.setTitle("Choose a folder...");
				dirChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
				File dir = dirChooser.showDialog(importMultipleButton.getScene().getWindow());
                // Process the file
				if (dir != null && dir.isDirectory()) {
					System.out.println(dir.getName());
					
					File[] files = dir.listFiles();
					for (File file : files) {
						addFile(false, file);
					}
				}
			}
		});
		
		settingButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				filterData.add(new Filter("", ""));
			}
		});
		
		processButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final String dir = System.getProperty("user.dir");
		        System.out.println(dir);
				for (File file : originalFileList) {
					
					addFile(true, CVReader.convertFile(file));
					System.out.println(CVReader.convertFile(file).getAbsolutePath().substring(
							System.getProperty("user.dir").length() + 1));
					textFilePathList.add(CVReader.convertFile(file).getAbsolutePath().substring(
							System.getProperty("user.dir").length() + 1));
				}
				setFilterList();
				scoreList = Interpreter.getQueryScore(textFilePathList, filterList, true);
				
				for (Integer score: scoreList) {
					System.out.println(score);
				}
			}
		});
		
		exportButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// export data
			}
		});
	}
	
	private void setFilterList() {
		for (Filter filter : settingTable.getItems()) {
			System.out.println(filter.getConstraint());
			filterList.add(filter.getConstraint());
		}
	}
	
	private boolean isCVFileType(String type) {
		if (!type.equals(DOC) && !type.equals(DOCX) && !type.equals(HTML) &&
				!type.equals(PDF) && !type.equals(TXT) && !type.equals(XLS) && !type.equals(XML)) {
			return false;
		}
		
		return true;
	}
	
	private void addFile(boolean isConverted, File file) {
		if (isCVFileType(getType(file))) {
			if (!isConverted) {
				originalFileList.add(file);
				fileData.add(new FileObject(getType(file), file.getName()));
			} else {
				textFileList.add(file);
			}
		}
	}
	
	private String getType(File file) {
		if (file.exists() && file.isFile()) {
			String filename = file.getName();
			String[] tokens = filename.split("\\.(?=[^\\.]+$)");
			
			return tokens[1];
		}
		
		return null;
	}
	
	public static class FileObject {
		private final SimpleStringProperty type;
		private final SimpleStringProperty filename;
		
		public FileObject(String type, String filename) {
			this.type = new SimpleStringProperty(type);
			this.filename = new SimpleStringProperty(filename);
		}
		
		public String getType() {
			return type.get();
		}
		
		public void setType(String type) {
			this.type.set(type);
		}
		
		public String getFilename() {
			return filename.get();
		}
		
		public void setFilename(String filename) {
			this.filename.set(filename);
		}
	}
	
	public static class Filter {
		private final SimpleStringProperty attribute;
		private final SimpleStringProperty constraint;
		
		public Filter(String attribute, String constraint) {
			this.attribute = new SimpleStringProperty(attribute);
			this.constraint = new SimpleStringProperty(constraint);
		}
		
		public String getAttribute() {
			return attribute.get();
		}
		
		public void setAttribute(String attribute) {
			this.attribute.set(attribute);
		}
		
		public String getConstraint() {
			return constraint.get();
		}
		
		public void setConstraint(String constraint) {
			this.constraint.set(constraint);
		}
	}
	
	public class FileTableButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("X");
        
        FileTableButtonCell () {
            
        	//Action when the button is pressed
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                	FileObject currentFile = (FileObject) FileTableButtonCell.this.getTableView().getItems().get(FileTableButtonCell.this.getIndex());
                	//remove selected item from the table list
                	fileData.remove(currentFile);
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            } else {
            	setGraphic(null);
            }
        }
    }
	
	public class SettingTableButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("X");
        
        SettingTableButtonCell () {
            
        	//Action when the button is pressed
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                	Filter currentFilter = (Filter) SettingTableButtonCell.this.getTableView().getItems().get(SettingTableButtonCell.this.getIndex());
                	//remove selected item from the table list
                	filterData.remove(currentFilter);
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            } else {
            	setGraphic(null);
            }
        }
    }
	
}
