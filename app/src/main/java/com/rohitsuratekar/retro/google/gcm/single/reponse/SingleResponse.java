
package com.rohitsuratekar.retro.google.gcm.single.reponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SingleResponse {

    @SerializedName("multicast_id")
    @Expose
    private long multicastId;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("failure")
    @Expose
    private Integer failure;
    @SerializedName("canonical_ids")
    @Expose
    private long canonicalIds;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();

    /**
     * 
     * @return
     *     The multicastId
     */
    public long getMulticastId() {
        return multicastId;
    }

    /**
     * 
     * @param multicastId
     *     The multicast_id
     */
    public void setMulticastId(long multicastId) {
        this.multicastId = multicastId;
    }

    /**
     * 
     * @return
     *     The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     * 
     * @return
     *     The failure
     */
    public Integer getFailure() {
        return failure;
    }

    /**
     * 
     * @param failure
     *     The failure
     */
    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    /**
     * 
     * @return
     *     The canonicalIds
     */
    public long getCanonicalIds() {
        return canonicalIds;
    }

    /**
     * 
     * @param canonicalIds
     *     The canonical_ids
     */
    public void setCanonicalIds(long canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

}
