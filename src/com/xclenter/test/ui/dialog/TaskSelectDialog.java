package com.xclenter.test.ui.dialog;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class TaskSelectDialog extends TitleAreaDialog {
	Set<String > tasks;
	
	public TaskSelectDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public TaskSelectDialog(Shell parentShell , Set<String> tasks){
		super(parentShell);
		
		this.tasks = tasks;
	}

	private Combo subjectCombo;

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2 , true);
		GridData grid = new GridData(SWT.FILL , SWT.FILL , true , true);
	    grid.widthHint = 150;
	    grid.heightHint = 250;
		container.setLayoutData(grid);
		container.setLayout(layout);

		createSubjectCombo(container);
		return area;
	}

	private void createSubjectCombo(Composite container) {
		// combine label with combo
		Composite cont = new Composite(container, SWT.NONE);
		cont.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1 , true);
		GridData grid = new GridData(SWT.FILL , SWT.FILL , true , true);
		cont.setLayoutData(grid);
		cont.setLayout(layout);
		
		// create label
		Label lbSubject = new Label(cont, SWT.NONE);
		lbSubject.setText("Çë×÷Òµ/¿¼ÊÔ");
		
		// create combo
		GridData dataSubject = new GridData();
		dataSubject.grabExcessHorizontalSpace = true;
		dataSubject.widthHint = 180;
		dataSubject.heightHint = 300;
		
		subjectCombo = new Combo(cont, 
            SWT.DROP_DOWN | SWT.MULTI | 
            SWT.V_SCROLL | SWT.H_SCROLL);
	
		setComboContents(subjectCombo , new TreeSet<String>(tasks));
		subjectCombo.select(0);

		subjectCombo.setLayoutData(dataSubject);
		
//		// Use selection listener to update stage list corresponding to given subject
//		subjectCombo.addSelectionListener(new SelectionListener(){
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent event) {
//			}
//
//			@Override
//			public void widgetSelected(SelectionEvent event) {
//				// update stage combo
//				setComboContents(stageCombo , new TreeSet<String>(subjects.get(subjectCombo.getText()).keySet()));
//				stageCombo.update();
//				// select the first element by default
//				stageCombo.select(0);
//			}
//			
//		});
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	
	public void setComboContents(Combo combo , TreeSet<String> data) {
	    combo.removeAll();
	    // sort the set
	    data.comparator();
	    
	    Iterator<String> ite = data.iterator();
	    
	    while(ite.hasNext()){
	    	combo.add(ite.next().toString());
	    }

	}
	
}
