package controlador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hpsf.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Vaica {
	
	private String localizacionExcel;
	XSSFWorkbook  workbook;
    XSSFSheet     sheet3;
    XSSFSheet     sheet2;   
    XSSFSheet     sheet1;
	
	public Vaica(String source) throws FileNotFoundException, IOException {
		super();
		this.localizacionExcel = source;
	    workbook = new XSSFWorkbook(new FileInputStream(localizacionExcel));
	    sheet3    = workbook.getSheetAt(2);
	    sheet2    = workbook.getSheetAt(1);
	    sheet1    = workbook.getSheetAt(0);
	}
	
	public void generador(String target) {
		
		Iterator<Row> rowIterator = sheet3.iterator();
		for(Row row : sheet3) {
			if(row.getCell(0) != null) {
				if(!(row.getCell(2).getCellType().toString().equals("Categoria"))){
					String name = row.getCell(6).toString();
					if(!name.equals("Nombre")) {
					String surname = row.getCell(4).toString();
					if(row.getCell(5) != null) {
						surname = surname + " "+row.getCell(5).toString(); 
					}
					String prorateoS = row.getCell(8).toString();
					
					boolean prorateo = false;
					if(prorateoS.equals("SI")) {
						prorateo = true;
					}
					Cell cell = row.getCell(3);
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			        java.util.Date date = cell.getDateCellValue();
			        String cellValue = df.format(date);
					String categoria = row.getCell(2).toString();
					String spliter[] = cellValue.split("/");
					String targeter[] = target.split("/");
					boolean apto = true;
					if(Integer.valueOf(spliter[2]) > Integer.valueOf(targeter[1])) {
						apto = false;
					}else if(Integer.valueOf(spliter[2]) == Integer.valueOf(targeter[1]) && Integer.valueOf(spliter[0]) >= Integer.valueOf(targeter[0])) {
						apto = false;
					}
					if(!apto) {
					System.out.println("-------------");
					System.out.println("Aun no tiene nomina");
					System.out.println(name + " "+surname);
					System.out.println("Fecha de alta: "+cellValue);
					System.out.println("DNI:" + row.getCell(7).toString());
					System.out.println("-------------");
					}else{
									
					System.out.println("-------------");
					System.out.println(name + " "+surname);
					System.out.println("Fecha de alta: "+cellValue);
					System.out.println("DNI:" + row.getCell(7).toString());
					System.out.println("Categoria: "+categoria);
					System.out.println("IBAN: "+row.getCell(11).toString());
					System.out.println("Prorrateo: "+ (prorateo==true ? "SI":"NO"));
					calculadorNonima(target,cellValue, categoria,prorateo);
					System.out.println("-------------");
					}
					}
					
					
				}
				
				
			}
			
		}
		
		//calculadorNonima(target,"1/1/2008", "Cuidador",false);

			
		
	}
	
	public void calculadorNonima(String target, String fechaAlta,String categoria,boolean prorata){

    
	double salario = this.salarioBase(categoria,sheet1);
	double complemento = this.complementos(categoria,sheet1);
	this.salarioBruto(salario, complemento,fechaAlta, target,prorata);
		
	}
	
	
	
	public void salarioBruto(double salario, double complemento, String alta, String actual, boolean ProRata) {
	
	
	
	double SalarioComplementos = salario + complemento;
	ArrayList<Double> cuotas = this.cuotas(sheet2);
	double cuotaGeneralTrabajador = cuotas.get(0);
	double cuotaDesempleoTrabajador = cuotas.get(1);
	double cuotaFormacionTrabajador = cuotas.get(2);
	double ContingenciasComunes = cuotas.get(3);
	double FogasaEmpresario = cuotas.get(4);
	double DesempleoEmpresario = cuotas.get(5);
	double FormacionEmpresario = cuotas.get(6);
	double AccidentesTrabajoEmpresario = cuotas.get(7);


	
	
	String strA[] = alta.split("/");
	int monthA = Integer.valueOf(strA[0]);
	int yearA = Integer.valueOf(strA[2]);
	String strO[] = actual.split("/");
	int monthO = Integer.valueOf(strO[0]);
	int mes = monthO;
	int yearO = Integer.valueOf(strO[1]);
	int mesesJunio = 0;
	int mesesDiciembre = 0;
	int mesesYear = 0;
	while(!(yearO == yearA && monthO == monthA)) {
		if(yearA  == yearO - 1 && monthA == 12) {
			mesesJunio++;
		}else if(yearA == yearO && monthA <6) {
			mesesJunio++;
		}else if(yearA == yearO && monthA >=6 && monthA<12){
			mesesDiciembre++;
		}
		
		if(yearO == yearA || (yearO-1 == yearA && monthA == 12)) {
			mesesYear++;
		}
			
		
		if(monthA == 12) {
			yearA++;
			monthA = 1;
		}else {
			monthA++;
		}
	}
	//12
	double sumaTrienios = this.sumatrienios(alta, actual);
	sumaTrienios = round(sumaTrienios,2);
	SalarioComplementos += sumaTrienios;
	SalarioComplementos = round(SalarioComplementos,2);
	double SalarioBase = salario / 14.0;
	SalarioBase = round(SalarioBase,2);
	double complementos = complemento / 14.0;
	complementos = round(complementos,2);
	double antiguedad = this.trienioActual(alta, actual);
	antiguedad = round(antiguedad,2);
	double prorrateoExtra = SalarioBase / 6.0 + complementos / 6.0 + trienioActualSoloProrrata(alta,actual) / 6.0; 
	String nuevoYear = "6/"+(yearO+1);
	if(monthO == 12 && trienioActual(alta,nuevoYear) != antiguedad) {
		prorrateoExtra = SalarioBase / 6.0 + complementos / 6.0 + trienioActual(alta,nuevoYear) / 6.0; 

	}
	prorrateoExtra = round(prorrateoExtra, 2);
	double BrutoM= SalarioBase + complementos + antiguedad + prorrateoExtra;
	BrutoM = round(BrutoM, 2);
	double SeguridadS = cuotaGeneralTrabajador * BrutoM;
	SeguridadS = round(SeguridadS,2);
	double Desem = cuotaDesempleoTrabajador * BrutoM;
	Desem = round(Desem, 2);
	double Forma= cuotaFormacionTrabajador * BrutoM;
	Forma = round(Forma,2 );
	double ir = this.buscadorIRPF(SalarioComplementos) / 100.0;
	double IR = ir *  BrutoM;
	IR = round (IR,2);
	double liquidoM = BrutoM - SeguridadS - Desem - Forma - IR;
	liquidoM = round(liquidoM,2);
	double SeguridadE = ContingenciasComunes * BrutoM;
	double DesemE = DesempleoEmpresario * BrutoM;
	double FogaE = FogasaEmpresario * BrutoM;
	double AcidentesE = AccidentesTrabajoEmpresario * BrutoM;
	double FormaE = FormacionEmpresario * BrutoM;
	double costE = SeguridadE + DesemE + FogaE + AcidentesE + FormaE + BrutoM;
	costE = round(costE,2);
	double totalE = SeguridadE + DesemE + FogaE + AcidentesE + FormaE;

	
	}
	//Si hay Diciembre
	if(ProRata) {
	//System.out.println(sumaTrienios);
	System.out.println("Salario Base: "+SalarioBase);
	System.out.println("Complemento: "+complementos);
	System.out.println("Antiguedad: "+antiguedad);
	System.out.println("Prorateo: "+prorrateoExtra);
	System.out.println("Total Devengos(Bruto Mensual): "+BrutoM);
	System.out.println("Desempleo: "+Desem);
	System.out.println("Contingencias generales: "+SeguridadS);
	System.out.println("Cuota Formacion: "+Forma);
	System.out.println("IRPF: "+IR);
	System.out.println("Total deduciones: " +(Desem + Forma + IR +SeguridadS) );
	System.out.println("Liquido a percivir: "+liquidoM);
	System.out.println("Calculo empresario BASE: "+BrutoM);
	System.out.println("Contingecias comunes empresario: "+SeguridadE);
	System.out.println("Desempleo empresario: "+DesemE);
	System.out.println("Formacion: "+FormaE);
	System.out.println("Accidentes de trabajo: "+AcidentesE);
	System.out.println("FOGASA: "+FogaE);
	System.out.println("Total empresario: "+totalE);
	System.out.println("Coste total trabajador: "+costE);
	}
		
	}
	

	
	}
	
	
	
	
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	double salarioBase(String target, XSSFSheet sheet) {
		
		double salario = 0;
		
		Iterator<Row> rowIterator = sheet.iterator();
		for(Row row : sheet) {
			
			if(row.getCell(0) != null) {
				
				if(row.getCell(0).toString().equals(target)){
					salario = Double.valueOf(row.getCell(1).toString());
					
				}
				
				
			}
			
		}
		
		
		
		return salario;
	}
	
	
double sumatrienios(String alta, String actual) {
	String strA[] = alta.split("/");
	int monthA = Integer.valueOf(strA[0]);
	int yearA = Integer.valueOf(strA[2]);
	String strO[] = actual.split("/");
	int monthO = Integer.valueOf(strO[0]);
	int yearO = Integer.valueOf(strO[1]);
	int trienios = 0;
	int meses = 0;
	double sumaTrienios = 0;
	int mesesTrabajados = 0;
	int yearEj = yearO + 1;
	
	while(!(yearEj == yearA && 1 == monthA)) {

		if(meses % 36 == 0 && meses != 0) {
			if((monthA == 6 && yearO == yearA) || (monthA == 12 && yearO == yearA)) {
				sumaTrienios += this.dineroTrienio(trienios, sheet1);
				sumaTrienios += this.dineroTrienio(trienios, sheet1);
			}else if(yearO == yearA){
				sumaTrienios += this.dineroTrienio(trienios, sheet1);	
			}
			trienios++;
			meses++;
		}else {
			if((monthA == 6 && yearO == yearA) || (monthA == 12 && yearO == yearA)) {
				sumaTrienios += this.dineroTrienio(trienios, sheet1);
				sumaTrienios += this.dineroTrienio(trienios, sheet1);
			}else if(yearO == yearA){
				sumaTrienios += this.dineroTrienio(trienios, sheet1);	
			}
			meses++;
		}
		
		if(yearA == yearO || (yearO-1 == yearA && monthA == 12)) {
			mesesTrabajados++;
		}
		
		
		if(monthA == 12) {
			yearA++;
			monthA = 1;
		}else {
			monthA++;
		}
	}
	return sumaTrienios;
}

double trienioActual(String alta, String actual) {
	String strA[] = alta.split("/");
	int monthA = Integer.valueOf(strA[0]);
	int yearA = Integer.valueOf(strA[2]);
	String strO[] = actual.split("/");
	int monthO = Integer.valueOf(strO[0]);
	int yearO = Integer.valueOf(strO[1]);
	int trienios = 0;
	int meses = 1;
	double sumaTrienios = 0;
	int mesesTrabajados = 0;
	int yearEj = yearO + 1;
	while(!(yearO == yearA && monthA >= monthO)) {
		if(meses % 36 == 0 && meses != 0) {
			sumaTrienios = this.dineroTrienio(trienios, sheet1);	
			trienios++;
		}else {
			sumaTrienios = this.dineroTrienio(trienios, sheet1);	
		}
		meses++;
		
		if(monthA == 12) {
			yearA++;
			monthA = 1;
		}else {
			monthA++;
		}
	}
	return sumaTrienios;
}

double trienioActualSoloProrrata(String alta, String actual) {
	String strA[] = alta.split("/");
	int monthA = Integer.valueOf(strA[0]);
	int yearA = Integer.valueOf(strA[2]);
	String strO[] = actual.split("/");
	int monthO = Integer.valueOf(strO[0]);
	int yearO = Integer.valueOf(strO[1]);
	int trienios = 0;
	int meses = 0;
	double sumaTrienios = 0;
	int mesesTrabajados = 0;
	int yearEj = yearO + 1;
	
	while(!(yearEj == yearA && 1 == monthA)) {

		if(meses % 36 == 0 && meses != 0) {
			if((monthA == 6 && yearO == yearA) || (monthA == 12 && yearO == yearA)) {
				sumaTrienios = this.dineroTrienio(trienios, sheet1);
			}else if(yearO == yearA){
				sumaTrienios = this.dineroTrienio(trienios, sheet1);	
			}
			trienios++;
			meses++;
		}else {
			if((monthA == 6 && yearO == yearA) || (monthA == 12 && yearO == yearA)) {
				sumaTrienios = this.dineroTrienio(trienios, sheet1);
			}else if(yearO == yearA){
				sumaTrienios = this.dineroTrienio(trienios, sheet1);	
			}
			meses++;
		}
		
		if(yearA == yearO || (yearO-1 == yearA && monthA == 12)) {
			mesesTrabajados++;
		}
		
		
		if(monthA == 12) {
			yearA++;
			monthA = 1;
		}else {
			monthA++;
		}
	}
	return sumaTrienios;
}

double complementos(String target, XSSFSheet sheet) {
		
		double complementos = 0;
		
		Iterator<Row> rowIterator = sheet.iterator();
		for(Row row : sheet) {
			
			if(row.getCell(0) != null) {
				
				if(row.getCell(0).toString().equals(target)){
					complementos =  Double.valueOf(row.getCell(2).toString());
					
				}
				
				
			}
			
		}
		
		
		
		return complementos;
	}
	
	
	double dineroTrienio(int trienios,XSSFSheet sheet) {
		
		
		double suma = 0;
		double trieniosS = Double.valueOf(String.valueOf(trienios));
		Iterator<Row> rowIterator = sheet.iterator();
		for(Row row : sheet) {
			if(row.getCell(5) != null) {
				if(row.getCell(5).toString().equals(String.valueOf(trieniosS))){
					suma =  Double.valueOf(row.getCell(6).toString());
					
				}
				
				
			}
			
		}

		return suma;
		
	}
	
	ArrayList<Double> cuotas(XSSFSheet sheet){
		
		ArrayList<Double> array = new ArrayList<Double>();
		
		Iterator<Row> rowIterator = sheet.iterator();
		for(Row row : sheet) {
			if(row.getCell(5) != null) {
				if(row.getCell(6).getCellType() == CellType.NUMERIC){
					array.add(Double.valueOf(row.getCell(6).toString()) / 100.0);
					
				}
				
				
			}
			
		}
		
		
		return array;
	}
	
	double buscadorIRPF(double target) {
		double irpf = 0.0;
		boolean entra = false;
		Iterator<Row> rowIterator = sheet2.iterator();
		for(Row row : sheet2) {
			if(row.getCell(0) != null) {
				if(row.getCell(0).getCellType() == CellType.NUMERIC && (target >= row.getCell(0).getNumericCellValue() && target < row.getCell(0).getNumericCellValue() + 1000.0) || entra == true){
				
					irpf = Double.valueOf(row.getCell(1).toString());
					entra = !entra;
					
				}
				
				
			}
			
		}
		
		return irpf;
	}
	
	
}
