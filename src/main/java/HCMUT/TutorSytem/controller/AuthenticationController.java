package HCMUT.TutorSytem.controller;

import HCMUT.TutorSytem.model.HcmutSso;
import HCMUT.TutorSytem.payload.request.LoginRequest;
import HCMUT.TutorSytem.payload.response.BaseResponse;
import HCMUT.TutorSytem.service.HcmutSsoService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private HcmutSsoService hcmutSsoService;

//    @GetMapping("/key")
//    public String getKey(){
//        SecretKey key = Jwts.SIG.HS256.key().build();
//        return Encoders.BASE64.encode(key.getEncoded());
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = hcmutSsoService.validateUser(loginRequest);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(token != null ? 200 : 401);
        response.setMessage(token != null ? "Login successful" : "Invalid credentials");
        response.setData(token);
        return ResponseEntity.ok(response);
    }
}
