package net.frontlinesms.plugins.learn.ui.gradebook;

import org.springframework.context.ApplicationContext;

import thinlet.Thinlet;

import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.domain.Gradebook;
import net.frontlinesms.plugins.learn.data.domain.StudentGrades;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.GradebookDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.handler.contacts.GroupSelecterDialog;
import net.frontlinesms.ui.handler.contacts.SingleGroupSelecterDialogOwner;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class GradebookTabHandler implements ThinletUiEventHandler, SingleGroupSelecterDialogOwner {
	private static final String XML_LAYOUT = "/ui/plugins/learn/gradebook/list.xml";

	private final FrontlineUI ui;
	private final Object tab;
	private final GroupDao groupDao;
	private final GradebookDao gradebookDao;

	public GradebookTabHandler(FrontlineUI ui, ApplicationContext ctx) {
		this.ui = ui;
		tab = ui.loadComponentFromFile(XML_LAYOUT, this);
		groupDao = (GroupDao) ctx.getBean("groupDao");
		gradebookDao = (GradebookDao) ctx.getBean("gradebookDao");
	}

	public Object getTab() {
		return tab;
	}
	
//> UI EVENT METHODS
	public void selectGroup() {
		GroupSelecterDialog dialog = new GroupSelecterDialog(ui, this, groupDao);
		dialog.init(new Group(null, null));
		dialog.show();
	}
	
//> GROUP SELECTION METHODS
	public void groupSelectionCompleted(Group group) {
		setGroup(group);
	}
	
//> UI HELPER METHODS
	private Object find(String componentName) {
		return Thinlet.find(tab, componentName);
	}
	
	private void setGroup(Group g) {
		ui.setText(find("tfClass"), g.getName());
		Gradebook gradebook = gradebookDao.getForClass(g);
		
		Object table = find("tbGrades");

		// Update table header
		Object header = Thinlet.get(table, Thinlet.HEADER);
		Object[] columns = ui.getItems(header);
		for (int i = 1; i < columns.length; i++) {
			ui.remove(columns[i]);
		}
		for(Topic t : gradebook.getTopics()) {
			ui.add(header, ui.createColumn(t.getName(), null));
		}
		
		// Update table contents
		for(StudentGrades r : gradebook.getResults()) {
			ui.add(table, createRow(r));
		}
	}

	private Object createRow(StudentGrades r) {
		Integer[] grades = r.getGrades();
		String[] cellText = new String[grades.length + 1];
		cellText[0] = r.getStudent().getName();
		for (int i = 0; i < grades.length; i++) {
			String grade = grades[i] == null
					? InternationalisationUtils.getI18nString("plugins.learn.gradebook.result.none")
					: grades[i] + "%";
			cellText[i + 1] = grade;
		}
		return ui.createTableRow(null, cellText);
	}
}
