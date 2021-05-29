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
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
import java.io.File;
import modelo.Categorias;
import modelo.Empresas;
import modelo.Nomina;
import modelo.Trabajadorbbdd;

public class Vaica {
    
    //Cosas PDF
    private String NombreEmpresa = "";
    
    private String CIFEmpresa = "";
    
    private String Categoria = "";
    
    private String FechaDeAlta = "";
    
    private String DNI = "";
    
    private String IBAN = "";
    
    private String Nombre = "";
    
    private String Apellidos = "";
    
    private String year = "";
    
    private String mes = "";
    
    private String localizacionExcel;
    XSSFWorkbook  workbook;
    XSSFSheet     sheet3;
    XSSFSheet     sheet2;   
    XSSFSheet     sheet1;
	ArrayList<Empresas> arrayEmpresas = new ArrayList<Empresas>();
	ArrayList<Categorias> arrayCategorias = new ArrayList<Categorias>();
	Set<Trabajadorbbdd> arrayTrabajadores = new HashSet(0);
	int idNomina = 5000;
	
	public Vaica(String source) throws FileNotFoundException, IOException {
		super();
		this.localizacionExcel = source;
	    workbook = new XSSFWorkbook(new FileInputStream(localizacionExcel));
	    sheet3    = workbook.getSheetAt(2);
	    sheet2    = workbook.getSheetAt(1);
	    sheet1    = workbook.getSheetAt(0);
	}
	
	public void generador(String target) {
		int id = 1000;
		Iterator<Row> rowIterator = sheet3.iterator();
		try {
		this.crearEmpresa();
		this.crearCategoria();

		}catch(Exception e) {
			System.out.println("Categoria/Empresa");
			System.out.println(e.getMessage());
		}

		for(Row row : sheet3) {
			if(row.getCell(0) != null) {
				if(!(row.getCell(2).toString().equals("Categoria"))){
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
                                        
                                        this.year = targeter[1];
                                        this.mes = targeter[0];

					if((spliter[2].compareTo(targeter[1]) > 0)) {
						apto = false;
					}
					if((spliter[2].compareTo(targeter[1]) == 0) && Integer.valueOf(spliter[0]) >= Integer.valueOf(targeter[0])) {
						apto = false;
					}

					if(!apto) {
					System.out.println("------------------");
					System.out.println("--------"+id+"--------");
					System.out.println("------------------");

					System.out.println("Aun no tiene nomina");
					System.out.println(name + " "+surname);
					System.out.println("Fecha de alta: "+cellValue);
					System.out.println("DNI:" + row.getCell(7).toString());

					}else{
					//Categoria
					Categorias categorias = getCategoria(categoria);
					//Empresa
					Empresas empresas = getEmpresa((row.getCell(0).toString()));
	
					
					System.out.println("------------------");
					System.out.println("--------"+id+"--------");
					System.out.println("------------------");
					
					try {
					Set<Nomina> set =  new HashSet<Nomina>();
					String dni = "";
					if(row.getCell(7) != null) {
						dni = row.getCell(7).toString();
					}
					String surname2 = "";
					if(row.getCell(5) != null) {
						surname2 = row.getCell(5).toString();
					}
					Trabajadorbbdd trabajador = new Trabajadorbbdd(id, categorias, empresas, name, row.getCell(4).toString(), surname2,dni, row.getCell(12).toString(), row.getCell(3).getDateCellValue(), row.getCell(9).toString(), row.getCell(11).toString(), set);				
					trabajador.setProrrateo(prorateo);
                                        this.NombreEmpresa = empresas.getNombre();
					System.out.println("Nombre Empresa: "+empresas.getNombre());
                                        this.CIFEmpresa = empresas.getCif();
					System.out.println("CIF Empresa: "+empresas.getCif());
                                        this.Nombre = name;
                                        this.Apellidos = surname;
					System.out.println(name + " "+surname);
                                        this.Categoria = categoria;
					System.out.println("Categoria: "+categoria);
                                        this.FechaDeAlta = cellValue;
					System.out.println("Fecha de alta: "+cellValue);
                                        this.DNI = row.getCell(7).toString();
					System.out.println("DNI:" + row.getCell(7).toString());
                                        this.IBAN = row.getCell(11).toString();
					System.out.println("IBAN: "+row.getCell(11).toString());
					System.out.println("Prorrateo: "+ (prorateo==true ? "SI":"NO"));
					
					calculadorNonima(target,cellValue, categoria,prorateo, set,trabajador);
					this.arrayTrabajadores.add(trabajador);
					categorias.setTrabajadorbbdds(this.arrayTrabajadores);
					empresas.setTrabajadorbbdds(this.arrayTrabajadores);
					}catch(Exception e) {
					System.out.println(e.getStackTrace());
							}

						}
					}
					
					
				}
				
				id++;
			}
			
		}
			
		System.out.println("--FIN--");
	}
	
	public void crearEmpresa() {
		Iterator<Row> rowIterator = sheet3.iterator();
		int id = 66;
		for(Row row : sheet3) {
			if(row.getCell(0) != null) {
				if(!(row.getCell(2).getCellType().toString().equals("Categoria"))){
				if(row.getCell(0) != null && row.getCell(1)!= null) {
				if(!contains(row.getCell(0).toString())) {
				Set<Trabajadorbbdd> trabajador = new HashSet<Trabajadorbbdd>();
				Empresas empresa = new Empresas(id,row.getCell(0).toString(),row.getCell(1).toString(),trabajador);
				this.arrayEmpresas.add(empresa);
				id++;
				}
				}
				}
			}
				
			}

	}
	public boolean contains(String nombre) {
		for(Empresas empresa : this.arrayEmpresas) {
			if(empresa.getNombre().equals(nombre)) {
				return true;
			}
		}
		return false;
	}
	public void crearCategoria() {
		Iterator<Row> rowIterator = sheet1.iterator();
		int id = 225;
		for(Row row : sheet1) {
			
			if(row.getCell(0) != null && !(row.getCell(0).toString().equals("Categoria"))) {
				Set<Trabajadorbbdd> trabajador = new HashSet<Trabajadorbbdd>();
				Categorias categoria = new Categorias(id,row.getCell(0).toString(),Double.valueOf(row.getCell(1).toString()),Double.valueOf(row.getCell(2).toString()),trabajador); 
				this.arrayCategorias.add(categoria);
				id++;
				}
				
				
			}

	}
	public Categorias getCategoria(String categoria) {
		Categorias cat = null;
		for(Categorias caters : this.arrayCategorias) {
			if(caters.getNombreCategoria().equals(categoria)) {
				cat = caters;
				return cat;
			}
		}
		
		return cat;
	}
	public Empresas getEmpresa(String nombre) {
		Empresas emp = null;
		for(Empresas empresa : this.arrayEmpresas) {
			if(empresa.getNombre().equals(nombre)) {
				emp = empresa;
				return emp;
			}
		}
		return emp;
	}
	
	
	
	//Calcular nomina
	public void calculadorNonima(String target, String fechaAlta,String categoria,boolean prorata, Set nominas, Trabajadorbbdd trabajador){

    
	double salario = this.salarioBase(categoria,sheet1);
	double complemento = this.complementos(categoria,sheet1);
	this.salarioBruto(salario, complemento,fechaAlta, target,prorata, nominas,trabajador);
		
	}
	
	
	
	public void salarioBruto(double salario, double complemento, String alta, String actual, boolean ProRata, Set nominas,Trabajadorbbdd trabajador) {
	
	
	//Cotas
	double BrutoAnual = salario + complemento;
	ArrayList<Double> cuotas = this.cuotas(sheet2);
	double cuotaGeneralTrabajador = cuotas.get(0);
	double cuotaDesempleoTrabajador = cuotas.get(1);
	double cuotaFormacionTrabajador = cuotas.get(2);
	double ContingenciasComunes = cuotas.get(3);
	double FogasaEmpresario = cuotas.get(4);
	double DesempleoEmpresario = cuotas.get(5);
	double FormacionEmpresario = cuotas.get(6);
	double AccidentesTrabajoEmpresario = cuotas.get(7);
	double MesesFrom0 = 0.0;
	//Meses trabajados
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
	int mes1 = 0;
	int mes2 = 0;
	while(!(yearO == yearA && monthO == monthA)) {
		if(yearA  == yearO - 1 && monthA == 12) {
			mesesJunio++;
		}else if(yearA == yearO && monthA <6) {
			mesesJunio++;
		}else if(yearA == yearO && monthA >=6 && monthA<12){
			mesesDiciembre++;
		}
		//
		if(yearO == yearA || (yearO-1 == yearA && monthA == 12)) {
			mesesYear++;

		}
		if(yearO == yearA) {
			MesesFrom0++;
		}
		if (yearO-1 == yearA && monthA == 12){
			mes1++;
		}
		if(yearO == yearA && monthA < 6) {
			mes1++;
		}else if(yearO == yearA && monthA >= 6 && monthA <12) {
			mes2++;
		}
		
		if(monthA == 12) {
			yearA++;
			monthA = 1;
		}else {
			monthA++;
		}
	}
	

	
	double sumaTrieniosTotales = this.sumatrienios(alta, actual);
	sumaTrieniosTotales = round(sumaTrieniosTotales,2);
	BrutoAnual += this.brutoAnual(alta, actual, ProRata);
	BrutoAnual = round(BrutoAnual,2);
	monthA = Integer.valueOf(strA[0]);
	yearA = Integer.valueOf(strA[2]);
	double sumaTrienios = BrutoAnual;
	int months = 0;
	int yearEj = yearO + 1;

	while(!(yearEj == yearA && 1 == monthA)) {
		if(yearA == yearO) {
		months++;
		}
		
		if(monthA == 12) {
			yearA++;
			monthA = 1;
		}else {
			monthA++;
		}
	}
	int trienios = numeroTrieniosPercividos(alta, actual);

	if(trienios == 0) {
	if(ProRata) {
		sumaTrienios = (sumaTrienios/12.0) * months;

	}else if(ProRata == false && months < 12) {
			
			double num = (sumaTrienios / 14.0) * (months);
			num += (sumaTrienios / 14.0) * (mes1/6.0);
			num+= (sumaTrienios / 14.0) * (mes2/6.0);
			sumaTrienios = num;
		}
	}
	//done
	BrutoAnual = sumaTrienios;
	//12//PRORATEO SI
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
	double BrutoMensual12= SalarioBase + complementos + antiguedad + prorrateoExtra;
	BrutoMensual12 = round(BrutoMensual12, 2);
	double SeguridadSocial = cuotaGeneralTrabajador * BrutoMensual12;
	SeguridadSocial = round(SeguridadSocial,2);
	double Desempleo = cuotaDesempleoTrabajador * BrutoMensual12;
	Desempleo = round(Desempleo, 2);
	double Formacion= cuotaFormacionTrabajador * BrutoMensual12;
	Formacion = round(Formacion,2 );
	double ifpf = this.buscadorIRPF(BrutoAnual) / 100.0;
	double IFPF12 = ifpf *  BrutoMensual12;
	IFPF12 = round (IFPF12,2);
	double LiquidoMensual12 = BrutoMensual12 - SeguridadSocial - Desempleo - Formacion - IFPF12;
	LiquidoMensual12 = round(LiquidoMensual12,2);
	double SeguridadEmpresario = ContingenciasComunes * BrutoMensual12;
	double DesempleoEmpresaioR = DesempleoEmpresario * BrutoMensual12;
	double FOGASA12 = FogasaEmpresario * BrutoMensual12;
	double AccidentesEmpresario = AccidentesTrabajoEmpresario * BrutoMensual12;
	double FormacionEmpresarioR = FormacionEmpresario * BrutoMensual12;
	double CosteEmpresario = SeguridadEmpresario + DesempleoEmpresaioR + FOGASA12 + AccidentesEmpresario + FormacionEmpresarioR + BrutoMensual12;
	CosteEmpresario = round(CosteEmpresario,2);
	double TotalEmpresario = SeguridadEmpresario + DesempleoEmpresaioR + FOGASA12 + AccidentesEmpresario + FormacionEmpresarioR;

	//14
	double BrutoMensual14 = SalarioBase + complementos + antiguedad;
	BrutoMensual14 = round(BrutoMensual14,2);
	double IFPF14 = ifpf * BrutoMensual14;
	IFPF14 = round(IFPF14,2);
	double LiquidoMensual14 =  BrutoMensual14 - SeguridadSocial - Desempleo - Formacion - IFPF14;
	LiquidoMensual14 = round(LiquidoMensual14,2);
	double CosteEmpresario14 = SeguridadEmpresario + DesempleoEmpresaioR + FOGASA12 + AccidentesEmpresario + FormacionEmpresarioR + BrutoMensual14;
	double TotalEmpresario14 = SeguridadEmpresario + DesempleoEmpresaioR + FOGASA12 + AccidentesEmpresario + FormacionEmpresarioR;
	CosteEmpresario14 = round(CosteEmpresario14,2);
	double pJunio = mesesJunio / 6.0;
	double pDiciembre = mesesDiciembre / 6.0;
	double brutoExtra = 0.0;
	double IRPFextra = 0.0;
	double liquidoExtra = 0.0;
	double costeRealEmpresario = 0.0;
	double SalarioBaseExtra = 0.0;
	double complementoExtra = 0.0;
	//Si hay junio
	if(mes == 6) {
	SalarioBaseExtra = SalarioBase * (mesesJunio/6.0); 
	complementoExtra = complementos * (mesesJunio/6.0);
	brutoExtra = BrutoMensual14 * pJunio;
	brutoExtra = round(brutoExtra,2);
	IRPFextra = ifpf * brutoExtra;
	IRPFextra = round(IRPFextra,2);
	liquidoExtra = brutoExtra - IRPFextra;
	liquidoExtra = round(liquidoExtra, 2);
	costeRealEmpresario = brutoExtra;
	}
	//Si hay diciembre
	if(mes == 12) {
	SalarioBaseExtra = SalarioBase * (mesesDiciembre/6.0); 
	complementoExtra = complementos * (mesesDiciembre/6.0);
	brutoExtra = BrutoMensual14 * pDiciembre;
	brutoExtra = round(brutoExtra,2);
	IRPFextra = ifpf * brutoExtra;
	IRPFextra = round(IRPFextra,2);
	liquidoExtra = brutoExtra - IRPFextra;
	liquidoExtra = round(liquidoExtra, 2);
	costeRealEmpresario = brutoExtra;

	}

	//Si hay Diciembre
	if(ProRata) {
	System.out.println("Bruto Anual: "+BrutoAnual);
	System.out.println("Salario Base: "+SalarioBase);
	System.out.println("Prorateo: "+prorrateoExtra);
	System.out.println("Complemento: "+complementos);
	System.out.println("Antiguedad: "+antiguedad);
	System.out.println("Total Devengos(Bruto Mensual): "+BrutoMensual12);
	System.out.println("Desempleo "+cuotaDesempleoTrabajador*100.0 +"% de "+BrutoMensual12+": "+Desempleo);
	System.out.println("Contingencias generales "+cuotaGeneralTrabajador*100.0 +"% de "+BrutoMensual12+": "+SeguridadSocial);
	System.out.println("Cuota Formacion "+cuotaFormacionTrabajador*100.0 +"% de "+BrutoMensual12+": "+Formacion);
	System.out.println("IRPF "+ifpf*100.0 +"% de "+BrutoMensual12+": "+IFPF12);
	System.out.println("Total deduciones: " +(Desempleo + Formacion + IFPF12 +SeguridadSocial) );
	System.out.println("Liquido a percivir: "+LiquidoMensual12);
	System.out.println("Calculo empresario BASE: "+BrutoMensual12);
	System.out.println("Contingecias comunes empresario: "+ContingenciasComunes*100.0 +"% de "+BrutoMensual12+": "+SeguridadEmpresario);
	System.out.println("Desempleo empresario "+DesempleoEmpresario*100.0 +"% de "+BrutoMensual12+": "+DesempleoEmpresaioR);
	System.out.println("Formacion "+FormacionEmpresario*100.0 +"% de "+BrutoMensual12+": "+FormacionEmpresarioR);
	System.out.println("Accidentes de trabajo "+AccidentesTrabajoEmpresario*100.0 +"% de "+BrutoMensual12+": "+AccidentesEmpresario);
	System.out.println("FOGASA "+FogasaEmpresario*100.0 +"% de "+BrutoMensual12+": "+FOGASA12);
	System.out.println("Total empresario: "+TotalEmpresario);
	System.out.println("Coste total trabajador: "+CosteEmpresario);
	Nomina nomina1 = new Nomina(idNomina++, trabajador, monthO, yearO, trienios, this.dineroTrienio(trienios, sheet1), SalarioBase, complementos, prorrateoExtra, BrutoAnual, ifpf, IFPF12, BrutoMensual12, ContingenciasComunes,SeguridadEmpresario ,DesempleoEmpresario,DesempleoEmpresaioR,  FormacionEmpresario, FormacionEmpresarioR, AccidentesTrabajoEmpresario, AccidentesEmpresario, FogasaEmpresario, FOGASA12, cuotaGeneralTrabajador, SeguridadSocial, cuotaDesempleoTrabajador, Desempleo, cuotaFormacionTrabajador, Formacion, BrutoMensual12, LiquidoMensual12, CosteEmpresario);
	nominas.add(nomina1);
	
        //Creamos el pdf de la nomina
        GenerarPDF nominaPDF = new GenerarPDF(this.NombreEmpresa,this.DNI,this.Nombre,this.Apellidos,this.year,this.mes,this.CIFEmpresa,this.IBAN,this.Categoria,this.FechaDeAlta,String.valueOf(BrutoAnual),String.valueOf(antiguedad),String.valueOf(cuotaGeneralTrabajador*100.0),String.valueOf(BrutoMensual12),String.valueOf(SeguridadSocial),String.valueOf(cuotaDesempleoTrabajador*100.0),String.valueOf(BrutoMensual12),String.valueOf(Desempleo),String.valueOf(cuotaFormacionTrabajador*100.0),String.valueOf(BrutoMensual12),String.valueOf(Formacion),String.valueOf(ifpf*100.0),String.valueOf(BrutoMensual12),String.valueOf(IFPF12),String.valueOf(prorrateoExtra),String.valueOf(SalarioBase),String.valueOf(complementos),String.valueOf((Desempleo + Formacion + IFPF12 +SeguridadSocial)),String.valueOf(BrutoMensual12),String.valueOf(LiquidoMensual12),String.valueOf(BrutoMensual12),String.valueOf(ContingenciasComunes*100.0),String.valueOf(SeguridadEmpresario),String.valueOf(DesempleoEmpresario*100.0),String.valueOf(DesempleoEmpresaioR),String.valueOf(FormacionEmpresario*100.0),String.valueOf(FormacionEmpresarioR),String.valueOf(AccidentesTrabajoEmpresario*100.0),String.valueOf(AccidentesEmpresario),String.valueOf(FogasaEmpresario*100.0),String.valueOf(FOGASA12),String.valueOf(TotalEmpresario),String.valueOf(CosteEmpresario),"NO");
            try {
                nominaPDF.generar();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            }
	
	}else {
	System.out.println("Bruto Anual: "+BrutoAnual);
	System.out.println("Salario Base: "+SalarioBase);
	System.out.println("Complemento: "+complementos);
	System.out.println("Antiguedad: "+antiguedad);
	System.out.println("Prorateo: "+0.0);
	System.out.println("Total Devengos(Bruto Mensual): "+BrutoMensual14);
	System.out.println("Desempleo "+cuotaDesempleoTrabajador*100.0 +"% de "+BrutoMensual12+": "+Desempleo);
	System.out.println("Contingencias generales "+cuotaGeneralTrabajador*100.0 +"% de "+BrutoMensual12+": "+SeguridadSocial);
	System.out.println("Cuota Formacion "+cuotaFormacionTrabajador*100.0 +"% de "+BrutoMensual12+": "+Formacion);
	System.out.println("IRPF "+ifpf*100.0 +"% de "+BrutoMensual14+": "+IFPF14);
	System.out.println("Total deduciones: " +(Desempleo + Formacion + IFPF14 +SeguridadSocial) );
	System.out.println("Liquido a percivir: "+LiquidoMensual14);
	System.out.println("Calculo empresario BASE: "+BrutoMensual12);
	System.out.println("Contingecias comunes empresario: "+ContingenciasComunes*100.0 +"% de "+BrutoMensual12+": "+SeguridadEmpresario);
	System.out.println("Desempleo empresario "+DesempleoEmpresario*100.0 +"% de "+BrutoMensual12+": "+DesempleoEmpresaioR);
	System.out.println("Formacion "+FormacionEmpresario*100.0 +"% de "+BrutoMensual12+": "+FormacionEmpresarioR);
	System.out.println("Accidentes de trabajo "+AccidentesTrabajoEmpresario*100.0 +"% de "+BrutoMensual12+": "+AccidentesEmpresario);
	System.out.println("FOGASA "+FogasaEmpresario*100.0 +"% de "+BrutoMensual12+": "+FOGASA12);
	System.out.println("Total empresario: "+TotalEmpresario14);
	System.out.println("Coste total trabajador: "+CosteEmpresario14);
	Nomina nomina1 = new Nomina(idNomina++, trabajador, monthO, yearO, trienios, this.dineroTrienio(trienios, sheet1), SalarioBase, complementos, 0.0, BrutoAnual, ifpf, IFPF14, BrutoMensual14, ContingenciasComunes,SeguridadEmpresario ,DesempleoEmpresario,DesempleoEmpresaioR,  FormacionEmpresario, FormacionEmpresarioR, AccidentesTrabajoEmpresario, AccidentesEmpresario, FogasaEmpresario, FOGASA12, cuotaGeneralTrabajador, SeguridadSocial, cuotaDesempleoTrabajador, Desempleo, cuotaFormacionTrabajador, Formacion, BrutoMensual14, LiquidoMensual14, CosteEmpresario);
	nominas.add(nomina1);
         GenerarPDF nominaPDF = new GenerarPDF(this.NombreEmpresa,this.DNI,this.Nombre,this.Apellidos,this.year,this.mes,this.CIFEmpresa,this.IBAN,this.Categoria,this.FechaDeAlta,String.valueOf(BrutoAnual),String.valueOf(antiguedad),String.valueOf(cuotaGeneralTrabajador*100.0),String.valueOf(BrutoMensual12),String.valueOf(SeguridadSocial),String.valueOf(cuotaDesempleoTrabajador*100.0),String.valueOf(BrutoMensual12),String.valueOf(Desempleo),String.valueOf(cuotaFormacionTrabajador*100.0),String.valueOf(BrutoMensual12),String.valueOf(Formacion),String.valueOf(ifpf*100.0),String.valueOf(BrutoMensual12),String.valueOf(IFPF12),String.valueOf(prorrateoExtra),String.valueOf(SalarioBase),String.valueOf(complementos),String.valueOf((Desempleo + Formacion + IFPF12 +SeguridadSocial)),String.valueOf(BrutoMensual12),String.valueOf(LiquidoMensual12),String.valueOf(BrutoMensual12),String.valueOf(ContingenciasComunes*100.0),String.valueOf(SeguridadEmpresario),String.valueOf(DesempleoEmpresario*100.0),String.valueOf(DesempleoEmpresaioR),String.valueOf(FormacionEmpresario*100.0),String.valueOf(FormacionEmpresarioR),String.valueOf(AccidentesTrabajoEmpresario*100.0),String.valueOf(AccidentesEmpresario),String.valueOf(FogasaEmpresario*100.0),String.valueOf(FOGASA12),String.valueOf(TotalEmpresario),String.valueOf(CosteEmpresario),"NO");
            try {
                nominaPDF.generar();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            }
	//Extra de Junio
	if(mes == 6) {
	System.out.println("-------------------");
	System.out.println("--Extra Junio--");
	System.out.println("-------------------");

	System.out.println("Bruto Anual: "+BrutoAnual);
	System.out.println("Salario Base: "+SalarioBaseExtra);
	System.out.println("Complemento: "+complementoExtra);
	System.out.println("Antiguedad: "+antiguedad);
	System.out.println("Prorateo: "+0.0);
	System.out.println("Total Devengos(Bruto Extra): "+brutoExtra);
	System.out.println("Desempleo "+cuotaDesempleoTrabajador*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Contingencias generales "+cuotaGeneralTrabajador*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Cuota Formacion "+cuotaFormacionTrabajador*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("IRPF "+ifpf*100.0 +"% de "+BrutoMensual14+": "+IRPFextra);
	System.out.println("Total deduciones: " +(IFPF14) );
	System.out.println("Liquido a percivir: "+liquidoExtra);
	System.out.println("Calculo empresario BASE: "+0.0);
	System.out.println("Contingecias comunes empresario: "+ContingenciasComunes*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Desempleo empresario "+DesempleoEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Formacion "+FormacionEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Accidentes de trabajo "+AccidentesTrabajoEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("FOGASA "+FogasaEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Total empresario: "+0.0);
	System.out.println("Coste real empresario: "+costeRealEmpresario);	
	Nomina nomina2 = new Nomina(idNomina++, trabajador, monthO, yearO, trienios, this.dineroTrienio(trienios, sheet1), SalarioBaseExtra, complementoExtra, 0.0, brutoExtra, ifpf, IFPF14, 0.0, ContingenciasComunes,0.0 ,DesempleoEmpresario,0.0,  FormacionEmpresario, 0.0, AccidentesTrabajoEmpresario, 0.0, FogasaEmpresario, 0.0, cuotaGeneralTrabajador, 0.0, cuotaDesempleoTrabajador, 0.0, cuotaFormacionTrabajador, 0.0, brutoExtra, liquidoExtra, costeRealEmpresario);
	nominas.add(nomina2);
        GenerarPDF nominaPDFExtra = new GenerarPDF(this.NombreEmpresa,this.DNI,this.Nombre,this.Apellidos,this.year,this.mes,this.CIFEmpresa,this.IBAN,this.Categoria,this.FechaDeAlta,String.valueOf(BrutoAnual),String.valueOf(antiguedad),String.valueOf(cuotaGeneralTrabajador*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(cuotaDesempleoTrabajador*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(cuotaFormacionTrabajador*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(ifpf*100.0),String.valueOf(BrutoMensual14),String.valueOf(IRPFextra),String.valueOf(0.0),String.valueOf(SalarioBaseExtra),String.valueOf(complementoExtra),String.valueOf(IFPF14),String.valueOf(brutoExtra),String.valueOf(liquidoExtra),String.valueOf(0.0),String.valueOf(ContingenciasComunes*100.0),String.valueOf(0.0),String.valueOf(DesempleoEmpresario*100.0),String.valueOf(0.0),String.valueOf(FormacionEmpresario*100.0),String.valueOf(0.0),String.valueOf(AccidentesTrabajoEmpresario*100.0),String.valueOf(0.0),String.valueOf(FogasaEmpresario*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(costeRealEmpresario),"SI");
            try {
                nominaPDFExtra.generar();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	//Extra de Diciembre
	if(mes == 12) {
	System.out.println("-------------------");
	System.out.println("--Extra Diciembre--");
	System.out.println("-------------------");
	System.out.println("Bruto Anual: "+BrutoAnual);
	System.out.println("Salario Base: "+ SalarioBaseExtra);
	System.out.println("Complemento: "+complementoExtra);
	System.out.println("Antiguedad: "+antiguedad);
	System.out.println("Prorateo: "+0.0);
	System.out.println("Total Devengos(Bruto Extra): "+brutoExtra);
	System.out.println("Desempleo "+cuotaDesempleoTrabajador*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Contingencias generales "+cuotaGeneralTrabajador*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Cuota Formacion "+cuotaFormacionTrabajador*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("IRPF "+ifpf*100.0 +"% de "+BrutoMensual14+": "+IRPFextra);
	System.out.println("Total deduciones: " +(IFPF14) );
	System.out.println("Liquido a percivir: "+liquidoExtra);
	System.out.println("Calculo empresario BASE: "+0.0);
	System.out.println("Contingecias comunes empresario: "+ContingenciasComunes*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Desempleo empresario "+DesempleoEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Formacion "+FormacionEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Accidentes de trabajo "+AccidentesTrabajoEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("FOGASA "+FogasaEmpresario*100.0 +"% de "+0.0+": "+0.0);
	System.out.println("Total empresario: "+0.0);
	System.out.println("Coste real empresario: "+costeRealEmpresario);	
	Nomina nomina2 = new Nomina(idNomina++, trabajador, monthO, yearO, trienios, this.dineroTrienio(trienios, sheet1),SalarioBaseExtra, complementoExtra, 0.0, brutoExtra, ifpf, IFPF14, 0.0, ContingenciasComunes,0.0 ,DesempleoEmpresario,0.0,  FormacionEmpresario, 0.0, AccidentesTrabajoEmpresario, 0.0, FogasaEmpresario, 0.0, cuotaGeneralTrabajador, 0.0, cuotaDesempleoTrabajador, 0.0, cuotaFormacionTrabajador, 0.0, brutoExtra, liquidoExtra, costeRealEmpresario);
	nominas.add(nomina2);
        
	GenerarPDF nominaPDFExtra = new GenerarPDF(this.NombreEmpresa,this.DNI,this.Nombre,this.Apellidos,this.year,this.mes,this.CIFEmpresa,this.IBAN,this.Categoria,this.FechaDeAlta,String.valueOf(BrutoAnual),String.valueOf(antiguedad),String.valueOf(cuotaGeneralTrabajador*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(cuotaDesempleoTrabajador*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(cuotaFormacionTrabajador*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(ifpf*100.0),String.valueOf(BrutoMensual14),String.valueOf(IRPFextra),String.valueOf(0.0),String.valueOf(SalarioBaseExtra),String.valueOf(complementoExtra),String.valueOf(IFPF14),String.valueOf(brutoExtra),String.valueOf(liquidoExtra),String.valueOf(0.0),String.valueOf(ContingenciasComunes*100.0),String.valueOf(0.0),String.valueOf(DesempleoEmpresario*100.0),String.valueOf(0.0),String.valueOf(FormacionEmpresario*100.0),String.valueOf(0.0),String.valueOf(AccidentesTrabajoEmpresario*100.0),String.valueOf(0.0),String.valueOf(FogasaEmpresario*100.0),String.valueOf(0.0),String.valueOf(0.0),String.valueOf(costeRealEmpresario),"SI");
            try {
                nominaPDFExtra.generar();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Vaica.class.getName()).log(Level.SEVERE, null, ex);
            }
	
				}
		
			}

	}
	
	
	
	
	
	
	
	
	
	
	
	//METODOS AUXILIARES
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
	


double cambiaTrienioProRata(String alta, String actual,int mes) {
	double sumaTrienios = 0.0;	
	String strA[] = alta.split("/");
	int monthA = Integer.valueOf(strA[0]);
	int yearA = Integer.valueOf(strA[2]);
	String strO[] = actual.split("/");
	int monthO = Integer.valueOf(strO[0]);
	int yearO = Integer.valueOf(strO[1]);
	int trienios = 0;
	int meses = 0;
	int yearEj = yearO + 1;
	double fin = 0.0;
	while(!(yearEj == yearA && 1 == monthA)) {
		if((monthA <= mes && yearO == yearA)) {
			fin = this.dineroTrienio(trienios, sheet1);
		}
		if(meses % 36 == 0 && meses != 0) {
			
			trienios++;
		}
		meses++;
		if(monthA == 12) {
			yearA++;
			monthA = 1;
		}else {
			monthA++;
		}	
		
	}
	return fin;
}

double brutoAnual(String alta, String actual, boolean proratea) {
double sumaTrienios = 0.0;	
String strA[] = alta.split("/");
int monthA = Integer.valueOf(strA[0]);
int yearA = Integer.valueOf(strA[2]);
String strO[] = actual.split("/");
int monthO = Integer.valueOf(strO[0]);
int yearO = Integer.valueOf(strO[1]);
int trienios = 0;
int meses = 0;
int yearEj = yearO + 1;
int trienioActualPaga = 0;
int month = 0;
int mes1 = 0;
int mes2 = 0;
while(!(yearEj == yearA && 1 == monthA)) {

	if(meses % 36 == 0 && meses != 0) {
		if((monthA == 6 && yearO == yearA) || (monthA == 12 && yearO == yearA)) {
			sumaTrienios += this.dineroTrienio(trienios, sheet1);
			if(!proratea) {
			sumaTrienios += this.dineroTrienio(trienios, sheet1);
			}
		}else if(yearO == yearA){
			sumaTrienios += this.dineroTrienio(trienios, sheet1);	
		}
		trienios++;
		meses++;
	}else {
		if((monthA == 6 && yearO == yearA) || (monthA == 12 && yearO == yearA)) {
			sumaTrienios += this.dineroTrienio(trienios, sheet1);
			if(!proratea) {
				sumaTrienios += this.dineroTrienio(trienios, sheet1);
				}
		}else if(yearO == yearA){
			sumaTrienios += this.dineroTrienio(trienios, sheet1);	
		}
		meses++;
	}
	if(yearO ==yearA) {
		month++;
	}
	if(proratea) {
	if(monthA<6 && yearO == yearA) {
		sumaTrienios += this.cambiaTrienioProRata(alta, actual, 6)/6.0;	
		mes1++;

	}else if(monthA>=6 && monthA < 12 && yearO == yearA){
		sumaTrienios += this.cambiaTrienioProRata(alta, actual, 13)/6.0;
		mes2++;
	}else if(monthA == 12 && yearO == yearA) {
		sumaTrienios += this.cambiaTrienioProRata(alta, monthO+"/"+(yearO+1),6)/6.0;

	}
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

int numeroTrieniosPercividos(String alta, String actual) {
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
	return trienios;
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
