package projects;

import java.math.BigDecimal;
import java.util.*;
import java.util.Scanner;
import projects.dao.DbConnection;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;



public class ProjectsApp {
	
	private ProjectService projectService = new ProjectService();
	
	private Scanner scanner= new Scanner (System.in);
	
	//@formatter:off
	private List<String> operations = List.of("1) Add a project");
	//@formatter:on
		
	
	public static void main(String[] args) {
		 new ProjectsApp().processUserSelections();
	}

	private void processUserSelections() {
		boolean done =false; 
				while (!done) {
					try {
						int selection = getUserSelection();
						switch (selection) {
						case -1:
							done =exitMenu();
							break;
						case 1:
							createProject();
							break;
						default:
							System.out.println("\n" + selection + " is not a valid selection. Try again.");
							break;
						}
					}
					catch (Exception e) {
						System.out.println("\nError: " + e + " Try again.");
					}
				}
	}

	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	private int getUserSelection() {
		printOperations();
		
		Integer input =getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1: input;
	}

	private Integer getIntInput(String prompt) {
		
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input) ) {
					
		return null;
	}
		try {
			return Integer.valueOf(input);
		}
		catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	private String getStringInput(String prompt) {
		System.out.println(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}

	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		operations.forEach(line -> System.out.println("    "+ line));
		
	}
	
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimateHours = getDecimalInput("Enter the estimate hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimateHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("you have successfully created project: " + dbProject);
	}

	private BigDecimal getDecimalInput(String prompt) {
		
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input) ) {
					
		return null;
	}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}
	
	
}

