package comp5216.sydney.edu.au.project_flume.Model;

public class MyProgressBar {
    String setter;
    String max;
    String viewer;
    String progress;

    public MyProgressBar(String setter, String max, String viewer, String progress) {
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

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getViewer() { return viewer; }
    public void setViewer(String viewer) {
        this.viewer = viewer;
    }

    public String getProgress() {
        return progress;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }
}
