package comp5216.sydney.edu.au.project_flume.Model;

public class User {

    private String id;
    private String username;
    private String imageUri;
    private String matchId;
    private String isMatch;
    private String progressMax;
    private String unLocked;



    public User (String id, String username, String imageURL, String progressMax, String unLocked) {
            this.id = id;
            this.username = username;
            this.imageUri = imageURL;
            this.progressMax = progressMax;
            this.unLocked = unLocked;
    }
    public User () {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }

    public String getIsMatch() { return isMatch; }
    public void setIsMatch(String isMatch) { this.isMatch = isMatch; }

    public String getProgressMax() { return progressMax; }

    public String getUnLocked() {
        return unLocked;
    }

    public void setUnLocked(String unLocked) {
        this.unLocked = unLocked;
    }

    public void setProgressMax(String progressMax) { this.progressMax = progressMax; }
}
