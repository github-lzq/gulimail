package com.atguigu.gmall.search;

import io.searchbox.client.JestClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchApplicationTests {
    @Autowired
    private JestClient jestClient;


    @Test
    public void contextLoads() {

    }

}
@Data
@AllArgsConstructor
@NoArgsConstructor
class User{
    private String name;
    private Integer age;
    private String password;
}
