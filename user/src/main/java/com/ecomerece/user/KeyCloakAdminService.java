package com.ecomerece.user;

import com.ecomerece.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakAdminService {


    @Value("${keycloak.admin.username}")
    private String adminUserName;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.server-url}")
    private String keyCloakServerUrl;

    @Value("${keycloak.admin.client-id}")
    private String clientId;


    @Value("${keycloak.admin.client-uid}")
    private String clientUIID;

    private final RestTemplate restTemplate = new RestTemplate();


    public String getAdminAccessToken(){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","password");
        params.add("client_id",clientId);
        params.add("username", adminUserName);
        params.add("password", adminPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params,headers);

        String url = keyCloakServerUrl + "/realms/"+realm +"/protocol/openid-connect/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(url,
                request, Map.class
                );
        assert response.getBody() != null;
        return response.getBody().get("access_token").toString();
    }
    public String   createUser(String token, UserRequest userRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String,Object> userPayload = new HashMap<>();
        userPayload.put("username",userRequest.getUsername());
        userPayload.put("email",userRequest.getEmail());
        userPayload.put("enabled", true);
        userPayload.put("firstName",userRequest.getFirstName());
        userPayload.put("lastName",userRequest.getLastName());
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", userRequest.getPassword());
        credentials.put("temporary", false);
        userPayload.put("credentials", List.of(credentials));


        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(userPayload, headers);

        String url = keyCloakServerUrl + "/admin/realms/"+ realm + "/users";
        ResponseEntity<String>  response = restTemplate.postForEntity(
            url,httpEntity,String.class
        );

        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            throw  new RuntimeException("The creation of user fialed"+ response.getBody());
        }

        URI location = response.getHeaders().getLocation();
        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);

    }

    public Map<String, Object> getRealmRepresentation(String token, String roleName){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = keyCloakServerUrl + "/admin/realms/"+realm+"/clients/"+clientUIID+"/roles/"+roleName;
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return responseEntity.getBody();
    }

    public void assignRealmRoleToUser(String username, String roleName, String userId){
        String token = getAdminAccessToken();
        Map<String, Object> roleRe = getRealmRepresentation(token, roleName);
        System.out.println(roleRe.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<List<Map<String,Object>>> entity = new HttpEntity<>(List.of(roleRe), headers);
        String url = keyCloakServerUrl
                +"/admin/realms/"
                + realm +"/users/"
                +userId+"/role-mappings/clients/"
                +clientUIID;
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("Faild to assign role:  "+ roleName + " to "+ username + " HTTP: "+ response.getStatusCode());
        }

    }
}
