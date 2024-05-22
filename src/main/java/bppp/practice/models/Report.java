package bppp.practice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {
   private String photo;
   private String name;
   private int count;
   private Double cost;
   private Double sum;
   private Double ood;
   private int stockCount;
}
