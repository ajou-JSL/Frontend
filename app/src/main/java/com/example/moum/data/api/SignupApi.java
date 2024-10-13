package com.example.moum.data.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SignupApi {

    @POST("/api/send-email")
    Call<EmailAuthResponse> emailAuth(

    );

    class EmailAuthResponse {
        private String success;
        private String verifyCode;

        public String getSuccess() {
            return success;
        }

        public String getVerifyCode() {
            return verifyCode;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public void setVerifyCode(String verifyCode) {
            this.verifyCode = verifyCode;
        }

    }
}
