package com.example.booklending.fireconfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String firebasePrivateKeyJson = "{" +
        "\"type\": \"service_account\"," +
        "\"project_id\": \"book-lending-ms\"," +
        "\"private_key_id\": \"735f07941e8a93bf7a43dcef574159386bcaf1fb\"," +
        "\"private_key\": \"-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC3B2GymDGjXPFp\nAxZaqBcEt7C0iKt5Ci33pPImPMSF5fGA7b7E6zS+g9JjUxEJNh8NeXqTV1QNq7Ht\nTWj0Z0fOTMTyRfeRV7qguTXr+nIU7ms07nqkqQB4EZL1LukV+5O/xmvmDRa90zbJ\nrWaz68ZUozcpkCQnzH3KA/w+XFDnn6JcSdXolae/eYSSklpMhxYdUz9HmAJxJXlz\n1vTdShdAwEQRUj+wSg/udf9odexccDFFM3iDMdK+M70Tp6HDCRf8jNLFCZHOJnOh\n52i/mMuGhtflZ/qS12M3mLhKpNqNFoImu2D1/wt0V1VF/K08S2cLNjMd9Mwt2ePt\nPKk1yhRHAgMBAAECggEARM7Tnc61qpLAdxggCUj0DmP4XIac4E5BZb5vAo0J3MTL\nfArlEybRl2X7bDfIScw89FXdDRNhmBoaM/pMIMXLKvWVfPds0QlIMjAu414uEUxo\npWeaV9jpQjppIx/VpNwFea1g1wXIrOiAymUUHUPgNeV+UXPNA+FO0OmapN8T019/\nVYG2/lR8u34tDtqZhpoDcaw13/ZHxJGBh5KF9kCWs0RYqwfbHfiIEqaNgrVAlheP\nzHYY56E8+EaQq5Jxz/EayBTmHpYxy8Bn8JgsFBOQ1X7SaRhJvU0JASMDVBs8GfYx\n/dqFg2tXMdxlR8bVAa/FR/LOk5Yw4L+TXB/LvJAZwQKBgQDfiYLN72/u4Oyn9+hn\nFEdGnXWenmmPoi4doWOkF0UlGSEXOoyuCh1uYp4EznslBYe4uETVaQWmJfR0GvtL\n8SHBDD94UWpCASz4ELkNOao6CDk4bbxZ7qGZ2VFHtXC2sbvmBRFcpX4uywYOeUUl\nTmsN78uciHCrv5Te6lUYLyIBsQKBgQDRm+JaU9czaiBL6rHXxsT+sjixuF7kPhwu\ncjapMC7p2ApyZizj3DVWeS0Iku1Ml+4VN7CiK4CZza4ERK2b+OZgTG1f5s7AJJBg\nRXmedLVOtpouRlRMJoo3XB4fXAI+MdH51QTQsf+efkOB+15vYa4m8eUrWbiykrbw\ne6Eu2m+7dwKBgQCkEaQGTuWTKVfJ+TWE4udltxCreY31NI4whVJDEClaD1WwXPMA\nksADUzK0SHEuPR7ev7EWCl1xE46W4W7s0HF75ed4AhnBlGdTM8L/KAH/UiXdev2x\n7GqEJ3N29H9kOJO3QqT5oSY6Zh1t5iiQNzxwID8MB0DbbmRwMQKAG6gUIQKBgG4k\nEFPQfGM638+kemDcxITlF19jdxFOBbbuMwTCGdSsvgiQ4gO1oZ1XvS8lLWYy61rg\n0YLVgxTOfCMoB2XO6xcs8cgT2w4h6G998kMuJls/HlM0h3UgRz6BvB5UVaVivmgd\n5BLZhW0+AbEaaevzEvTJ5NbbpW7IIzLIc9DP3ShPAoGAElUjiH6C94PoFXAF4Ei5\nwpp/L89WmEi3gmL73m8WEvcGspNcqA7j9GxMbk9GJd2M0+83GV56Wstdn8i/jV7m\n7NpdhpDfTk9mkR/shDnb8Z/NQCyWiV1I9AOyV/hUup/MhZIQwudqjjzsof7IKvb8\n5fZ0gwp02fkjVBqwGg5kK6Q=\n-----END PRIVATE KEY-----\n\"," + 
        "\"client_email\": \"firebase-adminsdk-xk2ku@book-lending-ms.iam.gserviceaccount.com\"," +
        "\"client_id\": \"110673771467306941310\"," +
        "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\"," +
        "\"token_uri\": \"https://oauth2.googleapis.com/token\"," +
        "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\"," +
        "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-xk2ku%40book-lending-ms.iam.gserviceaccount.com\"," +
        "\"universe_domain\": \"googleapis.com\"" +
      "}";

    @Bean
    public DatabaseReference firebaseDatabaseReference() throws IOException {
        initializeFirebase();
        return FirebaseDatabase.getInstance().getReference();
    }

    private void initializeFirebase() throws IOException {

        FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebasePrivateKeyJson.getBytes())))
                    .setDatabaseUrl("https://book-lending-ms-default-rtdb.firebaseio.com/")
                    .build();

        FirebaseApp.initializeApp(options);
    }
}

