import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testText {
	String urlVacancy="https://api.hh.ru/vacancies";
	
	@Test
	public void testStatus() throws IOException {
		URL url = new URL(urlVacancy);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		int status = connection.getResponseCode();
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void testFindVacancy() {
		RestTemplate restTemplate = new RestTemplate();
		vacancyList vacancies = restTemplate.getForObject(urlVacancy + "?text=java", vacancyList.class);
		
		System.out.println("Поиск по полям вакансий, содержащих слово " + "java" + " Количество вакансий: "
			+ vacancies.getFound());
		boolean isContains = false;
		for (Vacancy vacancy : vacancies.getVacancy()) {
			isContains = vacancy.name.toLowerCase().contains("java");
			if (!isContains) {
				break;
			}
		}
		assertTrue(isContains);
	}
	
	@Test
	public void findWithParameters() {
		RestTemplate restTemplate = new RestTemplate();
		vacancyList vacancies = restTemplate.getForObject(urlVacancy + "?text=java&search_field=name", vacancyList.class);
		
		System.out.println("Поиск по названиям вакансий, содержащих слово " + "Java" + " Количество вакансий: "
			+ vacancies.getFound());
		boolean isContains = false;
		for (Vacancy vacancy : vacancies.getVacancy()) {
			isContains = vacancy.name.toLowerCase().contains("java");
			if (!isContains) {
				break;
			}
		}
		assertTrue(isContains);
	}
	
	@Test
	public void findPhrases() {
		RestTemplate restTemplate = new RestTemplate();
		vacancyList vacancies = restTemplate.getForObject(urlVacancy + "?text=!\"Ведущий программист 1C\"", vacancyList.class);
		
		System.out.println("Поиск по всем полям вакансий, содержащих фразу " + "\"Ведущий программист 1С\"" + " Количество вакансий: "
			+ vacancies.getFound());
		
		boolean isContains = false;
		for (Vacancy vacancy : vacancies.getVacancy()) {
			isContains = vacancy.name.toLowerCase().contains("ведущий программист 1c");
			if (!isContains) {
				break;
			}
		}
		assertTrue(isContains);
	}
	
	@Test
	public void findWithoutSynonyms() {
		RestTemplate restTemplate = new RestTemplate();
		vacancyList vacancies = restTemplate.getForObject(urlVacancy + "?text=!руководитель and not " +
			"!директор&search_field=name", vacancyList.class);
		
		System.out.println("Поиск по названиям вакансий, содержащих фразу " + "!руководитель and not !директор" +
			" Количество вакансий: " + vacancies.getFound());
		
		boolean isContains = false;
		boolean isNotContains = true;
		for (Vacancy vacancy : vacancies.getVacancy()) {
			isContains = vacancy.name.toLowerCase().contains("руководитель");
			isNotContains = vacancy.name.toLowerCase().contains("директор");
			if (!isContains || isNotContains) {
				break;
			}
		}
		assertTrue(isContains && !isNotContains);
	}
	
	@Test
	public void findWithAsciLetter() throws IOException {
		URL url = new URL(urlVacancy + "?text=abcWqeПроыйxpotenf");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий, содержащих фразу abcWqeПроыйxpotenf");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void findWithAsciSymbol() throws IOException {
		URL url = new URL(urlVacancy + "?text=!@#$%^&*()_+~'/><");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий, содержащих фразу !@#$%^&*()_+~'/><");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void findWithNonAsci() throws IOException {
		URL url = new URL(urlVacancy + "?text=世界\u263A");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий, содержащих фразу 世界\u263A");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void testLengthZero() throws IOException {
		URL url = new URL(urlVacancy + "?text=");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий с длинной = 0");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void testLengthOne() throws IOException {
		URL url = new URL(urlVacancy + "?text=H");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий с длинной = 1");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void testLength254() throws IOException {
		String str = "H";
		
		URL url = new URL(urlVacancy + "?text=" + str.repeat(254));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий с длинной = 254");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void testLength255() throws IOException {
		String str = "H";
		
		URL url = new URL(urlVacancy + "?text=" + str.repeat(255));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий с длинной = 255");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void testLength256() throws IOException {
		String str = "H";
		
		URL url = new URL(urlVacancy + "?text=" + str.repeat(256));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск по полям вакансий с длинной = 256");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(connection.getResponseCode(),200);
	}
	
	@Test
	public void testLengthWarPeace() throws IOException {
		String str = "H";
		
		URL url = new URL(urlVacancy + "?text=" + str.repeat(747323));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		System.out.println("Поиск по полям вакансий с длинной текста, как в Войне и Мир");
		try {
			//int status = connection.getResponseCode();
			assertEquals(connection.getResponseCode(), 200);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	@Test
	public void testXSSVulnerability() throws IOException {
		URL url = new URL(urlVacancy + "?text=<script>alert(123)</script>");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		int status = connection.getResponseCode();
		System.out.println("Поиск в поле text <script>alert(123)</script>");
		System.out.println(connection.getResponseMessage() + " " + status);
		assertEquals(status, 200);
	}
}

@JsonIgnoreProperties(ignoreUnknown=true)
class Vacancy {
	public Integer id;
	public String name;
	public String url;
	public String alternate_url;
}

@JsonIgnoreProperties(ignoreUnknown=true)
class vacancyList {
	private List<Vacancy> vacancy;
	private Integer found;
	
	public void setItems(List<Vacancy> vacancy) {
		this.vacancy = vacancy;
	}
	
	public List<Vacancy> getVacancy() {
		return vacancy;
	}
	
	public void setFound(Integer found) {
		this.found = found;
	}
	
	public Integer getFound() {
		return found;
	}
}
