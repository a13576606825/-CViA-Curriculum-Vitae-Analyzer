package view;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.sun.prism.impl.Disposer.Record;

import evaluator.Comparator;
import evaluator.Evaluator;
import evaluator.Priority;
import evaluator.Result;
import interpreter.InterpreterRule;
import interpreter.SmartInterpreter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import parser.CVReader;
import predefinedValues.PredefinedValuesType;

@SuppressWarnings("restriction")
public class MainViewController extends VBox {
	
	private static final String DOC = "doc";
	private static final String DOCX = "docx";
	private static final String HTML = "html";
	private static final String PDF = "pdf";
	private static final String TXT = "txt";
	private static final String XLS = "xls";
	private static final String XML = "xml";
	
	private static final String BASE_PATH = "test/CVs";
	
	private static final String TITLE_TEXT = "CViA";
	private static final double TITLE_WIDTH = 90;
	
	private static final String ICON_PATH = "icon.png";
	private static final double ICON_WIDTH = 50;
	private static final double ICON_HEIGHT = 50;
	
	private static final String IMPORT_SINGLE_BUTTON_TEXT = "Add CV";
	private static final String IMPORT_MULTIPLE_BUTTON_TEXT = "Add CVs";
	private static final String FILTER_BUTTON_TEXT = "Add Req";
	private static final String PROCESS_BUTTON_TEXT = "Analyse";
	private static final String EXPORT_BUTTON_TEXT = "Export";
	
	private static final double BUTTON_WIDTH = 130;
	private static final double BUTTON_HEIGHT = 30;
	
	private static final double FILE_TABLE_WIDTH = 250;
	private static final double FILE_TABLE_HEIGHT = 300;
	private static final double FILTER_TABLE_WIDTH = 850;
	private static final double FILTER_TABLE_HEIGHT = 300;
	private static final double RESULT_TABLE_WIDTH = 1200;
	private static final double RESULT_TABLE_HEIGHT = 330;
	
	private static final double PADDING_SIZE = 20;
	
	private static final String EXPORT_FILE_NAME = "result";
	
	private static final String[] FILE_TABLE_COLUMN = {"Filename", "Type"};
	private static final String[] FILE_TABLE_COLUMN_PROPERTY = {"filename", "type"};
	
	private static final String[] FILTER_TABLE_COLUMN = {"Category", "Type", "Key", "Comparator", "Value", "Priority"};
	private static final String[] FILTER_TABLE_COLUMN_PROPERTY = {"category", "type", "key", "comparator", "value", "priority"};
	
	private ObservableList<FileObject> fileData = FXCollections.observableArrayList();
	private ObservableList<Filter> filterData = FXCollections.observableArrayList(
			new Filter("skill", "info", "java", "", "", "high")
			);
	
	private ObservableList<EvaluatedRecord> resultData = FXCollections.observableArrayList();
	private ObservableList<ObservableList<String>> evaluatedResultData = FXCollections.observableArrayList(); 
	
	private double stageWidth;
	@SuppressWarnings("unused")
	private double stageHeight;
	
	private double widthBetweenTable;
	private double widthBetweenButton;
	
	private ImageView iconImageView;
	private Image iconImage;
	private Label titleLabel;
	
	private Button importSingleButton;
	private Button importMultipleButton;
	private Button filterButton;
	private Button processButton;
	private Button exportButton;
	
	private HBox buttonBox;
	private HBox preprocessBox;
	
	private VBox resultBox;
	
	private TableView<FileObject> fileTable;
	private TableView<Filter> filterTable;
	@SuppressWarnings("rawtypes")
	private TableView resultTable;
	
	private ArrayList<File> originalFileList;
	private ArrayList<File> textFileList;
	private ArrayList<String> textFilePathList;
	
	private ArrayList<JSONObject> jsonList;
	
	private ArrayList<String> categoryList;
	
	private Callback<TableColumn<Filter, String>, TableCell<Filter, String>> categoryComboBoxCellFactory;
	private Callback<TableColumn<Filter, String>, TableCell<Filter, String>> typeComboBoxCellFactory;
	private Callback<TableColumn<Filter, String>, TableCell<Filter, String>> keyEditingCellFactory; // different from others
	private Callback<TableColumn<Filter, String>, TableCell<Filter, String>> comparatorComboBoxCellFactory;
	private Callback<TableColumn<Filter, String>, TableCell<Filter, String>> valueComboBoxCellFactory;
	private Callback<TableColumn<Filter, String>, TableCell<Filter, String>> priorityComboBoxCellFactory;
	
	@SuppressWarnings("rawtypes")
	private Callback<TableColumn, TableCell> resultTableCellFactory;
	
	
	private ArrayList<evaluator.Requirement> filterList;
	
	private ArrayList<Result> resultList;
	
	public MainViewController(double stageWidth, double stageHeight) {
		initializeCellFactory();
		initializeList();
		initializeStageSize(stageWidth, stageHeight);
		initializeMainView();
		initializePreprocessBox();
		initializeButtonBox();
		initializeResultBox();
		initializeStyleClass();
		initializeButtonEventHandler();
	}
	
	private void initializeCellFactory() {
		categoryComboBoxCellFactory = (TableColumn<Filter, String> param) -> new CategoryComboBoxCell();
		typeComboBoxCellFactory = (TableColumn<Filter, String> param) -> new TypeComboBoxCell();
		keyEditingCellFactory = TextFieldTableCell.forTableColumn();
		comparatorComboBoxCellFactory = (TableColumn<Filter, String> param) -> new ComparatorComboBoxCell();
		valueComboBoxCellFactory = (TableColumn<Filter, String> param) -> new ValueComboBoxCell();
		priorityComboBoxCellFactory = (TableColumn<Filter, String> param) -> new PriorityComboBoxCell();
		
		resultTableCellFactory = (@SuppressWarnings("rawtypes") TableColumn param) -> new EvaluatedResultTableCell();
	}
	
	private void initializeList() {
		originalFileList = new ArrayList<File>();
		textFileList = new ArrayList<File>();
		textFilePathList = new ArrayList<String>();
		jsonList = new ArrayList<JSONObject>();
		
		filterList = new ArrayList<evaluator.Requirement>();
		
		resultList = new ArrayList<Result>();
		
		categoryList = InterpreterRule.getCategoryList();
	}
	
	private void initializeStageSize(double stageWidth, double stageHeight) {
		this.stageWidth = stageWidth;
		this.stageHeight = stageHeight;
		
		this.widthBetweenButton = (this.stageWidth - MainViewController.BUTTON_WIDTH*5)/6;
		this.widthBetweenTable = (this.stageWidth - MainViewController.FILE_TABLE_WIDTH - MainViewController.FILTER_TABLE_WIDTH)/3;
	}
	
	@SuppressWarnings("rawtypes")
	private void initializeMainView() {
		iconImageView = new ImageView();
        iconImage = new Image(MainView.class.getResourceAsStream(ICON_PATH));
        
        titleLabel = new Label(TITLE_TEXT);
		
		importSingleButton = new Button(IMPORT_SINGLE_BUTTON_TEXT);
		importMultipleButton = new Button(IMPORT_MULTIPLE_BUTTON_TEXT);
		filterButton = new Button(FILTER_BUTTON_TEXT);
		processButton = new Button(PROCESS_BUTTON_TEXT);
		exportButton = new Button(EXPORT_BUTTON_TEXT);
		
		buttonBox = new HBox();
		buttonBox.getChildren().addAll(iconImageView, titleLabel, importSingleButton, importMultipleButton, filterButton, processButton, exportButton);
		
		fileTable = new TableView<FileObject>();
		initializeFileTableView(fileTable, FILE_TABLE_COLUMN, FILE_TABLE_COLUMN_PROPERTY);
		fileTable.setItems(fileData);
		
		filterTable = new TableView<Filter>();
		initializeFilterTableView(filterTable, FILTER_TABLE_COLUMN, FILTER_TABLE_COLUMN_PROPERTY);
		filterTable.setItems(filterData);
		
		preprocessBox = new HBox();
		preprocessBox.getChildren().addAll(fileTable, filterTable);
		
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
			TableColumn<FileObject, String> tableColumn = new TableColumn<FileObject, String>(columns[i]);
			tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			tableColumn.setCellValueFactory(new PropertyValueFactory<FileObject, String>(columnProperties[i]));
			table.getColumns().add(tableColumn);
		}
		addDeleteButtonToFileTable();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	}
	
	private void initializeFilterTableView(TableView<Filter> table, String[] columns, String[] columnProperties) {
		for (int i=0; i<columns.length; i++) {
			TableColumn<Filter, String> tableColumn = new TableColumn<Filter, String>(columns[i]);
			switch (i) {
				// Category
				case 0:
					tableColumn.setCellFactory(this.categoryComboBoxCellFactory);
					tableColumn.setOnEditCommit(new EventHandler<CellEditEvent<Filter, String>>() {
						@Override
						public void handle(CellEditEvent<Filter, String> event) {
							Filter filter = (Filter) event.getTableView().getItems().get(event.getTablePosition().getRow());
							filter.setCategory(event.getNewValue());
						}
					});
					tableColumn.setCellValueFactory(new PropertyValueFactory<Filter, String>(columnProperties[i]));
					table.getColumns().add(tableColumn);
					break;
					
				// Type
				case 1:
					tableColumn.setCellFactory(this.typeComboBoxCellFactory);
					tableColumn.setOnEditCommit(new EventHandler<CellEditEvent<Filter, String>>() {
						@Override
						public void handle(CellEditEvent<Filter, String> event) {
							System.out.println("edit");
							Filter filter = (Filter) event.getTableView().getItems().get(event.getTablePosition().getRow());
							filter.setCategory(event.getNewValue());
						}
					});
					tableColumn.setCellValueFactory(new PropertyValueFactory<Filter, String>(columnProperties[i]));
					table.getColumns().add(tableColumn);
					break;
					
				// Key
				case 2:
					tableColumn.setCellFactory(keyEditingCellFactory);
					tableColumn.setOnEditCommit(new EventHandler<CellEditEvent<Filter, String>>() {
						@Override
						public void handle(CellEditEvent<Filter, String> event) {
							Filter filter = (Filter) event.getTableView().getItems().get(event.getTablePosition().getRow());
							filter.setKey(event.getNewValue());
						}
					});
					tableColumn.setCellValueFactory(new PropertyValueFactory<Filter, String>(columnProperties[i]));
					table.getColumns().add(tableColumn);
					break;
					
				// Comparator
				case 3:
					tableColumn.setCellFactory(this.comparatorComboBoxCellFactory);
					tableColumn.setCellValueFactory(new PropertyValueFactory<Filter, String>(columnProperties[i]));
					table.getColumns().add(tableColumn);
					break;
					
				// Value
				case 4:
					tableColumn.setCellFactory(this.valueComboBoxCellFactory);
					tableColumn.setCellValueFactory(new PropertyValueFactory<Filter, String>(columnProperties[i]));
					table.getColumns().add(tableColumn);
					break;
					
				// Priority
				case 5:
					tableColumn.setCellFactory(this.priorityComboBoxCellFactory);
					tableColumn.setCellValueFactory(new PropertyValueFactory<Filter, String>(columnProperties[i]));
					table.getColumns().add(tableColumn);
					break;
					
				default:
					break;
			}
		}
		addDeleteButtonToFilterTable();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setEditable(true);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initializeResultTableView(TableView table) {
		table.getColumns().clear();
		evaluatedResultData.clear();
		TableColumn filenameColumn = new TableColumn("Filename");
		//filenameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		//filenameColumn.setCellValueFactory(new PropertyValueFactory<EvaluatedRecord, String>("filename"));
		filenameColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> param) {
				return new SimpleObjectProperty(param.getValue().get(0), "value", param.getValue().get(0));
			}
		});
		table.getColumns().add(filenameColumn);
		
		TableColumn scoreColumn = new TableColumn("Score");
		//scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		//scoreColumn.setCellValueFactory(new PropertyValueFactory<EvaluatedRecord, String>("score"));
		scoreColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> param) {
				return new SimpleObjectProperty(param.getValue().get(1), "value", param.getValue().get(1));
			}
		});
		table.getColumns().add(scoreColumn);
		
		ArrayList<String> filterList = new ArrayList<String>();
		ArrayList<String> filterMatchList = new ArrayList<String>();
		
		for (Filter f : this.filterData) {
			filterList.add(f.getCategory());
			System.out.println(f.getCategory());
			filterMatchList.add("match");
		}
		System.out.println(filterList.size());
		for (int i=0; i<filterList.size(); i++) {
			int index = i;
			
			TableColumn filterColumn = new TableColumn(filterList.get(i));
			filterColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> param) {
					return new SimpleObjectProperty(param.getValue().get((index+1)*2), "value", param.getValue().get((index+1)*2));
				}
			});
			table.getColumns().add(filterColumn);
			
			TableColumn filterMatchColumn = new TableColumn(filterMatchList.get(i));
			filterMatchColumn.setCellFactory(this.resultTableCellFactory);
			filterMatchColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ObservableList<String>, String> param) {
					return new SimpleObjectProperty(param.getValue().get((index+1)*2+1), "value", param.getValue().get((index+1)*2+1));
				}
			});
			table.getColumns().add(filterMatchColumn);
		}

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDeleteButtonToFileTable() {
		TableColumn deleteColumn = new TableColumn<>("Actions");
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
	private void addDeleteButtonToFilterTable() {
		TableColumn deleteColumn = new TableColumn<>("Actions");
		filterTable.getColumns().add(deleteColumn);
		
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
                return new FilterTableButtonCell();
            }
        
        });
	}
	
	private void initializePreprocessBox() {
		HBox.setMargin(fileTable, new Insets(PADDING_SIZE, widthBetweenTable/2, PADDING_SIZE, widthBetweenTable));
		HBox.setMargin(filterTable, new Insets(PADDING_SIZE, widthBetweenTable, PADDING_SIZE, widthBetweenTable/2));
		fileTable.setMaxSize(FILE_TABLE_WIDTH, FILE_TABLE_HEIGHT);
		fileTable.setMinSize(FILE_TABLE_WIDTH, FILE_TABLE_HEIGHT);
		filterTable.setMaxSize(FILTER_TABLE_WIDTH, FILTER_TABLE_HEIGHT);
		filterTable.setMinSize(FILTER_TABLE_WIDTH, FILTER_TABLE_HEIGHT);
	}
	
	private void initializeButtonBox() {
		HBox.setMargin(iconImageView, new Insets(PADDING_SIZE/2, 0, PADDING_SIZE/2, widthBetweenButton/3));
		iconImageView.setFitHeight(ICON_HEIGHT);
		iconImageView.setFitWidth(ICON_WIDTH);
		iconImageView.setImage(iconImage);
		
		HBox.setMargin(titleLabel, new Insets(PADDING_SIZE/1.5, 0, PADDING_SIZE/2, widthBetweenButton/2));
		titleLabel.setMaxWidth(TITLE_WIDTH);
		titleLabel.setMinWidth(TITLE_WIDTH);
		
		HBox.setMargin(importSingleButton, new Insets(PADDING_SIZE, widthBetweenButton/4, PADDING_SIZE, widthBetweenButton));
		HBox.setMargin(importMultipleButton, new Insets(PADDING_SIZE, widthBetweenButton/4, PADDING_SIZE, widthBetweenButton/4));
		HBox.setMargin(filterButton, new Insets(PADDING_SIZE, widthBetweenButton/4, PADDING_SIZE, widthBetweenButton/4));
		HBox.setMargin(processButton, new Insets(PADDING_SIZE, widthBetweenButton/4, PADDING_SIZE, widthBetweenButton/4));
		HBox.setMargin(exportButton, new Insets(PADDING_SIZE, widthBetweenButton, PADDING_SIZE, widthBetweenButton/4));
		
		importSingleButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		importSingleButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		importMultipleButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		importMultipleButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		filterButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		filterButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		processButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		processButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		exportButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		exportButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	
	private void initializeResultBox() {
		VBox.setMargin(resultTable, new Insets(PADDING_SIZE, PADDING_SIZE, PADDING_SIZE, PADDING_SIZE));
		
		resultBox.setMaxSize(RESULT_TABLE_WIDTH, RESULT_TABLE_HEIGHT);
		resultBox.setMinSize(RESULT_TABLE_WIDTH, RESULT_TABLE_HEIGHT);
	}
	
	private void initializeStyleClass() {
		titleLabel.getStyleClass().add("title-label");
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
				fileChooser.setInitialDirectory(new File(BASE_PATH));
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
				dirChooser.setInitialDirectory(new File(BASE_PATH));
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
		
		filterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (Filter f : filterData) {
					System.out.println(f.getCategory() + ", " + f.getType() + ", " + f.getKey() + ", " + f.getComparator() + ", " + f.getValue() + ", " + f.getPriority());
				}
				filterData.add(new Filter("", "", "", "", "", ""));
			}
		});
		
		processButton.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
		        
				for (File file : originalFileList) {
					addFile(true, CVReader.convertFile(file));
					
					textFilePathList.add(CVReader.convertFile(file).getAbsolutePath().substring(
							System.getProperty("user.dir").length() + 1));
				}
				
				setFilterList();
				
				for (File file : textFileList) {
					SmartInterpreter interpreter = new SmartInterpreter(file);
					jsonList.add(interpreter.build());
				}
				
				Evaluator evaluator = new Evaluator();
				for (JSONObject json : jsonList) {
					evaluator.addJsonObj(json);
				}
				
				for (File file : textFileList) {
					evaluator.addCV(file);
				}
				
				System.out.println("start evaluating");
				resultList =  evaluator.query(filterList);
				System.out.println("finish evaluating");
				
				initializeResultTableView(resultTable);
				resultTable.setItems(evaluatedResultData);
				
				
				for(Result result : resultList) {
					ObservableList<String> row = FXCollections.observableArrayList();
				    row.add(result.getfileName().replace(".txt", ""));
				    row.add(Integer.toString(result.getMark()));
				    
				    for (int i=0; i<result.getFilterString().size(); i++) {
				    	row.add(filterList.get(i).getCategory() + " " + filterList.get(i).getKeyword());
				    	row.add(result.getFilterString().get(i));
				    }
				    System.out.println("one row added");
				    resultTable.getItems().add(row);
				}
				
				System.out.println("finish processing");
			
			}
		});
		
		exportButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser dirChooser = new DirectoryChooser();
				dirChooser.setTitle("Choose a folder...");
				dirChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
				File dir = dirChooser.showDialog(importMultipleButton.getScene().getWindow());
				
				if (dir != null && dir.isDirectory()) {
					System.out.println(dir.getAbsolutePath() + "/" + EXPORT_FILE_NAME + "." + TXT);
					File exportFile = new File(dir.getAbsolutePath() + "/" + EXPORT_FILE_NAME + "." + TXT);
					if (!exportFile.exists()) {
						try {
							exportFile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(exportFile.getAbsolutePath()));
						
						for (int i=0; i<resultData.size(); i++) {
							bw.write(resultData.get(i).getFilename() + "   " + resultData.get(i).getScore());
							bw.newLine();
							bw.flush();
						}
						
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void setFilterList() {
		filterList = new ArrayList<evaluator.Requirement>();
		
		for (Filter f : filterData) {
			filterList.add(new evaluator.Requirement(f.getCategory(), f.getType(), f.getKey(), Comparator.getComparator(f.getComparator()), f.getValue(), Priority.getPriority(f.getPriority())));
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
		if (file.exists() && !file.isDirectory() && file.isFile()) {
			String filename = file.getName();
			String[] tokens = filename.split("\\.(?=[^\\.]+$)");
			System.out.println(tokens[1]);
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
		private final SimpleStringProperty category;
		private final SimpleStringProperty type;
		private final SimpleStringProperty key;
		private final SimpleStringProperty comparator;
		private final SimpleStringProperty value;
		private final SimpleStringProperty priority;
		
		public Filter() {
			this.category = new SimpleStringProperty("");
			this.type = new SimpleStringProperty("");
			this.key = new SimpleStringProperty("");
			this.comparator = new SimpleStringProperty("");
			this.value = new SimpleStringProperty("");
			this.priority = new SimpleStringProperty("");
		}
		
		public Filter(String category, String type, String key, String comparator, String value, String priority) {
			this.category = new SimpleStringProperty(category);
			this.type = new SimpleStringProperty(type);
			this.key = new SimpleStringProperty(key);
			this.comparator = new SimpleStringProperty(comparator);
			this.value = new SimpleStringProperty(value);
			this.priority = new SimpleStringProperty(priority);
		}
		
		public String getCategory() {
			return category.get();
		}
		
		public String getType() {
			return type.get();
		}
		
		public String getKey() {
			return key.get();
		}
		
		public String getComparator() {
			return comparator.get();
		}
		
		public String getValue() {
			return value.get();
		}
		
		public String getPriority() {
			return priority.get();
		}
		
		public void setCategory(String category) {
			this.category.set(category);
		}
		
		public void setType(String type) {
			this.type.set(type);
		}
		
		public void setKey(String key) {
			this.key.set(key);
		}
		
		public void setComparator(String comparator) {
			this.comparator.set(comparator);
		}
		
		public void setValue(String value) {
			this.value.set(value);
		}
		
		public void setPriority(String priority) {
			this.priority.set(priority);
		}
		
		public StringProperty categoryProperty() {
			return category;
		}
		
		public StringProperty typeProperty() {
			return type;
		}
		
		public StringProperty keyProperty() {
			return key;
		}
		
		public StringProperty comparatorProperty() {
			return comparator;
		}
		
		public StringProperty valueProperty() {
			return value;
		}
		
		public StringProperty priorityProperty() {
			return priority;
		}
	}
	
	public static class EvaluatedRecord {
		private final SimpleStringProperty filename;
		private final SimpleStringProperty score;
		private final ArrayList<String> filterList;
		private final ArrayList<String> filterMatchList;
		
		public EvaluatedRecord(String filename, String score) {
			this.filename = new SimpleStringProperty(filename);
			this.score = new SimpleStringProperty(score);
			this.filterList = new ArrayList<String>();
			this.filterMatchList = new ArrayList<String>();
		}
		
		public EvaluatedRecord(String filename, String score, ArrayList<String> filterList, ArrayList<String> filterMatchList) {
			this.filename = new SimpleStringProperty(filename);
			this.score = new SimpleStringProperty(score);
			this.filterList = filterList;
			this.filterMatchList = filterMatchList;
		}
		
		public String getFilename() {
			return filename.get();
		}
		
		public void setFilename(String filename) {
			this.filename.set(filename);
		}
		
		public String getScore() {
			return score.get();
		}
		
		public void setScore(String score) {
			this.score.set(score);
		}
		
		public ArrayList<String> getFilterList() {
			return this.filterList;
		}
		
		public ArrayList<String> getFilterMatchList() {
			return this.filterMatchList;
		}
	}
	
	public class EvaluatedResultTableCell extends TableCell<String, String>{
		@Override
		public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            this.setText(item);
            this.setTooltip(
                    (empty || item==null) ? null : new Tooltip(item));
        }
	}
	
	public class CategoryComboBoxCell extends TableCell<Filter, String> {
		private ComboBox<String> comboBox;
		private ArrayList<String> options;

        private CategoryComboBoxCell() {
        	this.options = categoryList;
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                comboBox.setValue(getText());
                setText(null);
                setGraphic(comboBox);
            } else {
            	super.startEdit();
            	createComboBox();
            	comboBox.setValue(null);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(comboBox.getValue());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
            	if (comboBox == null) {
            		createComboBox();
            	}
            	comboBox.setValue(item);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        private void createComboBox() {
            comboBox = new ComboBox<String>();
            comboBox.getItems().addAll(options);

            comboBox.setPromptText("");
            comboBox.setEditable(false);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					commitEdit(comboBox.getSelectionModel().getSelectedItem());
					
					if (newValue != "") {
						((Filter) getTableView().getItems().get(getTableRow().getIndex())).setCategory(comboBox.getValue());
						((Filter) getTableView().getItems().get(getTableRow().getIndex())).setType(" ");
						((Filter) getTableView().getItems().get(getTableRow().getIndex())).setType("");
						((Filter) getTableView().getItems().get(getTableRow().getIndex())).setType(" ");
					}
					
				}
                 
            });
//            
//            comboBox.setOnAction((e) -> {
//                //System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
//            	System.out.println("haha");
//            	getTableView().edit(getIndex(), getTableColumn());
//                commitEdit(comboBox.getSelectionModel().getSelectedItem());
//            });
        }
	}
	
	public class TypeComboBoxCell extends TableCell<Filter, String> {
		private ComboBox<String> comboBox;
		private String category;
		private ArrayList<String> options;

        private TypeComboBoxCell() {
        	this.options = new ArrayList<String>();
        }
        
        public void setCategory(String category) {
        	this.category = category;
        	this.options = InterpreterRule.getPredefinedValuesTypesByCategory(this.category);
        	createComboBox();
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                comboBox.setValue(getText());
                setText(null);
                setGraphic(comboBox);
            } else {
            	super.startEdit();
            	createComboBox();
            	comboBox.setValue(null);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(comboBox.getValue());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
            	if (comboBox == null) {
            		createComboBox();
            	}
            	comboBox.setValue(item);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        private void createComboBox() {
            comboBox = new ComboBox<String>();
            comboBox.getItems().addAll(options);

            comboBox.setPromptText("");
            comboBox.setEditable(false); 
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					commitEdit(comboBox.getSelectionModel().getSelectedItem());
					if (newValue == " ") {
						setCategory(((Filter) getTableView().getItems().get(getTableRow().getIndex())).getCategory());
					} else {
						if (newValue != "") {
							((Filter) getTableView().getItems().get(getTableRow().getIndex())).setType(comboBox.getValue());
							((Filter) getTableView().getItems().get(getTableRow().getIndex())).setValue(" ");
							((Filter) getTableView().getItems().get(getTableRow().getIndex())).setValue("");
							((Filter) getTableView().getItems().get(getTableRow().getIndex())).setValue(" ");
						}
					}
				}
                 
            });
        }
	}
	
	public class ComparatorComboBoxCell extends TableCell<Filter, String> {
		private ComboBox<String> comboBox;
		private ArrayList<String> options;

		private ComparatorComboBoxCell() {
        	options = new ArrayList<String>();
        	for (Comparator c : Comparator.values()) {
        		options.add(c.toString());
        	}
        }

		@Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                comboBox.setValue(getText());
                setText(null);
                setGraphic(comboBox);
            } else {
            	super.startEdit();
            	createComboBox();
            	comboBox.setValue(null);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(comboBox.getValue());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
            	if (comboBox == null) {
            		createComboBox();
            	}
            	comboBox.setValue(item);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        private void createComboBox() {
            comboBox = new ComboBox<String>();
            comboBox.getItems().addAll(options);

            comboBox.setPromptText("");
            comboBox.setEditable(false);   
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					commitEdit(comboBox.getSelectionModel().getSelectedItem());
					((Filter) getTableView().getItems().get(getTableRow().getIndex())).setComparator(comboBox.getValue());
				}
                 
            });
        }
	}
	
	public class ValueComboBoxCell extends TableCell<Filter, String> {
		private ComboBox<String> comboBox;
		private String type;
		private ArrayList<String> options;

        private ValueComboBoxCell() {
        	this.options = new ArrayList<String>();
        }
        
        public void setType(String type) {
        	this.type = type;
        	this.options = PredefinedValuesType.fromString(this.type).getTypeValues();
        	createComboBox();
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                comboBox.setValue(getText());
                setText(null);
                setGraphic(comboBox);
            } else {
            	super.startEdit();
            	createComboBox();
            	comboBox.setValue(null);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(comboBox.getValue());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
            	if (comboBox == null) {
            		createComboBox();
            	}
            	comboBox.setValue(item);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        private void createComboBox() {
            comboBox = new ComboBox<String>();
            comboBox.getItems().addAll(options);

            comboBox.setPromptText("");
            comboBox.setEditable(true);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (comboBox.getValue() == " ") {
						setType(((Filter) getTableView().getItems().get(getTableRow().getIndex())).getType());
					}
					
					commitEdit(comboBox.getSelectionModel().getSelectedItem());
					((Filter) getTableView().getItems().get(getTableRow().getIndex())).setValue(comboBox.getValue());
				}
            });
        }
	}
	
	public class PriorityComboBoxCell extends TableCell<Filter, String> {
		private ComboBox<String> comboBox;
		private ArrayList<String> options;
		
		private PriorityComboBoxCell() {
			options = new ArrayList<String>();
        	for (Priority p : Priority.values()) {
        		options.add(p.toString());
        	}
        }

		@Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                comboBox.setValue(getText());
                setText(null);
                setGraphic(comboBox);
            } else {
            	super.startEdit();
            	createComboBox();
            	comboBox.setValue(null);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(comboBox.getValue());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
            	if (comboBox == null) {
            		createComboBox();
            	}
            	comboBox.setValue(item);
            	setText(null);
            	setGraphic(comboBox);
            }
        }

        private void createComboBox() {
            comboBox = new ComboBox<String>();
            comboBox.getItems().addAll(options);

            comboBox.setPromptText("");
            comboBox.setEditable(false);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					commitEdit(comboBox.getSelectionModel().getSelectedItem());
					((Filter) getTableView().getItems().get(getTableRow().getIndex())).setPriority(comboBox.getValue());
				}
                 
            });
        }
	}
	
	public class FileTableButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("X");
        final Button viewButton = new Button("O");
        
        final HBox buttonPart = new HBox();
        
        FileTableButtonCell () {
            
        	//Action when the button is pressed
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                	FileObject currentFile = (FileObject) FileTableButtonCell.this.getTableView().getItems().get(FileTableButtonCell.this.getIndex());
                	//remove selected item from the table list
                	fileData.remove(currentFile);
                	for (int i=0; i<originalFileList.size(); i++) {
                		if (currentFile.getFilename().equals(originalFileList.get(i).getName())) {
                			originalFileList.remove(i);
                			break;
                		}
                	}
                }
            });
            
            viewButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                	FileObject currentFile = (FileObject) FileTableButtonCell.this.getTableView().getItems().get(FileTableButtonCell.this.getIndex());
                	File file = null;
                	
                	for (int i=0; i<originalFileList.size(); i++) {
                		if (currentFile.getFilename().equals(originalFileList.get(i).getName())) {
                			file = originalFileList.get(i);
                			break;
                		}
                	}
                	
                	Desktop dt = Desktop.getDesktop();
                    try {
						if (file == null) {
							System.out.println("Error");
						} else {
							dt.open(file);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
            	viewButton.setStyle("-fx-text-fill: green;");
            	cellButton.setStyle("-fx-text-fill: red;");
            	buttonPart.getChildren().clear();
            	buttonPart.getChildren().add(cellButton);
            	buttonPart.getChildren().add(viewButton);
            	buttonPart.setPadding(new Insets(0, 0, 0, 7));
                setGraphic(buttonPart);
            } else {
            	setGraphic(null);
            }
        }
    }
	
	public class FilterTableButtonCell extends TableCell<Record, Boolean> {
        final Button cellButton = new Button("X");
        
        FilterTableButtonCell () {
            
        	//Action when the button is pressed
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                	Filter currentFilter = (Filter) FilterTableButtonCell.this.getTableView().getItems().get(FilterTableButtonCell.this.getIndex());
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
            	cellButton.setStyle("-fx-text-fill: red;");
                setGraphic(cellButton);
            } else {
            	setGraphic(null);
            }
        }
    }
	
}
