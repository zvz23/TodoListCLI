package todopack;

public class ToDo {

    private String title;
    private boolean checked;

    public ToDo(String title){
        this.title = title;
        this.checked = false;
    }

    public boolean isChecked(){
        return checked;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String text){
        this.title = text;
    }

    public void check(){
        checked = true;
    }
    public void uncheck(){
        checked = false;
    }

    @Override public String toString(){
        String temp = isChecked() ? "done" : "not done";
        return title + " " + temp;
    }
}
