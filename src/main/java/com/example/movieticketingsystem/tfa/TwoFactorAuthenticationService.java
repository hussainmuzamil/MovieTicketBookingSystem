package com.example.movieticketingsystem.tfa;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwoFactorAuthenticationService {
    public String generateNewSecret(){
        return new DefaultSecretGenerator(64).generate();
    }
//    public String generateQrCodeImageUri(String secret){
//        QrData data = new QrData.Builder()
//                .label("Movie Ticketing System")
//                .secret(secret)
//                .issuer("Movie Ticketing System")
//                .algorithm(HashingAlgorithm.SHA1) // More on this below
//                .digits(6)
//                .period(30)
//                .build();
//
//        QrGenerator generator = new ZxingPngQrGenerator();
//        byte[] imageData = new byte[0];
//        try{
//            imageData = generator.generate(data);
//        }catch (QrGenerationException exception){
//            exception.printStackTrace();
//            log.error("Unable to generate imageData {}",imageData,exception);
//        }
//        return Utils.getDataUriForImage(imageData,generator.getImageMimeType());
//    }
    public boolean isValidOTP(String secret,String otp){
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        DefaultCodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator,timeProvider);
        codeVerifier.setTimePeriod(30);
        codeVerifier.setAllowedTimePeriodDiscrepancy(3);
        boolean isSuccessful = codeVerifier.isValidCode(secret,otp);
        return isSuccessful;
    }
    public boolean isOtpNotValid(String secret,String otp){
        return !isValidOTP(secret,otp);
    }
}
