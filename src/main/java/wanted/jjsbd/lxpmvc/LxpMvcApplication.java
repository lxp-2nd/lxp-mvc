package wanted.jjsbd.lxpmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 🔥 이 어노테이션이 있어야 @CreatedDate가 작동합니다!
@SpringBootApplication
public class LxpMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(LxpMvcApplication.class, args);
    }

}
