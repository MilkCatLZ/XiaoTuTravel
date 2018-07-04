package shy.car.sdk.travel.update;


import com.base.update.model.BaseUpdate;
import com.google.gson.annotations.SerializedName;


/**
 * Created by LZ on 2016/8/16.
 */
public class UpdateInfo implements BaseUpdate {
    

    @SerializedName( "ver")
    private String version;
    @SerializedName( "content")
    private String content;
    @SerializedName( "download_url")
    private String downloadUrl;
    @SerializedName( "compulsion")
    private int compulsion;
    @SerializedName( "time")
    private String time;

    
    public String getVersion() { return version;}
    
    public void setVersion(String version) { this.version = version;}
    
    public String getContent() { return content;}
    
    public void setContent(String content) { this.content = content;}
    
    public String getDownloadUrl() { return downloadUrl;}
    
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl;}
    
    public int getCompulsion() { return compulsion;}
    
    public void setCompulsion(int compulsion) { this.compulsion = compulsion;}
    
    public String getTime() { return time;}
    
    public void setTime(String time) { this.time = time;}
    
}
