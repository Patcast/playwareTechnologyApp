package pat.international.playwaretwo.PianoTiles.GameCode;

import android.graphics.PointF;

public class MainThread extends Thread{
    protected ThreadHandler threadHandler;
    protected int pos;
    protected PointF viewSize;
    protected PointF start;
    protected boolean isClicked;


    public MainThread(ThreadHandler threadHandler, int pos, PointF viewSize){
        this.threadHandler = threadHandler;
        this.pos = pos;
        this.viewSize = viewSize;
        this.isClicked = false;
        this.start = new PointF();
        this.setPoint();
    }


    public void setPoint(){
        if(this.pos == 1){
            this.start.set(0, - this.viewSize.y/4);
        } else if(this.pos == 2){
            this.start.set(this.viewSize.x/4, -this.viewSize.y/4);
        } else if(this.pos == 3){
            this.start.set(this.viewSize.x/2, -this.viewSize.y/4);
        } else if(this.pos == 4){
            this.start.set(this.viewSize.x/4*3, -this.viewSize.y/4);
        }
    }

    public void run(){
        while(check(this.start.y)){
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            this.threadHandler.clearRect(new PointF(this.start.x, this.start.y));
            this.start.set(this.start.x, this.start.y + this.viewSize.y*0.005f);
            this.threadHandler.drawRect(new PointF(this.start.x, this.start.y), this.isClicked);
        }
        this.threadHandler.popOut();
        if(!this.isClicked){                    /// This parts verifies if tile was clicked on time
            this.threadHandler.removeHealth();
        }
        return;
    }


    public boolean checkScore(float coordinateOfColumn){
        /// Translate tiles number to coordinates.
        if(this.isClicked) return true;

        if(coordinateOfColumn >= this.start.x && coordinateOfColumn <= this.start.x + this.viewSize.x/4){ // Checks if it was clicked on the right column
            if(  this.start.y >= this.viewSize.y*2/4){ // Checks if it was clicked at the right y location
                this.threadHandler.addScore(this.pos);
                this.threadHandler.clearRect(new PointF(this.start.x, this.start.y));
                this.isClicked = true;

            }
        }
        return isClicked;
    }


    public boolean check(float y){ /// this is how it checks if it is on the screen
        if(y >= this.viewSize.y){
            return false;
        }
        return true;
    }


}
