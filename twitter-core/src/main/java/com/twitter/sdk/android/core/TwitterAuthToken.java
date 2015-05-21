package com.twitter.sdk.android.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an authorization token and its secret.
 */
public class TwitterAuthToken extends AuthToken implements Parcelable {

    public static final Parcelable.Creator<TwitterAuthToken> CREATOR
            = new Parcelable.Creator<TwitterAuthToken>() {
        public TwitterAuthToken createFromParcel(Parcel in) {
            return new TwitterAuthToken(in);
        }

        public TwitterAuthToken[] newArray(int size) {
            return new TwitterAuthToken[size];
        }
    };

    @SerializedName("token")
    public final String token;

    @SerializedName("secret")
    public final String secret;

    public TwitterAuthToken(String token, String secret) {
        super();
        this.token = token;
        this.secret = secret;
    }

    // for testing purposes
    TwitterAuthToken(String token, String secret, long createdAt) {
        super(createdAt);
        this.token = token;
        this.secret = secret;
    }

    private TwitterAuthToken(Parcel in) {
        super();
        this.token = in.readString();
        this.secret = in.readString();
    }

    @Override
    public boolean isExpired() {
        // Twitter does not expire OAuth1a tokens
        return false;
    }

    @Override
    public Map<String, String> getAuthHeaders(TwitterAuthConfig authConfig,
            String method, String url, Map<String, String> postParams) {
        final Map<String, String> headers = new HashMap<String, String>(1);
        final String authorizationHeader =
                new OAuth1aHeaders().getAuthorizationHeader(authConfig, this, null, method, url,
                        postParams);
        headers.put(HEADER_AUTHORIZATION, authorizationHeader);
        return headers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder()
                .append("token=").append(this.token)
                .append(",secret=").append(this.secret);
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(token);
        out.writeString(secret);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterAuthToken)) return false;

        final TwitterAuthToken that = (TwitterAuthToken) o;

        if (secret != null ? !secret.equals(that.secret) : that.secret != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        return result;
    }
}