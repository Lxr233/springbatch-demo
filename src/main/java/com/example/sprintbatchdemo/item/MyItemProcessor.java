package com.example.sprintbatchdemo.item;

import com.example.sprintbatchdemo.dto.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

/**
 * @description:
 * @author: l00427576
 * @create: 2019-05-20 17:56
 **/
@Slf4j
public class MyItemProcessor  implements ItemProcessor<Person,Person> {
    @Override
    public Person process(Person item){
        log.info("process {}",item.getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        item.setId(item.getId()+1);
        return item;
    }
}
