package com.xclenter.test.util.action;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class ActionUtil {
	
	/*
	 * ���� menuLabel  acitonId  Ψһȷ��һ��action  
	 * ����ҵ������޸��� action�� enable״̬  ����true
	 * ���� ���� false
	 */
	public static boolean enableAction(String menuLabel,String actionId,boolean enabled){
		IWorkbench workbench = PlatformUI.getWorkbench();

		Menu menubar = workbench.getActiveWorkbenchWindow().getShell()
				.getMenuBar();
		if (menubar == null) {
			return false;
		}
		
		MenuItem[] mItems = menubar.getItems();
		if (mItems == null) {
			return false;
		}
		
		for (MenuItem mItem : mItems) {
			if (mItem.getText().equals(menuLabel)) {
				Menu menu = mItem.getMenu();
				for (MenuItem item : menu.getItems()) {
					SubContributionItem cItem = null;
					Object menuItemData = item.getData();
					if (menuItemData == null) {
						continue;
					}
					if (menuItemData instanceof SubContributionItem) {
						cItem = (SubContributionItem) menuItemData;
						IContributionItem conItems = cItem.getInnerItem();
						if (conItems instanceof ActionContributionItem) {
							ActionContributionItem items = (ActionContributionItem) conItems;
							String id = cItem.getId();
							if (id != null && id.equals(actionId)) {
								items.getAction().setEnabled(enabled);
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}
