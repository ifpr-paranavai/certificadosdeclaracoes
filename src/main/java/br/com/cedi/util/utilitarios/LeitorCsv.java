package br.com.cedi.util.utilitarios;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import org.apache.commons.csv.CSVRecord;
import org.primefaces.model.UploadedFile;

import br.com.cedi.model.Pessoa;
import net.sf.jasperreports.engine.virtualization.RecordedValuesSerializer;


    public class LeitorCsv {


    	List<Pessoa> pessoas;
        Pessoa pessoa;



         public String ler(UploadedFile f) {
        	 this.pessoa = new Pessoa();
     		 this.pessoas = new ArrayList<>();

        	 	String texto= "";
        	 	List<List<String>> records = new ArrayList<>();




        	 		InputStream is = new ByteArrayInputStream(f.getContents());



       		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
       		    String line;
       		    while ((line = br.readLine()) != null) {
       		        String[] values = line.split(",");
       		        records.add(Arrays.asList(values));
       		    }
       		} catch (FileNotFoundException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		} catch (IOException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
       		for (int i = 0; i < records.size(); i++) {

					pessoa.setNome(records.get(i).get(0) + ",");
					pessoa.setCpf(records.get(i).get(1)+ ",");
					pessoa.setEmail(records.get(i).get(2)+ ",");
					pessoa.setMatricula(records.get(i).get(3)+ ";");
					System.out.println(pessoa.getNome() + pessoa.getCpf() +  pessoa.getEmail() + pessoa.getMatricula());
					pessoas.add(pessoa);
					texto += pessoa.getNome() + pessoa.getCpf() +  pessoa.getEmail() + pessoa.getMatricula();

			}

            	 /**
            	  * 
            	  * Outra forma de printar 
            	  * try {
            	CSVParser parser = CSVFormat.newFormat(',').parse(
            		    new InputStreamReader(new ByteArrayInputStream(f.getContents()), "UTF8"));
            		//CSVPrinter printer = CSVFormat.newFormat(',').print;
            		for (CSVRecord record : parser) {
            		 
            			  
            			  for (int i = 0; i < record.size(); i++) {
            				  texto+= record.get(0);
            				  
						}
            			  System.out.println(texto);
            			  
            			  
            			  
            		    //printer.printRecord(record);
            		 
            		}
            		parser.close();
            		//printer.close();
            	 } catch (Exception e) {
//         		    throw new RuntimeException("Error at line "
//         		      + parser.getCurrentLineNumber(), e);
         		  }
				return texto;*/
			return texto;
            }

}
