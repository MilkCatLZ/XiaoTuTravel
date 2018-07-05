package shy.car.sdk.travel.update;


import com.base.update.model.BaseUpdate;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by LZ on 2016/8/16.
 */
public class UpdateInfo implements BaseUpdate {


    @SerializedName("ver")
    private String version;
    @SerializedName("message")
    private String[] contents;
    @SerializedName("download_url")
    private String downloadUrl;
    @SerializedName("compulsion")
    private int compulsion;
    @SerializedName("time")
    private String time;


    public String getVersion() {
        return version;
    }


    @Override
    public String getContent() {
        String message = "";
        for (String content : contents) {
            message += "\n" + content;
        }
        return message;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getContents() {
        return contents;
    }

    public void setContents(String[] content) {
        this.contents = content;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getCompulsion() {
        return compulsion;
    }

    public void setCompulsion(int compulsion) {
        this.compulsion = compulsion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
