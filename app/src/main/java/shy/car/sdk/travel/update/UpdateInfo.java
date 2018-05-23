package shy.car.sdk.travel.update;


import com.base.update.model.BaseUpdate;
import com.google.gson.annotations.SerializedName;


/**
 * Created by LZ on 2016/8/16.
 */
public class UpdateInfo implements BaseUpdate {
    
    
    /**
     * version : 0.5.2
     * content : 版本说明
     * download_url :
     * compulsion : 1
     * date : 2017-03-01
     * sessionTime : 2592000
     * patchVersion : patch3.0.175.xy
     * patchDownload : http://cn2.php.net/get/php-7.1.2.tar.gz/from/this/mirror
     */
    
    @SerializedName( "version")
    private String version;
    @SerializedName( "content")
    private String content;
    @SerializedName( "download_url")
    private String downloadUrl;
    @SerializedName( "compulsion")
    private int compulsion;
    @SerializedName( "date")
    private String date;
    @SerializedName( "sessionTime")
    private int sessionTime;
    @SerializedName( "patchVersion")
    private String patchVersion;
    @SerializedName( "patchDownload")
    private String patchDownload;
    
    public String getVersion() { return version;}
    
    public void setVersion(String version) { this.version = version;}
    
    public String getContent() { return content;}
    
    public void setContent(String content) { this.content = content;}
    
    public String getDownloadUrl() { return downloadUrl;}
    
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl;}
    
    public int getCompulsion() { return compulsion;}
    
    public void setCompulsion(int compulsion) { this.compulsion = compulsion;}
    
    public String getDate() { return date;}
    
    public void setDate(String date) { this.date = date;}
    
    public int getSessionTime() { return sessionTime;}
    
    public void setSessionTime(int sessionTime) { this.sessionTime = sessionTime;}
    
    public String getPatchVersion() { return patchVersion;}
    
    public void setPatchVersion(String patchVersion) { this.patchVersion = patchVersion;}
    
    public String getPatchDownload() { return patchDownload;}
    
    public void setPatchDownload(String patchDownload) { this.patchDownload = patchDownload;}
}
