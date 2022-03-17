package sg.edu.nus.iss.CalculatorMock.RestController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
@RequestMapping("")
public class CalculatorRestController {

    @PostMapping(path ="/calculate", consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> getResult(@RequestBody String payload, @RequestHeader("User-Agent") String userAgent){
        JsonObject body;
        try(InputStream is = new ByteArrayInputStream(payload.getBytes())){
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        }catch(Exception ex){
            body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }
        System.out.println(">>>>>> payload:" + payload);
        System.out.println(">>>>>> body:" + body);

        int x = body.getInt("oper1");
        int y = body.getInt("oper2");
        String operand = body.getString("ops");

        int result = 0;

        switch(operand){
            case "plus":
                result = x + y;
            case "minus":
                result = x - y;
            case "multiply":
                result = x * y;
            case "divide":
                result = x / y;
            break;
        }

        JsonObject showResult = Json.createObjectBuilder()
            .add("result", result)
            .add("timestamp", (new Date().toString())) //System.currentTimeMillis()
            .add("userAgent", userAgent)
            .build();

        return ResponseEntity.ok(showResult.toString());
    }
}
