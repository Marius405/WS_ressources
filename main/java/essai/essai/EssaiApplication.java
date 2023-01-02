package essai.essai;

import Model.Error;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Model.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@RestController
public class EssaiApplication {

	public static void main(String[] args) {

		SpringApplication.run(EssaiApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry){
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
			}
		};
	}
	@GetMapping("/hello/{id}")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name, @PathVariable String id) {
		return String.format("Hello %s! "+id, name);
	}
	@GetMapping("/date")
	public String getDate() {
		SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date=new Date();
		return String.format(formatter.format(date));
	}

	@GetMapping("/historique")
	public Return get_historique(){
        Return r=null;

		ArrayList<Historique> val=null;
		try{
			val=new Admin().get_historique();
            Data data=new Data();
            data.setData(val);
            r=data;
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			Error er=new Error();
			er.setMessage(e.getMessage());
			r=er;
		}


		return r;
	}
	@PostMapping("/montant")
	public void insert_montant(@RequestParam String name, @RequestParam int montant) {
		try{
			new Admin().insert_historique(name, montant);
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();

		}
	}
	@GetMapping("/montant")
	public Return get_montant(){
		Return r=null;

		Double val=null;
		try{
			val=new Admin().get_montant();
			Data data=new Data();
			data.setData(val);
			r=data;
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			Error er=new Error();
			er.setMessage(e.getMessage());
			r=er;
		}


		return r;
	}
	@GetMapping("/historique/{date}")
	public Return get_historique (@PathVariable Date date){
		Return r=null;
		ArrayList<Historique> val=null;
		try{
			val=new Admin().get_historique(date);
			Data data=new Data();
			data.setData(val);
			r=data;
		}catch(Exception e){
			System.out.println(e.getMessage());
			Error er=new Error();
			er.setMessage("Exception");
			r=er;
		}

		return r;
	}

}


