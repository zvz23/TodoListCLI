package todopack;

public interface TodoList {

    void add(String todo);
    boolean edit(int id, String text);
    boolean toggle(int id);
    boolean remove(int id);
    void showList();
}
