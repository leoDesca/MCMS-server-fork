import java.util.Date;

public class Challenge {

    private String name;
    private Date startDate;
    private Date finishDate;
    private int duration;

    public Challenge(String name, Date startDate, Date finishDate, int duration) {
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.duration = duration;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date stratDate) {
        this.startDate = stratDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    //Methods
    //method to get questions
    public void getQuestions(int challengeId) {

    }

    //overloaded for if the method is accessed by name
    public void getQuestions(String name){

    }

}
