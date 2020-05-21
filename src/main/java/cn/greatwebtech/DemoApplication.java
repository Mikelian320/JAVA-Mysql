package cn.greatwebtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableAutoConfiguration
@RestController
public class DemoApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList("SELECT sql_calc_found_rows SN,MAC FROM ml_switch WHERE Test_Station='总检' AND Test_Require='以管理板为主' AND Product_Model='S5750-24GT8SFP-P' ORDER BY Record_Time DESC limit 0,50");
        return result.toString();
    }

}