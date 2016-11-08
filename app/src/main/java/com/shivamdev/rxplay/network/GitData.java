package com.shivamdev.rxplay.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 12/8/16.
 */

public class GitData {

    @SerializedName("name")
    public String repoName;

    @SerializedName("description")
    public String repoDesc;

    @SerializedName("html_url")
    public String htmlUrl;

    @Override
    public String toString() {
        return "Repo Name : " + repoName + " \n" +
                "Repo Desc : " + repoDesc + " \n" +
                "Repo Url : " + htmlUrl + " \n\n";
    }
}
