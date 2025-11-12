package HCMUT.TutorSytem.service;

import HCMUT.TutorSytem.payload.request.LoginRequest;

public interface HcmutSsoService {
    String validateUser(LoginRequest loginRequest);
}
