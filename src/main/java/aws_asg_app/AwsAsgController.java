package aws_asg_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsAsgController {

    @GetMapping("/health")
    public String health() {
        return "Application is Healthy";
    }
}