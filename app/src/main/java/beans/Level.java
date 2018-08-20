package beans;


public class Level {

    private int level ;
    private int progressLevel;
    private int maxProgress;

    public Level(){

    }
    public Level( int level){
      maxProgress=getLevel()*100+getMaxProgress();
        this.level=level;
    }

    public Level(int level,int progressLevel,int maxProgress ){
        this.level=level;
        this.progressLevel=progressLevel;
        this.maxProgress=maxProgress;
    }


    public int getLevel(){
        return level;
    }
    public void setLevel(int level){
        this.level=level;
    }
    public int getProgressLevel(){
        return progressLevel;
    }
    public void setProgressLevel(int progressLevel){
        this.progressLevel=progressLevel;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
