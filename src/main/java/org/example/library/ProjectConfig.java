package org.example.library;

import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

  @Bean
  public Book book() {
    return new Book();
  }

  @Bean
  public Reader reader(){
    return new Reader();
  }

  @Bean
  public Record record(){
    return new Record(LocalDate.now().plusWeeks(2), book(), reader());
  }
  
}
