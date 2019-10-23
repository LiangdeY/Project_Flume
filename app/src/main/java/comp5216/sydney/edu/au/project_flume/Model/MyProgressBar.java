package comp5216.sydney.edu.au.project_flume.Model;

public class MyProgressBar {
    private String setter;
    private String viewer;
    private String max;
    private String progress;

    public MyProgressBar(String setter, String viewer, String max, String progress) {
        this.setter = setter;
        this.max = max;
        this.viewer = viewer;
        this.progress = progress;
    }
    public MyProgressBar(){}

    public String getSetter() {
        return setter;
    }
    public void setSetter(String setter) {
        this.setter = setter;
    }

    public String getViewer() { return viewer; }
    public void setViewer(String viewer) {
        this.viewer = viewer;
    }

    public String getMax() {
        return max;
    }
    public void setMax(String max) {
        this.max = max;
    }


    public String getProgress() {
        return progress;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }
}
