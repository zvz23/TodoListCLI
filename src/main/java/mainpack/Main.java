package mainpack;

import todopack.*;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.*;


public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        TodoListCLI todo = new  TodoListCLI();
        System.out.println("=============");
        System.out.println("Todo-list CLI");
        System.out.println("=============");
        System.out.println("//enter help to show options");
        boolean todoLoop = true;
        while(todoLoop){
            Pattern tempPattern = Pattern.compile("\\s*(?<command>\\w+)\\s*");
            String command = " ";
            while(!tempPattern.matcher(command).matches()){
                System.out.print("> ");
                command = scan.nextLine();
            }
            Matcher tempMatcher = tempPattern.matcher(command);
            tempMatcher.matches();
            command = tempMatcher.group("command");
            switch(command){
                case "add":
                    String toAdd = " ";
                    Pattern pattern = Pattern.compile("\\s*(?<text>[A-Za-z0-9]+.*)");
                    while(toAdd.matches("\\s*")){
                        System.out.print("Enter text to add: ");
                        toAdd = scan.nextLine();
                    }
                    Matcher matcher = pattern.matcher(toAdd);
                    if(matcher.matches()){
                        todo.add(matcher.group("text"));
                        System.out.println("Successfully added");
                    }

                    break;
                case "edit":
                    try{
                        int tempId = validateInt("Enter id to edit: ", "Invalid id");
                        if(todo.hasId(tempId)){
                            System.out.println("What do you want to edit?");
                            System.out.println("1. edit text");
                            System.out.println("2. toggle check/uncheck");
                            System.out.println("3. exit");
                            try{
                                int option = validateInt("Enter option: ", "Invalid option");
                                switch(option){
                                    case 1:
                                        System.out.print("Enter text to change: ");
                                        String temp = scan.nextLine();
                                        if(!temp.matches("\\s*")){
                                            boolean accepted = todo.edit(tempId, temp);
                                            if(accepted){
                                                System.out.println("Successfully edited");
                                            }
                                            else{
                                                System.out.println("Failed to edit");
                                            }
                                        }
                                        else{
                                            System.out.println("Please enter a text");
                                        }
                                        break;
                                    case 2:
                                        if(todo.toggle(tempId)){
                                            System.out.println("Toggled successfully");
                                        }
                                        else{
                                            System.out.println("Failed to toggle");
                                        }
                                        break;
                                    case 3:
                                        break;
                                    default:
                                        System.out.println("Invalid option");
                                        break;
                                }
                            }catch (NumberFormatException e){
                                System.out.println("Invalid option");
                            }

                        }
                        else{
                            System.out.println("Id not found");
                        }

                    }catch (InputMismatchException e){
                        System.out.println("Id number not valid");
                    }
                    break;
                case "remove":
                    int tempId = validateInt("Enter id to remove: ", "Invalid id");
                    boolean removed = todo.remove(tempId);
                    if(removed){
                        System.out.println("Item successfully removed");
                    }
                    else{
                        System.out.println("Id not found");
                    }
                    break;
                case "list":
                    todo.showList();
                    System.out.println();
                    break;
                case "help":
                    showHelp();
                    break;
                case "exit":
                    System.out.println("Bye!");
                    todoLoop = false;
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }

    }

    private static int validateInt(String text, String errText){
        Scanner scan = new Scanner(System.in);
        int tempId = 0;
        boolean notValidInt = true;
        while(notValidInt){
            try{
                System.out.print(text);
                tempId = scan.nextInt();
                notValidInt = false;
            }catch (InputMismatchException e){
                System.out.println(errText);
            }
            finally{
                scan.nextLine();
            }
        }
        return tempId;
    }

    private static void showHelp(){
        System.out.println("add - add an item");
        System.out.println("edit - edit a specific item");
        System.out.println("remove - remove a specific item");
        System.out.println("list - display all items");
        System.out.println("help - display this page");
        System.out.println("exit - terminate the program\n");
    }

}
