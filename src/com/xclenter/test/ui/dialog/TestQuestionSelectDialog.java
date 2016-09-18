package com.xclenter.test.ui.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.xclenter.test.model.QuestionModel;

public abstract class TestQuestionSelectDialog extends TitleAreaDialog {
	List<QuestionModel> questions;
	public String examid;

	public TestQuestionSelectDialog(Shell parentShell) {
		super(parentShell);
	}

	public TestQuestionSelectDialog(Shell parentShell, String eid,
			List<QuestionModel> questions) {
		super(parentShell);
		this.examid = eid;
		this.questions = questions;
	}

	private Table questionTable;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		super.create();
		setTitle("Test");
		setMessage("choose a question to test");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, true);
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true);
		container.setLayoutData(grid);
		container.setLayout(layout);

		createTaskTable(container);
		return area;
	}

	private void createTaskTable(Composite container) {
		// combine label with combo
		Composite cont = new Composite(container, SWT.NONE);
		cont.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, false);
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true);
		cont.setLayoutData(grid);
		cont.setLayout(layout);

		// create label
		Label lbSubject = new Label(cont, SWT.NONE);
		lbSubject.setText("please choose a question for testing");

		GridData dataSubject = new GridData();
		dataSubject.grabExcessHorizontalSpace = true;
		dataSubject.horizontalAlignment = GridData.FILL;

		questionTable = new Table(cont, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.FULL_SELECTION);
		questionTable.setLayoutData(dataSubject);

		setTableContents(questionTable, questions);
	}

	protected abstract void doAfterSelect(QuestionModel questionmodel);

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		TableItem[] items = questionTable.getSelection();
		if (items != null && items.length == 1) {
			TableItem tableItem = items[0];
			QuestionModel selectedQuestion = new QuestionModel(
					tableItem.getText(0), tableItem.getText(1));
			doAfterSelect(selectedQuestion);
		} else {
			MessageBox messageBox = new MessageBox(this.getShell(),
					SWT.ICON_INFORMATION);
			messageBox.setMessage("please select one item");
			messageBox.open();
		}
	}

	public void setTableContents(Table table, List<QuestionModel> data) {
		table.removeAll();

		table.setHeaderVisible(true);

		TableColumn idColumn = new TableColumn(table, SWT.NONE);
		idColumn.setText("id");
		TableColumn nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setText("name");

		for (QuestionModel item : data) {
			TableItem rowItem = new TableItem(table, SWT.NONE);
			rowItem.setText(0, item.getQid());
			rowItem.setText(1, item.getName());
		}

		final TableColumn[] columns = table.getColumns();
		for (TableColumn column : columns) {
			column.pack();
		}
	}

}
