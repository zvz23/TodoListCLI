package todopack;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import encryptionpack.*;

public class TodoListCLI implements TodoList{
    private Map<Integer, ToDo> dataMap = new Hashtable<>();
    private List<ToDo> todoList = new ArrayList<>();
    private String dataFile = "./data.dat";
    private boolean changed = false;
    private Encryption encryption;
    public TodoListCLI(){
        encryption = new Encryption();
        encryption.setAlgorithm(new ShiftAlgorithm());
        loadData();
    }

    public boolean hasId(int id){
        return dataMap.containsKey(id);
    }

    @Override
    public void add(String todo) {
        changed = true;
        ToDo todoItem = new ToDo(todo);
        todoList.add(todoItem);
        dataMap.put(dataMap.size() + 1, todoItem);
        saveData();
        loadData();
    }

    @Override
    public boolean edit(int id, String text) {
        if(dataMap.get(id) != null){
            dataMap.get(id).setTitle(text);
            saveData();
            loadData();
            changed = true;
            return true;
        }
        return false;
    }
    public boolean toggle(int id){
        ToDo tempTodo = dataMap.get(id);
        if(tempTodo != null){
            if(tempTodo.isChecked()){
                tempTodo.uncheck();
            }
            else{
                tempTodo.check();
            }
            changed = true;
            saveData();
            loadData();
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        if(dataMap.get(id) != null){
            todoList.remove(dataMap.get(id));
            changed = true;
            saveData();
            loadData();
            return true;
        }
        return false;
    }


    @Override
    public void showList() {
        if(dataMap.size() == 0){
            System.out.println("No data");
            return;
        }
        Set<Integer> keySet =  dataMap.keySet().stream().sorted().collect(Collectors.toSet());
        for(Integer keys : keySet){
            ToDo tempToDo = dataMap.get(keys);
            String tempCheck = tempToDo.isChecked() ? "Done" : "Not done";
            System.out.println(keys+". "+tempToDo.getTitle() + " || " + tempCheck);
        }
    }
    private void loadData(){
        dataMap.clear();
        todoList.clear();
        final String regex = "\\s*\\[(?<title>.*)\\]\\s(?<check>\\w+)\\s*";
        Pattern pattern = Pattern.compile(regex);
        File file = new File(dataFile);
        int count = 1;
        Scanner scan = null;
        try{
            scan = new Scanner(file);
            while(scan.hasNextLine()) {
                String line = scan.nextLine();
                Matcher matcher = pattern.matcher(line);
                if(matcher.matches()){
                    ToDo tempToDo = new ToDo(encryption.decrypt(matcher.group("title"), 5));
                    if(matcher.group("check").equals("true")){
                        tempToDo.check();
                    }
                    else{
                        tempToDo.uncheck();
                    }
                    todoList.add(tempToDo);
                    dataMap.put(count++, tempToDo);
                }
            }
            System.out.println("Data file successfully loaded.");
        }
        catch (FileNotFoundException e){
            System.out.println("Note: no data file found, create a new one by adding an item in the list");
        }
        finally{
            if(scan != null){
                scan.close();
            }
        }
    }

    private void saveData(){
        if(!changed){
            System.out.println("Nothing to save.");
        }
        else{
            FileWriter writer = null;
            try{
                writer = new FileWriter(dataFile, false);
                for(ToDo todo : todoList){
                    String encrpyedTitle = encryption.encrypt(todo.getTitle(), 5);
                    writer.write("["+encrpyedTitle+"] "+ todo.isChecked());
                    writer.write("\n");
                }
                writer.close();
            }catch (IOException e){
                throw new IllegalArgumentException("There was an error");
            }

        }
    }


}
