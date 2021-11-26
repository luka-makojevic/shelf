package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.TokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.repository.TokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.util.TokenGenerator;
import com.htec.shelfserver.util.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    private final TokenRepository confirmationTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final UserValidator userValidator;

    private final String emailVerificationLink;

    public RegisterService(UserRepository userRepository,
                           TokenRepository confirmationTokenRepository,
                           TokenGenerator tokenGenerator, BCryptPasswordEncoder bCryptPasswordEncoder,
                           EmailService emailService, UserValidator userValidator,
                           @Value("${emailVerificationLink}") String emailVerificationLink) {

        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.emailVerificationLink = emailVerificationLink;

    }


    public void registerUser(UserDTO userDTO) {

        userRepository.findByEmail(userDTO.getEmail()).ifPresent(
                userEntity -> {
                    throw ExceptionSupplier.recordAlreadyExists.get();
                });

        userValidator.isUserValid(userDTO);

        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(userDTO);

        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setEmailVerified(false);
        userEntity.setRole(new RoleEntity(3L));

        String salt = tokenGenerator.generateSalt(8);
        userEntity.setSalt(salt);

        String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword() + salt);
        userEntity.setPassword(encryptedPassword);

        UserEntity storedUser = userRepository.save(userEntity);
        createAndSendToken(storedUser);

    }

    public void registerUserMicrosoft(String bearerToken) {


        RestTemplate restTemplate = new RestTemplate();
        String url = "https://graph.microsoft.com/v1.0/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJub25jZSI6InVkdi1yN3ZZYUJBbGZTS2s1T0pLa3RVUHdWbDFHUHY1RDk5bExwbW8xWm8iLCJhbGciOiJSUzI1NiIsIng1dCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC85Zjg1NjY1Yi03ZWZkLTQ3NzYtOWRmZS1iNmJmZGEyNTY1ZWUvIiwiaWF0IjoxNjM3OTIxODQzLCJuYmYiOjE2Mzc5MjE4NDMsImV4cCI6MTYzNzkyNjUxMywiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkUyWmdZQ2lvNUhQWDJCblVkdjZQc1dxcGNmT2FEdm5rOTh6N3RoVzhOWjhzbi9vcnhBRUEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Ik9mZmljZUhvbWUiLCJhcHBpZCI6IjQ3NjU0NDViLTMyYzYtNDliMC04M2U2LTFkOTM3NjUyNzZjYSIsImFwcGlkYWNyIjoiMiIsImZhbWlseV9uYW1lIjoiTWFrb2pldmljIiwiZ2l2ZW5fbmFtZSI6Ikx1a2EiLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiI4Mi4xMTcuMjE3LjIyMiIsIm5hbWUiOiJMdWthIE1ha29qZXZpYyIsIm9pZCI6IjRlNGJkMWMxLWYzODQtNDJiZS05M2IxLTMyODBjMjhhODdlYyIsInBsYXRmIjoiOCIsInB1aWQiOiIxMDAzMjAwMUEwRkI1NDM4IiwicmgiOiIwLkFVNEFXMmFGbl8xLWRrZWRfcmFfMmlWbDdsdEVaVWZHTXJCSmctWWRrM1pTZHNwT0FMVS4iLCJzY3AiOiJlbWFpbCBGaWxlcy5SZWFkV3JpdGUuQWxsIE5vdGVzLkNyZWF0ZSBvcGVuaWQgUGVvcGxlLlJlYWQgUHJlc2VuY2UuUmVhZC5BbGwgcHJvZmlsZSBTaXRlcy5SZWFkLkFsbCBUYXNrcy5SZWFkV3JpdGUgVXNlci5SZWFkQmFzaWMuQWxsIiwic3ViIjoiRUhUcHNLcnI2RVdJSlA5RUloaWpZXzFTdmVuTnZsdmQ3VXpvSmV3eG10YyIsInRlbmFudF9yZWdpb25fc2NvcGUiOiJFVSIsInRpZCI6IjlmODU2NjViLTdlZmQtNDc3Ni05ZGZlLWI2YmZkYTI1NjVlZSIsInVuaXF1ZV9uYW1lIjoibHVrYS5tYWtvamV2aWNAaHRlY2dyb3VwLmNvbSIsInVwbiI6Imx1a2EubWFrb2pldmljQGh0ZWNncm91cC5jb20iLCJ1dGkiOiJoOVFkenhZU2RVdTNaNVFzeEdzVEFRIiwidmVyIjoiMS4wIiwid2lkcyI6WyJiNzlmYmY0ZC0zZWY5LTQ2ODktODE0My03NmIxOTRlODU1MDkiXSwieG1zX3N0Ijp7InN1YiI6InA2QjdwNjhOcER5TE14OTdHQlJvVkJraHJRdWh1VkVkUXdLRWlXQ1UxaG8ifSwieG1zX3RjZHQiOjE2MzQwMzk0NjF9.TtNhwRuT7RKH7nshPN5vb13jEdVDAvnElMViEmqBuXPVGE0ykjVsNaUO06vrTGy9aKP0MjmeJDyldHwsslqGOGhgfn5OYXnVvLFnjooP5-uUmjw1uqZePWPavE3WZuwtKFnR8Olr2o7g6YDUe8W0rzCObEieOS_MYI6iuUnoC18h85RCaupqtJAaSA30ESM84UMxucA3l-ABhRUUBgQq7Ah3mQzBM9fltWe_1HeBB2XJy01APsMsMwUOZYxa0xZ_nXiIvzN59807J_uy08FUgDmvlb89L_uHYDJbcgw5bXrBBO1rKX0zLvPB4MEmkL1RKfkJRxzJetDZdjmUELLddQ");


        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        System.out.println(response);
        //getForEntity( url, request , String.class );


//            URL url = new URL("https://graph.microsoft.com/v1.0/me");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//            con.setRequestMethod("GET");
//
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJub25jZSI6IkY0bEw3UnNSNENZNVNvd0pUWklIa09lV1dFWDFMMEw2Nml3V3dyVFFrZGsiLCJhbGciOiJSUzI1NiIsIng1dCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCIsImtpZCI6Imwzc1EtNTBjQ0g0eEJWWkxIVEd3blNSNzY4MCJ9.eyJhdWQiOiJodHRwczovL3N1YnN0cmF0ZS5vZmZpY2UuY29tIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvOWY4NTY2NWItN2VmZC00Nzc2LTlkZmUtYjZiZmRhMjU2NWVlLyIsImlhdCI6MTYzNzkxODEzMywibmJmIjoxNjM3OTE4MTMzLCJleHAiOjE2Mzc5MjI1MjUsImFjY3QiOjAsImFjciI6IjEiLCJhaW8iOiJBVFFBeS84VEFBQUFCdmhURG1Ic1p4Rmg3dXJrUE9QMUhEVVFDbGo4Qk5RWThFQkxMMytpaFY4MlVuWVI5eEhWN21YVXJPcit5cjNHIiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjQ3NjU0NDViLTMyYzYtNDliMC04M2U2LTFkOTM3NjUyNzZjYSIsImFwcGlkYWNyIjoiMiIsImZhbWlseV9uYW1lIjoiTWFrb2pldmljIiwiZ2l2ZW5fbmFtZSI6Ikx1a2EiLCJpcGFkZHIiOiI4Mi4xMTcuMjE3LjIyMiIsIm5hbWUiOiJMdWthIE1ha29qZXZpYyIsIm9pZCI6IjRlNGJkMWMxLWYzODQtNDJiZS05M2IxLTMyODBjMjhhODdlYyIsInB1aWQiOiIxMDAzMjAwMUEwRkI1NDM4IiwicmgiOiIwLkFVNEFXMmFGbl8xLWRrZWRfcmFfMmlWbDdsdEVaVWZHTXJCSmctWWRrM1pTZHNwT0FMVS4iLCJzY3AiOiJBY3Rpdml0eUZlZWQtSW50ZXJuYWwuUmVhZFdyaXRlIEZpbGVzLlJlYWQgRmlsZXMuUmVhZFdyaXRlIEZpbGVzLlJlYWRXcml0ZS5TaGFyZWQgTm90ZXMuUmVhZFdyaXRlIE9mZmljZUZlZWQtSW50ZXJuYWwuUmVhZFdyaXRlIFBlb3BsZVByZWRpY3Rpb25zLUludGVybmFsLlJlYWQgUm9hbWluZ1VzZXJTZXR0aW5ncy5SZWFkV3JpdGUgU3Vic3RyYXRlU2VhcmNoLUludGVybmFsLlJlYWRXcml0ZSIsInN1YiI6Ino0SUxxVGN3WVJSVDRLelR4Sl9KWnNvM1N1X29pNjFMeE80Wm1PR25Jc3MiLCJ0aWQiOiI5Zjg1NjY1Yi03ZWZkLTQ3NzYtOWRmZS1iNmJmZGEyNTY1ZWUiLCJ1bmlxdWVfbmFtZSI6Imx1a2EubWFrb2pldmljQGh0ZWNncm91cC5jb20iLCJ1cG4iOiJsdWthLm1ha29qZXZpY0BodGVjZ3JvdXAuY29tIiwidXRpIjoiWl8xci1tWEc2ay1DZ21LZGNTM3pBZyIsInZlciI6IjEuMCIsIndpZHMiOlsiYjc5ZmJmNGQtM2VmOS00Njg5LTgxNDMtNzZiMTk0ZTg1NTA5Il19.FxJ4q9kNC_RSVa0QOPM5gNgo6Hqa7sXvAkX2QYSy25mUVZmHzItv4scmu7sbPH1aXbljX0zYoTEL3kvD0JEtHwDEmP-jqMkndQochXv6bjYY_dLc0iL0Jo8nOBl1NKtN9FK3sA5rftxc630-eDCE47Uwgxv732B8_Vc6pWpZk2Khf_pSdJrW7TCH6kOIslezoMBJmXv4St1R6syLvtLabf6IUPSoq3oJMZdjc_6BgBQxQaUaxUzhf8i4ZTGDUunzV0jUCfKadqbF6t08k8yZutsOVeEoi4txet4G_my7zb9qCvUvd3m__CLsY1DPldokytH6YIXqptzMYodUNgqhug");
//
//            System.out.println("Connection :: " + con.getHeaderField(2).toString());
//
//            con.setDoOutput(true);
//
//            int responseCode = con.getResponseCode();
//
//            System.out.println("GET Response Code :: " + responseCode);
//
//            if (responseCode == HttpURLConnection.HTTP_OK) { // success
//                BufferedReader in = new BufferedReader(new InputStreamReader(
//                        con.getInputStream()));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                // print result
//                System.out.println(response.toString());
//            } else {
//                System.out.println("GET request not worked");
//            }


    }

    void createAndSendToken(UserEntity userEntity) {
        String token = tokenGenerator.generateConfirmationToken(userEntity.getId());

        TokenEntity confirmationToken = new TokenEntity(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userEntity
        );

        confirmationTokenRepository.save(confirmationToken);

        String confirmationLink = emailVerificationLink + token;

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", userEntity.getFirstName());
        model.put("confirmationLink", confirmationLink);

        emailService.sendEmail(userEntity.getEmail(), model, "email-confirmation.html", "Confirm your email");
    }
}
